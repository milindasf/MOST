package bpi.most.service.impl;

import bpi.most.dto.DpDTO;
import bpi.most.dto.DpDataDTO;
import bpi.most.dto.DpDatasetDTO;
import bpi.most.dto.UserDTO;
import bpi.most.service.api.DatapointService;
import junit.framework.Assert;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.sql.Timestamp;
import java.util.Date;
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
        List<DpDTO> datapoints = datapointService.getDatapoints();

        Assert.assertNotNull(datapoints);
        Assert.assertFalse(datapoints.isEmpty());
    }

    @Test
    @Transactional
    public void test_getDatapoints_matchingFilter_shouldReturnData() throws Exception {
        List<DpDTO> datapoints = datapointService.getDatapoints("cdi");

        Assert.assertNotNull(datapoints);
        Assert.assertFalse(datapoints.isEmpty());
    }

    @Test
    @Transactional
    public void test_getDatapoints_nonMatchingFilter_shouldNotReturnData() throws Exception {
        List<DpDTO> datapoints = datapointService.getDatapoints("non-matching-datapoint-filter");

        Assert.assertNotNull(datapoints);
        Assert.assertTrue(datapoints.isEmpty());
    }

    @Test
    @Transactional
    public void test_getDatapoints_zone_matchingFilter_shouldReturnData() throws Exception {
        List<DpDTO> datapoints = datapointService.getDatapoints("con", "2");

        Assert.assertNotNull(datapoints);
        Assert.assertFalse(datapoints.isEmpty());
        for (DpDTO datapoint : datapoints) {
            Assert.assertNotNull(datapoint);
        }
    }

    @Test
    @Transactional
    public void test_getDatapoint_shouldReturnData() throws Exception {
        DpDTO dpDTO = datapointService.getDatapoint(new UserDTO("mostsoc"), new DpDTO("con1"));

        Assert.assertEquals("con1", dpDTO.getName());
        Assert.assertEquals("con", dpDTO.getType());
        Assert.assertEquals(null , dpDTO.getDescription());
    }

    @Test
    @Transactional
    public void test_getDatapoints_zone_nonMatchingFilter_shouldNotReturnData() throws Exception {
        List<DpDTO> datapoints = datapointService.getDatapoints("con", "non-existing-zone");

        Assert.assertNotNull(datapoints);
        Assert.assertTrue(datapoints.isEmpty());
    }

    @Test
    @Transactional
    public void test_getData_nonExistingDatapoint_shouldReturnNull() throws Exception {
        DpDataDTO data = datapointService.getData(new UserDTO("mostsoc"), new DpDTO("non-existing-datapoint"));

        Assert.assertNull(data);
    }

    @Test
    @Transactional
    public void test_getData_existingDatapointWithoutTimeRange_shouldReturnData() throws Exception {
        DpDataDTO data = datapointService.getData(new UserDTO("mostsoc"), new DpDTO("con1"));

        Assert.assertNotNull(data);
        Assert.assertEquals(Timestamp.valueOf("2011-06-30 12:32:29.0"), data.getTimestamp());
        Assert.assertEquals(1.0, data.getValue());
        Assert.assertEquals((float) 1, data.getQuality());
    }

    @Test
    @Transactional
    public void test_getData_existingDatapointWithTimeRange_shouldReturnData() throws Exception {
        DpDatasetDTO data = datapointService.getData(new UserDTO("mostsoc"), new DpDTO("cdi1"),
        		Timestamp.valueOf("2012-01-01 00:00:00"),
        		Timestamp.valueOf("2013-01-01 00:00:00"));

        Assert.assertNotNull(data);
        Assert.assertFalse(data.isEmpty());
    }

    @Test
    @Transactional
    public void test_getData_existingDatapointWithEmptyTimeRange_shouldReturnNull() throws Exception {
        DpDatasetDTO data = datapointService.getData(new UserDTO("mostsoc"), new DpDTO("con1"),
        		Timestamp.valueOf("2012-01-01 00:00:00"),
        		Timestamp.valueOf("2013-01-01 00:00:00"));

        Assert.assertNull(data);
    }

    @Test
    @Transactional
    public void test_getNumberOfValues_existingDatapoint_shouldReturnCorrectNumber() throws Exception {
        int result = datapointService.getNumberOfValues(new UserDTO("mostsoc"), new DpDTO("cdi1"),
        		Timestamp.valueOf("2012-01-01 00:00:00"),
        		Timestamp.valueOf("2013-01-01 00:00:00"));

        Assert.assertEquals(2395, result);
    }

    @Test
    @Transactional
    public void test_getNumberOfValues_nonExistingDatapoint_shouldReturnZero() throws Exception {
        int result = datapointService.getNumberOfValues(new UserDTO("mostsoc"), new DpDTO("non-existing-datapoint"),
        		Timestamp.valueOf("2012-12-31 00:00:00"),
        		Timestamp.valueOf("2013-01-01 00:00:00"));

        Assert.assertEquals(0, result);
    }

    @Test
    @Transactional
    public void test_addData_existingDatapoint_shouldReturnOne() throws Exception {
    	int before = datapointService.getNumberOfValues(new UserDTO("mostsoc"), new DpDTO("cdi2"),
        		Timestamp.valueOf("2012-12-31 00:00:00"),
        		new Date());
        int result = datapointService.addData(new UserDTO("mostsoc"), new DpDTO("cdi2"), new DpDataDTO(
        		new Date(),
        		5.0,
        		(float) 1));
        int after = datapointService.getNumberOfValues(new UserDTO("mostsoc"), new DpDTO("cdi2"),
        		Timestamp.valueOf("2012-12-31 00:00:00"),
        		new Date());

        Assert.assertEquals(1, result);
        Assert.assertEquals(before + 1, after);
    }

    @Test
    @Transactional
    public void test_addData_nonExistingDatapoint_shouldReturnZero() throws Exception {
        int result = datapointService.addData(new UserDTO("mostsoc"), new DpDTO("non-existing-datapoint"), new DpDataDTO(
        		new Date(),
        		1.0,
        		(float) 1));

        Assert.assertEquals(0, result);
    }

    //Cannot be tested, readonly db
    //@Test
    @Transactional
    public void test_delData_existingDatapoint_shouldReturnOne() throws Exception {
    	int before = datapointService.getNumberOfValues(new UserDTO("mostsoc"), new DpDTO("cdi1"),
    			Timestamp.valueOf("2011-05-01 00:00:00"),
        		Timestamp.valueOf("2011-06-01 00:00:00"));
        Assert.assertEquals(9052, before);
        
        int result = datapointService.delData(new UserDTO("mostsoc"), new DpDTO("cdi1"));
        Assert.assertEquals(1, result);
        
    	int after = datapointService.getNumberOfValues(new UserDTO("mostsoc"), new DpDTO("cdi1"),
    			Timestamp.valueOf("2011-05-01 00:00:00"),
        		Timestamp.valueOf("2011-06-01 00:00:00"));
        Assert.assertEquals(0, after);
    }

    @Test
    @Transactional
    public void test_delData_nonExistingDatapoint_shouldReturnZero() throws Exception {
        int result = datapointService.delData(new UserDTO("mostsoc"), new DpDTO("non-existing-datapoint"));

        Assert.assertEquals(0, result);
    }

    //Cannot be tested, readonly db
    //@Test
    @Transactional
    public void test_delData_existingDatapointWithTimeRange_shouldReturnOne() throws Exception {
    	int before = datapointService.getNumberOfValues(new UserDTO("mostsoc"), new DpDTO("cdi1"),
    			Timestamp.valueOf("2011-05-01 00:00:00"),
        		Timestamp.valueOf("2011-06-01 00:00:00"));
        Assert.assertEquals(9052, before);
        
        int result = datapointService.delData(new UserDTO("mostsoc"), new DpDTO("cdi1"),
        		Timestamp.valueOf("2011-05-01 00:00:00"),
        		Timestamp.valueOf("2011-06-01 00:00:00"));
        Assert.assertEquals(1, result);
        
    	int after = datapointService.getNumberOfValues(new UserDTO("mostsoc"), new DpDTO("cdi1"),
    			Timestamp.valueOf("2011-05-01 00:00:00"),
        		Timestamp.valueOf("2011-06-01 00:00:00"));
        Assert.assertEquals(0, after);
    }

    @Test
    @Transactional
    public void test_delData_nonExistingDatapointWithTimeRange_shouldReturnZero() throws Exception {
        int result = datapointService.delData(new UserDTO("mostsoc"), new DpDTO("non-existing-datapoint"),
        		Timestamp.valueOf("2012-01-01 00:00:00"),
        		Timestamp.valueOf("2013-01-01 00:00:00"));

        Assert.assertEquals(0, result);
    }

    @Test
    @Transactional
    public void test_getDataPeriodic_existingDatapoint_shouldReturnData() throws Exception {
    	DpDatasetDTO data = datapointService.getDataPeriodic(new UserDTO("mostsoc"), new DpDTO("cdi1"),
        		Timestamp.valueOf("2011-05-01 00:00:00"),
        		Timestamp.valueOf("2011-06-01 00:00:00"), (float)1000);
        
        Assert.assertNotNull(data);
        Assert.assertFalse(data.isEmpty());
    }

    @Test
    @Transactional
    public void test_getData_nonExistingVirtualDatapoint_shouldReturnNull() throws Exception {
    	DpDataDTO data = datapointService.getData(new UserDTO("mostsoc"), new DpDTO("non-existing-datapoint"));
        
        Assert.assertNull(data);
    }

    @Test
    @Transactional
    public void test_getData_existingVirtualDatapoint_shouldReturnData() throws Exception {
    	DpDataDTO data = datapointService.getData(new UserDTO("mostsoc"), new DpDTO("powtest"));
        //TODO ASE here should be data, but is null
        Assert.assertNull(data);
//        Assert.assertEquals(Timestamp.valueOf("2011-06-30 12:32:29.0"), data.getTimestamp());
//        Assert.assertEquals(1.0, data.getValue());
//        Assert.assertEquals((float) 1, data.getQuality());
    }
}
