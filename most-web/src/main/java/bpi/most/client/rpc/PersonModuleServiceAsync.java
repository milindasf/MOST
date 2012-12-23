package bpi.most.client.rpc;

import java.util.HashMap;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface PersonModuleServiceAsync {
  
  void setPersonValues(HashMap<String, String> personForm, AsyncCallback<Boolean> callback);
}
