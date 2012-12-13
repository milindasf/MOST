package bpi.most.service.impl;

import bpi.most.service.api.DatapointService;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

/**
 * Tests for {@link DatapointServiceImpl}.
 *
 * @author Lukas Weichselbaum
 */
@ContextConfiguration(locations = "/test-context.spring.xml")
public class DatapointServiceTest extends AbstractTransactionalJUnit4SpringContextTests{

    @Inject
    private DatapointService datapointService;

    @Test
    @Transactional
    public void testName() throws Exception {
        // TODO: write tests
    }
}
