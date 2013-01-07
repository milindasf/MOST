package bpi.most.server.services.rest.impl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;

/**
 * Tests for {@link bpi.most.server.services.rest.impl.ZoneResImpl}.
 *
 * @author David Bittermann
 */
//TODO ASE Jakob 
//@ContextConfiguration(locations = "/WEB-INF/service.spring.xml")
public class ZoneResImplTest { //extends AbstractTransactionalJUnit4SpringContextTests {
    
	@Inject
	ZoneResImpl zoneRes;
	
	@Before
    public void setUp() throws Exception {
		zoneRes = new ZoneResImpl();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testGetZone() throws Exception {
//        ZoneDTO zone = zoneRes.getZone(1);
//        Assert.assertNotNull(zone);
    }
}
