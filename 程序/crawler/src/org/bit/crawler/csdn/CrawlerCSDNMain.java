package org.bit.crawler.csdn;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bit.proxy.ProxyList;
import org.bit.scheduler.Scheduler;

public class CrawlerCSDNMain {
	public static ArrayList<Scheduler> schedulerList = new ArrayList<Scheduler>();
	public static int thrdNum = 4;
	public static String startPage = "";
	public static int schdlPolicy = Scheduler.SCHEDULE_POLICY_AUTO;
	
	public static void doShutDownWork() {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override 
			public void run() {
				System.out.println("Do shut down work!");
	
				for(Scheduler s: schedulerList){
					s.saveInfo();
				}
			}
		});
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if(args.length >= 1){
			thrdNum = Integer.parseInt(args[0]);
		}
		if(args.length >= 2){
			startPage = args[1];
		}
		
		if(args.length >= 3){
			schdlPolicy = Integer.parseInt(args[2]);
		}
		
		Logger logger = Logger.getLogger ("");
		logger.setLevel (Level.OFF);
		doShutDownWork();
		
		ProxyList pList = new ProxyList();
		pList.buildList2();
		
		Scheduler s = null;
		
		s =  new Scheduler("csdn", 1);
		s.setStartPage(startPage);
		s.setSchdlPolicy(schdlPolicy);
		
		schedulerList.add(s);
		Thread tcsdn = new Thread(s);
		tcsdn.start();
		
		try {
			tcsdn.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
