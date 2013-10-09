package bpi.most.server.services.opcua.server;

import javax.inject.Inject;

import bpi.most.dto.UserDTO;
import bpi.most.opcua.server.core.ClientIdentity;
import bpi.most.opcua.server.core.RequestContext;
import bpi.most.opcua.server.core.Session;
import bpi.most.opcua.server.core.auth.IUserPasswordAuthenticator;
import bpi.most.service.api.AuthenticationService;

public class OpcUaAuthenticator implements IUserPasswordAuthenticator{

	@Inject
	private AuthenticationService authenticationService;
	
	public OpcUaAuthenticator() {
	}

	/**
	 * @param authenticationService
	 */
	public OpcUaAuthenticator(AuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
	}

	@Override
	public boolean authenticate(ClientIdentity clientIdentity) {
		
		UserDTO mostUser = new UserDTO(clientIdentity.getUsername());
		
		boolean isValid = authenticationService.isValidPassword(clientIdentity.getUsername(), clientIdentity.getPassword());
		
		/*
		 * in the session we set the User object so that it is accessible in all
		 * further actions.
		 */
		Session s = RequestContext.get().getSession();
		s.setCustomObj(mostUser);

		return isValid;
	}

}
