package bpi.most.service.impl;

import bpi.most.dto.VdpProviderDTO;
import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/**
 *
 * User: harald
 *
 *
 */

public class InMemoryVdpRegistryTest {

    private static final String SERVER1 = "rmi://10.0.0.1:1199/DatapointService";
    private static final String SERVER2 = "rmi://10.0.0.2:1199/DatapointService";

    private InMemoryVdpRegistry registry;

    @Before
    public void setup() throws URISyntaxException {
        registry = new InMemoryVdpRegistry();

        //vdp1 is implemented by two servers
        registry.addProvider(new VdpProviderDTO("vdp1", new URI(SERVER1)));
        registry.addProvider(new VdpProviderDTO("vdp1", new URI(SERVER2)));

        //vdp2 is implemented by one server
        registry.addProvider(new VdpProviderDTO("vdp2", new URI(SERVER1)));
    }

    @After
    public void tearDown() {
        registry.clear();
    }

    @Test
    public void testGetProvider_existingSeveralProvider() throws Exception {
        VdpProviderDTO provider = registry.getProvider("vdp1");
        Assert.assertEquals("vdp1", provider.getVdpType());
        Assert.assertNotNull(provider.getEndpoint());
    }

    @Test
    public void testGetProvider_existingOneProvider() throws Exception {
        VdpProviderDTO provider = registry.getProvider("vdp2");
        Assert.assertEquals("vdp2", provider.getVdpType());
        Assert.assertEquals(SERVER1, provider.getEndpoint().toString());
    }

    @Test
    public void testGetProvider_nonExistingProvider() throws Exception {
        VdpProviderDTO provider = registry.getProvider("vdp3");
        Assert.assertNull(provider);
    }

    @Test
    public void testAddNewNewVdpWithNewProvider() throws Exception {
        final VdpProviderDTO provider = new VdpProviderDTO("newVdp", new URI("rmi://10.0.0.69:1199/DatapointService"));
        registry.addProvider(provider);
        VdpProviderDTO prov = registry.getProvider(provider.getVdpType());
        Assert.assertEquals(provider.getVdpType(), prov.getVdpType());
        Assert.assertEquals(provider.getEndpoint(), prov.getEndpoint());
        Assert.assertEquals(1, registry.getProviders(provider.getVdpType()).size());
    }

    @Test
    public void testAddNewVdpToExistingProvider() throws Exception {
        final VdpProviderDTO provider = new VdpProviderDTO("newVdp", new URI(SERVER2));
        registry.addProvider(provider);
        VdpProviderDTO prov = registry.getProvider(provider.getVdpType());
        Assert.assertEquals(provider.getVdpType(), prov.getVdpType());
        Assert.assertNotNull(prov.getEndpoint());
        List<VdpProviderDTO> providers = registry.getProviders(provider.getVdpType());
        Assert.assertEquals(1, providers.size());
    }

    @Test
    public void testAddExistingVdpNewProvider() throws Exception {
        final VdpProviderDTO provider = new VdpProviderDTO("vdp1", new URI("rmi://10.0.0.69:1199/DatapointService"));
        registry.addProvider(provider);
        List<VdpProviderDTO> providers = registry.getProviders(provider.getVdpType());
        Assert.assertEquals(3, providers.size());
        boolean found1, found2, found3;
        found1 = found2 = found3 = false;
        for (VdpProviderDTO p: providers){
            Assert.assertEquals(provider.getVdpType(), p.getVdpType());
            if (p.getEndpoint().toString().equals(SERVER1)){
                found1 = true;
            } else if (p.getEndpoint().toString().equals(SERVER2)){
                found2 = true;
            } else if (p.getEndpoint().equals(provider.getEndpoint())){
                found3 = true;
            } else {
                Assert.fail("found wrong provider: " + p.getEndpoint());
            }
        }
        Assert.assertTrue(found1 && found2 && found3);
    }

    @Test
    public void testGetProviders() throws Exception {
        List<VdpProviderDTO> providers = registry.getProviders("vdp1");
        Assert.assertEquals(2, providers.size());
        boolean found1, found2;
        found1 = found2 = false;
        for (VdpProviderDTO p: providers){
            Assert.assertEquals("vdp1", p.getVdpType());
            if (p.getEndpoint().toString().equals(SERVER1)){
                found1 = true;
            } else if (p.getEndpoint().toString().equals(SERVER2)){
                found2 = true;
            } else {
                Assert.fail("found wrong provider: " + p.getEndpoint());
            }
        }
        Assert.assertTrue(found1 && found2);
    }

    @Test
    public void testRemoveExistingProvider() throws Exception {
        registry.removeProvider("vdp2", new URI(SERVER1));
        Assert.assertNull(registry.getProvider("vdp2"));
        Assert.assertTrue(registry.getProviders("vdp2").isEmpty());
    }

    @Test
    public void testRemoveNonExistingProvider() throws Exception {
        registry.removeProvider("vdp2", new URI("rmi://10.0.0.21:1199/DatapointService"));
        Assert.assertEquals(1, registry.getProviders("vdp2").size());
    }

    @Test
    public void testRemoveAllProvider() throws Exception {
        registry.removeProvider(new URI(SERVER1));
        Assert.assertEquals(1, registry.getProviders("vdp1").size());
        Assert.assertEquals(0, registry.getProviders("vdp2").size());
    }

    @Test
    public void testClear() throws Exception {
        registry.clear();
        Assert.assertEquals(0, registry.getProviders("vdp1").size());
        Assert.assertEquals(0, registry.getProviders("vdp2").size());
    }
}
