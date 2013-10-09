package bpi.most.client.rpc;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("services/gwtrpc/authentication")
public interface AuthenticationService extends RemoteService {
  
  boolean login(String userId, String plainPassword);
  boolean logout();
  boolean isValidSession(String sessionID);
  boolean hasModulePermissions(String userId, String moduleId);
  
}
