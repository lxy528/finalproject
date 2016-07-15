package org.bit.crawler.gist.http;

import org.bit.crawler.gist.http.model.Snippets_msg;
import org.bit.crawler.gist.http.model.User;

public class test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		//Utils.getUser(new User("gag","123"));
		Utils.getToken(new User("lwz","123123"));
		Snippets_msg tMsg = (Snippets_msg) Utils.pullSnippet();
		System.out.println(tMsg.getMsg().toString());

	}

}
