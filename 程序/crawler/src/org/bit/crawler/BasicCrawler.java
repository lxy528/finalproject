package org.bit.crawler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Pattern;

import org.bit.crawler.gist.CrawlerMain;
import org.bit.handler.QuietCssErrorHandler;
import org.bit.handler.SilentIncorrectnessListener;
import org.bit.proxy.CrawlerProxy;
import org.bit.proxy.ProxyList;
import org.bit.scheduler.CrawlerTask;
import org.bit.scheduler.Scheduler;



import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.ProxyConfig;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public abstract class BasicCrawler implements Runnable{
	protected static final int IDLE_SLEEP_TIME = 10000;
	
	private int id;
	
	protected String webSite;
	private String startUrl;
	private String currListUrl;
	
	protected Pattern listPtn;
	protected Pattern pagePtn;
	
	protected String dataPath;
	protected String savePath;
	protected String infoPath;
	
	protected ArrayList<String> listUrls;
	protected ArrayList<String> pageUrls;
	//protected ArrayList<String> downloadedUrls;
	protected ArrayList<String> visitedListUrls;
	
	protected WebClient webClient;
	private ProxyList pList;
	protected int crawledNum;
	
	protected Scheduler scheduler;
	
	private boolean isIdle;
	private boolean shouldQuit;
	
	public BasicCrawler(){
		this.scheduler = null;
		this.setIdle(true);
		this.setShouldQuit(false);
	}
	
	public BasicCrawler(String webSite, String startUrl, String listPtn, String pagePtn, String dataPath){
		this.scheduler = null;
		this.webSite = webSite;
		this.setStartUrl(startUrl);
		this.listPtn = Pattern.compile(listPtn);
		this.pagePtn = Pattern.compile(pagePtn);
		this.dataPath = dataPath;
		this.savePath = this.dataPath + "/data/";
		this.infoPath = this.dataPath + "/info/";
		
		if(CrawlerMain.startPage.length() > 0){
			this.setStartUrl(CrawlerMain.startPage);
		}
		
		File path = new File(this.dataPath);
		if(!path.exists()){
			System.out.println("[" + this.webSite + "]Path does not exist:" + this.dataPath);
			System.exit(-1);
		}
		
		path = new File(this.savePath);
		if(!path.exists()){
			path.mkdir();
		}
		path = new File(this.infoPath);
		if(!path.exists()){
			path.mkdir();
		}
		Date date = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd HH_mm_ss");//设置日期格式
		savePath += df.format(date);
		path = new File(this.savePath);
		if(!path.exists()){
			path.mkdir();
		}
		
		listUrls = new ArrayList<String>();
		pageUrls = new ArrayList<String>();
		//downloadedUrls = new ArrayList<String>();
		visitedListUrls = new ArrayList<String>();
		
		webClient = new WebClient();
		webClient.setJavaScriptEnabled(true);
		webClient.setCssEnabled(false);
		webClient.setAjaxController(new NicelyResynchronizingAjaxController());
		webClient.setTimeout(350000);
		webClient.setThrowExceptionOnScriptError(false);
		webClient.setIncorrectnessListener( new SilentIncorrectnessListener() ) ;
		webClient.setCssErrorHandler( new QuietCssErrorHandler() ) ;
		
		if(Scheduler.debug){
			System.out.println("[debug:t" + this.id + "]Start to crawl " + this.webSite);
		}
	}
	
	public void manageTask(){
		if(this.listUrls.size() == 0 && this.pageUrls.size() == 0)
		{
			if(this.scheduler != null){
				CrawlerTask t = this.scheduler.getTask(this);
				this.listUrls.addAll(t.getLists());
				this.pageUrls.addAll(t.getDetails());
				if(t.isEmpty()){
					if(Scheduler.debug){
						System.out.println("[debug:t" + this.id + "]manageTask: set to idle");
					}
					this.setIdle(true);
					try {
						Thread.sleep((long)this.IDLE_SLEEP_TIME);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					this.setIdle(false);
				}
				else{
					this.setIdle(false);
				}
			}
		}
		else if(this.listUrls.size() >= scheduler.LIST_PUSH_THRESHOLD ||
				this.pageUrls.size() >= scheduler.PAGE_PUSH_THRESHOLD){
			CrawlerTask t = new CrawlerTask();
			int listSize = this.listUrls.size();
			int pageSize = this.pageUrls.size();
			
			if(this.listUrls.size() >= scheduler.LIST_PUSH_THRESHOLD){
				for(int i = 0; i < listSize/2; i++){
					t.getLists().add(this.listUrls.remove(0));
				}
			}
			if(this.pageUrls.size() >= scheduler.PAGE_PUSH_THRESHOLD){
				for(int i = 0; i < pageSize/2; i++){
					t.getDetails().add(this.pageUrls.remove(0));
				}
			}
			this.scheduler.putTask(this, t);
		}
	}
	
	public void setupProxy(){
		//set up proxy configuration
		if(this.pList != null){
			ArrayList<CrawlerProxy> cProxy = this.pList.mallocProxy(1);
			ProxyConfig pConfig = new ProxyConfig(cProxy.get(0).getIp(), 
					Integer.parseInt(cProxy.get(0).getPort()), 
					false);
			this.webClient.setProxyConfig(pConfig);
		}
	}
	public void rebuildUrls(ArrayList<String> downloadedUrls){
		File file = new File(this.dataPath + "/data");
		String[] dirList = file.list();
		for(String dir : dirList){
			File file2 = new File(this.dataPath + "/data/" + dir);
			String[] fileList = file2.list();
			for(String html: fileList){
				if(html.indexOf(".html") < 0){
					continue;
				}
				FileReader reader;
				try {
					reader = new FileReader(this.dataPath + "/data/" + dir + "/" + html);
					BufferedReader br = new BufferedReader(reader);
					String url = br.readLine();
					br.close();
					reader.close();
					downloadedUrls.add(url);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		//listUrls.add(getStartUrl());
		if(Scheduler.debug){
			System.out.println("[debug]rebuildUrls: num=" + downloadedUrls.size());
		}
		crawledNum = 0;
	}
	public void loadInfo(ArrayList<String> downloadedUrls){
		FileReader reader;
		try {
			File f = new File(infoPath + "urls.txt");
			if(!f.exists()){
				f.createNewFile();
			}
			reader = new FileReader(infoPath + "urls.txt");
			BufferedReader br = new BufferedReader(reader);
			String url = null;
			while((url = br.readLine()) != null) {
				if(url.trim().length() > 0){
					downloadedUrls.add(url);
				}
			}
			br.close();
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		//listUrls.add(getStartUrl());
		if(Scheduler.debug){
			System.out.println("[debug]loadInfo: num=" + downloadedUrls.size());
		}
		crawledNum = 0;
	}
	public void saveInfo(ArrayList<String> downloadedUrls){
		FileWriter fw;
		try {
			fw = new FileWriter(infoPath + "urls.txt", false);
			BufferedWriter bw = new BufferedWriter(fw);
			for(String url: downloadedUrls){
				if(url != null){
					bw.write(url);
					bw.newLine();
				}
			}
			bw.close();
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	abstract public void parseListPage(HtmlPage listPage);
	abstract public void parseDetailPage(HtmlPage p, String name);
	abstract public String getId(String url);
	abstract public String getNextListUrl(String currListUrl, int policy);
	
	public void crawlAll(){
		while(true){
			this.manageTask();
			if(this.isShouldQuit()){
				if(Scheduler.debug){
					System.out.println("[debug]crawlAll: thread quit");
				}
				break;
			}
			if(this.pageUrls.size() <= 0){
				if(this.listUrls.size() > 0){
					String listUrl = this.listUrls.remove(0);
					this.scheduler.addVisitedList(listUrl);
					this.visitedListUrls.add(listUrl);
					try {
						System.out.println("[" + this.webSite + "]" + listUrl);
						HtmlPage listPage = this.webClient.getPage(listUrl);
						this.parseListPage(listPage);
						//this.pageUrls.addAll(this.parseListPage(listPage));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}else{
					//break;
					try {
						Thread.sleep((long) (Math.random()*8000));
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}else{
				String detailUrl = this.pageUrls.remove(0);
				this.visitedListUrls.add(detailUrl);
				this.scheduler.addVisitedPage(detailUrl);
				try {
					HtmlPage detailPage = webClient.getPage(detailUrl);
					String id = this.getId(detailUrl);
					this.savePage(detailUrl, savePath + "/" + id + ".html", detailPage);
					parseDetailPage(detailPage, id);
					System.out.println("[" + this.webSite + "]" + detailUrl);
					this.crawledNum++;
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			try {
				Thread.sleep((long) (Math.random()*8000));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	protected static void savePage(String url, String path,HtmlPage pp) throws IOException{
		File f = new File(path);
		if(!f.exists())
			f.createNewFile();					
		FileOutputStream fs = new FileOutputStream(f);		
		//pp.save(f);
		
		String text = url + "\n\n" + pp.getWebResponse().getContentAsString();
		fs.write(text.getBytes());
		fs.close();		
	}
	
	public void setpList(ProxyList pList) {
		this.pList = pList;
	}

	public ProxyList getpList() {
		return pList;
	}

	public void setScheduler(Scheduler scheduler) {
		this.scheduler = scheduler;
	}

	public Scheduler getScheduler() {
		return scheduler;
	}

	public synchronized void setIdle(boolean isIdle) {
		this.isIdle = isIdle;
	}

	public synchronized boolean isIdle() {
		return isIdle;
	}

	public synchronized void setShouldQuit(boolean shouldQuit) {
		this.shouldQuit = shouldQuit;
	}

	public synchronized boolean isShouldQuit() {
		return shouldQuit;
	}
	
	@Override
	public void run(){
		//this.setupProxy();
		//this.loadInfo();
		this.crawlAll();
		//this.saveInfo();
	}

	public void setStartUrl(String startUrl) {
		this.startUrl = startUrl;
	}

	public String getStartUrl() {
		return startUrl;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCurrListUrl() {
	    return currListUrl;
    }

	public void setCurrListUrl(String currListUrl) {
	    this.currListUrl = currListUrl;
    }
}
