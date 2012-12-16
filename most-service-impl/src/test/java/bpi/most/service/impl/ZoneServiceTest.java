package bpi.most.service.impl;

import bpi.most.domain.user.User;
import bpi.most.domain.zone.Zone;
import bpi.most.service.api.ZoneService;
import junit.framework.Assert;
import org.junit.After;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.transaction.annotation.Transactional;

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
        ((ZoneServiceImpl) zoneService).resetCache();
    }

    @Test
    public void testGetHeadZones_fromUser_shouldReturn1HeadZone() throws Exception {
        User user = new User();
        user.setName("mostsoc");
        List<Zone> zones = zoneService.getHeadZones(user);
        Assert.assertEquals(1, zones.size());
        Assert.assertEquals(10, zones.get(0).getZoneId());
    }

    @Test
    public void testGetHeadZones_fromInvalidUser_shouldReturnEmptyList() throws Exception {
        User user = new User();
        user.setName("invalid-username");
        List<Zone> zones = zoneService.getHeadZones(user);
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
        Zone SearchZone = new Zone(zoneId);
        Zone zone = zoneService.getZone(SearchZone);
        Assert.assertEquals(zoneId, zone.getZoneId());
    }

    @Test
    public void testGetZone_withSearchPattern_shouldReturn23Zone() throws Exception {
        List<Zone> zones = zoneService.getZone("city");
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
}
