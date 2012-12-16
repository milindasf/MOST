/* TODO: Refactor! */


package bpi.most.domain.zone;

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
