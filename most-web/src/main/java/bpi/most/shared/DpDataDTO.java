package bpi.most.shared;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Represents a single measurement
 * Quality is an parameter of how many physical measurements are used for the respective value (used in getPeriodicValues, etc.). 
 * @author robert.zach@tuwien.ac.at
 */	
@XmlRootElement(name = "dpdata")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class DpDataDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Date timestamp = null;
	private Double value = null;
	private Float quality = (float)1;	//default value is 1

	public DpDataDTO() {
		super();
	}
	public DpDataDTO(Date timestamp, Double value) {
		super();
		this.timestamp = timestamp;
		this.value = value;
	}
	public DpDataDTO(Date timestamp, Double value, Float quality) {
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
		return "DpDataDTO [timestamp=" + timestamp + ", value=" + value
				+ ", quality=" + quality + "]";
	}
}
