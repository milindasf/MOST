package bpi.most.server.services.opcua;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link bpi.most.service.impl.ZoneServiceImpl}.
 *
 * @author Lukas Weichselbaum
 */
public class OpcMostServerStartServletTest {
    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testInit() throws Exception {
        OpcMostServerStartServlet opcMostServerStartServlet = new OpcMostServerStartServlet();
        opcMostServerStartServlet.init();
    }
}
