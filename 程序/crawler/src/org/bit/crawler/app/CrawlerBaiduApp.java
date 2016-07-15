package org.bit.crawler.app;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import org.apache.commons.io.IOUtils;
import org.bit.crawler.BasicCrawler;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.UnexpectedPage;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;


public class CrawlerBaiduApp extends BasicCrawler  {

	public CrawlerBaiduApp(String dataPath){
		super("http://shouji.baidu.com/",
				 "http://shouji.baidu.com/software/list?cid=0&board_type=topnew&from=&f=list_software_0%40sort_popular_1%40&page_num=1",
				 "javascript:void\\(0\\);",
				 "/software/item\\?docid=\\d+.*",
				 dataPath);
	}
	
	@Override
	public void parseListPage(HtmlPage listPage) {
		ArrayList<String> urls = new ArrayList<String>();
		//System.out.println(listPage.asXml());
		ArrayList<String> lists = new ArrayList<String>();
		ArrayList<String> pages = new ArrayList<String>();
		
		List<HtmlAnchor> l = listPage.getAnchors();	
		for(HtmlAnchor a : l){
			String href = a.getHrefAttribute();
			Matcher lm = listPtn.matcher(href);
			//System.out.println(href);
			if(lm.matches()){
				String strNum = a.getTextContent();
				if(strNum.matches("\\d+")){
					href = this.getStartUrl().substring(0, this.getStartUrl().length() - 1) + strNum;
					/*if(this.visitedListUrls.indexOf(href) < 0 && listUrls.indexOf(href) < 0){
						listUrls.add(href);
					}*/
					lists.add(href);
				}
				continue;
			}
			href = a.getHrefAttribute();
			Matcher dm = pagePtn.matcher(href);
			//if(dm.matches() && this.downloadedUrls.indexOf(this.webSite + href) < 0){
			if(dm.matches()){
				pages.add(this.webSite + href);
			}
		}
		if(this.scheduler != null){
			this.scheduler.putLists(this.getId(),lists);
			this.scheduler.putPages(this.getId(), pages);
		}
	}
	@Override
	public void parseDetailPage(HtmlPage p, String name) {
		List<HtmlAnchor> l = p.getAnchors();	
		for(HtmlAnchor a : l){
			if(a.getHrefAttribute().matches("http://.*apk")){
				try {
					UnexpectedPage detailPage = this.webClient.getPage(a.getHrefAttribute());
					InputStream is = detailPage.getInputStream();
					FileOutputStream output = new FileOutputStream(this.savePath + "//" + name + ".apk");
					IOUtils.copy(is, output);
			        output.close();
			        is.close();
					//detailPage.save(new File(this.savePath + "1.apk"));
				}catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void run(){
		//this.loadInfo();
		//this.rebuildUrls();
		this.crawlAll();
		//this.saveInfo();
	}

	@Override
	public String getId(String url) {
		int index = url.indexOf("docid=");
		String id = url.substring(index + 6);
		index = id.indexOf("&");
		id = id.substring(0, index);
		return id;
	}

	@Override
    public String getNextListUrl(String currListUrl, int policy) {
	    // TODO Auto-generated method stub
	    return null;
    }
}
