package bpi.most.obix.objects;

import bpi.most.dto.DpDTO;
import bpi.most.dto.DpDataDTO;
import bpi.most.dto.UserDTO;
import bpi.most.obix.history.HistoryQueryOutImpl;
import bpi.most.obix.history.HistoryRollupOutImpl;
import bpi.most.obix.io.ObixEncoder;
import bpi.most.obix.server.IObjectBroker;
import bpi.most.service.api.DatapointService;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import javax.inject.Inject;

/**
 * Created with IntelliJ IDEA.
 * User: Alexej Strelzow
 * Date: 30.12.12
 * Time: 17:16
 * To change this template use File | Settings | File Templates.
 */
@ContextConfiguration(locations = "/META-INF/obix.spring.xml")
public class DatapointTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Inject
    private DatapointService datapointService;

    @Inject
    private IObjectBroker objectBroker;


    /**
     * There you can find names of data points
     * @throws Exception
     */
    @Test
    public void testExample() throws Exception {
        bpi.most.obix.objects.List dataPoints = objectBroker.getAllDps();

        ObixEncoder.dump(dataPoints);
    }

    @Test
    public void testGetDp_fromUser_shouldReturnSameResultsAsService() throws Exception {
        UserDTO user = new UserDTO("mostsoc");
        DpDTO dpDto = new DpDTO("cdi1");
        dpDto = datapointService.getDatapoint(user, dpDto);

        Dp dp = objectBroker.getDp(user, dpDto);

        junit.framework.Assert.assertEquals(dp.getDatapointName().get(), dpDto.getName());
        junit.framework.Assert.assertEquals(dp.getType().get(), dpDto.getType());
    }

    @Test
    public void testGetDpData_fromUser_shouldReturnSameResultsAsService() throws Exception {
        UserDTO user = new UserDTO("mostsoc");
        DpDTO dpDto = new DpDTO("cdi1");
        dpDto = datapointService.getDatapoint(user, dpDto);
        DpDataDTO dpDataDto = datapointService.getData(user, dpDto) ;

        DpData dpData = objectBroker.getDpData(user, dpDto);

        junit.framework.Assert.assertEquals(dpData.getTimestamp().get(), dpDataDto.getTimestamp().getTime());
        junit.framework.Assert.assertEquals(dpData.getValue().get(), dpDataDto.getValue());
    }

    @Test
    public void testGetDpDataFromIntervall_fromUser_shouldReturnSameResultsAsService() throws Exception {
        UserDTO user = new UserDTO("mostsoc");
        DpDTO dpDto1 = new DpDTO("cdi1");
        String fromDateTime = "2012-08-01T00:00:00";
        String toDateTime = "2012-08-30T09:00:00";

        HistoryQueryOutImpl queryOutput = objectBroker.getDpData(user, dpDto1, fromDateTime, toDateTime);
        ObixEncoder.dump(queryOutput);
    }

    @Test
    public void testGetDpDataPeriodic_fromUser_shouldReturnSameResultsAsService() throws Exception {
        UserDTO user = new UserDTO("mostsoc");
        DpDTO dpDto = new DpDTO("cdi1");
        dpDto = datapointService.getDatapoint(user, dpDto);

        String fromDateTime = "2012-08-28T17:00:00";
        String toDateTime = "2012-08-29T17:00:00";

        HistoryRollupOutImpl rollupOutput = objectBroker.getDpPeriodicData(user, dpDto, fromDateTime, toDateTime, (float) 60 * 5, 1, 60*60);
        rollupOutput.setDpName(dpDto.getName());
        rollupOutput.setUnits("kilowatt");

        ObixEncoder.dump(rollupOutput);
    }

}
