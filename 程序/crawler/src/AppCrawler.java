

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bit.proxy.ProxyList;
import org.bit.scheduler.Scheduler;

public class AppCrawler {
	public static ArrayList<Scheduler> schedulerList = new ArrayList<Scheduler>();
	public static int thrdNum = 1;
	
	public static void doShutDownWork() {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override 
			public void run() {
				System.out.println("hahaah");
	
				for(Scheduler s: schedulerList){
					s.saveInfo();
					//System.out.println("save info " + c.webSite);
				}
			}
		});
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if(args.length > 1){
			thrdNum = Integer.parseInt(args[0]);
		}
		
		Logger logger = Logger.getLogger ("");
		logger.setLevel (Level.OFF);
		doShutDownWork();
		
		ProxyList pList = new ProxyList();
		pList.buildList2();
		
		Scheduler s =  new Scheduler("360", 1);
		schedulerList.add(s);
		Thread t360 = new Thread(s);
		
		s =  new Scheduler("baidu", 1);
		schedulerList.add(s);
		Thread tbaidu = new Thread(s);
		
		t360.start();
		tbaidu.start();
		
		try {
			t360.join();
			tbaidu.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
