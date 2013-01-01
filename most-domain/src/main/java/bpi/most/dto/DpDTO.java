package bpi.most.dto;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

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
	public String name;
	public String type;
	public String description;
	
	public DpDTO() {
		this.name = "null";
		this.type = "null";
		this.description = "null";
	}

	public DpDTO(String datapointName) {
		this.name = datapointName;
	}

    public DpDTO(String name, String type, String description) {
        this.name = name;
        this.type = type;
        this.description = description;
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

	@Override
	public String toString() {
		return "DpDTO [name=" + name + ", type=" + type + ", description="
				+ description + "]";
	}
}
