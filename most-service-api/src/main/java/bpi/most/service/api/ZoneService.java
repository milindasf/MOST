package bpi.most.service.api;

import bpi.most.domain.user.User;
import bpi.most.domain.zone.Zone;

import java.util.List;

/**
 * Interface Specification of Zone Service.
 *
 * @author Lukas Weichselbaum
 */
public interface ZoneService {
    public Zone getZone(Zone zone);

    public Zone getZone(int zoneId);

    public List<Zone> getZone(String searchPattern);

    public List<Zone> getHeadZones();

    public List<Zone> getHeadZones(User user);
}
