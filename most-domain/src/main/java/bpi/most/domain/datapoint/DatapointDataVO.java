package bpi.most.domain.datapoint;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

/**
 * Represents a single measurement
 * Quality is an parameter of how many physical measurements are used for the respective value (used in getPeriodicValues, etc.). 
 * @author robert.zach@tuwien.ac.at
 */	
@XmlRootElement(name = "dpdata")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class DatapointDataVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Date timestamp = null;
	private Double value = null;
	private Float quality = 1F;	//default value is 1

	public DatapointDataVO() {
		super();
	}
	public DatapointDataVO(Date timestamp, Double value) {
		super();
		this.timestamp = timestamp;
		this.value = value;
	}
	public DatapointDataVO(Date timestamp, Double value, Float quality) {
		super();
		this.timestamp = timestamp;
		this.value = value;
		this.quality = quality;
	}
	
	@XmlAttribute
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	
	@XmlAttribute
	public Double getValue() {
		return value;
	}
	public void setValue(Double value) {
		this.value = value;
	}
	
	@XmlAttribute
	public Float getQuality() {
		return quality;
	}
	public void setQuality(Float quality) {
		this.quality = quality;
	}
	
	@Override
	public String toString() {
		return "DatapointDataVO [timestamp=" + timestamp + ", value=" + value
				+ ", quality=" + quality + "]";
	}
}
