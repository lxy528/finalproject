package org.bit.crawler.gist.http.apitest;

import org.bit.crawler.gist.http.Utils;
import org.bit.crawler.gist.http.model.User;
import org.bit.crawler.gist.http.struct.Snippet;
import org.bit.crawler.gist.http.struct.SnippetCapture;

public class ApiTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//Utils.getUser(new User("lwz", "123123"));
		  Snippet s = new Snippet("hello", "", "java", "javac", "eclipse", "win 7", "System.out.println(\"hello world\");", "", "private");
		  SnippetCapture sc = new SnippetCapture("adadfjl234llkjfn", "hello", "", "java", "javac", "eclipse", "win 7", "System.out.println(\"hello world\");", "", "private");
		  System.out.println(Utils.getToken(new User("lwz","123123")).toString());
		  System.out.println(Utils.pullSnippet().toString());
		  System.out.println(Utils.pushSnippet(s).toString());
		  System.out.println(Utils.captureSnippt(sc));
		  
	}

}
