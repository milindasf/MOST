package bpi.most.server.services.gwtrpc;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import bpi.most.server.services.User;

import bpi.most.service.api.AuthenticationService;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * Handles session and permission management. HttpSession is used for login/logout. 
 * @author robert.zach@tuwien.ac.at
 */
public class AuthenticationServiceImpl extends SpringGwtServlet implements bpi.most.client.rpc.AuthenticationService, IsSerializable {
  private static final long serialVersionUID = 1L;

  @Inject
  private AuthenticationService authenticationService;

  public AuthenticationServiceImpl() {

  }
  

  /**
   * user object is stored in session "mostUser" attribute
   * @return true if user/password is valid, false if undefined/not valid
   */
  public boolean login(String userId, String plainPassword) {
	  boolean result = false;
	  User loginUser = new User(userId);

	  //check if user is valid
	  if (authenticationService.isValidPassword(userId, plainPassword)) {
		  result = true;
		//TODO add user to session
		 HttpSession session = this.getThreadLocalRequest().getSession(true);
		 session.setAttribute("mostUser", loginUser);
	  }
	return result;
  }

  
  /**
   * destroy current session
   */
  public boolean logout() {
      HttpSession session = this.getThreadLocalRequest().getSession();
      session.invalidate();
      return true;
  }
  
  @Override
  public boolean isValidSession(String sessionID) {
    HttpServletRequest request = this.getThreadLocalRequest();
    HttpSession session = request.getSession();
    String id = session.getId();
    if (id.equals(sessionID)) {
      return true;
    }
    return false;
  }
 
  /**
   * check if user has permissions for respective client module
   * TODO: Each module is defined as a role --> check if user is in role
   */
  public boolean hasModulePermissions(String userId, String moduleId){
	  
	  return true;
	  
	//not implemented yet
	//  public ArrayList<String> getRolesFromDb(String user) throws SQLException {
	//
//	    try {
//	      ArrayList<String> roles = new ArrayList<String>();
//	      Connection con = DbPool.getDbSingleton().getConnection();
//	      PreparedStatement pstmt = con.prepareStatement("SELECT r.name,r.description FROM role r, user u, user_role ur WHERE u.name = ? AND u.id = ur.uid AND r.id = ur.rid ");
//	      pstmt.setString(1, user);
//	      ResultSet res = pstmt.executeQuery();
//	      while (res.next()) {
//	        roles.add(res.getString(1));
//	      }
//	      res.close();
//	      pstmt.close();
//	      con.close();
//	      return roles;
//	    } catch (Exception e) {
//	      e.printStackTrace();
//	      return null;
//	    }
	//  }
	  
  }
  
 

}
