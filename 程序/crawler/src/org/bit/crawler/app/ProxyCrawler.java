package org.bit.crawler.app;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

import org.bit.crawler.BasicCrawler;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class ProxyCrawler extends BasicCrawler {

	@Override
	public void parseListPage(HtmlPage listPage) {

	}

	@Override
	public void parseDetailPage(HtmlPage p, String name) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getId(String url) {
		// TODO Auto-generated method stub
		return null;
	}

	public void crawProxyList(String url){
		try {
			HtmlPage p = this.webClient.getPage(url);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

	@Override
    public String getNextListUrl(String currListUrl, int policy) {
	    // TODO Auto-generated method stub
	    return null;
    }
}
