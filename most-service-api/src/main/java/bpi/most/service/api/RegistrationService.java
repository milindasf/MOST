package bpi.most.service.api;

import bpi.most.dto.VdpProviderDTO;

import java.net.URI;
import java.util.List;

/**
 *
 * The RegistrationService allows Virtual Datapoint Providers (VDP) to register their service.
 *
 * VDPs provide implementation for specific <italic>virtual datapoint types</italic>. Every
 * virtual datapoint has a specific type like radiance or heat. To calculate the actual value some
 * specific environment may be needed. Servers can register through this service their ability
 * to calculate particular virtual datapoints.
 *
 * @author hare
 *
 */
public interface RegistrationService {

    /**
     * registers an service for ability to calculate virtual datapoints of the given type.
     */
    void register(VdpProviderDTO provider);

    /**
     * unregisters a service by vdp type and endpoint
     * @param vdpType
     * @param serviceEndpoint
     */
    void unregister(String vdpType, URI serviceEndpoint);

    /**
     * unregisters all providers for a given endpoint
     * @param serviceEndpoint
     */
    void unregister(URI serviceEndpoint);

    /**
     * returns all registered service providers for a given virtual datapoint type
     * @param vdpType
     * @return
     */
    List<VdpProviderDTO> getServiceProvider(String vdpType);
}
