package bpi.most.server.services.opcua.server.nodes;

import org.opcfoundation.ua.core.NodeClass;

import bpi.most.opcua.server.ReferenceType;
import bpi.most.opcua.server.annotation.Description;
import bpi.most.opcua.server.annotation.DisplayName;
import bpi.most.opcua.server.annotation.ID;
import bpi.most.opcua.server.annotation.Property;
import bpi.most.opcua.server.annotation.Reference;
import bpi.most.opcua.server.annotation.UaNode;

/**
 * @author harald
 *
 */

@UaNode(nodeClass = NodeClass.Object)
public class DpNode {

	@ID
	@DisplayName
	private String name;
	
	@Property
	private  String type;
	
	@Description
	private String description;
	
	/**
	 * this is the current data of the datapoint
	 */
	@Reference(refType = ReferenceType.hasComponent)
	private DpDataNode data;
	
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
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
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
	 * @return the data
	 */
	public DpDataNode getData() {
		return data;
	}
	/**
	 * @param data the data to set
	 */
	public void setData(DpDataNode data) {
		this.data = data;
	}
}
