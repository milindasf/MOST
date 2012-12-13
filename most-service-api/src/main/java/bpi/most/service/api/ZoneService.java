package bpi.most.service.api;

import java.util.List;
import bpi.most.domain.User;
import bpi.most.domain.Dp;
import bpi.most.domain.Zone;


/**
 * Interface Specification of Zone Service.
 *
 * @author Lukas Weichselbaum
 */
public interface ZoneService {

    public List<Zone> getHeadZones(User user);

    public List<Zone> getSubzones(User user, Zone zoneEntity, int sublevels);

    public Zone getZone(User user, Zone zone);

    public List<Dp> getDatapoints(User user, Zone zoneEntity, int sublevels);
}
