package org.bit.crawler.gist;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bit.crawler.gist.http.Utils;
import org.bit.crawler.gist.http.model.Capture_msg;
import org.bit.crawler.gist.http.struct.SnippetCapture;
import org.bit.proxy.ProxyList;
import org.bit.scheduler.Scheduler;

public class ParserMain {

	public static void main(String[] args) throws IOException {
	
		if(args.length < 1){
			System.out.println("USAGE: java -jar gistParser.jar dir");
			return;
		}
		
		File data = new File(args[0]);
		File[] dayList = data.listFiles();
		for(int i = 0; i < dayList.length; i++){
			File folder = new File(dayList[i].getAbsolutePath());
			File[] fileList = folder.listFiles();
			for(int j = 0; j < fileList.length; j++){
				try{
					String key = fileList[j].getName().replace(".html", "");
					key = key.substring(key.indexOf("-") + 1);
					SnippetCapture gist = new SnippetCapture();
					gist.setSnippet_key(key);
					BufferedReader in = new BufferedReader(new FileReader(fileList[j]));
		            String str = "";
		            String line = "";
		            while ((line = in.readLine()) != null)     {
		                  str += line;
		            }
		            in.close();
		            
		            String description = "";
		            int index = str.indexOf("gist-description"); 
		            if(index >= 0){
		            	description = str.substring(index);
		            	index = description.indexOf("<div>");
		            	description = description.substring(index + 5);
		            	index = description.indexOf("</div>");
		            	description = description.substring(0, index);
		            }
		            gist.setComment(description);
		            
		            index = str.indexOf("forked from ");
		            if(index >= 0){
		            	str = str.substring(index);
		            	index = str.indexOf(">");
		            	str = str.substring(index + 1);
		            	index = str.indexOf("<");
		            	str = str.substring(0, index);
		            	
		            }
		            else{
		            	str = "";
		            }
		            gist.setForkedFrom(str);
		            
		            if(description.length() > 0 || str.length() > 0){
		            	Capture_msg result = (Capture_msg) Utils.updateSnippt(gist);
						System.out.println(result.getMsg().getCapture_result());
		            }
		            System.out.println(key + ":" + description + ":" + str);
				}catch(Exception e){
					System.out.println(e.toString());
				}
	            
			}
		}
		
	}
}
