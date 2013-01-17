package bpi.most.obix.objects;

import bpi.most.dto.DpDTO;
import bpi.most.dto.UserDTO;
import bpi.most.dto.ZoneDTO;
import bpi.most.obix.io.ObixEncoder;
import bpi.most.obix.server.IObjectBroker;
import bpi.most.service.api.DatapointService;
import bpi.most.service.api.ZoneService;
import org.junit.After;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import javax.inject.Inject;
import java.util.List;

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
    private ZoneService zoneService;

    @Inject
    private DatapointService datapointService;

    @Inject
    private IObjectBroker objectBroker;

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testZoneTransformationToObix_fromUser_shouldTransform1Zone() throws Exception {
        UserDTO user = new UserDTO("mostsoc");
        List<ZoneDTO> zones = zoneService.getHeadZones(user);
        junit.framework.Assert.assertEquals(1, zones.size());

        ZoneDTO zone = zoneService.getZone(user, zones.get(0));  // ATTENTION: ZoneDTOs have just set the zoneId per default, if you would like to have all values set, you have to use zoneService.getZone(user, zoneDtoObj)!!
        int id = zone.getZoneId();
        String name = zone.getName();
        Uri zoneUri = new Uri(bpi.most.obix.objects.Zone.OBIX_ZONE_PREFIX + id);
        bpi.most.obix.objects.Zone oBixZone = new bpi.most.obix.objects.Zone(id, name);

        junit.framework.Assert.assertEquals(zone.getZoneId(), (int)oBixZone.getZone().get());
        junit.framework.Assert.assertEquals(zone.getName(), oBixZone.getZoneName().get());

    }

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
        Dp dp = objectBroker.getDp(user, dpDto);

        dpDto = datapointService.getDatapoint(user, dpDto);

        junit.framework.Assert.assertEquals(dp.getDatapointName().get(), dpDto.getName());
        junit.framework.Assert.assertEquals(dp.getType().get(), dpDto.getType());
    }

}
