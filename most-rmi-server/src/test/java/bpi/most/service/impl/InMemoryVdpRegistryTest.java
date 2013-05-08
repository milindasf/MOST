package bpi.most.service.impl;

import bpi.most.dto.VdpProviderDTO;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;

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

    //TODO implement other tests

    @Test
    public void testAddProvider() throws Exception {

    }

    @Test
    public void testGetProviders() throws Exception {

    }

    @Test
    public void testRemoveProvider() throws Exception {

    }

    @Test
    public void testRemoveAllProvider() throws Exception {

    }
}
