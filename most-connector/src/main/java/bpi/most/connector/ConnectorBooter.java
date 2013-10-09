package bpi.most.connector;

import bpi.most.domain.connector.ConnectorVO;
import bpi.most.dto.UserDTO;
import bpi.most.service.api.ConnectorService;
import bpi.most.service.api.DatapointService;
import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;
import java.util.ServiceLoader;

@Component
public class ConnectorBooter {

    private final static Logger log = Logger.getLogger( ConnectorBooter.class );

//      is now directly injected into the Connector implementation
//    @Inject
//    private DatapointService dpService;

    @Inject
    private ConnectorService connService;

    /**
     * is used to autowire connectors after creation. This way, connectors created by the serviceloader
     * can use dependency injection.
     */
    @Inject
    private ApplicationContext ctx;

    /**
     * holds all active connectors
     */
    List<Connector> activeConnectors;

    public void boot() throws Exception {
        //#### service abstraction objects for MOST access
        //TODO use local service implementation
        List<ConnectorVO> selectedConnectors = null;
        activeConnectors = new LinkedList<Connector>();    //currently active Connectors

        //Class.forName("org.sqlite.JDBC");
        //create connection to sqlite DB
        //Connection sqlConn = DriverManager.getConnection("jdbc:sqlite:/path/to/sqllite/file");

        //#### read config
        Config config = Config.getInstance();

        //get login information (most url, user)
        //TODO connect to MOST services using provided information
        //a) check user
        UserDTO user = new UserDTO(config.mostUsername);
        //b) connect

        //TODO ask which datapoints/connections should be connected (based on zone, type, etc.)
        //default, load all
        selectedConnectors = connService.getConnection(user);

        //#### start PollService, define how often Connectors should be checked if polling is required.
        //The Connector variable "pollInterval" defines the actual poll timing of the respective Connector (e.g. "pollInterval 360;")
        PollService pollService = PollService.getInstance(config.generalPollIntervall);
        pollService.start();

        //#### Loop through all connector definitions may covered in this connector
        for (ConnectorVO connectionMetadata : selectedConnectors) {

            //A) try to get a Connector (driver) using the Java Service Loader based on ConnectorDTO (connection_type, writable, vendor, model, dpName)
            //use unique vendor and model for new connector implementations
            //The default OPC/JDBC connector use vendor and model null or "" and connection_type "opc-da"/"jdbc".
            ServiceLoader<ConnectorFactory> connectorLoader = ServiceLoader.load(ConnectorFactory.class);
            //loop through all ConnectorFactory implementations
            //multiple connectors are added if they support the requested connection
            Connector currentConnector = null;
            for (ConnectorFactory connector : connectorLoader) {
                //creating requested connector object
                currentConnector = connector.getConnector(connectionMetadata, user);
                if (currentConnector != null) {

                    try{
                        //inject spring services into connection; we catch the error so that one connection does not break the whole initialization loop
                        ctx.getAutowireCapableBeanFactory().autowireBean(currentConnector);
                        ctx.getAutowireCapableBeanFactory().initializeBean(currentConnector, null);
                    }catch(BeansException e){
                        log.error(e.getMessage(), e);
                    }

                    //add connector to running connector list
                    activeConnectors.add(currentConnector);
                    log.info("Connector for " + connectionMetadata.getDpName() + " registered - " + connectionMetadata.toString());
                }
            }
            if (currentConnector == null) {
                //no appropriate connector found
                log.info("No Connector found for " + connectionMetadata.getDpName() + " - " + connectionMetadata.toString());
            }
        }

//		Iterator<String> datapoints =  allDatapointNames.iterator();
//		while (datapoints.hasNext()) {
//			
//			
//			String datapointName = datapoints.next(); 
//			ConnectorJdbcImpl test = new ConnectorJdbcImpl(datapointName, myMonEngine.getMonSrvConn(), sqlConn);
//			ConnectorJdbcImpl test2 = new DpConnectorJdbcDst(datapointName, myMonEngine.getMonSrvConn(), sqlConn);
//			
//			System.out.println("User: " + test.getDatapointName());
//			System.out.println("User: " + test.getCustomAttr("test2"));
//			if (test.getData() != null) {
//				System.out.println("data: " + test.getData().getTimestamp() );
//				System.out.println("data: " + test.getData().getValue() );				
//			}
//			
//			if (test.getDatapointName().equals("tem1")) {
//				DateFormat df = new SimpleDateFormat( "yyyy-MM-dd hh:mm:ss" );
//				System.out.println("tem1: " + test.getData(df.parse("2010-12-29 12:02:30"), df.parse("2010-12-29 14:02:30")).get(3).getTimestamp());
//			}
//
//			if (test.getDatapointName().equals("name3")) {
//				System.out.println("measurements: " + test.getSourceData().getValue());
//			}
//			if (test.getDatapointName().equals("name3")) {
//				DateFormat df = new SimpleDateFormat( "yyyy-MM-dd hh:mm:ss" );
//				System.out.println("measurements: " + test.getSourceData(df.parse("2010-04-10 12:35:01"), df.parse("2010-04-19 17:35:01")).get(30).getTimestamp());
//				System.out.println("measurements: " + test.getSourceData(df.parse("2010-04-10 12:35:01"), df.parse("2010-04-19 17:35:01")).get(30).getValue());
//
//				System.out.println("measurements: " + test2.getSourceData(df.parse("2010-04-10 12:35:01"), df.parse("2010-04-19 17:35:01")).get(30).getTimestamp());
//				System.out.println("measurements: " + test2.getSourceData(df.parse("2010-04-10 12:35:01"), df.parse("2010-04-19 17:35:01")).get(30).getValue());
//			}
//						
//		}
//		
//		sqlConn.close();
//		myMonEngine.close();
    }

    public List<Connector> getActiveConnectors() {
        return activeConnectors;
    }
}