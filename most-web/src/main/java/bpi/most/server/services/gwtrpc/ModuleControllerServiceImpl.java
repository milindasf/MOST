package bpi.most.server.services.gwtrpc;

import java.io.File;
import bpi.most.client.rpc.ModuleControllerService;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class ModuleControllerServiceImpl extends RemoteServiceServlet implements  ModuleControllerService {
  
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  
  @Override
  public boolean hasPermission(String MODULE_NAME) {

    
    
    
    
    System.out.println(MODULE_NAME);
//    HttpSession session = this.getThreadLocalRequest().getSession();
//    ArrayList<String> t = new ArrayList<String>();
//    HashMap<String, ArrayList<String>> h = new HashMap<String, ArrayList<String>>();
//    h = (HashMap<String, ArrayList<String>>) session
//        .getAttribute("activeUserRoles");
//    t = h.get("roles");
//    if (t.contains(MODULE_NAME)) {
//      return true;
//    }
    // TODO Auto-generated method stub
    return true;
  }
  
  @Override
  public boolean doesFileExist(String path) {

    return new File(path).exists();
    // TODO Auto-generated method stub
  }
}
