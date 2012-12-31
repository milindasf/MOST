package bpi.most.server.services.opcua.server.nodes;

import bpi.most.opc.uaserver.annotation.*;
import org.opcfoundation.ua.core.NodeClass;

import java.util.List;

@UaNode(nodeClass = NodeClass.Object)
public class ZoneNode {

	@ID
	private int zoneID;
	
	@DisplayName
	private String name;
	
	@Description
	private String description;
	
	@Property
	private String country;
	
	@Property
	private String state;
	
	@Property
	private String county;
	
	@Property
	private String city;
	
	@Property
	private String building;
	
	@Property
	private String floor;
	
	@Property
	private String room;
	
	@Property
	private Double area;
	
	@Property
	private Double volume;
	
	private List<ZoneNode> subZones;
	
	private List<DpNode> dataPoints;
	
	//@Reference(refType = ReferenceType.hasComponent)
	public List<ZoneNode> getSubZones(){
		return null;
	}
	
	//@Reference(refType = ReferenceType.hasComponent)
	public List<DpNode> getDatapoints(){
		return null;
	}
	
	
	/**
	 * @return the zoneID
	 */
	public int getZoneID() {
		return zoneID;
	}

	/**
	 * @param zoneID the zoneID to set
	 */
	public void setZoneID(int zoneID) {
		this.zoneID = zoneID;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * @param country the country to set
	 */
	public void setCountry(String country) {
		this.country = country;
	}

	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}

	/**
	 * @param state the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * @return the county
	 */
	public String getCounty() {
		return county;
	}

	/**
	 * @param county the county to set
	 */
	public void setCounty(String county) {
		this.county = county;
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * @return the building
	 */
	public String getBuilding() {
		return building;
	}

	/**
	 * @param building the building to set
	 */
	public void setBuilding(String building) {
		this.building = building;
	}

	/**
	 * @return the floor
	 */
	public String getFloor() {
		return floor;
	}

	/**
	 * @param floor the floor to set
	 */
	public void setFloor(String floor) {
		this.floor = floor;
	}

	/**
	 * @return the room
	 */
	public String getRoom() {
		return room;
	}

	/**
	 * @param room the room to set
	 */
	public void setRoom(String room) {
		this.room = room;
	}

	/**
	 * @return the area
	 */
	public Double getArea() {
		return area;
	}

	/**
	 * @param area the area to set
	 */
	public void setArea(Double area) {
		this.area = area;
	}

	/**
	 * @return the volume
	 */
	public Double getVolume() {
		return volume;
	}

	/**
	 * @param volume the volume to set
	 */
	public void setVolume(Double volume) {
		this.volume = volume;
	}

	/**
	 * @return the dataPoints
	 */
	public List<DpNode> getDataPoints() {
		return dataPoints;
	}

	/**
	 * @param dataPoints the dataPoints to set
	 */
	public void setDataPoints(List<DpNode> dataPoints) {
		this.dataPoints = dataPoints;
	}

	/**
	 * @param subZones the subZones to set
	 */
	public void setSubZones(List<ZoneNode> subZones) {
		this.subZones = subZones;
	}
	
}
