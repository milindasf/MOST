package bpi.most.service.impl;

import bpi.most.service.api.ZoneService;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

/**
 * Tests for {@link ZoneServiceImpl}.
 *
 * @author Lukas Weichselbaum
 */
@ContextConfiguration(locations = "/META-INF/most-service.spring.xml")
public class ZoneServiceTest extends AbstractTransactionalJUnit4SpringContextTests{

    @Inject
    private ZoneService zoneService;

    @Test
    @Transactional
    public void testName() throws Exception {
        // TODO: write tests
    }
}
