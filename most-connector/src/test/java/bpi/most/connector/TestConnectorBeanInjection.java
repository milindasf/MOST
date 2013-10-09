package bpi.most.connector;

import bpi.most.service.api.DatapointService;
import junit.framework.Assert;
import org.junit.AfterClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import javax.inject.Inject;
import java.util.List;

/**
 *
 * Tests if Connector-Implementations get their spring beans injected correctly
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:META-INF/connector.test.spring.xml"})
public class TestConnectorBeanInjection {

    @Inject
    ConnectorBooter booter;

    @Inject
    private ApplicationContext ctx;

    @Test
    public void testGotBooter(){
        Assert.assertNotNull(booter);
    }

    /**
     * tests if the one and only dummyconnector got his spring-services correctly wired.
     * @throws Exception
     */
    @Test
    public void testInjection() throws Exception {
        booter.boot();
        List<Connector> connectorList = booter.getActiveConnectors();
        Assert.assertEquals(1, connectorList.size());
        Assert.assertTrue(connectorList.get(0) instanceof DummyConnector);

        DummyConnector dummyCon = (DummyConnector) connectorList.get(0);
        Assert.assertNotNull(dummyCon.getDpService());
        Assert.assertTrue(dummyCon.getDpService() instanceof  DummyDpService);
    }

    @Test
    public void testPostConstructIsCalled() throws Exception{
        booter.boot();
        List<Connector> connectorList = booter.getActiveConnectors();
        DummyConnector dummyCon = (DummyConnector) connectorList.get(0);
        Assert.assertTrue(dummyCon.isInitialized());
    }

    @Test
    public void testSeveralPostConstructMethods() throws Exception{
        booter.boot();
        List<Connector> connectorList = booter.getActiveConnectors();
        DummyConnector dummyCon = (DummyConnector) connectorList.get(0);
        Assert.assertTrue(dummyCon.isInitialized());
        Assert.assertTrue(dummyCon.isAnotherInit());
    }
}
