/* TODO: Refactor! */

package bpi.most.domain.datapoint;

import bpi.most.dto.DpDTO;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.math.BigDecimal;

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
    private String unit;

    //min value
    private BigDecimal min;
    private BigDecimal max;
    private BigDecimal accuracy;
    private BigDecimal deadband;
    private BigDecimal sample_interval;
    private BigDecimal sample_interval_min;
    private BigDecimal watchdog;
    private String custom_attr;
    private Integer zone_idzone;
	
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

    public DatapointVO(String name, String type, String description, String virtual, String unit) {
		this.name = name;
		this.type = type;
		this.description = description;
		this.virtual = virtual;
        this.unit = unit;
	}


    public DatapointVO(String name, String type, String description, String virtual, String unit, BigDecimal min, BigDecimal max, BigDecimal accuracy, BigDecimal deadband, BigDecimal sample_interval, BigDecimal sample_interval_min, BigDecimal watchdog, String custom_attr, Integer zone_idzone) {
        this.name = name;
        this.type = type;
        this.description = description;
        this.virtual = virtual;
        this.unit = unit;
        this.min = min;
        this.max = max;
        this.accuracy = accuracy;
        this.deadband = deadband;
        this.sample_interval = sample_interval;
        this.sample_interval_min = sample_interval_min;
        this.watchdog = watchdog;
        this.custom_attr = custom_attr;
        this.zone_idzone = zone_idzone;
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
        return new DpDTO(name, type, description, getVirtual(), unit);
    }

    @Override
    public String toString() {
        return "DatapointVO{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", description='" + description + '\'' +
                ", virtual='" + virtual + '\'' +
                ", unit='" + unit + '\'' +
                ", min=" + min +
                ", max=" + max +
                ", accuracy=" + accuracy +
                ", deadband=" + deadband +
                ", sample_interval=" + sample_interval +
                ", sample_interval_min=" + sample_interval_min +
                ", watchdog=" + watchdog +
                ", custom_attr='" + custom_attr + '\'' +
                ", zone_idzone=" + zone_idzone +
                '}';
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getUnit() {
        return unit;
    }
    public BigDecimal getDeadband()
    {
        return deadband;
    }
    public BigDecimal getMin()
    {
        return min;
    }
    public BigDecimal getMax()
    {
        return max;
    }
    public BigDecimal getSample_interval()
    {
        return sample_interval;
    }
    public BigDecimal getSample_interval_min()
    {
        return  sample_interval_min;
    }
}
