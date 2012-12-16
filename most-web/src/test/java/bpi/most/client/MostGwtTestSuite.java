package bpi.most.client;

import com.google.gwt.junit.tools.GWTTestSuite;
import junit.framework.Test;
import junit.framework.TestSuite;

public abstract class MostGwtTestSuite extends GWTTestSuite
{
    
    public static Test suite()
    {
        TestSuite suite = new TestSuite("Most GWT integration-test suite");
        // suite.addTestSuite(XYZ.class);
        return suite;
    }

}
