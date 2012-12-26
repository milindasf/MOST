package bpi.most.server.services.gwtrpc;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bpi.most.client.rpc.ModuleControllerService;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class GwtRpcModuleControllerService extends RemoteServiceServlet implements  ModuleControllerService {

	private static final Logger LOG = LoggerFactory.getLogger(GwtRpcModuleControllerService.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public boolean hasPermission(String moduleName) {





		LOG.debug(moduleName);
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
