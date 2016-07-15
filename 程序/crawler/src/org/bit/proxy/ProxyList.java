package org.bit.proxy;

import java.util.ArrayList;

public class ProxyList {
	ArrayList<CrawlerProxy> proxyList;
	String srcWeb;
	
	public ProxyList(){
		this.srcWeb = "https://hidemyass.com/proxy-list/";
		this.proxyList = new ArrayList<CrawlerProxy>();
	}
	public ProxyList(String src){
		this.srcWeb = src;
	}
	
	public void buildList(){
		
	}
	public void buildList2(){
		CrawlerProxy p = new CrawlerProxy();
		p.setIp("195.103.219.108");
		p.setPort("8080");
		this.proxyList.add(p);
		
		p = new CrawlerProxy();
		p.setIp("95.31.42.89");
		p.setPort("3128");
		this.proxyList.add(p);
		
		p = new CrawlerProxy();
		p.setIp("85.185.45.234");
		p.setPort("80");
		this.proxyList.add(p);
		
		p = new CrawlerProxy();
		p.setIp("80.178.157.92");
		p.setPort("5555");
		this.proxyList.add(p);
		
	}
	public synchronized ArrayList<CrawlerProxy> mallocProxy(int thrdNum){
		ArrayList<CrawlerProxy> list = new ArrayList<CrawlerProxy>();
		list.add(this.proxyList.remove(0));
		return list;
	}
	
	public synchronized void freeProxy(ArrayList<CrawlerProxy> list){
		this.proxyList.addAll(list);
	}
}
