package bpi.most.domain.zone;

import bpi.most.dto.ZoneDTO;

import javax.persistence.*;

/**
 * Entity specifying a zone.
 *
 * @author Lukas Weichselbaum
 */
@Entity
@Table(name = "zone",
        uniqueConstraints = @UniqueConstraint(name = "name_UNIQUE", columnNames = {"name"}))
public class Zone {
	
    public Zone() {
    }

    public ZoneDTO getDTO(){
        return new ZoneDTO(idzone, name, description, country, state, country, city, building, floor, room, area, volume);
    }

    @Id
    @GeneratedValue
    @Column(name = "idzone")
    private Integer idzone;

    @Column(name = "name", nullable = true, length = 45)
    private String name;

    @Column(name = "description", nullable = true, length = 45)
    private String description;

    @Column(name = "country", nullable = true, length = 45)
    private String country;

    @Column(name = "state", nullable = true, length = 45)
    private String state;

    @Column(name = "county", nullable = true, length = 45)
    private String county;

    @Column(name = "city", nullable = true, length = 45)
    private String city;

    @Column(name = "building", nullable = true, length = 45)
    private String building;

    @Column(name = "floor", nullable = true, length = 45)
    private String floor;

    @Column(name = "room", nullable = true, length = 45)
    private String room;

    @Column(name = "area", nullable = true)
    private double area;

    @Column(name = "volume", nullable = true)
    private double volume;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getIdzone() {
        return idzone;
    }

    public void setIdzone(Integer idzone) {
        this.idzone = idzone;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public int getZoneId() {
        return idzone;
    }

    public Zone (int zoneId){
        this.idzone = zoneId;
    }

	@Override
	public String toString() {
		return "Zone [zoneId=" + idzone + ", name=" + name
				+ ", description=" + description + ", country=" + country
				+ ", state=" + state + ", county=" + county + ", city=" + city
				+ ", building=" + building + ", floor=" + floor + ", room="
				+ room + ", area=" + area + ", volume=" + volume + "]";
	}


}
