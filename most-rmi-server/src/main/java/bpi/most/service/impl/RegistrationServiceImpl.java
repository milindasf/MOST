package bpi.most.service.impl;

import bpi.most.service.api.RegistrationService;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * implements a RegistrationService which keeps track of registered VDPs in a local
 * map.
 *
 * @author hare
 */
public class RegistrationServiceImpl implements RegistrationService{

    //TODO implement this

    /**
     * maps types of virtual datapoints to server addresses which implement them. One type of
     * a virtual datapoint can be implemented by several server, hence the Set of addresses.
     */
    private Map<String, Set<String>> vdpProviders = new HashMap<String, Set<String>>();

    /**
     * adds a service endpoint for the vdp type
     * @param vdpType
     * @param serviceEndpoint
     */
    private void addProvider(String vdpType, String serviceEndpoint){

    }

    /**
     * returns a service endpoint for the given vdp type
     * @param vdpType
     * @return
     */
    private String getProvider(String vdpType){
        return null;
    }

    /**
     * removes registration for a vdp type for the particular endpoint
     * @param vdpType
     * @param serviceEndpoint
     */
    private void removeProvider(String vdpType, String serviceEndpoint){

    }

    /**
     * removes all registrations for a serviceEndpoint
     * @param serviceEndpoint
     */
    private void removeProvider(String serviceEndpoint){

    }

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
