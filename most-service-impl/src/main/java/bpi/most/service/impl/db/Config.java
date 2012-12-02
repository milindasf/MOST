package bpi.most.service.impl.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Properties;

/**
 * Provides access to local config file.
 * Workaround to support non web application usage
 *
 * @author robert
 */
public class Config {

    private static final Logger log = LoggerFactory.getLogger(Config.class);

    private Properties properties;

    public Config(String filename) {
        properties = new Properties();
        BufferedInputStream stream;
        try {
            stream = new BufferedInputStream(new FileInputStream(filename));
            properties.load(stream);
            stream.close();
        } catch (FileNotFoundException e) {
            log.info("Could not find a config file");
        } catch (Exception e) {
            log.warn("Unexpected Exception while reading config file", e);
        }
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }
}

