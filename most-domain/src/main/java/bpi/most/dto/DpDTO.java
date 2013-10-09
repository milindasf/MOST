package bpi.most.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Simplified POJO instance of a datapoint.
 * Provides only name, type and description. No data access methods!
 * @author mike
 */

@XmlRootElement(name = "Datapoint")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class DpDTO implements Serializable {


    //TODO: move permission definition out of DTO
	//available permissions
	public enum Permissions {
		   READ,WRITE,ADMIN;
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private String type;
	private String description;
    private String virtual;
    private String unit;

    /**
     * address of server which implements the datapoint.
     * this is used for dynamically bind to the correct DatapointService.
     */
    private String providerAddress;
	
	public DpDTO() {
		this.name = "null";
		this.type = "null";
		this.description = "null";
        this.unit = "null";
	}

	public DpDTO(String datapointName) {
		this.name = datapointName;
	}

    public DpDTO(String name, String type, String description) {
        this.name = name;
        this.type = type;
        this.description = description;
    }

    public DpDTO(String name, String type, String description, String virtual) {
        this.name = name;
        this.type = type;
        this.description = description;
        this.virtual = virtual;
    }

    public DpDTO(String name, String type, String description, String virtual, String unit) {
        this.name = name;
        this.type = type;
        this.description = description;
        this.virtual = virtual;
        this.unit = unit;
    }
	
	@XmlAttribute
	public String getName() {
		return name;
	}

	@XmlAttribute
	public String getType() {
		return type;
	}

	@XmlAttribute
	public String getDescription() {
		return description;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setDescription(String description) {
		this.description = description;
	}

    public boolean isVirtual() {
        return virtual != null && !virtual.equals("null");
    }

    public String getVirtual() {
        return virtual;
    }

    public void setVirtual(String virtual) {
        this.virtual = virtual;
    }

    public String getProviderAddress() {
        return providerAddress;
    }

    public void setProviderAddress(String providerAddress) {
        this.providerAddress = providerAddress;
    }
    @XmlAttribute
    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @Override
	public String toString() {
		return "DpDTO [name=" + name + ", type=" + type + ", description="
				+ description + ", unit=" + unit + "]";
	}
}
