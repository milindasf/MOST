package bpi.most.obix.server.rest.impl;

import bpi.most.dto.DpDTO;
import bpi.most.obix.server.impl.ObixServer;
import bpi.most.obix.server.rest.ObixDpResource;
import bpi.most.server.services.rest.impl.BaseResImpl;

import javax.inject.Inject;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

/**
 * Created with IntelliJ IDEA.
 * User: Alexej Strelzow
 * Date: 06.01.13
 * Time: 19:05
 */
public class ObixDpResourceImpl extends BaseResImpl implements ObixDpResource {

    @Inject
    ObixServer server;

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDp(@PathParam("name") String name) {
        return server.getDp(getUser(), new DpDTO(name));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDpData(@PathParam("name") String name) {
        return server.getDpData(getUser(), new DpDTO(name));
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
    public String getDpData(@PathParam("name") String name, @QueryParam("from") String from, @QueryParam("to") String to) {
        return server.getDpData(getUser(), new DpDTO(name), from, to);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addDp(String encodedDp) {
        server.addDp(getUser(), encodedDp);
    }
}
