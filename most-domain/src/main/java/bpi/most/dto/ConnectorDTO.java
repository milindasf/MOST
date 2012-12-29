package bpi.most.dto;

import java.io.Serializable;

/**
 * Contains the metadata of a connection (Datapoint - Connector - Device)
 * @author robert.zach@tuwien.ac.at
 */
public class ConnectorDTO implements Serializable{
	private static final long serialVersionUID = 743912991919824495L;
	public int connectionNumber = 0;	//unique identifier of a connection
	public String dpName = null;
	public String deviceName = null;
	public String connectionType = null;
	public String connectionVariables = null;
	public boolean writeable = false;
	public String vendor = null;
	public String model = null;
	//dp specific
	public String unit = null;
	public String type = null;
	//set worst case value. used if set to null
	public double min = - Double.MAX_VALUE;
	public double max = Double.MAX_VALUE;
	public double deadband = 0.0;
	public int sampleInterval = 0; //0 means not used (null)!
	public int minSampleInterval = 0;
	
	/**
	 * constructor without deadband, min/max, etc. information 
	 */
	public ConnectorDTO(int connectionNumber, String dpName, String deviceName, String connectionType,
			String connectionVariables, boolean writeable, String vendor,
			String model) {
		super();
		this.connectionNumber = connectionNumber;
		this.dpName = dpName;
		this.deviceName = deviceName;
		this.connectionType = connectionType;
		this.connectionVariables = connectionVariables;
		this.writeable = writeable;
		this.vendor = vendor;
		this.model = model;
	}
	
	/**
	 * constructor including deadband, min/max, etc. information 
	 */
	public ConnectorDTO(int connectionNumber, String dpName, String deviceName, String connectionType,
			String connectionVariables, boolean writeable, String vendor,
			String model, String unit, String type, double min, double max, double deadband, int sampleInterval, int minSampleInterval) {
		super();
		this.connectionNumber = connectionNumber;
		this.dpName = dpName;
		this.deviceName = deviceName;
		this.connectionType = connectionType;
		this.connectionVariables = connectionVariables;
		this.writeable = writeable;
		this.vendor = vendor;
		this.model = model;
		this.unit = unit;
		this.type = type;
		this.min = min;
		this.max = max;
		this.deadband = deadband;
		this.sampleInterval = sampleInterval;
		this.minSampleInterval = minSampleInterval;
	}



	/**
	 * 
	 * @return all variable in one String
	 */
	public String getVariables() {
		return connectionVariables;
	}
	
	/**
	 * TODO Example 
	 * @param variable requested variable
	 * @return value of variable (as string), null if not defined
	 */
	public String getVariable(String variable) {
		String	myResult = null;
		String	allVariables = null;
		int	myFound = 0;
		
		allVariables = getVariables();
		if (allVariables != null) {
			//split multiple Attributes, ex. "myvariable1 someValue; myvariable2 someValue"
			for (String oneAttr : allVariables.split(";")) {
				//split one Attribute definition, ex. "myvariable1 someValue"
				for (String myVariable : oneAttr.split(" ")) {
					//rebuild string
					if (myFound > 1) {
						myResult = myResult + " " + myVariable;
						myFound++;
					}
					if (myFound == 1) {
						//to prevent first " "
						myResult = myVariable;
						myFound++;
					}
					if (myFound == 0 && myVariable.equals(variable)) {
						myFound = 1;
					}
				}
				myFound = 0;
			}
		}
		return myResult;
	}
}
