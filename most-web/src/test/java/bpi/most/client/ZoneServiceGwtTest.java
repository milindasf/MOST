package bpi.most.client;

import java.util.ArrayList;
import java.util.List;

import bpi.most.client.rpc.AuthenticationService;
import bpi.most.client.rpc.AuthenticationServiceAsync;
import bpi.most.client.rpc.ZoneService;
import bpi.most.client.rpc.ZoneServiceAsync;
import bpi.most.shared.ZoneDTO;

import com.google.gwt.core.client.GWT;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

/**
 * Tests for MOST GwtRpcZoneService.
 * 
 * @author Lukas Weichselbaum
 * @author Christoph Lauscher
 */
public class ZoneServiceGwtTest extends GWTTestCase
{
	// The service that we will test.
    ZoneServiceAsync zoneService;
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
        zoneService = GWT.create(ZoneService.class);
        ServiceDefTarget target = (ServiceDefTarget) zoneService;
        target.setServiceEntryPoint(GWT.getModuleBaseURL() + "services/gwtrpc/zone");
        
        authenticationService = GWT.create(AuthenticationService.class);
        ServiceDefTarget targetAuth = (ServiceDefTarget) authenticationService;
        targetAuth.setServiceEntryPoint(GWT.getModuleBaseURL() + "services/gwtrpc/authentication");

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
                
            	// query head zones after successful login
            	List<String> expected = new ArrayList<String>();
            	expected.add("name4");
            	
                zoneService.getHeadZones(createZoneAsyncCallback(expected)); // TODO: replace null with something useful

                // Now that we have received a response, we need to tell the
                // test runner that the test is complete. You must call finishTest() after
                // an asynchronous test finishes successfully, or the test will time out.
                //finishTest();
            }
        };
    }
    
    private AsyncCallback<List<ZoneDTO>> createZoneAsyncCallback(final List<String> expectedResult){
    	return new AsyncCallback<List<ZoneDTO>>()
        {
            public void onFailure(Throwable caught)
            {
                // The request resulted in an unexpected error.
                fail("Request failure: " + caught.getMessage());
            }

            public void onSuccess(List<ZoneDTO> result)
            {
                // Verify that the response is correct.
        		assertNotNull(result);
        		
        		List<String> resultNames = new ArrayList<String>();
        		
            	for(ZoneDTO z : result){
            		resultNames.add(z.getName());
            	}
            	
            	assertEquals(expectedResult, resultNames);

                // Now that we have received a response, we need to tell the
                // test runner that the test is complete. You must call finishTest() after
                // an asynchronous test finishes successfully, or the test will time out.
                finishTest();
            }
        };
    }

    public void testGetHeadZones()
    {
    	// Send login request to the server.
        authenticationService.login("mostsoc", "demo12", createBooleanAsyncCallback(true));
    }
}
