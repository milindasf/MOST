package bpi.most.server.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.util.Properties;

/**
 * Provides access to local config file.
 * Workaround to support non web application usage 
 * @author robert
 */
public class Config 
{
	private static final Logger LOG = LoggerFactory.getLogger(Config.class);

	Properties properties;
	public Config(String filename)
	{
		properties = new Properties();
		BufferedInputStream stream;
		try {
			stream = new BufferedInputStream(new FileInputStream(filename));
			properties.load(stream);
			stream.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOG.error("An exception occured", e);
		}
	}

	public String getProperty(String key)
	{
		return this.properties.getProperty(key);
	}
}

