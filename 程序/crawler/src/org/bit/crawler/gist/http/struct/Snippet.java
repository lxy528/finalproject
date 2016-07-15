package org.bit.crawler.gist.http.struct;

public class Snippet {
	private String name;
	private String tag;
	private String language;
	private String compiler;
	private String ide;
	private String os;
	private String code;
	private String comment;
	private String accessibility;
	
	public Snippet(String name, String tag, String language, String compiler,
			String ide, String os, String code, String comment,
			String accessibility) {
		super();
		this.name = name;
		this.tag = tag;
		this.language = language;
		this.compiler = compiler;
		this.ide = ide;
		this.os = os;
		this.code = code;
		this.comment = comment;
		this.accessibility = accessibility;
	}
	public Snippet(){
		
	}
	
	public String getName() {
	    return name;
    }
	public void setName(String name) {
	    this.name = name;
    }
	public String getTag() {
	    return tag;
    }
	public void setTag(String tag) {
	    this.tag = tag;
    }
	public String getLanguage() {
	    return language;
    }
	public void setLanguage(String language) {
	    this.language = language;
    }
	public String getCompiler() {
	    return compiler;
    }
	public void setCompiler(String compiler) {
	    this.compiler = compiler;
    }
	public String getIde() {
	    return ide;
    }
	public void setIde(String ide) {
	    this.ide = ide;
    }
	public String getOs() {
	    return os;
    }
	public void setOs(String os) {
	    this.os = os;
    }
	public String getCode() {
	    return code;
    }
	public void setCode(String code) {
	    this.code = code;
    }
	public String getComment() {
	    return comment;
    }
	public void setComment(String comment) {
	    this.comment = comment;
    }
	public String getAccessibility() {
	    return accessibility;
    }
	public void setAccessibility(String accessibility) {
	    this.accessibility = accessibility;
    }
	
	
	
}
