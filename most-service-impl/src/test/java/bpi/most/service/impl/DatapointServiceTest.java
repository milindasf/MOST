package bpi.most.service.impl;

import bpi.most.domain.datapoint.DatapointVO;
import bpi.most.service.api.DatapointService;
import junit.framework.Assert;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;

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

    @Test
    @Transactional
    public void test_getDatapoints_noFilter_shouldReturnData() throws Exception {
        List<DatapointVO> datapoints = datapointService.getDatapoints();

        Assert.assertNotNull(datapoints);
        Assert.assertFalse(datapoints.isEmpty());
    }

    @Test
    @Transactional
    public void test_getDatapoints_matchingFilter_shouldReturnData() throws Exception {
        List<DatapointVO> datapoints = datapointService.getDatapoints("cdi");

        Assert.assertNotNull(datapoints);
        Assert.assertFalse(datapoints.isEmpty());
    }

    @Test
    @Transactional
    public void test_getDatapoints_nonMatchingFilter_shouldNotReturnData() throws Exception {
        List<DatapointVO> datapoints = datapointService.getDatapoints("non-matching-datapoint-filter");

        Assert.assertNotNull(datapoints);
        Assert.assertTrue(datapoints.isEmpty());
    }

    @Test
    @Transactional
    public void test_getDatapoints_zone_matchingFilter_shouldReturnData() throws Exception {
        List<DatapointVO> datapoints = datapointService.getDatapoints("con", "2");

        Assert.assertNotNull(datapoints);
        Assert.assertFalse(datapoints.isEmpty());
        for (DatapointVO datapoint : datapoints) {
            Assert.assertNotNull(datapoint);
        }
    }

    @Test
    @Transactional
    public void test_getDatapoints_zone_nonMatchingFilter_shouldNotReturnData() throws Exception {
        List<DatapointVO> datapoints = datapointService.getDatapoints("con", "non-existing-zone");

        Assert.assertNotNull(datapoints);
        Assert.assertTrue(datapoints.isEmpty());
    }

}
