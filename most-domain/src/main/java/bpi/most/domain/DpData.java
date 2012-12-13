/* TODO: Refactor! */

package bpi.most.domain;

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
public class DpData implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Date timestamp = null;
	private Double value = null;
	private Float quality = (float)1;	//default value is 1

	public DpData() {
		super();
	}
	public DpData(Date timestamp, Double value) {
		super();
		this.timestamp = timestamp;
		this.value = value;
	}
	public DpData(Date timestamp, Double value, Float quality) {
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
		return "DpData [timestamp=" + timestamp + ", value=" + value
				+ ", quality=" + quality + "]";
	}
}
