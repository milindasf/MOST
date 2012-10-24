package bpi.most.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface AuthenticationServiceAsync {
  
  void login(String userId, String plainPassword, AsyncCallback<Boolean> callback);
  void logout(AsyncCallback<Boolean> callback);
  void isValidSession(String sessionID, AsyncCallback<Boolean> callback);
  void hasModulePermissions(String userId, String moduleId, AsyncCallback<Boolean> callback);
  
}
