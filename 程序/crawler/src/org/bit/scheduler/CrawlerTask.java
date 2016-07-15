package org.bit.scheduler;

import java.util.ArrayList;

public class CrawlerTask {
	private ArrayList<String> details;
	private ArrayList<String> lists;
	
	public CrawlerTask(){
		this.details = new ArrayList<String>();
		this.lists = new ArrayList<String>();
	}
	public void setDetails(ArrayList<String> details) {
		this.details = details;
	}
	public ArrayList<String> getDetails() {
		return details;
	}
	public void setLists(ArrayList<String> lists) {
		this.lists = lists;
	}
	public ArrayList<String> getLists() {
		return lists;
	}
	
	public boolean isEmpty(){
		if(this.lists.size() == 0 && this.details.size() == 0){
			return true;
		}
		else{
			return false;
		}
	}
}
