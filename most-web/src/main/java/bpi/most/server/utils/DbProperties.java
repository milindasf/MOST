package bpi.most.server.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * database properties which are read from JNDI.
 * for now, the values are configured in web.xml
 * Support for properties file "config.properties" will be removed soon
 * @author harald
 *
 */
public final class DbProperties {
	
	private static final Logger LOG = LoggerFactory.getLogger(DbProperties.class);
	
	private static final Integer CON_POOL_MIN = 10;
	private static final Integer CON_POOL_MAX = 100;
	
	private static DbProperties instance = new DbProperties();

	/**
	 * for testing purposes;
	 * cause in jetty, jndi does not work. (of course it would
	 * work, but therefore some configuration has to be done)
	 * 
	 * this way, when jndi exception occurs, the values stay
	 * as they got initialized.
	 */
	private String hostname = "demo-most.bpi.tuwien.ac.at";
	private String username = "mostsoc";
	private String password = "demo12";
	private String database = "mon_development";
	private Integer conPoolMin = CON_POOL_MIN;
	private Integer conPoolMax = CON_POOL_MAX;
	
	/**
	 * initializes the values from JNDI
	 */
	private DbProperties(){
		//A) try to get config from proporties file (will be removed soon)
		Config config = new Config("config.properties");
		if (config.getProperty("hostname") != null) {
			hostname = config.getProperty("hostname");
			username = config.getProperty("username");
			password = config.getProperty("password");
			database = config.getProperty("database");
			conPoolMin = Integer.parseInt(config.getProperty("conPoolMin"));
			conPoolMax = Integer.parseInt(config.getProperty("conPoolMax"));
		}
		

		//B) get config from web.xml
		try {
			Context initCtx = new InitialContext();
			Context dbCtx = (Context) initCtx.lookup("java:comp/env/db");
			hostname = (String) dbCtx.lookup("hostname");
			username = (String) dbCtx.lookup("username");
			password = (String) dbCtx.lookup("password");
			database = (String) dbCtx.lookup("database");
			conPoolMin = (Integer) dbCtx.lookup("conPoolMin");
			conPoolMax = (Integer) dbCtx.lookup("conPoolMax");
			
		} catch (NamingException e) {
			LOG.warn("no context supported!");
			//e.printStackTrace();
		}
		LOG.info("using the following db config:");
		LOG.info("hostname: " + hostname + " username: " + username + " database: " + database);
	}

	public static String getHostname() {
		return instance.hostname;
	}
	public static String getUsername() {
		return instance.username;
	}
	public static String getPassword() {
		return instance.password;
	}
	public static String getDatabase() {
		return instance.database;
	}
	public static Integer getConPoolMin() {
		return instance.conPoolMin;
	}
	public static Integer getConPoolMax() {
		return instance.conPoolMax;
	}
/*
	public static String HOSTNAME = "demo-most.bpi.tuwien.ac.at";
	public static String USERNAME = "mostsoc";
	public static String PASSWORD = "demo12";
	public static String DATABASE = "mon_development";
	public static int CONNECTION_POOL_MIN = 10;
	public static int CONNECTION_POOL_MAX = 100;
*/	
}
