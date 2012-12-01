package bpi.most.client;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.google.gwt.junit.tools.GWTTestSuite;

public class MostGwtTestSuite extends GWTTestSuite
{
    
    public static Test suite()
    {
        TestSuite suite = new TestSuite("Most GWT integration-test suite");
        suite.addTestSuite(BasicGwtTest.class);
        suite.addTestSuite(AuthenticationServiceGwtTest.class);
        suite.addTestSuite(ZoneServiceGwtTest.class);
        suite.addTestSuite(DatapointServiceGwtTest.class);
        return suite;
    }

}
