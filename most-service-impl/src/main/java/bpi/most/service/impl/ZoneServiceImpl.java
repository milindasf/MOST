package bpi.most.service.impl;

import bpi.most.domain.datapoint.Dp;
import bpi.most.domain.user.User;
import bpi.most.domain.zone.Zone;
import bpi.most.service.api.ZoneService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Implementation of {@link bpi.most.service.api.ZoneService}.
 *
 * @author Lukas Weichselbaum
 */
@Service
public class ZoneServiceImpl implements ZoneService{

    private static final Logger log = LoggerFactory.getLogger(ZoneServiceImpl.class);

    @PersistenceContext(unitName = "most")
    private EntityManager em;

    @Override
    public List<Zone> getHeadZones(User user) {
        return null;  //TODO: implement
    }

    @Override
    public List<Zone> getSubzones(User user, Zone zoneEntity, int sublevels) {
        return null;  //TODO: implement
    }

    @Override
    public Zone getZone(User user, Zone zone) {
        return null;  //TODO: implement
    }

    @Override
    public List<Dp> getDatapoints(User user, Zone zoneEntity, int sublevels) {
        return null;  //TODO: implement
    }
}
