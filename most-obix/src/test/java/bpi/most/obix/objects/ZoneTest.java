package bpi.most.obix.objects;

import bpi.most.dto.DpDTO;
import bpi.most.dto.UserDTO;
import bpi.most.dto.ZoneDTO;
import bpi.most.obix.server.IObjectBroker;
import bpi.most.service.api.ZoneService;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import javax.inject.Inject;

/**
 * Created with IntelliJ IDEA.
 * User: Alexej Strelzow
 * Date: 18.01.13
 * Time: 11:30
 */
@ContextConfiguration(locations = "/META-INF/obix.spring.xml")
public class ZoneTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Inject
    private ZoneService zoneService;

    @Inject
    private IObjectBroker objectBroker;

    @Test
    public void testZoneTransformationToObix_fromUser_shouldTransform1Zone() throws Exception {
        UserDTO user = new UserDTO("mostsoc");
        java.util.List<ZoneDTO> zones = zoneService.getHeadZones(user);
        junit.framework.Assert.assertEquals(1, zones.size());

        ZoneDTO zone = zoneService.getZone(user, zones.get(0));
        int id = zone.getZoneId();
        String name = zone.getName();
        Uri zoneUri = new Uri(bpi.most.obix.objects.Zone.OBIX_ZONE_PREFIX + id);
        bpi.most.obix.objects.Zone obixZone = new bpi.most.obix.objects.Zone(id, name);

        junit.framework.Assert.assertEquals(zone.getZoneId(), (int)obixZone.getZone().get());
        junit.framework.Assert.assertEquals(zone.getName(), obixZone.getZoneName().get());
    }

    @Test
    public void testGetZone_fromUser() throws Exception {
        UserDTO user = new UserDTO("mostsoc");
        java.util.List<ZoneDTO> zones = zoneService.getHeadZones(user);
        junit.framework.Assert.assertEquals(1, zones.size());

        ZoneDTO zone = zoneService.getZone(user, zones.get(0));

        Zone obixZone = objectBroker.getZone(user, zone);

        junit.framework.Assert.assertEquals(zone.getZoneId(), (int)obixZone.getZone().get());
        junit.framework.Assert.assertEquals(zone.getName(), obixZone.getZoneName().get());
        junit.framework.Assert.assertEquals(zone.getDescription(), obixZone.getDescription().get());
        junit.framework.Assert.assertEquals(zone.getCountry(), obixZone.getCountry().get());
        junit.framework.Assert.assertEquals(zone.getState(), obixZone.getState().get());
        junit.framework.Assert.assertEquals(zone.getCounty(), obixZone.getCounty().get());
        junit.framework.Assert.assertEquals(zone.getCity(), obixZone.getCity().get());
        junit.framework.Assert.assertEquals(zone.getBuilding(), obixZone.getBuilding().get());
        junit.framework.Assert.assertEquals(zone.getRoom(), obixZone.getRoom().get());
        junit.framework.Assert.assertEquals(zone.getFloor(), obixZone.getFloor().get());
        junit.framework.Assert.assertEquals(zone.getArea(), obixZone.getArea().get());
        junit.framework.Assert.assertEquals(zone.getVolume(), obixZone.getVolume().get());
    }

    @Test
    public void testGetDpFromZone_fromUser() throws Exception {
        UserDTO user = new UserDTO("mostsoc");
        java.util.List<ZoneDTO> zones = zoneService.getHeadZones(user);
        junit.framework.Assert.assertEquals(1, zones.size());

        ZoneDTO zone = zoneService.getZone(user, zones.get(0));
        java.util.List<DpDTO> list = zoneService.getDatapoints(user, zone, 1);

        List obixList = objectBroker.getDpsForZone(user, zone, 1);

        junit.framework.Assert.assertEquals(list.size(), obixList.size());

        for (DpDTO d : list) {
            Obj obj = obixList.get(d.getName());
            junit.framework.Assert.assertNotNull(obj);

            Dp dp = (Dp) obj;

            junit.framework.Assert.assertEquals(dp.getDatapointName().get(), d.getName());
            junit.framework.Assert.assertEquals(dp.getType().get(), d.getType());
        }
    }



}
