package bpi.most.client;

import bpi.most.client.rpc.AuthenticationService;
import bpi.most.client.rpc.AuthenticationServiceAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

/**
 * Tests for MOST AuthenticationServiceImpl.
 * 
 * @author Lukas Weichselbaum
 */
public class AuthenticationServiceGwtTest extends GWTTestCase
{
	// The service that we will test.
    AuthenticationServiceAsync authenticationService;

    /**
     * Must refer to a valid module that sources this class.
     */
    public String getModuleName()
    {
        return "bpi.most.MostTest";
    }
    
    @Override
    protected void gwtSetUp() throws Exception {
    	// Create the service that we will test.
        authenticationService = GWT.create(AuthenticationService.class);
        ServiceDefTarget target = (ServiceDefTarget) authenticationService;
        target.setServiceEntryPoint(GWT.getModuleBaseURL() + "services/gwtrpc/authentication");

        // Since RPC calls are asynchronous, we will need to wait for a response
        // after this test method returns. This line tells the test runner to
        // wait up to 10 seconds before timing out.
        delayTestFinish(10000);
    }
    
    private AsyncCallback<Boolean> createBooleanAsyncCallback(final Boolean expectedResult){
    	return new AsyncCallback<Boolean>()
        {
            public void onFailure(Throwable caught)
            {
                // The request resulted in an unexpected error.
                fail("Request failure: " + caught.getMessage());
            }

            public void onSuccess(Boolean result)
            {
                // Verify that the response is correct.
            	assertEquals(expectedResult, result);

                // Now that we have received a response, we need to tell the
                // test runner that the test is complete. You must call finishTest() after
                // an asynchronous test finishes successfully, or the test will time out.
                finishTest();
            }
        };
    }

    public void testLogin_invalidUser()
    {
        // Send login request to the server.
        authenticationService.login("invaliduser", "invalidpassword", createBooleanAsyncCallback(false));
    }
    
    public void testLogin_validUser()
    {
        // Send login request to the server.
        authenticationService.login("mostsoc", "demo12", createBooleanAsyncCallback(true));
    }
    
    public void testLogout_success()
    {
        // Send login request to the server.
        authenticationService.login("mostsoc", "demo12", createBooleanAsyncCallback(true));
        
        // Send logout request to the server.
        authenticationService.logout(createBooleanAsyncCallback(true));
    }

}
