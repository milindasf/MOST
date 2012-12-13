/* TODO: Refactor! */


package bpi.most.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Simplified POJO instance of a Zone.
 * No direct DB access!
 */
@XmlRootElement(name = "Zone")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Zone implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private int zoneId; //unique
	private String name, description, country, state, county, city, building, floor, room; 
	private Double area, volume;
	
	//constructor
	public Zone() {
		super();
	}
	public Zone(int zoneId) {
		super();
		this.zoneId = zoneId;
	}	
	public Zone(int zoneId, String name, String description,
                String country, String state, String county, String city,
                String building, String floor, String room, Double area,
                Double volume) {
		super();
		this.zoneId = zoneId;
		this.name = name;
		this.description = description;
		this.country = country;
		this.state = state;
		this.county = county;
		this.city = city;
		this.building = building;
		this.floor = floor;
		this.room = room;
		this.area = area;
		this.volume = volume;
	}
	
	//getter, setter
	@XmlAttribute
	public int getZoneId() {
		return zoneId;
	}
	public void setZoneId(int zoneId) {
		this.zoneId = zoneId;
	}
	
	@XmlAttribute
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@XmlAttribute
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	@XmlAttribute
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	
	@XmlAttribute
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	@XmlAttribute
	public String getCounty() {
		return county;
	}
	public void setCounty(String county) {
		this.county = county;
	}
	@XmlAttribute
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	@XmlAttribute
	public String getBuilding() {
		return building;
	}
	public void setBuilding(String building) {
		this.building = building;
	}
	@XmlAttribute
	public String getFloor() {
		return floor;
	}
	public void setFloor(String floor) {
		this.floor = floor;
	}
	@XmlAttribute
	public String getRoom() {
		return room;
	}
	public void setRoom(String room) {
		this.room = room;
	}
	@XmlAttribute
	public Double getArea() {
		return area;
	}
	public void setArea(Double area) {
		this.area = area;
	}
	@XmlAttribute
	public Double getVolume() {
		return volume;
	}
	public void setVolume(Double volume) {
		this.volume = volume;
	}
	@Override
	public String toString() {
		return "Zone [zoneId=" + zoneId + ", name=" + name
				+ ", description=" + description + ", country=" + country
				+ ", state=" + state + ", county=" + county + ", city=" + city
				+ ", building=" + building + ", floor=" + floor + ", room="
				+ room + ", area=" + area + ", volume=" + volume + "]";
	}
}
