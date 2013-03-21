package bpi.most.server.services;

import bpi.most.dto.DpDTO;
import bpi.most.dto.ZoneDTO;

import java.util.List;


/**
 * Represents a user
 */
public class User {
	private String userName = null;
	
	//constructors
	public User(String userName) {
		super();
		this.userName = userName;
	}
	
	//TODO: implement, permissions are hierarchical! ro,rw,admin
	public boolean hasPermission(DpDTO dp, DpDTO.Permissions permissions) {
			return true;
	}
	//TODO: implement
	/**
	 * permissions are hierarchical! ro, rw, admin
	 * 
	 * @param zone
	 * @param permissions
	 * @return
	 */
	public boolean hasPermission(ZoneDTO zone, DpDTO.Permissions permissions) {
				return true;
	}
		
	//TODO: implement
	public boolean changePassword(String oldPlainPassword, String newPlainPassword) {
		return false;
	}
	
	//TODO: implement
	public boolean addToRole(String role) {
			return false;
	}
	//TODO: implement
	public boolean delFromRole(String roleId) {
		return false;
	}
	//TODO: implement
	public boolean isInRole(String roleId) {
		return false;
	}
	//TODO: implement
	public List<String> getRoles() {
		return null;
	}
	
	//getter / setter
	public String getUserName() {
		return userName;
	}

}
