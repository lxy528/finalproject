package org.bit.scheduler;

import java.util.ArrayList;

import org.bit.crawler.BasicCrawler;
import org.bit.crawler.app.Crawler360App;
import org.bit.crawler.app.CrawlerBaiduApp;
import org.bit.crawler.csdn.CrawlerCSDN;
import org.bit.crawler.gist.CrawlerGist;

public class Scheduler implements Runnable{
	private static final int LIST_MALLOC_THRESHOLD = 1;
	private static final int PAGE_MALLOC_THRESHOLD = 10;
	public static final  int LIST_PUSH_THRESHOLD = 4;
	public static final  int PAGE_PUSH_THRESHOLD = 20;
	
	public static final int SCHEDULER_SLEEP_TIME = 2000;
	
	public static final int SCHEDULE_POLICY_AUTO = 1;
	public static final int SCHEDULE_POLICY_INSC = 2;
	public static final int SCHEDULE_POLICY_DESC = 3;
	
	
	public static final boolean debug = true;
	
	private ArrayList<String> pages;
	private ArrayList<String> lists;
	private ArrayList<String> visitedLists;
	private ArrayList<String> downloadedPages;
	
	private ArrayList<Thread> thrdList;
	private ArrayList<BasicCrawler> crawlerList;

	private int schdlPolicy = SCHEDULE_POLICY_AUTO;
	
	private String website = "";
	private String startPage = "";
	private String currListUrl = "";
	private int thrdNum = 1;
	
	public Scheduler(){
		pages = new ArrayList<String>();
		lists = new ArrayList<String>();
		visitedLists = new ArrayList<String>();
		downloadedPages = new ArrayList<String>();
		thrdList = new ArrayList<Thread>();
		crawlerList = new ArrayList<BasicCrawler>();
	}
	
	public Scheduler(String website, int num){
		this.website = website;
		this.thrdNum = num;
		pages = new ArrayList<String>();
		lists = new ArrayList<String>();
		visitedLists = new ArrayList<String>();
		downloadedPages = new ArrayList<String>();
		thrdList = new ArrayList<Thread>();
		crawlerList = new ArrayList<BasicCrawler>();
	}
	public void addCrawler(Thread c){
		this.thrdList.add(c);
	}
	public boolean testAndQuit(){
		int count = 0;
		for(int i = 0; i < this.crawlerList.size(); i++){
			if(this.crawlerList.get(i).isIdle()){
				count++;
			}
		}
		if(this.debug){
			//System.out.println("[DEBUG:Scheduler] Idle Num. = " + count);
		}
		if(count == this.crawlerList.size() && this.lists.size() == 0 && this.pages.size() == 0){
			if(debug){
				System.out.println("[debug]all done, quit");
			}
			for(BasicCrawler c: this.crawlerList){
				c.setShouldQuit(true);
			}
			return true;
		}
		
		return false;
	}
	public synchronized CrawlerTask getTask(BasicCrawler c){
		int tid = c.getId();
		
		CrawlerTask t = new CrawlerTask();
		//default policy: auto
		for(int i = 0; i < this.lists.size() && i < this.LIST_MALLOC_THRESHOLD; i++){
			t.getLists().add(this.lists.remove(0));
		}
		//specified policy: insc
		if(t.getLists().size() == 0){
			String nextListUrl = c.getNextListUrl(this.currListUrl, this.schdlPolicy);
			t.getLists().add(nextListUrl);
			this.currListUrl = nextListUrl;
		}
		for(int i = 0; i < this.pages.size() && i < this.PAGE_MALLOC_THRESHOLD; i++){
			String page = this.pages.remove(0);
			t.getDetails().add(page);
			//this.downloadedPages.add(page);
		}
		if(debug){
			System.out.println("[DEBUG:t" + tid + "]getTask:list=" + t.getLists().size() + ", page=" + t.getDetails().size());
			System.out.println("[DEBUG:Scheduler]left:list=" + this.lists.size() + ", page=" + this.pages.size());
		}
		return t;
	}
	public synchronized void putTask(BasicCrawler c, CrawlerTask t){
		int tid = c.getId();
		if(debug){
			System.out.println("[DEBUG:t" + tid + "]putTask:list=" + t.getLists().size() + ", page=" + t.getDetails().size());
		}
		if(this.schdlPolicy == this.SCHEDULE_POLICY_AUTO){
			for(String url: t.getLists()){
				if(this.lists.indexOf(url) < 0 && this.visitedLists.indexOf(url) < 0){
					this.lists.add(url);
				}
			}
		}
		for(String url: t.getDetails()){
			if(this.pages.indexOf(url) < 0 && this.downloadedPages.indexOf(url)< 0){
				this.pages.add(url);
			}
		}
		if(debug){
			System.out.println("[DEBUG:t" + tid + "]left:list=" + this.lists.size() + ", page=" + this.pages.size());
		}
	}
	
	public synchronized void putLists(int tid, ArrayList<String> lists){
		if(debug){
			System.out.println("[DEBUG:t" + tid + "]putList:list=" + lists.size());
		}
		for(String url: lists){
			if(this.lists.indexOf(url) < 0 && this.visitedLists.indexOf(url) < 0){
				this.lists.add(url);
			}else{
				System.out.println("visited:" + url);
			}
		}
		if(debug){
			System.out.println("[DEBUG:Scheduler]left:list=" + this.lists.size());
		}
	}
	
	public synchronized void putPages(int tid, ArrayList<String> pages){
		if(debug){
			System.out.println("[DEBUG:t" + tid + "]putPages:page=" + pages.size());
		}
		for(String url: pages){
			if(this.pages.indexOf(url) < 0 && this.downloadedPages.indexOf(url)< 0){
				this.pages.add(url);
			}
		}
		if(debug){
			System.out.println("[DEBUG:Scheduler]left:page=" + this.pages.size());
		}
	}
	
	public synchronized void addVisitedList(String url){
		this.visitedLists.add(url);
	}
	public synchronized void addVisitedPage(String url){
		this.downloadedPages.add(url);
	}
	
	public void start(String website, int thrdNum){
		if(website.equals("360")){
			for(int i = 0; i < thrdNum; i++){
				//360 crawler
				Crawler360App c360 = new Crawler360App("F:\\apps\\360");
				//c360.setpList(pList);
				c360.setScheduler(this);
				crawlerList.add(c360);
				Thread t360 = new Thread(c360);
				thrdList.add(t360);
			}
		}
		else if(website.equals("baidu")){
			for(int i = 0; i < thrdNum; i++){
				//Baidu crawler
				CrawlerBaiduApp cbaidu = new CrawlerBaiduApp(
						 "F:\\apps\\baidu"
						 );
				//cbaidu.setpList(pList);
				cbaidu.setScheduler(this);
				crawlerList.add(cbaidu);
				Thread tbaidu = new Thread(cbaidu);
				thrdList.add(tbaidu);
			}
		}
		else if(website.equals("gist")){
			for(int i = 0; i < thrdNum; i++){
				//Baidu crawler
				CrawlerGist cgist = new CrawlerGist(
						 "F:\\gist"
						 );
				//cbaidu.setpList(pList);
				cgist.setScheduler(this);
				cgist.setId(i);
				crawlerList.add(cgist);
				Thread tgist = new Thread(cgist);
				thrdList.add(tgist);
			}
		}
		else if(website.equals("csdn")){
			for(int i = 0; i < thrdNum; i++){
				//CSDN crawler
				CrawlerCSDN ccsdn = new CrawlerCSDN(
						 "f:\\csdn"
						 );
				//cbaidu.setpList(pList);
				ccsdn.setScheduler(this);
				ccsdn.setId(i);
				crawlerList.add(ccsdn);
				Thread tgist = new Thread(ccsdn);
				thrdList.add(tgist);
			}
		}
		


		if(thrdNum > 0){
			crawlerList.get(0).rebuildUrls(this.downloadedPages);
			if(this.startPage.trim().length() > 0){
				this.lists.add(this.startPage);
				this.currListUrl = this.startPage;
			}else{
				this.lists.add(crawlerList.get(0).getStartUrl());
				this.currListUrl = crawlerList.get(0).getStartUrl();
			}
		}
		
		try {
			// start all thread
			for(Thread t: thrdList){
				t.start();
			}
			//test and signal quit
			while(true){
				if(this.testAndQuit()){
					break;
				}
				else{
					try {
						Thread.sleep((long)this.SCHEDULER_SLEEP_TIME);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			if(this.debug){
				System.out.println("[DEBUG:Scheduler] quit.");
			}
			//waiting for join
			for(Thread t: thrdList){
				t.join();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(thrdNum > 0){
			crawlerList.get(0).saveInfo(this.downloadedPages);
		}
	}
	public void saveInfo(){
		if(this.crawlerList.size() > 0){
			crawlerList.get(0).saveInfo(this.downloadedPages);
		}
	}

	@Override
	public void run() {
		start(this.website, this.thrdNum);
	}

	public int getSchdlPolicy() {
	    return schdlPolicy;
    }

	public void setSchdlPolicy(int schdlPolicy) {
	    this.schdlPolicy = schdlPolicy;
    }

	public String getStartPage() {
	    return startPage;
    }

	public void setStartPage(String startPage) {
	    this.startPage = startPage;
    }
}
