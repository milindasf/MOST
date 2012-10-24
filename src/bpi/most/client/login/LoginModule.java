package bpi.most.client.login;

import bpi.most.client.mainlayout.RootModule;
import bpi.most.client.rpc.AuthenticationService;
import bpi.most.client.rpc.AuthenticationServiceAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * The Class LoginModule. Login logic. defines functions for user
 * authentification
 */
public class LoginModule extends Composite {
  
  /** The Constant loginService. */
  public final AuthenticationServiceAsync authenticationService = GWT.create(AuthenticationService.class);
  /** The Constant binder. */
  private static final Binder binder = GWT.create(Binder.class);
  
  /** The static class ref. */
  // public static LoginModule ref = null;
  /**
   * The Interface Binder.
   */
  interface Binder extends UiBinder<Widget, LoginModule> {
  }
  
  /** The login btn. */
  @UiField
  Button loginBtn;
  /** The user. */
  @UiField
  TextBox user;
  /** The pw. */
  @UiField
  PasswordTextBox pw;
  
  /**
   * Instantiates a new login module.
   */
  public LoginModule() {

    initWidget(binder.createAndBindUi(this));
    
    user.getElement().setAttribute("placeholder", "Username");
    pw.getElement().setAttribute("placeholder", "Password");
  }
  
	@UiHandler("user")
	void onKeyDownUser(KeyDownEvent event) {
		if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
			loginBtn.click();
		}
	}

	@UiHandler("user")
	void onFocusUser(FocusEvent event) {
		user.selectAll();
	}

	@UiHandler("pw")
	void onKeyDownPW(KeyDownEvent event) {
		if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
			loginBtn.click();
		}
	}
	
	@UiHandler("pw")
	void onFocusPw(FocusEvent event) {
		pw.selectAll();
	}
  
  /**
   * Handle click.
   * 
   * @param e the e
   */
  @UiHandler("loginBtn")
  void handleClick(ClickEvent e) {

	  authenticationService.login(user.getValue(), pw.getValue(), new AsyncCallback<Boolean>() {
          @Override
          public void onSuccess(Boolean result) {
            if (result) {
              RootPanel.get().clear();
              RootLayoutPanel.get().clear();
              RootPanel.get().add(RootLayoutPanel.get());
              RootLayoutPanel.get().add(new RootModule());
            } else {
              Window.alert("Wrong Username and/or Password. Please try again.");
            }
          }
          @Override
          public void onFailure(Throwable caught) {
            Window.alert(caught.getMessage());
          }
        });
  }
}
