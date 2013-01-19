package bpi.most.obix.objects;

import bpi.most.dto.DpDTO;
import bpi.most.dto.DpDataDTO;
import bpi.most.dto.DpDatasetDTO;
import bpi.most.dto.UserDTO;
import bpi.most.obix.contracts.HistoryQueryOut;
import bpi.most.obix.history.HistoryQueryOutImpl;
import bpi.most.obix.history.HistoryRecordImpl;
import bpi.most.obix.io.ObixDecoder;
import bpi.most.obix.io.ObixEncoder;
import bpi.most.obix.server.IObjectBroker;
import bpi.most.server.services.rest.utils.DateUtils;
import bpi.most.service.api.DatapointService;
import junit.framework.Assert;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Alexej Strelzow
 * Date: 18.01.13
 * Time: 10:25
 */
@ContextConfiguration(locations = "/META-INF/obix.spring.xml")
public class TestHistory extends AbstractTransactionalJUnit4SpringContextTests {

    @Inject
    private DatapointService datapointService;

    @Inject
    private IObjectBroker objectBroker;

    @Test
    public void testHistoryRecord() {
        UserDTO user = new UserDTO("mostsoc");
        DpDTO dpDto1 = new DpDTO("cdi1");
        dpDto1 = datapointService.getDatapoint(user, dpDto1);

        String fromDateTime = "2012-08-29T17:24:00";
        String toDateTime = "2012-08-29T17:25:00";

        DpDatasetDTO data = datapointService.getData(user, dpDto1, DateUtils.returnNowOnNull(fromDateTime), DateUtils.returnNowOnNull(toDateTime));

        ArrayList<HistoryRecordImpl> records = new ArrayList<HistoryRecordImpl>();

        for (DpDataDTO d : data) {
            Real o = new Real("value", d.getValue());
            Abstime time = new Abstime("timestamp", d.getTimestamp().getTime());
            HistoryRecordImpl r = new HistoryRecordImpl(o, time);
            records.add(r);
        }

        HistoryQueryOutImpl query = new HistoryQueryOutImpl(records);
        query.setDpName(dpDto1.getName());
        query.setUnits("celsius");

        String history = ObixEncoder.toString(query);
        System.out.println(history);
        System.out.println();

        HistoryQueryOut decodedHistory = (HistoryQueryOut) ObixDecoder.fromString(history);
        ObixEncoder.dump((Obj)decodedHistory);

        Assert.assertEquals(query.count().get(), decodedHistory.count().get());
        Assert.assertEquals(query.start().get(), decodedHistory.start().get());
        Assert.assertEquals(query.end().get(), decodedHistory.end().get());
    }

    @Test
    public void testHistoryRollup() {
        UserDTO user = new UserDTO("mostsoc");
        DpDTO dpDto = new DpDTO("cdi1");
        dpDto = datapointService.getDatapoint(user, dpDto);

        Date fromDate = DateUtils.returnNowOnNull("2012-08-28T17:00:00");
        Date toDate = DateUtils.returnNowOnNull("2012-08-29T17:00:00");

        DpDatasetDTO data = datapointService.getDataPeriodic(user, dpDto, fromDate, toDate, (float) 60*5, 1);

        System.out.println();
    }

}
