package bpi.most.server.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import bpi.most.server.services.User;
import bpi.most.server.utils.BCrypt;
import bpi.most.server.utils.DbPool;

/**
 * Provides permission and authentication processing
 * Session management (login, logout, timeout, etc.) is not included. It must be implemented in the respective service.
 * TODO: move addUserToRole(), etc. methods to User class
 * @author robert.zach@tuwien.ac.at
 */
public final class AuthenticationController {
	private static AuthenticationController ref;
	
	// Singleton
	private AuthenticationController() {
		super();
	}
	public static AuthenticationController getInstance() {
		if (ref == null) {
			ref = new AuthenticationController();
		}
		return ref;
	}
	
	//TODO: implement
	public boolean addUser(User user, String plainPassword) {
		return false;
	}
	//TODO: implement
	public boolean addRole(String roleId) {
		return false;
	}
	//TODO: implement
	public boolean delUser(User user) {
		return false;
	}
	//TODO: implement
	public boolean delRole(String roleId) {
		return false;
	}
	//TODO: implement
	public boolean isValidUser(User user) {
		return false;
	}
	//TODO: implement
	public boolean isValidRole(String roleId) {
		return false;
	}
	
	
	/**
	 * @param user User object
	 * @param plainPassword
	 * @return true-correct, false-incorrect or user invalid
	 */
	  public boolean isValidPassword(User user, String plainPassword) {
		  boolean result = false;
		  String pwHash;
		  
		  //use this hash for new user passwords
		  System.out.println(BCrypt.hashpw(plainPassword, BCrypt.gensalt()));
		  
		  pwHash = getPwHashFromDb(user.getUserName());
		  if (pwHash != null) {
			result = BCrypt.checkpw(plainPassword, pwHash);
		  }
		  return result;
	  }
	  
	  
	  /**
	   * @param username String of the requested user name
	   * @return Hash (BCrypt.java) or null of user not valid 
	   */
	  public String getPwHashFromDb(String username) {
	    String hash = null;
	    try {
		    Connection con = DbPool.getInstance().getConnection();
		    //TODO: use stored procedure
		    PreparedStatement select = con.prepareStatement("Select password from user where name = ?");
			select.setString(1, username);
		    ResultSet rs = select.executeQuery();
		    //expect single return value
		    if (rs.next()) {
		    	hash = rs.getString(1);
		    }
		    rs.close();
		    select.close();
		    con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	    return hash;
	  }
	  

}
