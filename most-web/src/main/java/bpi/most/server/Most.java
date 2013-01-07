package bpi.most.server;

import bpi.most.server.utils.PollService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Server side initialization during startup 
 * @author robert.zach@tuwien.ac.at
 */
public class Most implements ServletContextListener{
	
	private static final Logger LOG = LoggerFactory.getLogger(Most.class);
	
	public static final int POLL_INTERVAL = 10000;
	public static final int POLL_INTERVAL_WARNING = 10000;
	
	//create service to poll and notify DP with new measurements
	//only used for DPs not using the Java abstraction layer for adding new values.
	//set 10 seconds poll interval
	PollService pollService = PollService.getInstance(POLL_INTERVAL, POLL_INTERVAL);
	
	//ServletContext context;
	public void contextInitialized(ServletContextEvent contextEvent) {
		LOG.info("Context Created");
		pollService.start();
		
		//context = contextEvent.getServletContext();
		// set variable to servlet context
		//context.setAttribute("TEST", "TEST_VALUE");
	}
	
	public void contextDestroyed(ServletContextEvent contextEvent) {
		//context = contextEvent.getServletContext();
		LOG.info("Context Destroyed");
		pollService.stop();
	}

}
