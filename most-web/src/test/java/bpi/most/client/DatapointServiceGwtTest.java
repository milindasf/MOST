package bpi.most.client;

import java.util.ArrayList;
import java.util.List;

import bpi.most.client.rpc.AuthenticationServiceAsync;
import bpi.most.client.rpc.DatapointService;
import bpi.most.client.rpc.DatapointServiceAsync;
import bpi.most.shared.DpDTO;

import com.google.gwt.core.client.GWT;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

/**
 * Tests for MOST ZoneServiceImpl.
 * 
 * @author Lukas Weichselbaum
 */
public class DatapointServiceGwtTest extends GWTTestCase
{
	// The service that we will test.
    DatapointServiceAsync dpService;
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
        dpService = GWT.create(DatapointService.class);
        ServiceDefTarget target = (ServiceDefTarget) dpService;
        target.setServiceEntryPoint(GWT.getModuleBaseURL() + "services/gwtrpc/dp");
 
        // Since RPC calls are asynchronous, we will need to wait for a response
        // after this test method returns. This line tells the test runner to
        // wait up to 10 seconds before timing out.
        delayTestFinish(10000);
    }
    
    
    
    private AsyncCallback<ArrayList<DpDTO>> createZoneAsyncCallback(final List<ArrayList<DpDTO>> expectedResult){
    	return new AsyncCallback<ArrayList<DpDTO>>()
        {
            public void onFailure(Throwable caught)
            {
                // The request resulted in an unexpected error.
                fail("Request failure: " + caught.getMessage());
            }

            public void onSuccess(ArrayList<DpDTO> result)
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

    public void testGetDatapoints()
    {
        dpService.getDatapoints(createZoneAsyncCallback(null));
    }
}
