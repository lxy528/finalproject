package org.bit.crawler.gist.http.model;

import java.util.List;



public class Snippets_msg {
	int ret;
	Snippets msg = new Snippets();
	public Snippets_msg(int ret, Snippets msg) {
		super();
		this.ret = ret;
		this.msg = msg;
	}
	public Snippets_msg() {
		super();
		// TODO Auto-generated constructor stub
	}
	public int getRet() {
		return ret;
	}
	public void setRet(int ret) {
		this.ret = ret;
	}
	public Snippets getMsg() {
		return msg;
	}
	public void setMsg(Snippets msg) {
		this.msg = msg;
	}
	@Override
	public String toString() {
		return "Snippets_msg [ret=" + ret + ", msg=" + msg + "]";
	}
	
	
	
		public class Snippets{
			int count;
			List<SnippetRemote> snippets = null;
			public Snippets(int count, List<SnippetRemote> snippets) {
				super();
				this.count = count;
				this.snippets = snippets;
			}
			public Snippets() {
				super();
				// TODO Auto-generated constructor stub
			}
			public int getCount() {
				return count;
			}
			public void setCount(int count) {
				this.count = count;
			}
			public List<SnippetRemote> getSnippets() {
				return snippets;
			}
			public void setSnippets(List<SnippetRemote> snippets) {
				this.snippets = snippets;
			}
			@Override
			public String toString() {
				return "Snippets [count=" + count + ", snippets=" + snippets
						+ "]";
			}
			
			
		}

}
