package bpi.most.server.security;

import bpi.most.server.services.User;

import java.security.Principal;

public class MostUserPrincipal implements Principal {

	private User user;
	
	public MostUserPrincipal(User user) {
		this.user = user;
	}

	@Override
	public String getName() {
		return user != null ? user.getUserName() : null;
	}
	
	public User getUser(){
		return user;
	}

}
