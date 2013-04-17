package bpi.most.service.impl;

import bpi.most.service.api.RegistrationService;

import java.net.URI;
import java.util.List;

/**
 *
 * implements a RegistrationService which keeps track of registered VDPs in a local
 * map.
 *
 * @author hare
 */
public class RegistrationServiceimpl implements RegistrationService{

    //TODO move this into module most-rmi-server cause he has to implement this.

    @Override
    public void register(String vdpType, URI serviceEndpoint) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void unregister(String vdpType, URI serviceEndpoint) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<URI> getServiceProvider(String vdpType) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
