package bpi.most.server.model;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Observable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bpi.most.server.utils.DbPool;
import bpi.most.dto.DpDTO;
import bpi.most.dto.DpDataDTO;
import bpi.most.dto.DpDatasetDTO;

/**
 * Abstract representation of a datapoint. Use this interface if you access
 * datapoints!
 * 
 * @author robert.zach@tuwien.ac.at FIXME: use exceptions for error processing
 */
public abstract class Datapoint extends Observable {
	
	private static final Logger LOG = LoggerFactory.getLogger(Datapoint.class);
	
	// identifier of the datapoint
	private String datapointName;
	//
	boolean isPolled = true;

	/**
	 * constructors
	 */
	public Datapoint(String datapointName) {
		super();
		this.datapointName = datapointName;
	}

	/**
	 * store a measurement of the datapoint
	 * 
	 * @return 1 = inserted; < 0 constraints violated or procedure error
	 */
	public int addData(DpDataDTO measurement) {
		//remove polled flag if value is added once using the Java abstraction layer
		setPolledFlag(false);
		// first, set change flag!!
		setChanged();
		// notify observers with new measurement
		notifyObservers(measurement);
		return 1;
	}

	/**
	 * provide setChanged public.
	 * Required for DpPollService to mark DPs changed.
	 */
	public void setChanged() {
		super.setChanged();
	}
	/**
	 * Deletes all stored data of datapoint. Use with caution!!
	 */
	public abstract int delData();

	/**
	 * Deletes data of a given timeslot
	 */
	public abstract int delData(Date starttime, Date endtime);

	/**
	 * @return The latest value in Monitoring DB as DatapointDataVO, null if empty
	 */
	public abstract DpDataDTO getData();

	/**
	 * @return A DatapointDatasetVO from start- to endtime, null if empty
	 */
	public abstract DpDatasetDTO getData(Date starttime, Date endtime);

	/**
	 * @return a DatapointDatasetVO from start- to endtime, null if empty
	 */
	public abstract DpDatasetDTO getDataPeriodic(Date starttime, Date endtime,
			Float period, int mode);

	/**
	 * A method used to get the number of values in a specific time frame.
	 * 
	 * @param starttime
	 *            Start time of the time frame you want to know how many values
	 *            are in.
	 * @param endtime
	 *            End time of the time frame you want to know how many values
	 *            are in.
	 * @return Number of values in the specific time frame between start and end
	 *         time.
	 */
	public abstract int getNumberOfValues(Date starttime, Date endtime);

	// FIXME: add getDataPeriodicWhere... functions

	// getters and setters
	public String getDatapointName() {
		return datapointName;
	}

	// FIXME: add support for datapoint name change (change name in DB, etc.)
	public void setDatapointName(String datapointName) {
		this.datapointName = datapointName;
	}

	public String getAccuracy() {
		return getColumnString("accuracy");
	}

	public String getUnit() {
		return getColumnString("unit");
	}

	public double getMin() {
		return getColumnDouble("min");
	}

	public double getMax() {
		return getColumnDouble("max");
	}

	public String getType() {
		return getColumnString("type");
	}

	public String getMathOperations() {
		return getColumnString("math_operations");
	}

	public double getDeadband() {
		return getColumnDouble("deadband");
	}

	public double getSampleInterval() {
		return getColumnDouble("sample_interval");
	}

	public double getSampleIntervalMin() {
		return getColumnDouble("sample_interval_min");
	}

	public double getWatchdog() {
		return getColumnDouble("watchdog");
	}

	public double getZoneIdZone() {
		return getColumnDouble("zone_idzone");
	}

	public String getDeviceName() {
		return getColumnString("device_name");
	}

	public String getModel() {
		return getColumnString("model");
	}

	public String getVendor() {
		return getColumnString("vendor");
	}

	public String getVirtual() {
		return getColumnString("virtual");
	}

	public Zone getZone() {
		ZoneController zoneCtl = ZoneController.getInstance();
		return zoneCtl.getZone((int) getColumnDouble("zone_idzone"));
	}

	public boolean getPolledFlag() {
		return isPolled;
	}

	public void setPolledFlag(boolean isPolled) {
		this.isPolled = isPolled;
	}

	/**
	 * @param source columnname of requested source (e.g. OPC or JDBC). if note defined, try to guess it.
	 * @param argument if null, everything is returned. Allows to request variables defined like:
	 * "trigger=monitoring01:OpcTrigger5Min; typepath=monitoring01:gweno1.S.ele26_S4;"
	 * getSource("opc", "trigger") only returns "monitoring01:OpcTrigger5Min".
	 * @return source definition
	 */
	public String getSource(String source, String argument) {
		//TODO: implement logic to preprocess source information
		return getColumnString(source);
	}
	
	/**
	 * processes additional attributes defined in custom_attr. e.g.
	 * "myvariable1 value1; myvariable2 value2;"
	 * 
	 * @param requested
	 *            attribute e.g. "myvariable1"
	 * @return value of attribute (as string), "" if nothing found
	 */
	public String getCustomAttr(String myAttr) {
		String myResult = "";
		String myCustomAttr = null;
		int myFound = 0;

		myCustomAttr = getColumnString("custom_attr");
		if (myCustomAttr != null) {
			// split multiple Attributes, ex.
			// "myvariable1 someValue; myvariable2 someValue"
			for (String oneAttr : myCustomAttr.split(";")) {
				// split one Attribute definition, ex. "myvariable1 someValue"
				for (String myVariable : oneAttr.split(" ")) {
					// rebuild string
					if (myFound > 1) {
						myResult = myResult + " " + myVariable;
						myFound++;
					}
					if (myFound == 1) {
						// to prevent first " "
						myResult = myVariable;
						myFound++;
					}
					if (myFound == 0 && myVariable.equals(myAttr)) {
						myFound = 1;
					}
				}
				myFound = 0;
			}
		}
		return myResult;
	}
	
	/**
	 * @return String of requested column
	 */
	protected String getColumnString(String requestedColumn) {
		String columnContent = null;
		Connection connection = null;
		CallableStatement cstmt = null;
		ResultSet rs = null;

		try {
			connection = DbPool.getInstance().getConnection();
			cstmt = connection.prepareCall("{CALL getDatapoint(?)}");
			cstmt.setString(1, getDatapointName());
			cstmt.execute();
			rs = cstmt.getResultSet();
			if (rs.next()) {
				columnContent = rs.getString(requestedColumn);
			}
		} catch (SQLException e) {
			LOG.error("An exception occured while calling stored procedure 'getDatapoint'", e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}

				if (cstmt != null) {
					cstmt.close();
				}

				if (connection != null) {
					connection.close();
				}
			} catch (Exception e) {
				LOG.error("An exception occured while calling 'close'", e);
			}
		}
		return columnContent;
	}

	/**
	 * @return double of requested column, 0.0 if null
	 */
	protected double getColumnDouble(String requestedColumn) {
		double columnContent = 0.0;
		Connection connection = null;
		CallableStatement cstmt = null;
		ResultSet rs = null;

		try {
			connection = DbPool.getInstance().getConnection();
			cstmt = connection.prepareCall("{CALL getDatapoint(?)}");
			cstmt.setString(1, getDatapointName());
			cstmt.execute();
			rs = cstmt.getResultSet();
			if (rs.next()) {
				columnContent = rs.getDouble(requestedColumn);
			}
		} catch (SQLException e) {
			LOG.error("An exception occured while calling stored procedure 'getDatapoint'", e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (cstmt != null) {
					cstmt.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (Exception e) {
				LOG.error("An exception occured while calling 'close'", e);
			}
		}
		return columnContent;
	}



	/**
	 * 
	 * @return a DpDTO
	 */
	public DpDTO getDpDTO() {
		Connection connection = null;
		CallableStatement cstmt = null;
		ResultSet rs = null;
		DpDTO result = null;

		try {
			connection = DbPool.getInstance().getConnection();
			cstmt = connection.prepareCall("{CALL getDatapoint(?)}");
			cstmt.setString(1, getDatapointName());
			cstmt.execute();
			rs = cstmt.getResultSet();
			if (rs.next()) {
				result = new DpDTO(getDatapointName(), rs.getString("type"), "description not defined yet in the db");
			}
		} catch (SQLException e) {
			LOG.error("An exception occured while calling stored procedure 'getDatapoint'", e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}

				if (cstmt != null) {
					cstmt.close();
				}

				if (connection != null) {
					connection.close();
				}
			} catch (Exception e) {
				LOG.error("An exception occured while calling 'close'", e);
			}
		}
		return result;
	}
	
	/**
	 * @return true if virtual datapoint
	 */
	public boolean isVirtual() {
		boolean result = false;
		if (getVirtual() != null){		//null is returned for SQL NULL
			result = true;
		}
		return result;
	}
	
}
