package bpi.most.server.services.opcua.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.bouncycastle.openssl.PEMReader;
import org.bouncycastle.openssl.PasswordFinder;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.ApplicationDescription;
import org.opcfoundation.ua.core.UserTokenPolicy;
import org.opcfoundation.ua.transport.Endpoint;
import org.opcfoundation.ua.transport.security.Cert;
import org.opcfoundation.ua.transport.security.KeyPair;
import org.opcfoundation.ua.transport.security.PrivKey;
import org.opcfoundation.ua.transport.security.SecurityMode;

import bpi.most.opcua.server.annotation.AnnotationNodeManager;
import bpi.most.opcua.server.annotation.IAnnotatedNodeSource;
import bpi.most.opcua.server.annotation.IAnnotationHistoryManager;
import bpi.most.opcua.server.core.UAServer;
import bpi.most.opcua.server.core.auth.IUserPasswordAuthenticator;
import bpi.most.server.services.opcua.server.nodes.DpDataNode;
import bpi.most.server.services.opcua.server.nodes.DpNode;
import bpi.most.server.services.opcua.server.nodes.ZoneNode;

/**
 * an OPC-UA Server for the Most database. it lets opc ua clients access zones
 * and datapoints. for datapoints the last (= current) value and history values can be retrieved. <br/>
 * 
 * @author hare
 * 
 */
public class MostOpcUaServer {

	private static final Logger LOG = Logger.getLogger(MostOpcUaServer.class);

	private String endpointUrl;
	private URL certUrl;
	private URL keyUrl;
	private String keyPhrase; // keyphrase for the keyFile
/*	
	private AuthenticationService authService;
	private DatapointService dpService;
	private ZoneService zoneService;
*/
	@Inject
	private IUserPasswordAuthenticator authenticator;
	
	@Inject
	private IAnnotatedNodeSource mostNodeManager;
	
	@Inject
	private IAnnotationHistoryManager mostHistoryManager;
	
	/**
	 * the creates {@link UAServer} instance which can be started and stopped
	 */
	private UAServer uaServer;
	
	public MostOpcUaServer() {
		System.out.println("mostopcuaserver created");
	}

	/**
	 * inits the server object
	 * 
	 * @param endpointUrl
	 * @param certUrl
	 * @param keyUrl
	 * @param keyPhrase
	 * @throws Exception 
	 */
	public void init(String endpointUrl, URL certUrl, URL keyUrl, String keyPhrase) throws Exception {
		this.endpointUrl = endpointUrl;
		this.certUrl = certUrl;
		this.keyUrl = keyUrl;
		this.keyPhrase = keyPhrase;
	}
	
	
	
	private void initServer() throws Exception {
		try {
			// create a UAServer instance
			UAServer uaServerInstance = new UAServer();

			/*
			 * set the allowed authentication policies. here we support
			 * username+password.
			 */
			uaServerInstance.addUserTokenPolicy(authenticator, UserTokenPolicy.SECURE_USERNAME_PASSWORD, UserTokenPolicy.SECURE_USERNAME_PASSWORD_BASIC256);

			// Set some applicationdescriptions
			ApplicationDescription appDesc = new ApplicationDescription();
			uaServerInstance.setServerDesc(appDesc);

			Endpoint endpoint = new Endpoint(new URI(endpointUrl),
					SecurityMode.NONE);
			uaServerInstance.setEndpoint(endpoint);

			// set a X.509 certificate for the server - this is mandatory
			uaServerInstance.addApplicationInstanceCertificate(getApplicationInstanceCertificate());

			// set nodemanger
			AnnotationNodeManager annoNMgr = new AnnotationNodeManager(
					mostNodeManager, "Zones",
					"root node for all most zones", "mostzones");
			// add nodes to get introspected at startup -> this is a good
			// practice
			annoNMgr.addObjectToIntrospect(new ZoneNode());
			annoNMgr.addObjectToIntrospect(new DpNode());
			annoNMgr.addObjectToIntrospect(new DpDataNode());
			
			//add a history manager
//			HistoryNodeManager myHistNMgr = new HistoryNodeManager(dpService);
			annoNMgr.setHistoryManager(mostHistoryManager);
			
			uaServerInstance.addNodeManager(annoNMgr);
			
			uaServer = uaServerInstance;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * starts the server
	 */
	public void start() {
		if (uaServer != null){
			uaServer.start();
		}else{
			//TODO throw some exception
			LOG.warn("opc server is not initialized, cannot be started");
		}
	}

	/**
	 * stops the server
	 */
	public void stop() {
		if (uaServer != null){
			uaServer.stop();
		}else{
			//TODO throw some exception
			LOG.warn("opc server is not initialized, cannot be stopped");
		}
	}

	/**
	 * loads the server certificate with the given key.
	 *
	 * @return
	 * @throws ServiceResultException
	 * @throws IOException
	 * @throws CertificateException
	 */
	private KeyPair getApplicationInstanceCertificate()
			throws ServiceResultException, IOException, CertificateException {
		KeyPair kp = null;

		BufferedReader reader = new BufferedReader(new InputStreamReader(
				certUrl.openStream()));
		String line;

		//print out certificate and keyfile to check if they are accessable
		StringBuffer sb = new StringBuffer();
		while ((line = reader.readLine()) != null) {
			sb.append(line);
		}
		LOG.debug("\n\ncertfile:\n" + sb.toString());

		sb = new StringBuffer();
		reader.close();
		reader = new BufferedReader(new InputStreamReader(keyUrl.openStream()));
		while ((line = reader.readLine()) != null) {
			sb.append(line);
		}
		LOG.debug("\n\nkeyfile:\n" + sb.toString());
		reader.close();

		Cert cert = Cert.load(certUrl);
		
		
		final PEMReader pemReader = new PEMReader(new InputStreamReader(
				keyUrl.openStream()), new PasswordFinder() {
			public char[] getPassword() {
				return keyPhrase.toCharArray();
			}
		});

		byte[] privKey = null;
		Object certObject = pemReader.readObject();
//		if (certObject instanceof X509CertificateObject){
//			privKey = ((X509CertificateObject) certObject).getEncoded();
//		}else if (certObject instanceof java.security.KeyPair){
			privKey = ((java.security.KeyPair) certObject).getPrivate().getEncoded();
//		}
		
		LOG.info("readobject is: " + certObject.getClass().getName());
		
		PrivKey key = null;
		try {
			key = new PrivKey(privKey);
		} catch (InvalidKeySpecException e) {
			LOG.error(e.getMessage(), e);
		}
		kp = new KeyPair(cert, key);

		// cleanup
		pemReader.close();

		return kp;
		/*
		 * create a keypair on the fly
		 */
/*
		KeyPair kp = null;
		try {
			//create one on the fly
			kp = CertificateUtils.createApplicationInstanceCertificate("sampleserver", "tu vienna", endpointUrl, 365);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return kp;
*/		
	}
	
	
}
