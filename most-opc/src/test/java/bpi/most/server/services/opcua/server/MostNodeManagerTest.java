package bpi.most.server.services.opcua.server;

import java.util.List;

import javax.inject.Inject;

import bpi.most.dto.UserDTO;
import bpi.most.opcua.server.core.RequestContext;
import bpi.most.opcua.server.core.Session;
import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import bpi.most.domain.zone.Zone;
import bpi.most.server.services.opcua.server.nodes.DpNode;
import bpi.most.server.services.opcua.server.nodes.ZoneNode;

/**
 * Tests for {@link MostNodeManager}.
 *
 * @author Lukas Weichselbaum
 */
@ContextConfiguration(locations = "/META-INF/opcua.service.spring.xml")
public class MostNodeManagerTest extends AbstractTransactionalJUnit4SpringContextTests {
    @Inject
    MostNodeManager mostNodeManager;

    @Before
    public void setUp() throws Exception{
        Session s = new Session();
        s.setCustomObj(new UserDTO("mostsoc"));
        RequestContext.get().setSession(s);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testGetTopLevelElements() throws Exception {
        List<ZoneNode> zones = (List<ZoneNode>) mostNodeManager.getTopLevelElements();
        Assert.assertEquals(10, zones.get(0).getZoneID());
    }
    
    @Test
    public void testGetChildren() throws Exception {
        List<Object> zones = (List<Object>) mostNodeManager.getChildren(ZoneNode.class, "1");
        Assert.assertEquals(5, zones.size());
        
        zones = (List<Object>) mostNodeManager.getChildren(null, "1");
        Assert.assertEquals(0, zones.size());
        
        zones = (List<Object>) mostNodeManager.getChildren(Zone.class, "1");
        Assert.assertEquals(0, zones.size());
    }
    
    @Test
    public void testGetObjectById() throws Exception {
    	Object o = mostNodeManager.getObjectById(null, "1");
        Assert.assertNull(o);
        
        o = mostNodeManager.getObjectById(ZoneNode.class, "1");
        Assert.assertNotNull(o);
        
        o = mostNodeManager.getObjectById(DpNode.class, "name5");
        Assert.assertNotNull(o);
    }
}
