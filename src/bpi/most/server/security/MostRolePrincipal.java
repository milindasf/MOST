package bpi.most.server.security;

import java.security.Principal;

public class MostRolePrincipal implements Principal{

	private String name;
	
	public MostRolePrincipal(String name){
		this.name = name;
	}
	
	@Override
	public String getName() {
		return name;
	}

}
