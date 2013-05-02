package bpi.most.service.api;

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
     * @param vdpType virtual datapoint type the service is able to calculate
     * @param serviceEndpoint endpoint the service is listening to
     */
    void register(String vdpType, URI serviceEndpoint);

    /**
     * unregisters a service by vdp type and endpoint
     * @param vdpType
     * @param serviceEndpoint
     */
    void unregister(String vdpType, URI serviceEndpoint);

    /**
     * returns all registered service providers for a given virtual datapoint type
     * @param vdpType
     * @return
     */
    List<URI> getServiceProvider(String vdpType);
}
