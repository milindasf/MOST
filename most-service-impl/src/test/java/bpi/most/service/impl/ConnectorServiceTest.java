package bpi.most.service.impl;

import bpi.most.service.api.ConnectorService;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

/**
 * Tests for {@link ConnectorServiceImpl}.
 *
 * @author Lukas Weichselbaum
 */
@ContextConfiguration(locations = "/test-context.spring.xml")
public class ConnectorServiceTest extends AbstractTransactionalJUnit4SpringContextTests{

    @Inject
    private ConnectorService connectorServiceService;

    @Test
    @Transactional
    public void testName() throws Exception {
        // TODO: write tests
    }
}
