package bpi.most.server.utils;

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
		e.printStackTrace();
	   }
   }
 
   public String getProperty(String key)
   {
	String value = this.properties.getProperty(key);		
	return value;
   }
}

