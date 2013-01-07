package bpi.most.client.rpc;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import java.util.HashMap;

@RemoteServiceRelativePath("services/gwtrpc/person")
public interface PersonModuleService extends RemoteService {
  
  boolean setPersonValues(HashMap<String, String> personForm);
}
