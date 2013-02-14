package bpi.most.server.services.opcua;

import java.io.IOException;
import java.net.URL;

import org.apache.log4j.Logger;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import bpi.most.server.services.opcua.server.MostOpcUaServer;

/**
 * running the most opc ua server standalone
 * 
 * @author harald
 * 
 */
public class MostOpcUaStandaloneServer {

	private static final Logger LOG = Logger.getLogger(MostOpcUaStandaloneServer.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		MostOpcUaStandaloneServer server = new MostOpcUaStandaloneServer();
		server.run();

	}

	private void run() {
		String endpointUrl = "opc.tcp://127.0.0.1:6001/mostopcua";
		String keyPhrase = "mostrulez";
		String certPath = "META-INF/pki/server.pem";
		String keyPath = "META-INF/pki/server.key";

		try {
			URL certUrl = getClass().getClassLoader().getResource(certPath);
			URL keyUrl = getClass().getClassLoader().getResource(keyPath);

			assert (keyUrl != null);

			// TODO get applicationcontext;
			AbstractApplicationContext appContext
            = new ClassPathXmlApplicationContext(new String []{"META-INF/opcua.service.spring.xml"});
			MostOpcUaServer uaServer = appContext.getBean(MostOpcUaServer.class);

			uaServer.init(endpointUrl, certUrl, keyUrl, keyPhrase);

			uaServer.start();

			/*
			 * //create and start the most opc ua server uaServer = new
			 * MostOpcUaServer(endpointUrl, certUrl, keyUrl, keyPhrase, new
			 * AuthenticationServiceImpl(), new DatapointServiceImpl(), new
			 * ZoneServiceImpl()); uaServer.start();
			 */
			try {
				// let the server run until key was entered in console
				System.out.println("stop server with any key");
				System.in.read();
			} catch (IOException e) {
				e.printStackTrace();
			}

			uaServer.stop();

		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
	}
}
