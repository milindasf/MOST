/* TODO: Refactor! */


package bpi.most.domain.datapoint;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Entity specifying a datapoint.
 *
 * @author Christoph Lauscher
 */
@Entity
@Table(name = "datapoint")
public class Datapoint {
	
    public Datapoint() {
    }

    @Id
    @Column(name = "datapoint_name", nullable = false, length = 100)
    private String name;

    @Column(name = "unit", nullable = true, length = 45)
    private String unit;

    @Column(name = "type", nullable = true, length = 45)
    private String type;

    @Column(name = "min", nullable = true, precision = 10, scale = 2)
    private BigDecimal min;

    @Column(name = "max", nullable = true, precision = 10, scale = 2)
    private BigDecimal max;

    @Column(name = "accuracy", nullable = true, precision = 10, scale = 2)
    private BigDecimal accuracy;

    @Column(name = "deadband", nullable = true, precision = 10, scale = 2)
    private BigDecimal deadband;

    @Column(name = "sample_interval", nullable = true, precision = 10, scale = 0)
    private BigDecimal sample_interval;

    @Column(name = "sample_interval_min", nullable = true, precision = 10, scale = 0)
    private BigDecimal sample_interval_min;

    @Column(name = "watchdog", nullable = true, precision = 10, scale = 0)
    private BigDecimal watchdog;

    @Column(name = "virtual", nullable = true, length = 100)
    private String virtual;

    @Column(name = "custom_attr", nullable = true, length = 500)
    private String custom_attr;
    
    @Column(name = "description", nullable = true, length = 500)
    private String description;

    @Column(name = "zone_idzone", nullable = true, precision = 11)
    private int zone_idzone;

    
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public BigDecimal getMin() {
		return min;
	}

	public void setMin(BigDecimal min) {
		this.min = min;
	}

	public BigDecimal getMax() {
		return max;
	}

	public void setMax(BigDecimal max) {
		this.max = max;
	}

	public BigDecimal getAccuracy() {
		return accuracy;
	}

	public void setAccuracy(BigDecimal accuracy) {
		this.accuracy = accuracy;
	}

	public BigDecimal getDeadband() {
		return deadband;
	}

	public void setDeadband(BigDecimal deadband) {
		this.deadband = deadband;
	}

	public BigDecimal getSample_interval() {
		return sample_interval;
	}

	public void setSample_interval(BigDecimal sample_interval) {
		this.sample_interval = sample_interval;
	}

	public BigDecimal getSample_interval_min() {
		return sample_interval_min;
	}

	public void setSample_interval_min(BigDecimal sample_interval_min) {
		this.sample_interval_min = sample_interval_min;
	}

	public BigDecimal getWatchdog() {
		return watchdog;
	}

	public void setWatchdog(BigDecimal watchdog) {
		this.watchdog = watchdog;
	}

	public String getVirtual() {
		return virtual;
	}

	public void setVirtual(String virtual) {
		this.virtual = virtual;
	}

	public String getCustom_attr() {
		return custom_attr;
	}

	public void setCustom_attr(String custom_attr) {
		this.custom_attr = custom_attr;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getZone_idzone() {
		return zone_idzone;
	}

	public void setZone_idzone(int zone_idzone) {
		this.zone_idzone = zone_idzone;
	}

	@Override
	public String toString() {
		return "Datapoint";
	}
}
