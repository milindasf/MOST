package bpi.most.client.login;

import bpi.most.client.mainlayout.RootModuleCreator;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * View widget used for user authentication.
 */
public class LoginViewWidget extends Composite implements LoginView {

    static final String WRONG_LOGIN_DATA_MSG = "Wrong Username and/or Password. Please try again.";

    /**
     * The Constant binder.
     */
    private static final Binder binder = GWT.create(Binder.class);

    /**
     * The Interface Binder.
     */
    interface Binder extends UiBinder<Widget, LoginViewWidget> {
    }

    /**
     * The login btn.
     */
    @UiField
    Button loginBtn;
    /**
     * The user.
     */
    @UiField
    TextBox user;
    /**
     * The pw.
     */
    @UiField
    PasswordTextBox pw;

    private final RootModuleCreator rootModuleCreator;
    private final LoginPresenter loginPresenter;

    /**
     * Instantiates a new login module.
     */
    public LoginViewWidget(RootModuleCreator rootModuleCreator) {
        this.rootModuleCreator = rootModuleCreator;
        this.loginPresenter = new LoginPresenter(this, new LoginUserVerifierImpl());

        initWidget(binder.createAndBindUi(this));

        user.getElement().setAttribute("placeholder", "Username");
        pw.getElement().setAttribute("placeholder", "Password");

        // assign DOM IDs to widgets for testing purposes
        loginBtn.getElement().setId("loginBtn");
        user.getElement().setId("loginUsernameTextBox");
        pw.getElement().setId("loginPasswordTextBox");
    }

    @Override
    public void navigateToRootView() {
        RootPanel.get().clear();
        RootLayoutPanel.get().clear();
        RootPanel.get().add(RootLayoutPanel.get());
        RootLayoutPanel.get().add(rootModuleCreator.createRootModule());
    }

    @Override
    public void showLoginError() {
        Window.alert(WRONG_LOGIN_DATA_MSG);
    }

    @Override
    public void showError(String errorMessage) {
        Window.alert(errorMessage);
    }

    @UiHandler("user")
    void onKeyDownUser(KeyDownEvent event) {
        if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
            checkLoginData();
        }
    }

    @UiHandler("user")
    void onFocusUser(FocusEvent event) {
        user.selectAll();
    }

    @UiHandler("pw")
    void onKeyDownPW(KeyDownEvent event) {
        if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
            checkLoginData();
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
        checkLoginData();
    }

    private void checkLoginData() {
        loginPresenter.login(user.getValue(), pw.getValue());
    }

}
