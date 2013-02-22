package bpi.most.connector;

/**
 * Contains variables of the config file "config.properties"
 * @author robert
 */
public class Config {
	//singleton during classloading (threadsafe)
	private static Config ref = new Config();
	public String mostUsername = "mostsoc";
	int generalPollIntervall = 10000;	//default 10s

	private Config() {
	}
	public static Config getInstance() {
		ConfigReader configReader = new ConfigReader("config.properties");
		//read config
		ref.mostUsername = configReader.getProperty("mostUsername");
		if (configReader.getProperty("generalPollIntervall") != null) {
			ref.generalPollIntervall = Integer.parseInt(configReader.getProperty("generalPollIntervall"));
		}
		return ref;
	}
	
}
