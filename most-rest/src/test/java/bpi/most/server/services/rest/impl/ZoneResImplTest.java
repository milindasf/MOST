package bpi.most.server.services.rest.impl;

import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;

/**
 * Tests for {@link bpi.most.server.services.rest.impl.ZoneResImpl}.
 *
 * //its an integration test --> ignored in maven build (See bpi.most.client.rmi.DpServiceRmiClientTest for a more detailed description)
 *
 * @author Jakob Korherr
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:META-INF/rest-cxf.spring.xml", "classpath:META-INF/most-rmi-service.spring.xml"})
public class ZoneResImplTest{
    
	@Inject
	private ZoneResImpl zoneRes;
	
	@Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testGetZone() throws Exception {
        Assert.assertNotNull(zoneRes);
    }
}
