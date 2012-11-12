package bpi.most.server.services;

import bpi.most.server.model.AuthenticationController;


/**
 * Common Service interface. Handles permissions, etc. Everything here is serializable
 * Different implementations (GWT-RPC, OPC UA, SOAP, etc.) should be based on this implementations
 * @author robert.zach@tuwien.ac.at
 */
public class AuthenticationService {
	AuthenticationController authCtrl = AuthenticationController.getInstance();
	private static AuthenticationService ref = null;
	
	//Singleton
	private AuthenticationService() {
		super();
	}
	public static AuthenticationService getInstance(){
		if (ref == null) {
			ref = new AuthenticationService();
		}
		return ref;
	}
	
	public boolean isValidPassword(User user, String plainPassword) {
		return authCtrl.isValidPassword(user, plainPassword);
	}
}
