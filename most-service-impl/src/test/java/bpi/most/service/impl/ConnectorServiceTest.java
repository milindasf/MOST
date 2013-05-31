package bpi.most.service.impl;

import bpi.most.service.api.ConnectorService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import javax.inject.Inject;

/**
 * Tests for {@link ConnectorServiceImpl}.
 *
 * @author Lukas Weichselbaum
 */
@ContextConfiguration(locations = "/META-INF/most-service.spring.xml")
public class ConnectorServiceTest extends AbstractTransactionalJUnit4SpringContextTests{

    @Inject
    private ConnectorService connectorServiceService;

    @Test
    public void testGetConnection_withNullAsUser_shouldReturnConnectors(){
        Assert.assertEquals(168, (connectorServiceService.getConnection(null).size()));
    }
}
