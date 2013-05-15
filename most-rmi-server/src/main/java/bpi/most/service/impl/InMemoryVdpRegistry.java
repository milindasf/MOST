package bpi.most.service.impl;

import bpi.most.dto.VdpProviderDTO;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.*;

/**
 * User: harald
 *
 * in-memory implementation which uses a hashmap
 *
 */
@Component
@Scope(value = "singleton") //default, but excplicitly here so that nobody changes it because we want only ONE registry!
public class InMemoryVdpRegistry implements IVdpRegistry{

    /**
     * maps types of virtual datapoints to server addresses which implement them. One type of
     * a virtual datapoint can be implemented by several servers, hence the Set of addresses.
     */
    private Map<String, Set<VdpProviderDTO>> vdpProviders;

    public InMemoryVdpRegistry() {
        this.vdpProviders = new HashMap<String, Set<VdpProviderDTO>>();
    }

    @Override
    public synchronized VdpProviderDTO getProvider(String vdpType) {
        VdpProviderDTO provider = null;

        if (vdpType != null){
            Set<VdpProviderDTO> providers = vdpProviders.get(vdpType);
            if (providers != null && !providers.isEmpty()){
                //choose a random one
                int randIndex = (int) (Math.random() * providers.size());
                provider = (VdpProviderDTO) providers.toArray()[randIndex];
            }
        }

        return provider;
    }

    @Override
    public synchronized void addProvider(VdpProviderDTO provider) {
        if (provider.getVdpType() != null && provider.getEndpoint() != null){
            Set<VdpProviderDTO> providers = vdpProviders.get(provider.getVdpType());
            if (providers == null){
                providers = new HashSet<VdpProviderDTO>();
                vdpProviders.put(provider.getVdpType(), providers);
            }

            providers.add(provider);
        }
    }

    @Override
    public synchronized List<VdpProviderDTO> getProviders(String vdpType) {
        List<VdpProviderDTO> providerList = new ArrayList<VdpProviderDTO>();

        if (vdpType != null){
            Set<VdpProviderDTO> providers = vdpProviders.get(vdpType);
            if (providers != null){
                providerList = new ArrayList<VdpProviderDTO>(providers);
            }
        }

        return providerList;
    }

    @Override
    public synchronized void removeProvider(String vdpType, URI serviceEndpoint) {
        if (vdpType != null && serviceEndpoint != null){
            Set<VdpProviderDTO> providers = vdpProviders.get(vdpType);
            if (providers != null){
                //find the provider and delete it
                VdpProviderDTO toDelete = null;
                for (VdpProviderDTO provider: providers){
                    if (provider.getEndpoint().equals(serviceEndpoint)){
                        toDelete = provider;
                        break;
                    }
                }
                if (toDelete != null){
                   providers.remove(toDelete);
                }
            }
        }
    }

    @Override
    public synchronized void removeProvider(URI serviceEndpoint) {
        if (serviceEndpoint != null){
            for (Set<VdpProviderDTO> providers: vdpProviders.values()){
                //find the provider and delete it
                VdpProviderDTO toDelete = null;
                for (VdpProviderDTO provider: providers){
                    if (provider.getEndpoint().equals(serviceEndpoint)){
                        toDelete = provider;
                        break;
                    }
                }
                if (toDelete != null){
                    providers.remove(toDelete);
                }
            }
        }
    }

    @Override
    public void clear() {
        vdpProviders.clear();
    }


}
