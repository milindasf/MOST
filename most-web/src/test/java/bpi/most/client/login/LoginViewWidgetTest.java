package bpi.most.client.login;

import bpi.most.client.mainlayout.RootModuleCreator;
import bpi.most.client.rpc.AuthenticationServiceAsync;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.gwt.test.GwtModule;
import com.googlecode.gwt.test.GwtTestWithEasyMock;
import com.googlecode.gwt.test.Mock;
import com.googlecode.gwt.test.WindowOperationsHandler;
import com.googlecode.gwt.test.utils.events.Browser;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static com.googlecode.gwt.test.assertions.GwtAssertions.assertThat;

/**
 * Tests for {@link LoginViewWidget}.
 *
 * @author Jakob Korherr
 */
@GwtModule("bpi.most.Most")
public class LoginViewWidgetTest extends GwtTestWithEasyMock {

    @Mock
    private AuthenticationServiceAsync serviceMock;

    @Mock
    private WindowOperationsHandler windowHandlerMock;

    @Mock
    private RootModuleCreator rootModuleCreatorMock;

    private LoginViewWidget loginViewWidget;

    @Before
    public void before() {
        loginViewWidget = new LoginViewWidget(rootModuleCreatorMock);

        setWindowOperationsHandler(windowHandlerMock);  // install window mock

        // Some pre-assertions
        assertThat(loginButton()).isVisible();
        assertThat(loginUsernameTextBox()).isVisible();
        assertThat(loginPasswordTextBox()).isVisible();
    }

    @After
    public void tearDown() throws Exception {
        loginViewWidget = null;
    }

    @Test
    public void test_validLogin_shouldShowRootModule() {
        // Arrange
        Browser.fillText(loginUsernameTextBox(), "validuser");
        Browser.fillText(loginPasswordTextBox(), "validpassword");

        serviceMock.login(EasyMock.eq("validuser"), EasyMock.eq("validpassword"), EasyMock.isA(AsyncCallback.class));
        expectServiceAndCallbackOnSuccess(true);
        Widget rootModuleMock = new Label("dummy");
        EasyMock.expect(rootModuleCreatorMock.createRootModule()).andReturn(rootModuleMock);
        replay();

        // Act: Click the 'Login' button
        Browser.click(loginButton());

        // Assert
        verify();
        Assert.assertEquals(1, RootLayoutPanel.get().getWidgetCount());
        Assert.assertEquals(rootModuleMock, RootLayoutPanel.get().getWidget(0));
    }

    @Test
    public void test_invalidLogin_shouldShowErrorMessage() {
        // Arrange
        Browser.fillText(loginUsernameTextBox(), "invaliduser");
        Browser.fillText(loginPasswordTextBox(), "invalidpassword");

        serviceMock.login(EasyMock.eq("invaliduser"), EasyMock.eq("invalidpassword"), EasyMock.isA(AsyncCallback.class));
        expectServiceAndCallbackOnSuccess(false);
        windowHandlerMock.alert(EasyMock.eq(LoginViewWidget.WRONG_LOGIN_DATA_MSG));
        EasyMock.expectLastCall();
        replay();

        // Act: Click the 'Login' button
        Browser.click(loginButton());

        // Assert
        verify();
    }

    @Test
    public void test_exceptionOnRcp_shouldShowExceptionMessage() {
        // Arrange
        Browser.fillText(loginUsernameTextBox(), "validuser");
        Browser.fillText(loginPasswordTextBox(), "validpassword");

        serviceMock.login(EasyMock.eq("validuser"), EasyMock.eq("validpassword"), EasyMock.isA(AsyncCallback.class));
        final String rpcExceptionMsg = "rpc exception message";
        expectServiceAndCallbackOnFailure(new RuntimeException(rpcExceptionMsg));
        windowHandlerMock.alert(EasyMock.eq(rpcExceptionMsg));
        EasyMock.expectLastCall();
        replay();

        // Act: Click the 'Login' button
        Browser.click(loginButton());

        // Assert
        verify();
    }

    private Button loginButton() {
        return loginViewWidget.loginBtn;
    }

    private TextBox loginUsernameTextBox() {
        return loginViewWidget.user;
    }

    private PasswordTextBox loginPasswordTextBox() {
        return loginViewWidget.pw;
    }

}
