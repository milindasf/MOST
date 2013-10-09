package bpi.most.client.login;

/**
 * Interface specification for the login view.
 *
 * @author Jakob Korherr
 */
public interface LoginView {

    void navigateToRootView();

    void showLoginError();

    void showError(String errorMessage);

}
