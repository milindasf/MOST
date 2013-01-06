package bpi.most.obix.server.rest.impl;

import bpi.most.obix.server.ObixServer;
import bpi.most.obix.server.rest.ObixZoneResource;

import javax.inject.Inject;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

/**
 * Created with IntelliJ IDEA.
 * User: Alexej Strelzow
 * Date: 06.01.13
 * Time: 12:23
 */
public class ObixZoneResourceImpl /*extends BaseResImpl*/ implements ObixZoneResource {

    @Inject
    ObixServer server;


    @Override
    public String getDpForZone(@PathParam("id") int id) {
        return server.getDpsForZone(id);
    }

    @Override
    public String getZone(@PathParam("id") int id) {
        return server.getDpDataForZone(id);
    }

    @Override
    public String getDpForAllZones() {
        return server.getDpsForAllZones();
    }

    @Override
    public String getAllZones() {
        return server.getAllZones();
    }

    @Override
    public String getDpForZone(@PathParam("id") int id, @QueryParam("from") String from, @QueryParam("to") String to) {
        return server.getDpForZone(id, from, to);
    }
}
