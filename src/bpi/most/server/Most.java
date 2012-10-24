package bpi.most.server;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import bpi.most.server.utils.PollService;

/**
 * Server side initialization during startup 
 * @author robert.zach@tuwien.ac.at
 */
public class Most implements ServletContextListener{
	//create service to poll and notify DP with new measurements
	//only used for DPs not using the Java abstraction layer for adding new values.
	//set 10 seconds poll interval
	PollService pollService = PollService.getInstance(10000, 10000);
	
	//ServletContext context;
	public void contextInitialized(ServletContextEvent contextEvent) {
		System.out.println("Context Created");
		pollService.start();
		
		//context = contextEvent.getServletContext();
		// set variable to servlet context
		//context.setAttribute("TEST", "TEST_VALUE");
	}
	
	public void contextDestroyed(ServletContextEvent contextEvent) {
		//context = contextEvent.getServletContext();
		System.out.println("Context Destroyed");
		pollService.stop();
	}

}
