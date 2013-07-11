/**
 * 
 */
package bpi.most.connector.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import bpi.most.domain.connector.ConnectorVO;
import bpi.most.dto.DpDataDTO;
import bpi.most.dto.DpDatasetDTO;
import bpi.most.dto.UserDTO;
import bpi.most.service.api.DatapointService;
import org.apache.log4j.Logger;

import bpi.most.connector.Connector;

/**
 * Implements a "connection driver" for any JDBC compatibe source (sensor)
 * It tries to detect the source "database structure" based on provided information
 * The following variables are supported
 * - sqlTableName - Name of the table including the values/measurements
 * - sqlTimestampColumn - Name of timestamp column
 * - sqlDatapointColumn - Columnname of the sensor
 * - sqlDatapointName - "ID" of datapoint (required only if all datapoints are in the same column)
 * - sqlValueColumn - Name of column containing the value (required only if all datapoints are in the same column)
 * - (optional) sqlDateFormatPattern - e.g. "yyyy-MM-dd hh:mm:ss", etc. or "seconds" (--> int since 1970)
 * 
 * Examples:
 * - for timestamp|sensor1|sensor2|sensor3|etc.
 * "sqlTableName data; sqlTimestampColumn timestamp; sqlDatapointColumn sensor1"
 * - for timestamp|sensorname(ID)|value
 * "sqlTableName data; sqlTimestampColumn timestamp; sqlDatapointColumn sensorname; sqlDatapointName sensor1; sqlValueColumn value"
 * 
 * Source structure Detection:
 * if sqlValueColumn == "" or sqlValueColumn == sqlDatapointColumn:
 * 		structure = timestamp|sensor1|sensor2|sensor3|etc. 
 * else 
 * 		structure = timestamp|sensorname(ID)|value
 * 
 * Timestamp is parsed in the following format "yyyy-MM-dd hh:mm:ss"
 * TODO add SimpleDateFormat pattern variable
 * TODO use preparedStatements
 * @author robert.zach@tuwien.ac.at
 */
public class ConnectorJdbcImpl extends Connector{
	private final static Logger log = Logger.getLogger( ConnectorJdbcImpl.class );
	
	DbConnectionFactory jdbcConnFactory = null;
	//connector variable definitions
	protected final String SQL_TABLE_NAME = "sqlTableName";
	protected final String SQL_TIMESTAMP_COLUMN = "sqlTimestampColumn";
	protected final String SQL_DATAPOINT_COLUMN = "sqlDatapointColumn";
	protected final String SQL_DATAPOINT_NAME = "sqlDatapointName";
	protected final String SQL_VALUE_COLUMN = "sqlValueColumn";
	protected final String SQL_DATE_FORMAT_PATTERN = "sqlDateFormatPattern";
	//used for date formating
	private String dateFormat = "yyyy-MM-dd hh:mm:ss";	//default date syntax
	private String dateSecondsA = "";
	private String dateSecondsB = "";
	private String dateSecondsC = "";
	private String dateSecondsD = "";
	
	/**
	 * JdbcConnectionFactory provides connections 
	 */
	public ConnectorJdbcImpl(ConnectorVO connectorDTO, DbConnectionFactory jdbcConnectionFactory, UserDTO user) {
		super(connectorDTO, user);
		jdbcConnFactory = jdbcConnectionFactory;
		//TODO check date format
		if (getConnectionVariable(SQL_DATE_FORMAT_PATTERN) == null) {
			return;
		} else if (getConnectionVariable(SQL_DATE_FORMAT_PATTERN).equals("seconds")) {
			//dateSecondsA = "datetime(";
			//dateSecondsB = ", 'unixepoch')";
			dateSecondsC = "strftime('%s',";
			dateSecondsD = ")";
			dateFormat = "s";	//read timestamp column as seconds
		} else {
			//get custom date format
			dateFormat = getConnectionVariable(SQL_DATE_FORMAT_PATTERN);
		}
		
	}

	//get for variables
	//TODO replace with direct access to the variables - metadata.getVariable(SQL_TABLE_NAME), ...
	protected String getSqlTableName() {
		return getConnectionVariable(SQL_TABLE_NAME);
	}
	protected String getSqlTimestampColumn() {
		return getConnectionVariable(SQL_TIMESTAMP_COLUMN);
	}
	protected String getSqlDatapointColumn() {
		return getConnectionVariable(SQL_DATAPOINT_COLUMN);
	}
	protected String getSqlDatapointName() {
		return getConnectionVariable(SQL_DATAPOINT_NAME);
	}
	protected String getSqlValueColumn() {
		return getConnectionVariable(SQL_VALUE_COLUMN);
	}
	
	
	
	
	@Override
	protected boolean writeData(DpDataDTO newValue) {
		//TODO implement
		// not supported yet
		return false;
	}
	
	/**
	 * @return the latest value of Source DB as DatapointData, null if empty
	 */
	public DpDataDTO getSourceData() {
		DpDatasetDTO queryResult = null;
		DpDataDTO result = null;

		/**
		 * try to detect source database structure
		 * if getSqlValueColumn() == "" || getSqlValueColumn() == getSqlDatapointColumn()
		 * the timestamp|sensor1|sensor2|etc. column structure is used
		 * else timestamp|sensorname|value structure is used
		 * The SQL statement needs to return something like timestamp|value
		 */
		if (getSqlValueColumn() == null || getSqlValueColumn().equals(getSqlDatapointColumn()) ) {
			//TODO: check if all sql statements are legal
			queryResult = getSourceDatasetFromQuery("SELECT "
					+ dateSecondsA
					+ getSqlTimestampColumn()
					+ dateSecondsB 
					+ ", "
					+ getSqlDatapointColumn() + " FROM " 
					+ getConnectionVariable(SQL_TABLE_NAME) + " ORDER BY " 
					+ getSqlTimestampColumn() + " DESC LIMIT 1;");
		} else {
			//TODO: check if all sql statements are legal
			queryResult = getSourceDatasetFromQuery("SELECT "
					+ dateSecondsA
					+ getSqlTimestampColumn()
					+ dateSecondsB
					+ ", "
					+ getSqlValueColumn() + " FROM " 
					+ getConnectionVariable(SQL_TABLE_NAME) + " WHERE "
					+ getSqlDatapointColumn() + " = '"
					+ getSqlDatapointName() + "' ORDER BY "
					+ getSqlTimestampColumn() + " DESC LIMIT 1;");
		}
		 
		
		if (queryResult != null) {
			//get first entry
			result = queryResult.get(0);
		}	
		return result;
	}
	
	@Override
	public DpDatasetDTO getSourceData(Date starttime) {
		DpDatasetDTO result = null;
		//try to detect source database structure
		//if getSqlValueColumn() == "" || getSqlValueColumn() == getSqlDatapointColumn() 
		//  the timestamp|sensor1|sensor2|etc. column structure is used
		//else timestamp|sensorname|value structure is used
		//The SQL statement needs to return something like timestamp|value
		if (getSqlValueColumn() == null || getSqlValueColumn().equals(getSqlDatapointColumn()) ) {
			//the timestamp|sensor1|sensor2|etc. column structure is used
			//ex. "SELECT date, barometer FROM wsintervaldata WHERE date >= '2010-03-09 17:35:01.0';"
			result = getSourceDatasetFromQuery("SELECT "
					+ dateSecondsA
					+ getSqlTimestampColumn() 
					+ dateSecondsB 
					+ ", "
					+ getSqlDatapointColumn() + " FROM " 
					+ getConnectionVariable(SQL_TABLE_NAME) + " WHERE " 
					+ getSqlTimestampColumn() + " >= "
					//create sql.Timestamp for sql time requests
					+ dateSecondsC
					+ "'" + new java.sql.Timestamp(starttime.getTime()) + "'" 
					+ dateSecondsD
					+ ";");
					
		} else {
			//the timestamp|sensorname|value column structure is used
			result = getSourceDatasetFromQuery("SELECT "
					+ dateSecondsA
					+ getSqlTimestampColumn()
					+ dateSecondsB 
					+ ", "
					+ getSqlValueColumn() + " FROM " 
					+ getConnectionVariable(SQL_TABLE_NAME) + " WHERE "
					+ getSqlDatapointColumn() + " = '"
					+ getSqlDatapointName() + "' AND " 
					+ getSqlTimestampColumn() + " >= "
					//create sql.Timestamp for sql time requests
					+ dateSecondsC
					+ "'" + new java.sql.Timestamp(starttime.getTime())  + "'"
					+ dateSecondsD
					+ ";");
		}
		return result;
	}
	
	/**
	 * @return a DatapointDataset from start- to endtime of Source DB, null if empty 
	 */
	public DpDatasetDTO getSourceData(Date starttime, Date endtime) {
		DpDatasetDTO result = null;
		
		//check start before endtime
		if ( starttime.before(endtime) ) {
			//try to detect source database structure
			//if getSqlValueColumn() == "" || getSqlValueColumn() == getSqlDatapointColumn() 
			//  the timestamp|sensor1|sensor2|etc. column structure is used
			//else timestamp|sensorname|value structure is used
			//The SQL statement needs to return something like timestamp|value
			if (getSqlValueColumn() == null || getSqlValueColumn().equals(getSqlDatapointColumn()) ) {
				//the timestamp|sensor1|sensor2|etc. column structure is used
				//TODO: check if all sql statements are legal
				//ex. "SELECT date, barometer FROM wsintervaldata WHERE date BETWEEN '2010-03-09 17:35:01.0' AND '2010-03-19 17:35:01.0';"
				result = getSourceDatasetFromQuery("SELECT "
						+ dateSecondsA
						+ getSqlTimestampColumn() 
						+ dateSecondsB 
						+ ", "
						+ getSqlDatapointColumn() + " FROM " 
						+ getConnectionVariable(SQL_TABLE_NAME) + " WHERE " 
						+ getSqlTimestampColumn() + " BETWEEN "
						//create sql.Timestamp for sql time requests
						+ dateSecondsC
						+ "'" + new java.sql.Timestamp(starttime.getTime()) + "'"
						+ dateSecondsD
						+" AND "
						+ dateSecondsC
						+ "'" + new java.sql.Timestamp(endtime.getTime()) + "'" 
						+ dateSecondsD
						+ ";");
			} else {
				//the timestamp|sensorname|value column structure is used
				//TODO: check if all sql statements are legal
				result = getSourceDatasetFromQuery("SELECT "
						+ dateSecondsA
						+ getSqlTimestampColumn()
						+ dateSecondsB
						+ ", "
						+ getSqlValueColumn() + " FROM " 
						+ getConnectionVariable(SQL_TABLE_NAME) + " WHERE "
						+ getSqlDatapointColumn() + " = '"
						+ getSqlDatapointName() + "' AND " 
						+ getSqlTimestampColumn() + " BETWEEN "
						//create sql.Timestamp for sql time requests
						+ dateSecondsC
						+ "'" + new java.sql.Timestamp(starttime.getTime()) + "'"
						+ dateSecondsD
						+ " AND "
						+ dateSecondsC
						+ "'" + new java.sql.Timestamp(endtime.getTime()) + "'"
						+ dateSecondsD
						+ ";");
			}
		}
		return result;
	}
	
	/**
	 * @return tries to return a DatapointDataset from source query string, empty DpDatasetDTO if no values
	 * column 1 = timestamp
	 * column 2 = value
	 * TODO switch to preparedStatements
	 */
	protected DpDatasetDTO getSourceDatasetFromQuery(String query) {
		//debug
		log.info("Executing source query: " + query);
		Connection jdbcConn = jdbcConnFactory.getConnection();
		Statement localStatement = null;
		ResultSet localResultSet = null;
		DpDatasetDTO resDataset = new DpDatasetDTO(getDpName());
				
		try {
			localStatement = jdbcConn.createStatement();
			localResultSet = localStatement.executeQuery(query);
			//check if result is empty, this returns false if the cursor is not before the first record or if there are no rows in the ResultSet
			if (localResultSet.isBeforeFirst()) {
				//define date format of source
				DateFormat df = new SimpleDateFormat( dateFormat );
				//populate requested Dataset
				while (localResultSet.next()) {
					//get timestamp and value
					//parse timestamp as defined
					Date tmpTimestamp = df.parse(localResultSet.getString(1));
					Double tmpValue = localResultSet.getDouble(2);
					resDataset.add(new DpDataDTO(tmpTimestamp, tmpValue) );
				}
			}
		} catch (SQLException e) {
			System.out.println("Exception in " + getDpName() + " " + getConnectionType());
			e.printStackTrace();
		} catch (ParseException e) {
			System.out.println("Exception in " + getDpName() + " " + getConnectionType());
			e.printStackTrace();
		}  finally {
			//close statement, resultset and connection
			try {
				if (localResultSet != null) {
					localResultSet.close();
				}

				if (localStatement != null) {
					localStatement.close();
				}
				jdbcConn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}		
		return resDataset;
	}


	
}


