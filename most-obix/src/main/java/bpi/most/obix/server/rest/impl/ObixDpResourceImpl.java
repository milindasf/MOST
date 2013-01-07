package bpi.most.obix.server.rest.impl;

import bpi.most.obix.server.ObixServer;
import bpi.most.obix.server.rest.ObixDpResource;

import javax.inject.Inject;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

/**
 * Created with IntelliJ IDEA.
 * User: Alexej Strelzow
 * Date: 06.01.13
 * Time: 19:05
 */
public class ObixDpResourceImpl implements ObixDpResource {

    @Inject
    ObixServer server;

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDp(@PathParam("name") String name) {
        return server.getDp(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDpData(@PathParam("name") String name) {
        return server.getDpData(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getAllDps() {
        return server.getAllDps();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getAllDpData() {
        return server.getAllDpData();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDatapoints(@QueryParam("from") String from, @QueryParam("to") String to) {
        return server.getDpData(from, to);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addDp(String encodedDp) {
        server.addDp(encodedDp);
    }
}
