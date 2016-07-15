package org.bit.crawler.csdn;

import java.util.ArrayList;
import java.util.List;

import org.bit.crawler.BasicCrawler;
import org.bit.crawler.gist.http.Utils;
import org.bit.crawler.gist.http.model.Capture_msg;
import org.bit.crawler.gist.http.struct.SnippetCapture;
import org.bit.handler.QuietCssErrorHandler;
import org.bit.handler.SilentIncorrectnessListener;
import org.bit.scheduler.Scheduler;

import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class CrawlerCSDN  extends BasicCrawler{
	public CrawlerCSDN(String dataPath){
		super("http://code.csdn.net",
			   "http://code.csdn.net/explore/snippets?page=1",
			  "http://code.csdn.net/explore/snippets\\?page=\\d+",
			  "",
			  dataPath);
		//this.webClient.getOptions().setUseInsecureSSL(true);
	}
	
	@Override
	public void parseListPage(HtmlPage listPage) {
		List<?> nextPagesAnchors = listPage.getByXPath(".//ul[@class='pagination']/li[@class='page']/a");
		ArrayList<String> listList = new ArrayList<String>();
		for(Object o: nextPagesAnchors){
			listList.add(this.webSite + ((HtmlAnchor)o).getHrefAttribute());
		}
		if(this.scheduler.getSchdlPolicy() == this.scheduler.SCHEDULE_POLICY_AUTO){
			this.scheduler.putLists(this.getId(), listList);
		}
		
		List<?> gistList =  listPage.getByXPath(".//dd[@class='pull-right explore_des']/dl/dt/a[2]");
		//System.out.println(gistList.size());
		
		ArrayList<String> pageList = new ArrayList<String>();
		for(Object o: gistList){
			
			//HtmlElement e = (HtmlElement)o;
			
			System.out.println("---------------------------------------------------------------------------");
			HtmlAnchor anchor = (HtmlAnchor) o;
			if(anchor != null){
				String link = anchor.getAttribute("href");
				System.out.println(link);
				pageList.add(this.webSite + link);
			}
		}
		if(this.scheduler != null){
			this.scheduler.putPages(this.getId(), pageList);
		}
	}
	@Override
	public void parseDetailPage(HtmlPage p, String name) {
		//System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
		String creator = "";
		String id = "";
		String time = "";
		String forkedFrom = "";
		String description = "";
		String content = "";
		String language = "";
		String lineNum = "";
		String title = "";
		String homeUrl = "";
		String tag = "";
		
		SnippetCapture gist = new SnippetCapture();
		
		//System.out.println(p.toString());
		
		try{
			HtmlAnchor anchor = ((HtmlAnchor) p.getFirstByXPath(".//span[@class='project_title']/span[@class='username']/a"));
			if(anchor != null){
				creator = anchor.getTextContent();
				homeUrl = this.webSite + anchor.getHrefAttribute();
				gist.setCreator(creator);
				System.out.println("-----creator-----");
				System.out.println(creator);
			}
			anchor = ((HtmlAnchor) p.getFirstByXPath(".//span[@class='project_title']/span[@class='nowrap']/strong/a"));
			if(anchor != null){
				title = anchor.getTextContent();
				gist.setName(title);
				System.out.println("-----title-----");
				System.out.println(title);
			}
			HtmlElement element = ((HtmlElement) p.getFirstByXPath(".//div[@class='pull-left']/span[3]"));
			if(element != null){
				language = element.getTextContent();
				language = language.replace("\n", "");
				gist.setLanguage(language);
				System.out.println("-----language-----");
				System.out.println(language);
			}
			element = ((HtmlElement) p.getFirstByXPath(".//div[@class='pull-left']/span[2]"));
			if(element != null){
				lineNum = element.getTextContent();
				lineNum = lineNum.replace("行", "");
				lineNum = lineNum.replace("\n", "");
				gist.setOs(lineNum);
				System.out.println("-----lines-----");
				System.out.println(lineNum);
			}
			
			element = ((HtmlElement) p.getFirstByXPath(".//div[@class='project_top']/p"));
			if(element != null){
				description = element.getTextContent();
				description = description.replace("\n", "");
				gist.setComment(description);
				System.out.println("-----description-----");
				System.out.println(description);
			}
			try{
				List<?> words =  p.getByXPath(".//div[@class='project_top']");
				element = (HtmlElement)words.get(0);
				DomNodeList<DomNode> cl = element.getChildNodes();
				DomNode dn = cl.get(5);
				tag = dn.asText();
				gist.setTag(tag);
				System.out.println("-----tags-----");
				System.out.println(tag);
			}catch(Exception e){
				System.out.println(e.toString());
			}
			
			try{
				gist.setSnippet_key(this.getId(p.getUrl().toString()));
				System.out.println(gist.getSnippet_key());
				List<?> lines =  p.getByXPath(".//div[@class='line']");
				//System.out.println(lines.toString());
				for(Object line : lines){
					element = (HtmlElement)line;
					//System.out.println(element.getAttribute("id"));
					content += element.asText() + "\r\n";
					//System.out.println(element.asText());
				}
				gist.setCode(content);
				System.out.println("-----content-----");
				//System.out.println(content);
			}catch(Exception e){
				System.out.println(e.toString());
			}
			
			gist.setTime(this.getDate(homeUrl, gist.getSnippet_key()));
			
			gist.setIde("CSDN");
			if(gist.getCode().length() > 0){
				Capture_msg result = (Capture_msg) Utils.captureSnippt(gist);
				System.out.println(result.getMsg().getCapture_result());
			}
		}catch(Exception e){
			System.out.println(e.toString());
			System.out.println(e.getStackTrace().toString());
			System.out.println(Thread.getAllStackTraces().toString());
		}
	}

	private String getDate(String url, String id){
		String date = "";
		WebClient webClient2;
		///users/laiquhecong/snippets
		//https://code.csdn.net/laiquhecong?tab=2
		url = url.replace("http", "https");
		url = url.replace("net/", "net/users/");
		url = url.replace("?tab=2", "/snippets");
		
		webClient2 = new WebClient();
		webClient2.setJavaScriptEnabled(true);
		webClient2.setCssEnabled(false);
		webClient2.setAjaxController(new NicelyResynchronizingAjaxController());
		webClient2.setTimeout(350000);
		webClient2.setThrowExceptionOnScriptError(false);
		webClient2.setIncorrectnessListener( new SilentIncorrectnessListener() ) ;
		webClient2.setCssErrorHandler( new QuietCssErrorHandler() ) ;
		webClient2.getOptions().setUseInsecureSSL(true);
		
		try {
			HtmlPage homePage = webClient2.getPage(url);
			//System.out.println(homePage.asText());
			this.savePage(url, savePath + "/home_" + id + ".html", homePage);
			
			List<?> gistList =  homePage.getByXPath(".//dd[@class='pull-right explore_des']/dl/dt/a[2]");
			
			for(Object o: gistList){
				HtmlAnchor anchor = (HtmlAnchor) o;
				if(anchor != null){
					String link = anchor.getAttribute("href");
					if(link.indexOf(id) >= 0){
						DomNode e = anchor.getParentNode().getParentNode();
						DomNodeList<DomNode> cl = e.getChildNodes();
						if(cl.size() >= 2){
							date = cl.get(2).getTextContent();
							//最近一次更新：2013年11月01日
							date = date.replace("最近一次更新：", "");
							date = date.replace("年", "-");
							date = date.replace("月", "-");
							date = date.replace("日", "T00:00:00Z");
							System.out.println(date);
							break;
						}
					}
					
				}
			}
			if(date.equals("")){
				
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return date;
	}
	@Override
	public void run(){
		this.crawlAll();
	}

	@Override
	public String getId(String url) {
		//https://code.csdn.net/snippets/52480
		int index = url.indexOf("snippets/");
		String id = url.substring(index + 9);
		return id;
	}

	@Override
    public String getNextListUrl(String currListUrl, int policy) {
		String next = currListUrl;
	    if(policy == Scheduler.SCHEDULE_POLICY_INSC){
	    	//gist.github.com/discover?page=1
	    	int index = currListUrl.lastIndexOf("=");
	    	if(index > 0){
	    		int pageNum = Integer.parseInt(currListUrl.substring(index + 1));
	    		pageNum++;
	    		next = currListUrl.substring(0, index + 1);
	    		next += pageNum;
	    	}
	    }
	    return next;
    }
}
