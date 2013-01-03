package bpi.most.service.impl;

import bpi.most.domain.zone.Zone;
import bpi.most.dto.DpDTO;
import bpi.most.dto.UserDTO;
import bpi.most.dto.ZoneDTO;
import bpi.most.service.api.ZoneService;
import junit.framework.Assert;
import org.junit.After;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import javax.inject.Inject;
import java.util.List;

/**
 * Tests for {@link ZoneServiceImpl}.
 *
 * @author Lukas Weichselbaum
 */
@ContextConfiguration(locations = "/META-INF/most-service.spring.xml")
public class ZoneServiceTest extends AbstractTransactionalJUnit4SpringContextTests{

    @Inject
    private ZoneService zoneService;

    @After
    public void tearDown() throws Exception {
        zoneService.resetCache();
    }

    @Test
    public void testGetHeadZones_fromUser_shouldReturn1HeadZone() throws Exception {
        UserDTO user = new UserDTO("mostsoc");
        List<ZoneDTO> zones = zoneService.getHeadZones(user);
        Assert.assertEquals(1, zones.size());
        Assert.assertEquals(10, zones.get(0).getZoneId());
    }

    @Test
    public void testGetHeadZones_fromInvalidUser_shouldReturnEmptyList() throws Exception {
        UserDTO user = new UserDTO("invalid-username");
        List<ZoneDTO> zones = zoneService.getHeadZones(user);
        Assert.assertEquals(0, zones.size());
    }

    @Test
    public void testGetZone_withId_shouldReturnZoneWithSameId() throws Exception {
        int zoneId = 6;
        Zone zone = zoneService.getZone(zoneId);
        Assert.assertEquals(zoneId, zone.getZoneId());
    }

    @Test
    public void testGetZone_withNotExistingId_shouldReturnNull() throws Exception {
        int zoneId = -999;
        Zone zone = zoneService.getZone(zoneId);
        Assert.assertNull(zone);
    }

    @Test
    public void testGetZone_withZone_shouldReturnZoneWithSameId() throws Exception {
        int zoneId = 8;
        ZoneDTO SearchZone = new ZoneDTO(zoneId);
        Zone zone = zoneService.getZone(SearchZone);
        Assert.assertEquals(zoneId, zone.getZoneId());
    }

    @Test
    public void testGetZone_withSearchPattern_shouldReturn23Zone() throws Exception {
        List<ZoneDTO> zones = zoneService.getZone("city");
        Assert.assertEquals(23, zones.size());
    }

    @Test
    public void testGetZone_withSearchPattern_shouldReturnNull() throws Exception {
        Assert.assertNull(zoneService.getZone("not-existing"));
    }

    @Test
    public void testGetHeadZones_shouldReturn3HeadZones() throws Exception {
        Assert.assertEquals(3, zoneService.getHeadZones().size());
    }


    @Test
    public void testGetSubZones_shouldReturnSubZones() throws Exception {
        List<ZoneDTO> zones = zoneService.getSubzones(new UserDTO("mostsoc"), new ZoneDTO(10), 1);
        Assert.assertEquals(2, zones.size());
    }

    @Test
    public void testGetDatapoints_shouldReturnDPsFromSubZones() throws Exception {
        List<DpDTO> zones = zoneService.getDatapoints(new UserDTO("mostsoc"), new ZoneDTO(10), 5);
        Assert.assertEquals(9, zones.size());

    }
}
