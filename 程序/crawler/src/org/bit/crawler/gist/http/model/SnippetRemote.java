package org.bit.crawler.gist.http.model;

import org.bit.crawler.gist.http.struct.Snippet;



public class SnippetRemote extends Snippet{
	
	private int snippet_id;
	private String create_time;
	
	
	public SnippetRemote(String name, String tag, String language, String compiler,
			String ide, String os, String code, String comment,
			String accessibility) {
		super(name, tag, language, compiler, ide, os, code, comment, accessibility);
		// TODO Auto-generated constructor stub
	}


	public SnippetRemote(int snippet_id,String name, String tag, String language,
			String compiler, String ide, String os, String code,
			String comment, String accessibility, 
			String create_time) {
		super(name, tag, language, compiler, ide, os, code, comment,
				accessibility);
		this.snippet_id = snippet_id;
		this.create_time = create_time;
	}


	


	public int getSnippet_id() {
		return snippet_id;
	}


	public void setSnippet_id(int snippet_id) {
		this.snippet_id = snippet_id;
	}


	public String getCreate_time() {
		return create_time;
	}


	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}


	@Override
	public String toString() {
		return "SnippetRemote [snippet_id=" + snippet_id + ", create_time="
				+ create_time + ", getName()=" + getName() + ", getTag()="
				+ getTag() + ", getLanguage()=" + getLanguage()
				+ ", getCompiler()=" + getCompiler() + ", getIde()=" + getIde()
				+ ", getOs()=" + getOs() + ", getCode()=" + getCode()
				+ ", getComment()=" + getComment() + ", getAccessibility()="
				+ getAccessibility() + " ]";
	}


	
	
	
	
}
