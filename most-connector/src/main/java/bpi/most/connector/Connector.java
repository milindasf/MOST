package bpi.most.connector;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.TimeZone;

import bpi.most.domain.connector.ConnectorVO;
import bpi.most.dto.*;
import bpi.most.service.api.DatapointService;
import org.apache.log4j.Logger;

import javax.inject.Inject;


/**
 * "Connects" a device (Sensor/Actor) with a datapoint in the monitoring DB
 * This class is extended by designated "connection driver" which must implement the methods
 * - getSourceData()	- return last measurement
 * - getSourceData(Date starttime) - return last measurement if timeframe is not supported
 * - (optional) getSourceData(Date starttime, Date endtime) - return last measurement if timeframe is not supported
 * - (optional) writeData(newValue) - write value (to actor), false if not supported
 * and
 * - register the Connector to the poll service (???) and/or
 * - register the Connector.processXXX() to respective event callbacks
 * 
 * The following variables are supported:
 * - timezone (optional) - defines timezone of measurement timestamps. Used to calculate UTC timestamps for the database. Default = "timezone UTC;" Example "timezone Europe/Viena;".
 * - pollInterval (optional) - [s] define how often this connection is polled --> calls processTimeframe(Date lastPollTimestamp).  e.g. "pollInterval 3600;"
 *  
 * This class implements the Observer pattern to write new values on change events if the connector is writeable.
 * To prevent loops, the last 100 values initiated by the connector itself are ignored during observer update events. 
 * @author robert.zach@tuwien.ac.at
 */
public abstract class Connector implements Observer{
	private final static Logger log = Logger.getLogger( Connector.class );
	//connector variables
	protected final String TIMEZONE = "timezone";	//default = "Europe/Vienna" --> GMT+1 = wintertime, GMT+2 = summertime
	protected final String POLL_INTERVAL = "pollInterval";
	//"connected" dp

    /**
     * gets injected by spring.
     * WARNING: the dpService is only set (and hence available) after the constructor
     */
    @Inject
	protected DatapointService dpService;

    private UserDTO user;
	protected String dpName = null;
	private String deviceName = null;
	private String connectionType = null;
	private String connectionVariables = null;
	private boolean writeable = false;
	private String vendor = null;
	private String model = null;

	//latest values which were read from the sensor/actor and added to the database
	protected List<DpDataDTO> valueHistory = new LimitedQueue<DpDataDTO>(100);		//stores the last 100 measurements, uses UTC!

	/**
	 * connect the connector to a datapoint
	 */
	public Connector(ConnectorVO connectorDTO, UserDTO user) {
        this.user = user;
        dpName = connectorDTO.getDpName();
		deviceName = connectorDTO.getDeviceName();
		connectionType = connectorDTO.getConnectionType();
		connectionVariables = connectorDTO.getConnectionVariables();
		writeable = connectorDTO.isWriteable();
		vendor = connectorDTO.getVendor();
		model = connectorDTO.getModel();
		//register to poll service if required
		String pollInterval = getConnectionVariable(POLL_INTERVAL);
		if (pollInterval != null) {
			int pollIntervalInt = 900; //set default (15 min) if pollInterval is defined without argument
			try {
				pollIntervalInt = Integer.parseInt(pollInterval);
			} catch (NumberFormatException n) {
				log.info("Datapoint " + dpName + " does not provide a valid pollInterval: " + pollInterval + " Using 900s (15min).");
				pollIntervalInt = 900;
			}
			PollService.getInstance().registerConnector(this, pollIntervalInt);
		}
		//register connector to value changes if connector is writeable
		if (isWriteable()) {
			//TODO prevent update (observer) to be called before connector initializaiton (construcors of childs) is finished
			dpService.addObserver(dpName, this);
		}
	}

	/**
	 * write value to device, return false if not supported
	 * timestamp with connector specific timezone
	 */
	protected abstract boolean writeData(DpDataDTO newValue);
	
	/**
	 * @return the latest value of the source, null if empty. Use connector specific timestamps.
	 */
	public abstract DpDataDTO getSourceData();
	
	/**
	 * Provide last measurement if timeframe is not supported
	 * @return new values from start- to endtime, null if empty. Use connector specific timestamps.
	 */
	public abstract DpDatasetDTO getSourceData(Date starttime);
	
	
	/**
	 * (optional)
	 * @return new values from start- to endtime, null if empty. Use connector specific timestamps.
	 */
	public DpDatasetDTO getSourceData(Date starttime, Date endtime){
		return getSourceData(starttime);
	}
	

	
	
	//##################   default connector stuff   ###########
	
	/**
	 * something happened in this connection -->
	 * process new values since last measurement
	 * @return timestamp (connector specific timezone) of latest Value processed, null if nothing was processed
	 */
	public Date processEvent() {
		DpDataDTO latestValue = null;
		Date lastTimestamp = null;	//connector specific timezone
		
		//get latest measurement if not saved yet
		if (valueHistory.isEmpty()) {
			//null if not value yet
			latestValue = dpService.getData(user, new DpDTO(dpName));
		} else {
			latestValue = valueHistory.get(valueHistory.size()-1);
		}
		
		//check if there is already a measurement
		if (latestValue != null) {
			//add UTC offset because DB and valueHistory use UTC and processXXX needs connector specific time 
			latestValue = addUtcOffset(latestValue);
			lastTimestamp = processTimeframe(latestValue.getTimestamp());
		}else {
			//set starttime 1970 if no measurement yet
			lastTimestamp = processTimeframe(new Date(0));
		}
		return lastTimestamp;
	}
	
	/**
	 * Processes a single new measurement
	 * @param measurement timestamp with connector specific timezone
	 */
	public void processEvent(DpDataDTO measurement) {
		//convert to UTC
		addData(removeUtcOffset(measurement));
	}
	
	/**
	 *  stores new values between starttime and endtime - adds only last measurement if timeframe is not supported
	 *  starttime use connector specific timezone
	 *  @return timestamp (connector specific timezone) of latest Value processed, null if nothing was processed
	 */
	public Date processTimeframe(Date starttime) {
		DpDataDTO lastValue = null;
		DpDatasetDTO dataset = getSourceData(starttime);
		//process measurements if available
		if ( ! dataset.isEmpty()) {
			//loop through each DpData of DpDataset
			for (DpDataDTO measurement : dataset) {
				//convert to UTC
				addData(removeUtcOffset(measurement));
				lastValue = measurement;
			}
			return lastValue.getTimestamp();
		}
		return null;
	}
	
	/**
	 *  stores new values between starttime and endtime - adds only last measurement if timeframe is not supported 
	 */
	public Date processTimeframe(Date starttime, Date endtime) {
		DpDataDTO lastValue = null;
		DpDatasetDTO dataset = getSourceData(starttime, endtime);
		//process measurements if available
		if ( ! dataset.isEmpty()) {
			//loop through each DpData of DpDataset
			for (DpDataDTO measurement : dataset) {
				//convert to UTC
				addData(removeUtcOffset(measurement));
				lastValue = measurement;
			}
			return lastValue.getTimestamp();
		}
		return null;
	}

	
	/**
	 * Store measurement in DB 
	 * @param measurement timestamp in UTC!
	 * @return >0 if stored, <0 if not stored
	 * TODO check storage rules within the connector and use addDataForced to improve performance, 0 sampleInterval means not used (null)!!
	 */
	private int addData(DpDataDTO measurement) {
		int result = 0;
		result = dpService.addData(user, new DpDTO(dpName),measurement);
		if (result > 0) {
			//remember measurements coming from this connector
			valueHistory.add(measurement);
			//TODO print only in debug mode
			log.info("Value added:" + getDpName() + " " + measurement.getTimestamp() + ": " + measurement.getValue()); 
		} else {
			switch (result) {
				//TODO implement log system
				case -2: log.info("Not a valid datapoint - " + getDpName() + ": " + measurement.getTimestamp() + " - " + measurement.getValue());		break;
				case -10: log.info("Storage rule violated (inside min_sample_interval) - " + getDpName() + ": " + measurement.getTimestamp() + " - " + measurement.getValue());		break;
				case -11: log.info("Storage rule violated (inside deadband) - " + getDpName() + ": " + measurement.getTimestamp() + " - " + measurement.getValue());		break;
				case -12: log.info("Storage rule violated ( > max) - " + getDpName() + ": " + measurement.getTimestamp() + " - " + measurement.getValue());		break;
				case -13: log.info("Storage rule violated ( < min) - " + getDpName() + ": " + measurement.getTimestamp() + " - " + measurement.getValue());		break;
				default:	log.info("Storage rule violated (" + result + ") - " + getDpName() + ": " + measurement.getTimestamp() + " - " + measurement.getValue());		break;
		    }
		}
		return result;
	}
	
	
	/**
	 * called if a new value is added to a writeable datapoint
	 * prevent loops by ignoring changes (new values) initiated by the connector itself (lastStored set within addData)!!!
	 * timstamp of measurement uses UTC
	 */
	@Override
	public void update(Observable o, Object arg) {
		//TODO: make it typesafe. Observer provided by Java are not type safe!! Whats going on with Oracle??
		DpDataDTO newValue = (DpDataDTO) arg;
		//Check if this value comes from myself (addData-->lastStored)
		//--> write new value, if it doesn't come from this connector
		if (! isSourceOfValue(newValue)) {
			writeData(addUtcOffset(newValue));
		}
	}
	

	/**
	 * Checks if "newValue" was initiated by this connector
	 * The default connector stores all measurements (and timestamps) of the last 100 measurements (valueHistory).
	 * This method checks the valueHistory.
	 * @param newValue 
	 * @return true if value comes from this connector
	 */
	private boolean isSourceOfValue(DpDataDTO newValue) {
		//newValue and valueHistory use UTC
		//loop through the last 100 values coming from this connector
		for (DpDataDTO iterateValues : valueHistory) {
			//TODO implement DpDataDTO.equals!
			if (newValue.getTimestamp().equals(iterateValues.getTimestamp()) && newValue.getValue().equals(iterateValues.getValue()) ) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * removes daylight saving time (DST, summer/winter) and GMT offset from a measurement
	 * @return timestamp - UTC offset
	 */
	protected DpDataDTO removeUtcOffset(DpDataDTO measurement) {
		TimeZone tz = null; 
		long offset = 0;
		
		if (getConnectionVariable(TIMEZONE) != null) {
			//TODO check timezone variable --> error if wrong type
			tz = TimeZone.getTimeZone(getConnectionVariable(TIMEZONE));
		}else {
			//default
			tz = TimeZone.getTimeZone("UTC");
		}
		//get offset of respective timzone incl. DST (summer-/wintertime)
		offset = tz.getOffset(measurement.getTimestamp().getTime());
		//change measurement timestamp
		measurement.setTimestamp(new Date(measurement.getTimestamp().getTime() - offset ));
		return  measurement;
	}
	
	/**
	 * adds daylight saving time (DST, summer/winter) and GMT offset to a measurement
	 * @return timestamp + offset
	 */
	protected DpDataDTO addUtcOffset(DpDataDTO measurement) {
		TimeZone tz = null; 
		long offset = 0;
		
		if (getConnectionVariable(TIMEZONE) != null) {
			//TODO check timezone variable --> error if wrong type
			tz = TimeZone.getTimeZone(getConnectionVariable(TIMEZONE));
		}else {
			//default
			tz = TimeZone.getTimeZone("UTC");
		}
		//get offset of respective timzone incl. DST (summer-/wintertime)
		offset = tz.getOffset(measurement.getTimestamp().getTime());
		//change measurement timestamp
		measurement.setTimestamp(new Date(measurement.getTimestamp().getTime() + offset ));
		return  measurement;
	}


	public String getDpName() {
		return dpName;
	}
	
	public String getDeviceName() {
		return deviceName;
	}
	
	public String getConnectionType() {
		return connectionType;
	}

	public String getConnectionVariables() {
		return connectionVariables;
	}
	
	/**
	 * TODO Example 
	 * @param variable requested variable
	 * @return value of variable (as string), null if not defined
	 */
	public String getConnectionVariable(String variable) {
		String	myResult = null;
		String	allVariables = null;
		int	myFound = 0;
		
		allVariables = getConnectionVariables();
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

	public boolean isWriteable() {
		return writeable;
	}

	public String getVendor() {
		return vendor;
	}

	public String getModel() {
		return model;
	}


	/**
	 * Limited queue to store measurements coming from this connector 
	 * @author robert.zach@tuwien.ac.at
	 * @param <E>
	 */
	public class LimitedQueue<E> extends LinkedList<E> {
		private static final long serialVersionUID = 1L;
		private int limit;

	    public LimitedQueue(int limit) {
	        this.limit = limit;
	    }

	    @Override
	    public boolean add(E o) {
	        super.add(o);
	        while (size() > limit) { super.remove(); }
	        return true;
	    }
	}

}
