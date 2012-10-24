package bpi.most.client.rpc;

import java.util.HashMap;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface PersonModuleServiceAsync {
  
  public void setPersonValues(HashMap<String, String> personForm, AsyncCallback<Boolean> callback);
}
