package bpi.most.obix.server.rest.impl;

import bpi.most.dto.DpDTO;
import bpi.most.obix.server.impl.ObixServer;
import bpi.most.obix.server.rest.ObixDpResource;
import bpi.most.server.services.rest.impl.BaseResImpl;

import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;

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
    public String getDp(String name) {
        return server.getDp(getUser(), new DpDTO(name));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDpData(String name) {
        LOG.info("returning data");
        return server.getDpData(getUser(), new DpDTO(name));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addDp(String encodedDp) {
        LOG.warn("should create new datapoint, but is not implemented yet.");
        throw new WebApplicationException(NOT_IMPLEMENTED);

        //server.addDp(getUser(), encodedDp);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addDpData(String dpName, String encodedDpData) {
        LOG.info("adding new data to datapoint: " + dpName);
        LOG.info(encodedDpData);
        server.addDpData(getUser(), new DpDTO(dpName), encodedDpData);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateDp(String dpName, String encodedDp) {
        LOG.warn("updating datapoint " + dpName + "\n" + encodedDp + "\nbut is is not implemented yet.");
        throw new WebApplicationException(NOT_IMPLEMENTED);

        //server.updateDp(getUser(), dpName, encodedDp);
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
    public String getDpData(String name, String from, String to) {
        LOG.info("returning data from " + name + " with interval " + from + " - " + to);
        return server.getDpData(getUser(), new DpDTO(name), from, to);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDpPeriodicData(String dpName, String from, String to, int period, int mode, int type) {
        LOG.info("returning periodic data");
        return server.getDpPeriodicData(getUser(), new DpDTO(dpName), from, to, (float) period, mode, type);
    }
}
