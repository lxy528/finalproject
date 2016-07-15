package org.bit.crawler.gist.http;


import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import org.bit.crawler.gist.http.model.ApiError;

public class server {
	
	
	
	public static Object Get(String url,Type type) throws ApiError {
		
		AtomicReference<String> body = new AtomicReference<String>();
		
		HttpRequest.get(Constant.URL+"?"+url)
		        .acceptJson()
		        .acceptEncoding(HttpRequest.CHARSET_UTF8)
		        .body(body)
		        .disconnect();
		return  Utils.parse(body.get(), type);
	}
	
	public static Object Post(Map<String, String> data,String url,Type type) throws ApiError {
		
		 
	        
	        AtomicReference<String> body = new AtomicReference<String>();
	         HttpRequest.post(Constant.URL+"?"+url)
	        .acceptJson()
	        .acceptCharset(HttpRequest.CHARSET_UTF8)
	        .form(data)
	        .body(body);
	         
	         return  Utils.parse(body.get(), type);
	}
	
}
