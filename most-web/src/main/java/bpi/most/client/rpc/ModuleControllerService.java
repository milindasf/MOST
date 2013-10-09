package bpi.most.client.rpc;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

// TODO: Auto-generated Javadoc
/**
 * The Interface ModuleControllerService.
 */
@RemoteServiceRelativePath("services/gwtrpc/modulectrl")
public interface ModuleControllerService extends RemoteService {
  
  /**
   * Checks for permission.
   *
   * @param moduleName the MODULE_NAME
   * @return true, if successful
   */
  boolean hasPermission(String moduleName);
  
  /**
   * Does file exist.
   *
   * @param path the path
   * @return true, if successful
   */
  boolean doesFileExist(String path);
}
