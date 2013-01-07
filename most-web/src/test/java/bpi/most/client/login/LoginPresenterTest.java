package bpi.most.client.login;

import com.google.gwt.user.client.rpc.AsyncCallback;
import org.easymock.EasyMock;
import org.easymock.IAnswer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.easymock.EasyMock.*;

/**
 * Test for {@link LoginPresenter}.
 *
 * @author Jakob Korherr
 */
@RunWith(JUnit4.class)
public class LoginPresenterTest {

    private LoginView view;
    private LoginUserVerifier userVerifier;
    private LoginPresenter presenter;

    @Before
    public void setUp() throws Exception {
        view = createMock(LoginView.class);
        userVerifier = createMock(LoginUserVerifier.class);
        presenter = new LoginPresenter(view, userVerifier);
    }

    @After
    public void tearDown() throws Exception {
        view = null;
        userVerifier = null;
        presenter = null;
    }

    @Test
    public void test_validLogin_shouldCall_navigateToRootView() throws Exception {
        // define mock expectations and behavior
        userVerifier.isValidUser(eq("validuser"), eq("validpassword"), isA(AsyncCallback.class));
        expectLastCall().andAnswer(new IAnswer<Void>() {
            @Override
            public Void answer() throws Throwable {
                AsyncCallback<Boolean> callback = (AsyncCallback<Boolean>) EasyMock.getCurrentArguments()[2];
                callback.onSuccess(true);
                return null;
            }
        });
        view.navigateToRootView();
        replay(userVerifier);
        replay(view);

        presenter.login("validuser", "validpassword");
        verify(userVerifier);
        verify(view);
    }

    @Test
    public void test_invalidLogin_shouldCall_showLoginError() throws Exception {
        // define mock expectations and behavior
        userVerifier.isValidUser(eq("invaliduser"), eq("invalidpassword"), isA(AsyncCallback.class));
        expectLastCall().andAnswer(new IAnswer<Void>() {
            @Override
            public Void answer() throws Throwable {
                AsyncCallback<Boolean> callback = (AsyncCallback<Boolean>) EasyMock.getCurrentArguments()[2];
                callback.onSuccess(false);
                return null;
            }
        });
        view.showLoginError();
        replay(userVerifier);
        replay(view);

        presenter.login("invaliduser", "invalidpassword");
        verify(userVerifier);
        verify(view);
    }

    @Test
    public void test_exceptionOnRpc_shouldCall_showError() throws Exception {
        final String errorMessage = "Error Message";

        // define mock expectations and behavior
        userVerifier.isValidUser(eq("validuser"), eq("validpassword"), isA(AsyncCallback.class));
        expectLastCall().andAnswer(new IAnswer<Void>() {
            @Override
            public Void answer() throws Throwable {
                AsyncCallback<Boolean> callback = (AsyncCallback<Boolean>) EasyMock.getCurrentArguments()[2];
                callback.onFailure(new RuntimeException(errorMessage));
                return null;
            }
        });
        view.showError(eq(errorMessage));
        replay(userVerifier);
        replay(view);

        presenter.login("validuser", "validpassword");
        verify(userVerifier);
        verify(view);
    }

}
