package org.bit.proxy;

public class CrawlerProxy {
	private String ip;
	private String port;
	private String source;
	public void setSource(String source) {
		this.source = source;
	}
	public String getSource() {
		return source;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getIp() {
		return ip;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getPort() {
		return port;
	}

}
