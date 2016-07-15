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


public class Crawler360App extends BasicCrawler  {

	public Crawler360App(String dataPath){
		super("http://zhushou.360.cn/",
				"http://zhushou.360.cn/list/index/cid/1/order/newest?page=1",
				"http://zhushou\\.360\\.cn/list/index/cid/1/order/newest\\\\?page=\\d+",
				"/detail/index/soft_id/\\d+",
				dataPath);
	}
	
	@Override
	public void parseListPage(HtmlPage listPage) {
		ArrayList<String> lists = new ArrayList<String>();
		ArrayList<String> pages = new ArrayList<String>();
		
		ArrayList<String> urls = new ArrayList<String>();
		//System.out.println(listPage.asXml());
		
		List<HtmlAnchor> l = listPage.getAnchors();	
		for(HtmlAnchor a : l){
			String href = a.getHrefAttribute();
			//System.out.println(href);
			if(href.indexOf("javascript:pg.toPage") >= 0){
				//ScriptResult sr = listPage.executeJavaScriptIfPossible(href, href, 1);  
				//ScriptResult sr = listPage.executeJavaScript(href);
				//href = sr.getNewPage().getUrl().toString();
				try {
					//Page p = a.click();
					//href = p.getUrl().toString();
					//System.out.println(p.getWebResponse().getContentAsString());
					//Matcher lm = listPtn.matcher(href);
					int index = href.indexOf("(");
					if(index > 0){
						href = href.substring(href.indexOf("(") + 1, index + 2);
						href = "http://zhushou.360.cn/list/index/cid/1/order/newest?page=" + href;
						/*if(this.visitedListUrls.indexOf(href) < 0 && listUrls.indexOf(href) < 0){
							listUrls.add(href);
						}*/
						lists.add(href);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				continue;
			}
			/*if(lm.matches() && listUrls.indexOf(this.webSite + href) < 0){
				listUrls.add(this.webSite + href);
			}*/
			href = a.getHrefAttribute();
			Matcher dm = pagePtn.matcher(href);
			//if(dm.matches() && this.downloadedUrls.indexOf(this.webSite + href) < 0){
			if(dm.matches()){
				//urls.add(this.webSite + href);
				pages.add(this.webSite + href);
			}
		}
		if(this.scheduler != null){
			this.scheduler.putLists(this.getId(), lists);
			this.scheduler.putPages(this.getId(), pages);
		}
		//return urls;
	}
	@Override
	public void parseDetailPage(HtmlPage p, String name) {
		String c = p.getWebResponse().getContentAsString();
		int index = c.indexOf("'downurl':'");
		if(index > 0){
			c = c.substring(index + 11);
			String apkUrl = c.substring(0, c.indexOf("'"));
			try {
				UnexpectedPage detailPage = this.webClient.getPage(apkUrl);
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

	@Override
	public void run(){
		//this.loadInfo();
		//this.rebuildUrls();
		this.crawlAll();
		//this.saveInfo();
	}

	@Override
	public String getId(String url) {
		int index = url.lastIndexOf("/");
		String id = url.substring(index + 1);
		return id;
	}

	@Override
    public String getNextListUrl(String currListUrl, int policy) {
	    // TODO Auto-generated method stub
	    return null;
    }
}
