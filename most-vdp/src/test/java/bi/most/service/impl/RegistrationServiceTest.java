package bi.most.service.impl;

import bpi.most.dto.VdpProviderDTO;
import bpi.most.service.api.RegistrationService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/**
 *
 * User: harald
 *
 * tests the registration service over RMI; that means this testcase acts as RMI client.
 *
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:META-INF/most-vdp.spring.xml"})
public class RegistrationServiceTest{

    private static final String SERVER1 = "rmi://10.0.0.1:1199/DatapointService";
    private static final String SERVER2 = "rmi://10.0.0.2:1199/DatapointService";

    @Inject
    private RegistrationService regService;

    @Before
    public void setup() throws URISyntaxException {

        //vdp1 is implemented by two servers
        regService.register(new VdpProviderDTO("vdp1", new URI(SERVER1)));
        regService.register(new VdpProviderDTO("vdp1", new URI(SERVER2)));

        //vdp2 is implemented by one server
        regService.register(new VdpProviderDTO("vdp2", new URI(SERVER1)));
    }

    @After
    public void tearDown() {
        regService.clear();
    }

    @Test
    public void testExistence(){
        Assert.assertNotNull(regService);
    }

    @Test
    public void testGetProvider_existingSeveralProvider() throws Exception {
        VdpProviderDTO provider = regService.getServiceProvider("vdp1");
        junit.framework.Assert.assertEquals("vdp1", provider.getVdpType());
        junit.framework.Assert.assertNotNull(provider.getEndpoint());
    }

    @Test
    public void testGetProvider_existingOneProvider() throws Exception {
        VdpProviderDTO provider = regService.getServiceProvider("vdp2");
        junit.framework.Assert.assertEquals("vdp2", provider.getVdpType());
        junit.framework.Assert.assertEquals(SERVER1, provider.getEndpoint().toString());
    }

    @Test
    public void testGetProvider_nonExistingProvider() throws Exception {
        VdpProviderDTO provider = regService.getServiceProvider("vdp3");
        junit.framework.Assert.assertNull(provider);
    }

    @Test
    public void testAddNewNewVdpWithNewProvider() throws Exception {
        final VdpProviderDTO provider = new VdpProviderDTO("newVdp", new URI("rmi://10.0.0.69:1199/DatapointService"));
        regService.register(provider);
        VdpProviderDTO prov = regService.getServiceProvider(provider.getVdpType());
        junit.framework.Assert.assertEquals(provider.getVdpType(), prov.getVdpType());
        junit.framework.Assert.assertEquals(provider.getEndpoint(), prov.getEndpoint());
        junit.framework.Assert.assertEquals(1, regService.getServiceProviders(provider.getVdpType()).size());
    }

    @Test
    public void testAddNewVdpToExistingProvider() throws Exception {
        final VdpProviderDTO provider = new VdpProviderDTO("newVdp", new URI(SERVER2));
        regService.register(provider);
        VdpProviderDTO prov = regService.getServiceProvider(provider.getVdpType());
        junit.framework.Assert.assertEquals(provider.getVdpType(), prov.getVdpType());
        junit.framework.Assert.assertNotNull(prov.getEndpoint());
        List<VdpProviderDTO> providers = regService.getServiceProviders(provider.getVdpType());
        junit.framework.Assert.assertEquals(1, providers.size());
    }

    @Test
    public void testAddExistingVdpNewProvider() throws Exception {
        final VdpProviderDTO provider = new VdpProviderDTO("vdp1", new URI("rmi://10.0.0.69:1199/DatapointService"));
        regService.register(provider);
        List<VdpProviderDTO> providers = regService.getServiceProviders(provider.getVdpType());
        junit.framework.Assert.assertEquals(3, providers.size());
        boolean found1, found2, found3;
        found1 = found2 = found3 = false;
        for (VdpProviderDTO p: providers){
            junit.framework.Assert.assertEquals(provider.getVdpType(), p.getVdpType());
            if (p.getEndpoint().toString().equals(SERVER1)){
                found1 = true;
            } else if (p.getEndpoint().toString().equals(SERVER2)){
                found2 = true;
            } else if (p.getEndpoint().equals(provider.getEndpoint())){
                found3 = true;
            } else {
                junit.framework.Assert.fail("found wrong provider: " + p.getEndpoint());
            }
        }
        junit.framework.Assert.assertTrue(found1 && found2 && found3);
    }

    @Test
    public void testGetProviders() throws Exception {
        List<VdpProviderDTO> providers = regService.getServiceProviders("vdp1");
        junit.framework.Assert.assertEquals(2, providers.size());
        boolean found1, found2;
        found1 = found2 = false;
        for (VdpProviderDTO p: providers){
            junit.framework.Assert.assertEquals("vdp1", p.getVdpType());
            if (p.getEndpoint().toString().equals(SERVER1)){
                found1 = true;
            } else if (p.getEndpoint().toString().equals(SERVER2)){
                found2 = true;
            } else {
                junit.framework.Assert.fail("found wrong provider: " + p.getEndpoint());
            }
        }
        junit.framework.Assert.assertTrue(found1 && found2);
    }

    @Test
    public void testRemoveExistingProvider() throws Exception {
        regService.unregister("vdp2", new URI(SERVER1));
        junit.framework.Assert.assertNull(regService.getServiceProvider("vdp2"));
        junit.framework.Assert.assertTrue(regService.getServiceProviders("vdp2").isEmpty());
    }

    @Test
    public void testRemoveNonExistingProvider() throws Exception {
        regService.unregister("vdp2", new URI("rmi://10.0.0.21:1199/DatapointService"));
        junit.framework.Assert.assertEquals(1, regService.getServiceProviders("vdp2").size());
    }

    @Test
    public void testRemoveAllProvider() throws Exception {
        regService.unregister(new URI(SERVER1));
        junit.framework.Assert.assertEquals(1, regService.getServiceProviders("vdp1").size());
        junit.framework.Assert.assertEquals(0, regService.getServiceProviders("vdp2").size());
    }

    @Test
    public void testClear() throws Exception {
        regService.clear();
        junit.framework.Assert.assertEquals(0, regService.getServiceProviders("vdp1").size());
        junit.framework.Assert.assertEquals(0, regService.getServiceProviders("vdp2").size());
    }

}
