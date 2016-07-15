package org.bit.crawler.gist.http;


import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import org.bit.crawler.gist.http.model.ApiError;
import org.bit.crawler.gist.http.model.Capture_msg;
import org.bit.crawler.gist.http.model.Snippets_msg;
import org.bit.crawler.gist.http.model.Token_msg;
import org.bit.crawler.gist.http.model.User;
import org.bit.crawler.gist.http.struct.Snippet;
import org.bit.crawler.gist.http.struct.SnippetCapture;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;



public class Utils {
	
	private static final String TAG = Utils.class.getName();
	
	
	public static Gson GSON;

    public static Gson getGson() {
        if (GSON == null) {
            GSON = new GsonBuilder()
                    .serializeNulls()
                    .create();
        }
        return GSON;
    }
    
	
	
    public static Object parse(String result, Type type) throws ApiError {
       // System.out.println(result);
        //return Utils.getGson().fromJson(result, type);
    	JsonParser parser = new JsonParser();
        JsonObject jsonObject = parser.parse(result).getAsJsonObject();
        int ret = jsonObject.get("ret").getAsInt();
        if (ret == 0) {
            return Utils.getGson().fromJson(result, type);
        } else {
            ApiError error = Utils.getGson().fromJson(result, ApiError.class);
            throw error;
        }
    }    
    
    
	
	public static Object pullSnippet()
	{
		Snippets_msg msg = null;
		try {
			msg = (Snippets_msg) server.Get("p=mysnippets.snippetsManager.pullSnippet&access_token="+Constant.access_token,Snippets_msg.class);
		} catch (ApiError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//System.out.println(msg);
		return msg;
	}
	
	public static Object getUser(User user)
	{
		Map<String, String> data = new HashMap<String, String>();
        data.put("username", user.getUsername());
        data.put("password", user.getPassword());
        
        Token_msg msg = null;
		try {
			msg = (Token_msg) server.Post(data,"p=mysnippets.auth.getUser",Token_msg.class);
			
		} catch (ApiError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Constant.access_token = msg.getMsg().getAccess_token();
	//	System.out.println(msg);
        return msg;
        
	}
	public static Object getToken(User user)
	{
		Map<String, String> data = new HashMap<String, String>();
        data.put("username", user.getUsername());
        data.put("password", user.getPassword());
        
        Token_msg msg = null;
		try {
			msg = (Token_msg) server.Post(data,"p=mysnippets.auth.getToken",Token_msg.class);
			
		} catch (ApiError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Constant.access_token = msg.getMsg().getAccess_token();
		//System.out.println(msg);
        return msg;
		
	}
	
	public static Object pushSnippet(Snippet s) 
	{
		/*
		 * 封装要上传的数据，
		 * url;
		 * 在调用接口
		 */
		Map<String, String> data = new HashMap<String, String>();
		data.put("access_token", Constant.access_token);
        data.put("name", s.getName());
        data.put("tag", s.getTag());
        data.put("language", s.getLanguage());
        data.put("compiler", s.getCompiler());
        data.put("ide", s.getIde());
        data.put("os", s.getOs());
        data.put("code", s.getCode());
        data.put("comment", s.getComment());
        data.put("accessibility", s.getAccessibility());

        Snippets_msg msg = null;
		try {
			msg = (Snippets_msg) server.Post(data,"p=mysnippets.snippetsManager.pushSnippet",Snippets_msg.class);
		} catch (ApiError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println(msg.getMsg().toString());
		return msg;
	}
	
	public static Object captureSnippt(SnippetCapture s)
	{
		Map<String, String> data = new HashMap<String, String>();
		data.put("snippet_key", s.getSnippet_key());
		data.put("creator", s.getCreator());
		data.put("time", s.getTime());
		data.put("forked_from", s.getAccessibility());
		data.put("name", s.getName());
        data.put("tag", s.getTag());
        data.put("language", s.getLanguage());
        data.put("compiler", s.getCompiler());
        data.put("ide", s.getIde());
        data.put("os", s.getOs());
        data.put("code", s.getCode());
        data.put("comment", s.getComment());
        data.put("accessibility", s.getAccessibility());
        
        Capture_msg msg =null;
        try {
			msg = (Capture_msg) server.Post(data,"p=mysnippets.snippetsManager.captureSnippet",Capture_msg.class);
		} catch (ApiError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println(msg.getMsg().toString());
		return msg;
        
	}
	
	public static Object updateSnippt(SnippetCapture s)
	{
		Map<String, String> data = new HashMap<String, String>();
		data.put("snippet_key", s.getSnippet_key());
		data.put("forked_from", s.getForkedFrom());
        data.put("comment", s.getComment());
        
        Capture_msg msg =null;
        try {
			msg = (Capture_msg) server.Post(data,"p=mysnippets.snippetsManager.update",Capture_msg.class);
		} catch (ApiError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println(msg.getMsg().toString());
		return msg;
        
	}
	
}
