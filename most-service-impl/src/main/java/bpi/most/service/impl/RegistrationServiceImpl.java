package bpi.most.service.impl;

import bpi.most.dto.VdpProviderDTO;
import bpi.most.service.api.RegistrationService;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
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
@Service
public class RegistrationServiceImpl implements RegistrationService{

    //TODO - move into most-service-impl or own most-registry-impl module

    @Inject
    IVdpRegistry registry;

    /**
     * returns a service endpoint for the given vdp type
     * @param vdpType
     * @return
     */
    private VdpProviderDTO getProvider(String vdpType){
        return registry.getProvider(vdpType);
    }

    @Override
    public void register(VdpProviderDTO provider) {
        registry.addProvider(provider);
    }

    @Override
    public void unregister(String vdpType, URI serviceEndpoint) {
        registry.removeProvider(vdpType, serviceEndpoint);
    }

    @Override
    public void unregister(URI serviceEndpoint) {
        registry.removeProvider(serviceEndpoint);
    }

    @Override
    public VdpProviderDTO getServiceProvider(String vdpType) {
        return registry.getProvider(vdpType);
    }

    @Override
    public List<VdpProviderDTO> getServiceProviders(String vdpType) {
        return registry.getProviders(vdpType);
    }

    @Override
    public void clear() {
        registry.clear();
    }
}
