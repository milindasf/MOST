package bpi.most.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.HashMap;

public interface PersonModuleServiceAsync {
  
  void setPersonValues(HashMap<String, String> personForm, AsyncCallback<Boolean> callback);
}
