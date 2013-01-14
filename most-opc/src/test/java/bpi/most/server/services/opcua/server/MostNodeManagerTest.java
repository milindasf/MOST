package bpi.most.server.services.opcua.server;

import bpi.most.server.services.opcua.server.nodes.DpNode;
import bpi.most.server.services.opcua.server.nodes.ZoneNode;
import bpi.most.service.api.DatapointService;
import bpi.most.service.api.ZoneService;
import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import javax.inject.Inject;
import java.util.List;

/**
 * Tests for {@link MostNodeManager}.
 *
 * @author Lukas Weichselbaum
 */
@ContextConfiguration(locations = "/META-INF/opcua.service.spring.xml")
public class MostNodeManagerTest extends AbstractTransactionalJUnit4SpringContextTests {


    @Inject private ZoneService zService;
    @Inject private DatapointService dpService;

    private MostNodeManager mostNodeManager;

    @Before
    public void setUp() throws Exception {
        mostNodeManager = new MostNodeManager("mostsoc", zService, dpService);
    }

    @After
    public void tearDown() throws Exception {
        mostNodeManager = null;
    }

    @Test
    public void testGetTopLevelElements() throws Exception {
        List<ZoneNode> zones = (List<ZoneNode>) mostNodeManager.getTopLevelElements();
        Assert.assertEquals(10, zones.get(0).getZoneID());
    }
    
    @Test
    public void testGetChildren() throws Exception {
        List<Object> zones = (List<Object>) mostNodeManager.getChildren(ZoneNode.class.getSimpleName(), "1");
        Assert.assertEquals(5, zones.size());
        
        zones = (List<Object>) mostNodeManager.getChildren(null, "1");
        Assert.assertEquals(0, zones.size());
        
        zones = (List<Object>) mostNodeManager.getChildren("asdf", "1");
        Assert.assertEquals(0, zones.size());
    }
    
    @Test
    public void testGetObjectById() throws Exception {
    	Object o = mostNodeManager.getObjectById(null, "1");
        Assert.assertNull(o);
        
        o = mostNodeManager.getObjectById(ZoneNode.class.getSimpleName(), "1");
        Assert.assertNotNull(o);
        
        o = mostNodeManager.getObjectById(DpNode.class.getSimpleName(), "name5");
        Assert.assertNotNull(o);
    }
}
