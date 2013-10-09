package bpi.most.client.login;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Presenter for {@link LoginView}.
 *
 * @author Jakob Korherr
 */
public class LoginPresenter {

    private final LoginView loginView;
    private final LoginUserVerifier loginUserVerifier;

    public LoginPresenter(LoginView loginView, LoginUserVerifier loginUserVerifier) {
        this.loginView = loginView;
        this.loginUserVerifier = loginUserVerifier;
    }

    public void login(String username, String password) {
        loginUserVerifier.isValidUser(username, password, new AsyncCallback<Boolean>() {
            @Override
            public void onFailure(Throwable throwable) {
                loginView.showError(throwable.getMessage());
            }

            @Override
            public void onSuccess(Boolean isValidUser) {
                if (isValidUser) {
                    loginView.navigateToRootView();
                }
                else {
                    loginView.showLoginError();
                }
            }
        });
    }

}
