package bpi.most.client.login;

import bpi.most.client.rpc.AuthenticationService;
import bpi.most.client.rpc.AuthenticationServiceAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Rpc implementation of {@link LoginUserVerifier}.
 *
 * @author Jakob Korherr
 */
public class LoginUserVerifierImpl implements LoginUserVerifier {

    private final AuthenticationServiceAsync authenticationService = GWT.create(AuthenticationService.class);

    @Override
    public void isValidUser(String username, String password, AsyncCallback<Boolean> callback) {
        // use authentication service RPC to verify user data
        authenticationService.login(username, password, callback);
    }
}
