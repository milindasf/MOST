package bpi.most.client.login;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Model for {@link LoginPresenter}.
 *
 * @author Jakob Korherr
 */
public interface LoginUserVerifier {

    void isValidUser(String username, String password, AsyncCallback<Boolean> callback);

}
