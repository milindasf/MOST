package bpi.most.server.services.rest;

import bpi.most.dto.DpDTO;


/**
 * Represents a user
 */
@Deprecated  // dummy class to make module compile; needs refactoring
public class User {
	private String userName = null;
	
	//constructors
	public User(String userName) {
		super();
		this.userName = userName;
	}
	
	public boolean hasPermission(Object o, DpDTO.Permissions permissions) {
        return true;
	}

	
	//getter / setter
	public String getUserName() {
		return userName;
	}

}
