/**
 * 
 */
package bpi.most.connector;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


/**
 * Demo implementation of a generic connector startup procedure
 * @author robert.zach@tuwien.ac.at
 * TODO Implement independent server side ConnectorController (creates only "server side" Connectors, directly access datapoints - without SOAP or db --> differnt service impl)
 */
public class ConnectorStartup{
	private final static Logger log = Logger.getLogger( ConnectorStartup.class );

    public static void main(String[] args) throws Exception {
        ApplicationContext context = new ClassPathXmlApplicationContext("META-INF/connector.spring.xml");
        ConnectorBooter connectorBooter = context.getAutowireCapableBeanFactory().createBean(ConnectorBooter.class);
        connectorBooter.boot();
    }

}
