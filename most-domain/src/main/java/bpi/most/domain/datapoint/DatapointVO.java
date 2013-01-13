/* TODO: Refactor! */

package bpi.most.domain.datapoint;

import bpi.most.dto.DpDTO;

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
public class DatapointVO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String name;
	private String type;
	private String description;
	private String virtual;
	
	public DatapointVO() {
		this.name = "null";
		this.type = "null";
		this.description = "null";
		this.virtual = "null";
	}

	public DatapointVO(String datapointName) {
		this.name = datapointName;
	}
	
	public DatapointVO(String name, String type, String description, String virtual) {
		this.name = name;
		this.type = type;
		this.description = description;
		this.virtual = virtual;
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

    public String getVirtual() {
		return virtual;
	}

	public void setVirtual(String virtual) {
		this.virtual = virtual;
	}
	
	public boolean isVirtual() {
		return virtual != null && !virtual.equals("null");
	}

	public DpDTO getDTO(){
        return new DpDTO(name, type, description);
    }

	@Override
	public String toString() {
		return "DatapointVO [name=" + name + ", type=" + type + ", description="
				+ description + ", virtual=" + virtual + "]";
	}
}
