package bpi.most.server.services.opcua;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import bpi.most.server.services.opcua.server.MostOpcUaServer;

/**
 * Servlet implementation class OpcMostServer
 */
public class OpcMostServerStartServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = Logger.getLogger(OpcMostServerStartServlet.class);

	private MostOpcUaServer uaServer;
	
	
	private String endpointUrl = "opc.tcp://127.0.0.1:6001/mostopcua";
	private String keyPhrase = "mostrulez";
	private String certPath = "/pki/server.pem";
	private String keyPath = "/pki/server.key";
	private String mostUserName = "mostsoc"; //used for database access. as long as the user is not retrieved from the opc client
	
	
	@Override
	public void destroy() {
		super.destroy();
		LOG.debug("closing server...");

		// stop the server.
		if (uaServer != null){
			uaServer.stop();
		}

		LOG.debug("server closed");
	}



	@Override
	public void init(ServletConfig config) throws UnavailableException {
		ServletContext ctx = config.getServletContext();

		initServerValues();
		
		try {
			URL certUrl = ctx.getResource(certPath);
			URL keyUrl= ctx.getResource(keyPath);
			
			//create and start the most opc ua server
			uaServer = new MostOpcUaServer(endpointUrl, certUrl, keyUrl, keyPhrase, mostUserName);
			uaServer.start();
			
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new UnavailableException(e.getMessage());
		}
	}
	
	/**
	 * reads opc server configuration from jndi
	 */
	private void initServerValues(){
		try {
			Context initCtx = new InitialContext();
			Context opcCtx = (Context) initCtx.lookup("java:comp/env/opc");
			endpointUrl = (String) opcCtx.lookup("endpoint");
			keyPhrase = (String) opcCtx.lookup("keyphrase");
			certPath = (String) opcCtx.lookup("certpath");
			keyPath = (String) opcCtx.lookup("keypath");
			mostUserName = (String) opcCtx.lookup("mostusername");
		} catch (NamingException e) {
			LOG.error(e.getMessage(), e);
		}
	}
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		out.write("<html><head><title>bla</title></head><body><h1>opc server start servlet available!</h1></body>");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
