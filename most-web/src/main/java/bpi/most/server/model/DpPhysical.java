/**
 * 
 */
package bpi.most.server.model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import bpi.most.server.utils.DbPool;
import bpi.most.shared.DpDataDTO;
import bpi.most.shared.DpDatasetDTO;

/**
 * Provides a implementation of a physical datapoint (real sensor/actor) which
 * stores the values in the database. datapointName is stored locally,
 * everything else is fetched from the DB at runtime.
 * 
 * @author robert.zach@tuwien.ac.at FIXME: Use prepared calls!!
 */
public class DpPhysical extends Datapoint {
	/**
	 * constructors
	 */
	public DpPhysical(String datapointName) {
		super(datapointName);
	}

	/**
	 * @return 1 = inserted; < 0 constraints violated or procedure error
	 */
	public int addData(DpDataDTO measurement) {
		int result = 0;

		if (measurement != null) {
			// call addData SP
			result = callStoredProcedure("CALL addData('"
					+ getDatapointName()
					+ "', '"
					+ new java.sql.Timestamp(measurement.getTimestamp()
							.getTime()) + "', '" + measurement.getValue()
					+ "');");
		}
		// notify obsevers (super.addData()) if value was inserted
		if (result == 1) {
			super.addData(measurement);
		}
		return result;
	}

	/**
	 * Deletes all data of datapoint. Use with caution!!
	 */
	public int delData() {
		int result = 0;

		// call emptyDatapoint SP
		result = callStoredProcedure("CALL emptyDatapoint('"
				+ getDatapointName() + "');");

		return result;
	}

	/**
	 * Deletes data of given timeslot
	 */
	public int delData(Date starttime, Date endtime) {
		int result = 0;

		// call emtpyDatapointTimeslot SP
		result = callStoredProcedure("CALL emtpyDatapointTimeslot('"
				+ getDatapointName() + "', '"
				+ new java.sql.Timestamp(starttime.getTime()) + "', '"
				+ new java.sql.Timestamp(endtime.getTime()) + "');");

		return result;
	}

	/**
	 * @return The latest value in Monitoring DB as DatapointDataVO, null if empty
	 */
	public DpDataDTO getData() {
		DpDatasetDTO queryResult = null;
		DpDataDTO result = null;

		// get last value
		queryResult = getDatasetFromQuery("CALL getValues('"
				+ getDatapointName() + "', null,null);");
		if (queryResult != null) {
			result = queryResult.get(0);
		}
		return result;
	}

	/**
	 * @return A DatapointDatasetVO from start- to endtime, null if empty
	 */
	public DpDatasetDTO getData(Date starttime, Date endtime) {
		DpDatasetDTO result = null;
		// check start before endtime
		// System.out.println("getData Start");
		if (starttime.before(endtime)) {
			// System.out.println("starttime.before(endtime)");
			result = getDatasetFromQuery("CALL getValues('"
					+ getDatapointName() + "', '"
					// create sql.Timestamp for mysql DATETIME
					+ new java.sql.Timestamp(starttime.getTime()) + "','"
					+ new java.sql.Timestamp(endtime.getTime()) + "');");
			// + starttime.getTime() + "','"
			// + endtime.getTime() + "');");
		}
		return result;
	}

	/**
	 * @return a DatapointDatasetVO from start- to endtime, null if empty
	 */
	public DpDatasetDTO getDataPeriodic(Date starttime, Date endtime,
			Float period, int mode) {
		DpDatasetDTO result = null;
		if (starttime.before(endtime)) {
			result = getDatasetFromQuery("CALL getValuesPeriodic('"
					+ getDatapointName() + "', '"
					+ new java.sql.Timestamp(starttime.getTime()) + "', '"
					+ new java.sql.Timestamp(endtime.getTime()) + "', '"
					+ period + "', '" + mode + "' );");
		}
		return result;
	}
	
	/* (non-Javadoc)
	 * @see bpi.most.server.model.Datapoint#getNumberOfValues(java.util.Date, java.util.Date)
	 */
	public int getNumberOfValues(Date starttime, Date endtime) {
		int result = 0;
		System.out.println("CALL 	getNumberOfValues('"
				+ getDatapointName() + "', '"
				+ new java.sql.Timestamp(starttime.getTime()) + "', '"
				+ new java.sql.Timestamp(endtime.getTime()) + "');");
		// call getNumberOfValues SP
		result = callStoredProcedure("CALL 	getNumberOfValues('"
				+ getDatapointName() + "', '"
				+ new java.sql.Timestamp(starttime.getTime()) + "', '"
				+ new java.sql.Timestamp(endtime.getTime()) + "');");
		return result;
	}

	// FIXME add getDataPeriodicWhere... functions

	/**
	 * Used for stored procedures that returns an integer value. Even for these
	 * that uses integer values as error code.
	 * 
	 * (1 = inserted; 2 = constraints violated; 0 = procedure error )
	 * 
	 * @param query	The stored procedure to call. Used as follows:
	 *            <pre>
	 * <code>
	 * "CALL addData('test', '2011-06-29 00:02:30.0', '4.6');"
	 * </code>
	 * </pre>
	 * @return Returns an integer value that is needed or an error code.
	 */
	protected int callStoredProcedure(String query) {
		Connection connection = null;
		Statement localStatement = null;
		ResultSet localResultSet = null;
		int result = 0;

		try {
			connection = DbPool.getInstance().getConnection();
			localStatement = connection.createStatement();
			// FIXME: handle SQL errors
			localResultSet = localStatement.executeQuery(query);
			// check if result is empty, this returns false if the cursor is not
			// before the first record or if there are no rows in the ResultSet
			if (localResultSet.isBeforeFirst()) {
				localResultSet.next();
				result = localResultSet.getInt(1);
			}
		} catch (SQLException e) {
			System.out.println("Exception in " + getDatapointName());
			e.printStackTrace();
		} finally {
			// close statement and resultset
			try {
				if (localResultSet != null) {
					localResultSet.close();
				}

				if (localStatement != null) {
					localStatement.close();
				}

				if (connection != null) {
					connection.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * @return A DatapointDatasetVO from query string, null if empty
	 */
	protected DpDatasetDTO getDatasetFromQuery(String query) {
		Connection connection = null;
		Statement localStatement = null;
		ResultSet localResultSet = null;
		DpDatasetDTO resDataset = null;

		try {
			connection = DbPool.getInstance().getConnection();
			localStatement = connection.createStatement();
			// FIXME: handle SQL errors
			localResultSet = localStatement.executeQuery(query);
			// check if result is empty, this returns false if the result set is empty
			if (localResultSet.next()) {
				// check if quality table exists
				ResultSetMetaData meta = localResultSet.getMetaData();
				int numCol = meta.getColumnCount();
				boolean qualityExists = false;
				for (int i = 1; i < numCol + 1; i++) {
					if (meta.getColumnName(i).equals("quality")) {
						qualityExists = true;
					}
				}
				// create and populate requested Dataset
				resDataset = new DpDatasetDTO(getDatapointName());
				do {
					// set quality=1 if not provided in resultSet
					if (qualityExists) {
						resDataset.add(new DpDataDTO(localResultSet
								.getTimestamp("timestamp"), localResultSet
								.getDouble("value"), localResultSet
								.getFloat("quality")));
					} else {
						resDataset.add(new DpDataDTO(localResultSet
								.getTimestamp("timestamp"), localResultSet
								.getDouble("value"), (float) 1));
					}

				} while (localResultSet.next());
			}
		} catch (SQLException e) {
			System.out.println("Exception in " + getDatapointName());
			e.printStackTrace();
		} finally {
			// close statement and resultset
			try {
				if (localResultSet != null) {
					localResultSet.close();
				}

				if (localStatement != null) {
					localStatement.close();
				}

				if (connection != null) {
					connection.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return resDataset;
	}

}
