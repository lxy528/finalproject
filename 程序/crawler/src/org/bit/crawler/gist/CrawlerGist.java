package org.bit.crawler.gist;

import java.util.ArrayList;
import java.util.List;

import org.bit.crawler.BasicCrawler;
import org.bit.crawler.gist.http.Utils;
import org.bit.crawler.gist.http.model.Capture_msg;
import org.bit.crawler.gist.http.struct.SnippetCapture;
import org.bit.scheduler.Scheduler;

import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class CrawlerGist  extends BasicCrawler{
	public CrawlerGist(String dataPath){
		super("https://gist.github.com",
			   "https://gist.github.com/discover?page=120",
			  "https://gist.github.com/discover\\?page=\\d+",
			  "",
			  dataPath);
		this.webClient.getOptions().setUseInsecureSSL(true);
	}
	
	@Override
	public void parseListPage(HtmlPage listPage) {
		List<?> nextPagesAnchors = listPage.getByXPath(".//div[@class='pagination']/a");
		ArrayList<String> listList = new ArrayList<String>();
		
		for(Object o: nextPagesAnchors){
			listList.add(this.webSite + ((HtmlAnchor)o).getHrefAttribute());
		}
		this.scheduler.putLists(this.getId(), listList);
		
		String nextPage =  ((HtmlAnchor)listPage.getFirstByXPath("//div[@class='pagination']/a")).getHrefAttribute();
		//this.listUrls.add(this.webSite + nextPage);
		
		List<?> gistList =  listPage.getByXPath("//div[@class='gist gist-item']");
		//System.out.println(gistList.size());
		
		ArrayList<String> pageList = new ArrayList<String>();
		for(Object o: gistList){
			
			HtmlElement e = (HtmlElement)o;
			
			System.out.println("---------------------------------------------------------------------------");
			HtmlAnchor anchor = ((HtmlAnchor) e.getFirstByXPath(".//div/a[@class='link-overlay']"));
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
		
		SnippetCapture gist = new SnippetCapture();
		
		//System.out.println(p.toString());
		
		try{
			HtmlAnchor anchor = ((HtmlAnchor) p.getFirstByXPath(".//div[@class='gist-author']/span/span/a"));
			if(anchor != null){
				creator = anchor.getTextContent();
				gist.setCreator(creator);
				//System.out.println(creator);
			}
			anchor = ((HtmlAnchor) p.getFirstByXPath(".//div[@class='gist-author']/strong/a"));
			if(anchor != null){
				id = anchor.getHrefAttribute();
				int index = id.lastIndexOf("/");
				if(index >= 0){
					id = id.substring(index + 1);
				}
				//id = id.replace("gist:", "");
				gist.setSnippet_key(id);
				//System.out.println(id);
				//get language
				String file = anchor.getTextContent();
				int dotIndex = file.lastIndexOf(".");
				if(dotIndex > 0){
					file = file.substring(dotIndex + 1);
					gist.setLanguage(file);
				}
			}
			HtmlElement element = ((HtmlElement) p.getFirstByXPath(".//span[@class='datetime']/time"));
			if(element != null){
				time = element.getAttribute("datetime");
				gist.setTime(time);
				//System.out.println(time);
			}
			anchor = ((HtmlAnchor) p.getFirstByXPath(".//span[@class='text']/a"));
			if(anchor != null){
				forkedFrom = anchor.getTextContent();
				gist.setForkedFrom(forkedFrom);
				//System.out.println(forkedFrom);
			}
			element = ((HtmlElement) p.getFirstByXPath("//span[@class='description']/div"));
			if(element != null){
				description = ((HtmlElement) p.getFirstByXPath(".//span[@class='description']/div")).getTextContent();
				gist.setComment(description);
				//System.out.println(description);
			}
			
			List<?> lines =  p.getByXPath(".//div[@class='line']");
			//System.out.println(lines.toString());
			for(Object line : lines){
				element = (HtmlElement)line;
				//System.out.println(element.getAttribute("id"));
				content += element.asText() + "\r\n";
				//System.out.println(element.asText());
			}
			gist.setCode(content);
			
			if(gist.getCode().length() > 0){
				Capture_msg result = (Capture_msg) Utils.captureSnippt(gist);
				System.out.println(result.getMsg().getCapture_result());
			}
		}catch(Exception e){
			System.out.println(e.toString());
		}
	}

	@Override
	public void run(){
		this.crawlAll();
	}

	@Override
	public String getId(String url) {
		int index = url.indexOf("com/");
		String id = url.substring(index + 4);
		id = id.replace("/", "-");
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
