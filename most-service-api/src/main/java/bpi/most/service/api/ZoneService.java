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
    Zone getZone(Zone zone);

    Zone getZone(int zoneId);

    List<Zone> getZone(String searchPattern);

    List<Zone> getHeadZones();

    List<Zone> getHeadZones(User user);
}
