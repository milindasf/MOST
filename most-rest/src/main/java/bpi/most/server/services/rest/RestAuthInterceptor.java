package bpi.most.server.services.rest;

import bpi.most.dto.UserDTO;
import bpi.most.service.api.AuthenticationService;
import org.apache.cxf.binding.soap.interceptor.SoapHeaderInterceptor;
import org.apache.cxf.configuration.security.AuthorizationPolicy;
import org.apache.cxf.endpoint.Endpoint;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Exchange;
import org.apache.cxf.message.Message;
import org.apache.cxf.transport.Conduit;
import org.apache.cxf.ws.addressing.EndpointReferenceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * based on:
 * http://chrisdail.com/2008/08/13/http-basic-authentication-with-apache-cxf-revisited/
 * 
 * @author harald
 *
 */
public class RestAuthInterceptor extends SoapHeaderInterceptor {

	private static final Logger LOG = LoggerFactory.getLogger(RestAuthInterceptor.class);

    @Inject
    private AuthenticationService authenticationService;
	
	@Override
	public void handleMessage(Message inMsg) throws Fault{
		// This is set by CXF
        AuthorizationPolicy policy = inMsg.get(AuthorizationPolicy.class);
        
        // If the policy is not set, the user did not specify credentials
        // A 401 is sent to the client to indicate that authentication is required
        if (policy == null) {
            sendErrorResponse(inMsg, HttpURLConnection.HTTP_UNAUTHORIZED);
            return;
        }
        
        
        //ask service for authentication
        UserDTO mostUser = new UserDTO(policy.getUserName());
        boolean isValid = authenticationService.isValidPassword(mostUser.getUserName(), policy.getPassword());
        if (!isValid) {
        	LOG.warn("Invalid username or password for user: " + policy.getUserName());
            sendErrorResponse(inMsg, HttpURLConnection.HTTP_FORBIDDEN);
            return;
        }
        
        //valid user, set it into the request.
        HttpServletRequest req = (HttpServletRequest)inMsg.get("HTTP.REQUEST");
        req.setAttribute("user", mostUser);
    }
    
    private void sendErrorResponse(Message message, int responseCode) {
        Message outMessage = getOutMessage(message);
        outMessage.put(Message.RESPONSE_CODE, responseCode);
        
        // Set the response headers
        Map<String, List<String>> responseHeaders =
            (Map<String, List<String>>)message.get(Message.PROTOCOL_HEADERS);
        if (responseHeaders != null) {
            responseHeaders.put("WWW-Authenticate", Arrays.asList(new String[]{"Basic realm=MostRealm"}));
            responseHeaders.put("Content-Length", Arrays.asList(new String[]{"0"}));
        }
        message.getInterceptorChain().abort();
        try {
            getConduit(message).prepare(outMessage);
            close(outMessage);
        } catch (IOException e) {
            LOG.warn(e.getMessage(), e);
        }
    }
    
    private Message getOutMessage(Message inMessage) {
        Exchange exchange = inMessage.getExchange();
        Message outMessage = exchange.getOutMessage();
        if (outMessage == null) {
            Endpoint endpoint = exchange.get(Endpoint.class);
            outMessage = endpoint.getBinding().createMessage();
            exchange.setOutMessage(outMessage);
        }
        outMessage.putAll(inMessage);
        return outMessage;
    }
    
    private Conduit getConduit(Message inMessage) throws IOException {
        Exchange exchange = inMessage.getExchange();
        EndpointReferenceType target = exchange.get(EndpointReferenceType.class);
        Conduit conduit =
            exchange.getDestination().getBackChannel(inMessage, null, target);
        exchange.setConduit(conduit);
        return conduit;
    }
    
    private void close(Message outMessage) throws IOException {
        OutputStream os = outMessage.getContent(OutputStream.class);
        os.flush();
        os.close();
    }

}
