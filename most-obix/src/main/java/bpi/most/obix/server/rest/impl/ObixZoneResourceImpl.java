package bpi.most.obix.server.rest.impl;

import bpi.most.dto.ZoneDTO;
import bpi.most.obix.server.impl.ObixServer;
import bpi.most.obix.server.rest.ObixZoneResource;
import bpi.most.server.services.rest.impl.BaseResImpl;

import javax.inject.Inject;

/**
 * Created with IntelliJ IDEA.
 * User: Alexej Strelzow
 * Date: 06.01.13
 * Time: 12:23
 */
public class ObixZoneResourceImpl extends BaseResImpl implements ObixZoneResource {

    @Inject
    ObixServer server;

    /**
     * {@inheritDoc}
     */
    @Override
    public String getZone(int id) {
        return server.getZone(getUser(), new ZoneDTO(id));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDpForZone(int id, int level) {
        return server.getDpsForZone(getUser(), new ZoneDTO(id), level);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getHeadZones() {
        return server.getHeadZones(getUser());
    }
}
