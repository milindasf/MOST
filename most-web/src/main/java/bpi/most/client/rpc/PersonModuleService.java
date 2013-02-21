package bpi.most.client.rpc;

import java.util.HashMap;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("services/gwtrpc/person")
public interface PersonModuleService extends RemoteService {
  
  boolean setPersonValues(HashMap<String, String> personForm);
}
