package bpi.most.server.services.opcua.server.nodes;

import bpi.most.opc.uaserver.annotation.DisplayName;
import bpi.most.opc.uaserver.annotation.Property;
import bpi.most.opc.uaserver.annotation.UaNode;
import bpi.most.opc.uaserver.annotation.Value;
import org.opcfoundation.ua.core.NodeClass;

import java.util.Date;

@UaNode(nodeClass=NodeClass.Variable)
/*
 * TODO
 * @HistoryAccess
 * @Monitorable
 */
public class DpDataNode {
	
	@DisplayName
	private String displName = "Datapoint Value";
	
	/**
	 * timestamp of the current value
	 */
	@Property
	//maybe change this to @Variable
	private Date timestamp;
	
	/**
	 * the current value of the datapoint
	 */
	@Value
	private Double value;
	
	/**
	 * quality of the current value
	 */
	@Property
	//maybe change this to @Variable
	private Float quality;

	public DpDataNode(){	
	}
	
	/**
	 * @param displName
	 * @param timestamp
	 * @param value
	 * @param quality
	 */
	public DpDataNode(String displName, Date timestamp, Double value,
			Float quality) {
		super();
		this.displName = displName;
		this.timestamp = timestamp;
		this.value = value;
		this.quality = quality;
	}
	
	/**
	 * @return the displName
	 */
	public String getDisplName() {
		return displName;
	}

	/**
	 * @param displName the displName to set
	 */
	public void setDisplName(String displName) {
		this.displName = displName;
	}

	/**
	 * @return the timestamp
	 */
	public Date getTimestamp() {
		return timestamp;
	}

	/**
	 * @param timestamp the timestamp to set
	 */
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * @return the value
	 */
	public Double getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(Double value) {
		this.value = value;
	}

	/**
	 * @return the quality
	 */
	public Float getQuality() {
		return quality;
	}

	/**
	 * @param quality the quality to set
	 */
	public void setQuality(Float quality) {
		this.quality = quality;
	}
}
