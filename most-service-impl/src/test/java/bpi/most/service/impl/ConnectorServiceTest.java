package bpi.most.service.impl;

import bpi.most.service.api.ConnectorService;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

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
    @Ignore  // TODO currently ignored, b/c of db bug
    public void testGetConnection_withNullAsUser_shouldReturnConnectors(){
        Assert.notEmpty(connectorServiceService.getConnection(null));    //TODO: improve test case; at the moment there is no table "connection" in DB!?
    }
}
