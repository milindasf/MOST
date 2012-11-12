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
   * @param MODULE_NAME the MODULE_NAME
   * @return true, if successful
   */
  public boolean hasPermission(String MODULE_NAME);
  
  /**
   * Does file exist.
   *
   * @param path the path
   * @return true, if successful
   */
  public boolean doesFileExist(String path);
}
