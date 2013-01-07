/* TODO: Refactor! */

package bpi.most.domain.connector;

import java.io.Serializable;

/**
 * Contains the metadata of a connection (Datapoint - Connector - Device)
 * @author robert.zach@tuwien.ac.at
 */
public class ConnectorVO implements Serializable {
    
	private static final long serialVersionUID = 743912991919824495L;
    
	private int connectionNumber = 0;	//unique identifier of a connection
	private String dpName;
	private String deviceName;
	private String connectionType;
	private String connectionVariables;
	private boolean writeable = false;
	private String vendor;
	private String model;

	//dp specific
    private String unit;
	private String type;

	//set worst case value. used if set to null
    private double min = - Double.MAX_VALUE;
	private double max = Double.MAX_VALUE;
	private double deadband = 0.0;
	private int sampleInterval = 0; //0 means not used (null)!
	private int minSampleInterval = 0;
	
	/**
	 * constructor without deadband, min/max, etc. information 
	 */
	public ConnectorVO(int connectionNumber, String dpName, String deviceName, String connectionType,
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
	public ConnectorVO(int connectionNumber, String dpName, String deviceName, String connectionType,
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

    public int getConnectionNumber() {
        return connectionNumber;
    }

    public void setConnectionNumber(int connectionNumber) {
        this.connectionNumber = connectionNumber;
    }

    public String getDpName() {
        return dpName;
    }

    public void setDpName(String dpName) {
        this.dpName = dpName;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getConnectionType() {
        return connectionType;
    }

    public void setConnectionType(String connectionType) {
        this.connectionType = connectionType;
    }

    public String getConnectionVariables() {
        return connectionVariables;
    }

    public void setConnectionVariables(String connectionVariables) {
        this.connectionVariables = connectionVariables;
    }

    public boolean isWriteable() {
        return writeable;
    }

    public void setWriteable(boolean writeable) {
        this.writeable = writeable;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
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

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public double getDeadband() {
        return deadband;
    }

    public void setDeadband(double deadband) {
        this.deadband = deadband;
    }

    public int getSampleInterval() {
        return sampleInterval;
    }

    public void setSampleInterval(int sampleInterval) {
        this.sampleInterval = sampleInterval;
    }

    public int getMinSampleInterval() {
        return minSampleInterval;
    }

    public void setMinSampleInterval(int minSampleInterval) {
        this.minSampleInterval = minSampleInterval;
    }
}
