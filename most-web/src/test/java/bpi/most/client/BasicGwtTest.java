package bpi.most.client;

import bpi.most.client.rpc.AuthenticationService;
import bpi.most.client.rpc.AuthenticationServiceAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

/**
 * Basic sample tests for MOST GWT.
 * 
 * @author Jakob Korherr
 * @author Lukas Weichselbaum
 */
public class BasicGwtTest extends GWTTestCase
{

    /**
     * Must refer to a valid module that sources this class.
     */
    public String getModuleName()
    {
        return "bpi.most.MostTest";
    }

    public void testDummy()
    {
        assertEquals(true, true); // dummy assertion
    }

    public void testAuthenticationService_wrongLoginData_shouldReturnFalse()
    {
        // Create the service that we will test.
        AuthenticationServiceAsync authenticationService = GWT.create(AuthenticationService.class);
        ServiceDefTarget target = (ServiceDefTarget) authenticationService;
        target.setServiceEntryPoint(GWT.getModuleBaseURL() + "services/gwtrpc/authentication");

        // Since RPC calls are asynchronous, we will need to wait for a response
        // after this test method returns. This line tells the test runner to
        // wait up to 10 seconds before timing out.
        delayTestFinish(10000);

        // Send a request to the server.
        authenticationService.login("invaliduser", "invalidpassword", new AsyncCallback<Boolean>()
        {
            public void onFailure(Throwable caught)
            {
                // The request resulted in an unexpected error.
                fail("Request failure: " + caught.getMessage());
            }

            public void onSuccess(Boolean result)
            {
                // Verify that the response is correct.
                assertFalse(result);

                // Now that we have received a response, we need to tell the
                // test runner that the test is complete. You must call finishTest() after
                // an asynchronous test finishes successfully, or the test will time out.
                finishTest();
            }
        });
    }
    
    public void testAuthenticationService_correctLoginData_shouldReturnTrue()
    {
        // Create the service that we will test.
        AuthenticationServiceAsync authenticationService = GWT.create(AuthenticationService.class);
        ServiceDefTarget target = (ServiceDefTarget) authenticationService;
        target.setServiceEntryPoint(GWT.getModuleBaseURL() + "services/gwtrpc/authentication");

        // Since RPC calls are asynchronous, we will need to wait for a response
        // after this test method returns. This line tells the test runner to
        // wait up to 10 seconds before timing out.
        delayTestFinish(10000);

        // Send a request to the server.
        authenticationService.login("mostsoc", "demo12", new AsyncCallback<Boolean>()
        {
            public void onFailure(Throwable caught)
            {
                // The request resulted in an unexpected error.
                fail("Request failure: " + caught.getMessage());
            }

            public void onSuccess(Boolean result)
            {
                // Verify that the response is correct.
                assertTrue(result);

                // Now that we have received a response, we need to tell the
                // test runner that the test is complete. You must call finishTest() after
                // an asynchronous test finishes successfully, or the test will time out.
                finishTest();
            }
        });
    }

}
