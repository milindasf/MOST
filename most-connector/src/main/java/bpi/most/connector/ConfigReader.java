package bpi.most.connector;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.util.Properties;
 
public class ConfigReader 
{
   Properties properties;
   public ConfigReader(String filename)
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

