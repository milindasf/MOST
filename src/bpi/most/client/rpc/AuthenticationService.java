package bpi.most.client.rpc;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("services/gwtrpc/authentication")
public interface AuthenticationService extends RemoteService {
  
  public boolean login(String userId, String plainPassword);
  public boolean logout();
  public boolean isValidSession(String sessionID);
  public boolean hasModulePermissions(String userId, String moduleId);
  
}
