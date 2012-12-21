package bpi.most.server.services.rest.impl;

import javax.annotation.Resource;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response.Status;

import org.apache.cxf.jaxrs.ext.MessageContext;
import org.apache.log4j.Logger;

import bpi.most.server.services.User;

public class BaseResImpl {
	
	protected static Logger LOG = Logger.getLogger(BaseResImpl.class);
	public static final int NOT_IMPLEMENTED = 501;
	
	@Resource
	protected MessageContext context;

	protected String getUsername(){
		String user = null;
		
		if (context != null){
			User u = (User) context.getHttpServletRequest().getAttribute("user");
			if (u != null){
				user = u.getUserName();
			}
/*			
			SecurityContext secCtxt = context.getSecurityContext();
			if (secCtxt != null && secCtxt.getUserPrincipal() != null){
				Principal userPrincpl = secCtxt.getUserPrincipal();
				if (userPrincpl instanceof MostUserPrincipal){
					user = ((MostUserPrincipal) userPrincpl).getUser().getUserName();
				}
			}
*/			
		}
		return user;
	}
	
	protected User getUser(){
		String userName = getUsername();
		if (userName == null){
			throw new WebApplicationException(Status.FORBIDDEN);
		}
		
		return new User(userName);
	}
}
