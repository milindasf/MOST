package bpi.most.service.impl;

import bpi.most.domain.connector.ConnectorVO;
import bpi.most.dto.ConnectorDTO;
import bpi.most.service.api.ConnectorService;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import javax.inject.Inject;
import java.util.List;

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

        /*
        Its a bad idea to use absolute numbers in testcases. this is only useful to prepare tests in some developing
        environment before deploying to production environment

        Assert.assertEquals(168, (connectorServiceService.getConnection(null).size()));
         */

        Assert.assertFalse((connectorServiceService.getConnection(null).isEmpty()));
    }
}
