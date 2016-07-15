package org.bit.crawler.gist.http.struct;

public class SnippetCapture extends Snippet{
	
	private String snippet_key;
	private String creator = "";
	private String time = "";
	private String forkedFrom = "";
	
	public SnippetCapture(String name, String tag, String language,
			String compiler, String ide, String os, String code,
			String comment, String accessibility) {
		super(name, tag, language, compiler, ide, os, code, comment, accessibility);
		// TODO Auto-generated constructor stub
	}

	public SnippetCapture(String snippet_key,String name, String tag, String language,
			String compiler, String ide, String os, String code,
			String comment, String accessibility) {
		super(name, tag, language, compiler, ide, os, code, comment,
				accessibility);
		this.setSnippet_key(snippet_key);
	}

	public SnippetCapture(){
		
	}
	public String getSnippet_key() {
		return snippet_key;
	}

	public void setSnippet_key(String snippet_key) {
		this.snippet_key = snippet_key;
	}

	@Override
	public String toString() {
		return "SnippetCapture [snippet_key=" + getSnippet_key() + ", getTime()=" + getTime() + 
				", getCreator()=" + getCreator() + ", getForkedFrom=" + getForkedFrom() + ", getName()="
				+ getName() + ", getTag()=" + getTag() + ", getLanguage()="
				+ getLanguage() + ", getCompiler()=" + getCompiler()
				+ ", getIde()=" + getIde() + ", getOs()=" + getOs()
				+ ", getCode()=" + getCode() + ", getComment()=" + getComment()
				+ ", getAccessibility()=" + getAccessibility()
				+  "]";
	}

	public String getCreator() {
	    return creator;
    }

	public void setCreator(String creator) {
	    this.creator = creator;
    }

	public String getTime() {
	    return time;
    }

	public void setTime(String time) {
	    this.time = time;
    }

	public String getForkedFrom() {
	    return forkedFrom;
    }

	public void setForkedFrom(String forkedFrom) {
	    this.forkedFrom = forkedFrom;
    }

	

	
}
