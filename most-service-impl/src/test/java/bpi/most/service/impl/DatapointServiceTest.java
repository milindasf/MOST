package bpi.most.service.impl;

import bpi.most.service.api.DatapointService;
import junit.framework.Assert;
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
@ContextConfiguration(locations = "/META-INF/most-service.spring.xml")
public class DatapointServiceTest extends AbstractTransactionalJUnit4SpringContextTests{

    @Inject
    private DatapointService datapointService;

    @Test
    @Transactional
    public void test_isValidDp_nonExistingDatapoint_shouldBeFalse() throws Exception {
        Assert.assertFalse(datapointService.isValidDp("non-existing-datapoint"));
    }

    @Test
    @Transactional
    public void test_isValidDp_existingDatapoint_shouldBeTrue() throws Exception {
        Assert.assertTrue(datapointService.isValidDp("cdi1"));
    }

}
