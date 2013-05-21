package bpi.most.dto;

import java.io.Serializable;
import java.net.URI;

/**
 * User: harald
 *
 * Identifies a virtual datapoint provider - i.e it provides an implementation for a specific virtual datapoint type
 */
public class VdpProviderDTO implements Serializable {

    /**
     * type of the virtual datapoint (e.g "radiance")
     */
    private String vdpType;

    /**
     * endpoint where the provider is listening to
     */
    private URI endpoint;

    public VdpProviderDTO(String vdpType, URI endpoint) {
        this.vdpType = vdpType;
        this.endpoint = endpoint;
    }

    public VdpProviderDTO() {
    }

    public String getVdpType() {
        return vdpType;
    }

    public void setVdpType(String vdpType) {
        this.vdpType = vdpType;
    }

    public URI getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(URI endpoint) {
        this.endpoint = endpoint;
    }

    @Override
    public String toString() {
        return "VdpProviderDTO{" +
                "vdpType='" + vdpType + '\'' +
                ", endpoint=" + endpoint +
                '}';
    }
}
