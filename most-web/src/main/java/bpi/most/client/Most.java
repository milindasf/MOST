/*
 *  Monitoring Application
 */
package bpi.most.client;

import bpi.most.client.login.LoginViewWidget;
import bpi.most.client.mainlayout.RootModule;
import bpi.most.client.mainlayout.RootModuleCreator;
import bpi.most.client.rpc.AuthenticationService;
import bpi.most.client.rpc.AuthenticationServiceAsync;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;


/**
 * 
 * Monitoring Showcase Application
 * 
 * @author sg
 * @author michi
 * @author gina
 * @author robert
 * @author rainer
 * @author jowe
 */
public class Most implements EntryPoint { // , ValueChangeHandler<String>
  
  /** The Constant INIT_HISTORY. */
  public static final String INIT_HISTORY = "init";
  /** The common service. */

  private final AuthenticationServiceAsync authenticationService = GWT.create(AuthenticationService.class);

    // TODO create DpController-Object here?
  /*
   * (non-Javadoc)
   * 
   * @see com.google.gwt.core.client.EntryPoint#onModuleLoad()
   */
  public void onModuleLoad() {
    String sessionID = Cookies.getCookie("sid");
    RootPanel.get().clear();
    RootLayoutPanel.get().clear();
    if (sessionID != null) {
      authenticationService.isValidSession(sessionID,
          new AsyncCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
              if (result) {
                RootLayoutPanel.get().add(new RootModule());
              } else {
                RootPanel.get().add(new LoginViewWidget(new RootModuleCreator()));
              }
            }
            
            @Override
            public void onFailure(Throwable caught) {

              // TODO Auto-generated method stub
            }
          });
    } else {
      RootPanel.get().add(new LoginViewWidget(new RootModuleCreator()));
    }
  }
}
