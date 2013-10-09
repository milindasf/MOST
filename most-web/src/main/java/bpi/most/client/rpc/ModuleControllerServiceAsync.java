package bpi.most.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;

// TODO: Auto-generated Javadoc
/**
 * The Interface ModuleControllerService.
 */
public interface ModuleControllerServiceAsync {
  
  /**
   * Checks for permission.
   *
   * @param moduleName the MODULE_NAME
   * @param callback the callback to return true, if successful
   */
  void hasPermission(String moduleName, AsyncCallback<Boolean> callback);
  
  /**
   * Does file exist.
   *
   * @param path the path
   * @param callback the callback to return true, if successful
   */
  void doesFileExist(String path, AsyncCallback<Boolean> callback);
}
