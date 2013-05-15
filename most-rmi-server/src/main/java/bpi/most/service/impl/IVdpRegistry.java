package bpi.most.service.impl;

import bpi.most.dto.VdpProviderDTO;

import java.net.URI;
import java.util.List;

/**
 *
 * User: harald
 *
 * Register for virtual datapoints. If it is persistent or not depends on the particular implementation (in-memory, memcache, database, ...)
 */

public interface IVdpRegistry {

    /**
     * adds a service endpoint for the vdp type
     */
    void addProvider(VdpProviderDTO provider);

    /**
     * returns a random provider for the given vdp type
     * @param vdpType
     * @return
     */
    VdpProviderDTO getProvider(String vdpType);

    /**
     * returns all servcie providers for a given vdp type
     * @param vdpType
     * @return
     */
    List<VdpProviderDTO> getProviders(String vdpType);

    /**
     * removes registration for a vdp type for the particular endpoint
     * @param vdpType
     * @param serviceEndpoint
     */
    void removeProvider(String vdpType, URI serviceEndpoint);

    /**
     * removes all registrations for a serviceEndpoint
     * @param serviceEndpoint
     */
    void removeProvider(URI serviceEndpoint);

    /**
     * removes all registry entries
     */
    void clear();

}
