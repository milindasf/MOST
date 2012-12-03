package bpi.most.client;

import java.util.ArrayList;
import java.util.List;

import bpi.most.client.rpc.AuthenticationService;
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
 * @author Christoph Lauscher
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
        
        authenticationService = GWT.create(AuthenticationService.class);
        ServiceDefTarget targetAuth = (ServiceDefTarget) authenticationService;
        targetAuth.setServiceEntryPoint(GWT.getModuleBaseURL() + "services/gwtrpc/authentication");
 
        // Since RPC calls are asynchronous, we will need to wait for a response
        // after this test method returns. This line tells the test runner to
        // wait up to 10 seconds before timing out.
        delayTestFinish(10000);
    }
    
    
    
    private AsyncCallback<ArrayList<DpDTO>> createDatapointAsyncCallback(final List<String> expectedResult){
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
        		assertNotNull(result);
        		
        		List<String> resultNames = new ArrayList<String>();
        		
            	for(DpDTO d : result){
            		resultNames.add(d.getName());
            	}
            	
            	assertEquals(expectedResult, resultNames);

                // Now that we have received a response, we need to tell the
                // test runner that the test is complete. You must call finishTest() after
                // an asynchronous test finishes successfully, or the test will time out.
                finishTest();
            }
        };
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
                
            	// query datapoints after successful login
            	List<String> expected = new ArrayList<String>();
            	expected.add("cdi1");
            	expected.add("cdi2");
            	expected.add("cdi3");
            	expected.add("cdi4");
            	expected.add("cdi5");
            	expected.add("cdi6");
            	expected.add("cdi7");
            	expected.add("con1");
            	expected.add("con2");
            	expected.add("con3");
            	expected.add("con4");
            	expected.add("con5");
            	expected.add("con6");
            	expected.add("con7");
            	expected.add("con8");
            	expected.add("con9");
            	expected.add("occ1");
            	expected.add("occ2");
            	expected.add("occ3");
            	expected.add("occ4");
            	expected.add("occ5");
            	expected.add("occ6");
            	expected.add("occ7");
            	expected.add("occ8");
            	expected.add("occ9");
            	expected.add("rhu1");
            	expected.add("rhu2");
            	expected.add("tem1");
            	expected.add("tem2");
            	expected.add("tem3");
            	expected.add("tem4");
            	expected.add("voc1");
            	expected.add("voc2");
            	expected.add("voc3");
            	expected.add("voc4");
            	expected.add("voc5");
            	expected.add("voc6");
            	expected.add("voc7");
            	expected.add("con10");
            	expected.add("con11");
            	expected.add("con12");
            	expected.add("con13");
            	expected.add("con14");
            	expected.add("con15");
            	expected.add("con16");
            	expected.add("con17");
            	expected.add("con18");
            	expected.add("con19");
            	expected.add("con20");
            	expected.add("con21");
            	expected.add("con22");
            	expected.add("con23");
            	expected.add("con24");
            	expected.add("con25");
            	expected.add("con26");
            	expected.add("name3");
            	expected.add("name4");
            	expected.add("name5");
            	expected.add("name6");
            	expected.add("name7");
            	expected.add("occ10");
            	expected.add("occ11");
            	expected.add("rhu11");
            	expected.add("rhu12");
            	expected.add("rhu13");
            	expected.add("rhu14");
            	expected.add("rhu15");
            	expected.add("rhu16");
            	expected.add("rhu17");
            	expected.add("tcon1");
            	expected.add("tcon2");
            	expected.add("tcon3");
            	expected.add("tcon4");
            	expected.add("tcon5");
            	expected.add("tcon6");
            	expected.add("tcon7");
            	expected.add("tcon8");
            	expected.add("tcon9");
            	expected.add("tem11");
            	expected.add("tem12");
            	expected.add("tem13");
            	expected.add("tem14");
            	expected.add("tem15");
            	expected.add("tem16");
            	expected.add("tem17");
            	expected.add("WsRad1");
            	expected.add("WsRhu1");
            	expected.add("WsTem1");
            	expected.add("illum1");
            	expected.add("illum2");
            	expected.add("illum3");
            	expected.add("illum4");
            	expected.add("illum5");
            	expected.add("illum6");
            	expected.add("illum7");
            	expected.add("illum8");
            	expected.add("illum9");
            	expected.add("tcon10");
            	expected.add("tcon11");
            	expected.add("tcon12");
            	expected.add("tem100");
            	expected.add("tem101");
            	expected.add("tem123");
            	expected.add("tem124");
            	expected.add("WsBaro1");
            	expected.add("illum10");
            	expected.add("illum11");
            	expected.add("light14");
            	expected.add("light15");
            	expected.add("light16");
            	expected.add("light17");
            	expected.add("light18");
            	expected.add("light19");
            	expected.add("light20");
            	expected.add("light21");
            	expected.add("light22");
            	expected.add("light23");
            	expected.add("light24");
            	expected.add("light25");
            	expected.add("light26");
            	expected.add("powtest");
            	expected.add("ele-met1");
            	expected.add("ele-met2");
            	expected.add("ele-met3");
            	expected.add("ele-met4");
            	expected.add("ele-met5");
            	expected.add("ele-met6");
            	expected.add("ele-met7");
            	expected.add("ele-met8");
            	expected.add("ele-met9");
            	expected.add("ele-pow1");
            	expected.add("ele-pow2");
            	expected.add("ele-pow3");
            	expected.add("ele-pow4");
            	expected.add("ele-pow5");
            	expected.add("ele-pow6");
            	expected.add("ele-pow7");
            	expected.add("ele-pow8");
            	expected.add("ele-pow9");
            	expected.add("ele-met10");
            	expected.add("ele-met11");
            	expected.add("ele-met12");
            	expected.add("ele-met13");
            	expected.add("ele-met14");
            	expected.add("ele-met15");
            	expected.add("ele-met16");
            	expected.add("ele-met17");
            	expected.add("ele-met18");
            	expected.add("ele-met19");
            	expected.add("ele-met20");
            	expected.add("ele-met21");
            	expected.add("ele-met22");
            	expected.add("ele-met23");
            	expected.add("ele-met24");
            	expected.add("ele-met25");
            	expected.add("ele-met26");
            	expected.add("ele-met27");
            	expected.add("ele-met28");
            	expected.add("ele-met29");
            	expected.add("ele-pow10");
            	expected.add("ele-pow11");
            	expected.add("ele-pow12");
            	expected.add("ele-pow13");
            	expected.add("ele-pow14");
            	expected.add("ele-pow15");
            	expected.add("ele-pow16");
            	expected.add("ele-pow17");
            	expected.add("ele-pow18");
            	expected.add("ele-pow19");
            	expected.add("ele-pow20");
            	expected.add("ele-pow21");
            	expected.add("ele-pow22");
            	expected.add("ele-pow23");
            	expected.add("ele-pow24");
            	expected.add("ele-pow25");
            	expected.add("ele-pow26");
            	expected.add("ele-pow27");
            	expected.add("ele-pow28");
            	expected.add("ele-pow29");
            	expected.add("WsRainrate1");
            	expected.add("WsWindSpeed1");
            	expected.add("WsWindDirection1");
            	
            	dpService.getDatapoints(createDatapointAsyncCallback(expected));

                // Now that we have received a response, we need to tell the
                // test runner that the test is complete. You must call finishTest() after
                // an asynchronous test finishes successfully, or the test will time out.
                //finishTest();
            }
        };
    }

    public void testGetDatapoints()
    {
    	// Send login request to the server.
        authenticationService.login("mostsoc", "demo12", createBooleanAsyncCallback(true));
        
        //dpService.getDatapoints(createZoneAsyncCallback(null));
    }
}
