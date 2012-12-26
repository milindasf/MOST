package bpi.most.server.utils;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Timer;
import java.util.TimerTask;

import bpi.most.server.model.Datapoint;
import bpi.most.server.model.DpController;
import bpi.most.shared.DpDataDTO;

/**
 * call start(), stop() after instantiating!
 * A)
 * Polls for new values of defined DPs and notifies respective DP objects.
 * Required for DPs which write new measurements directly into the database.
 * Only DPs which do not use the Java abstraction layer for adding new values are polled. 
 * (The poll flag of DPs is checked during polling)
 * UTC time of database server is used as "endtime" for requesting new values!! 
 * default poll interval is 60 seconds.
 * B)
 * polls new warnings and notifies WarningController
 * default poll interval is 60 seconds.
 * @author robert.zach@tuwien.ac.at
 */
public final class PollService {
	
	private static PollService ref = null;
	private static final int POLL_INTERVAL = 60000;
	
	Timer dpPolltimer = new Timer();
	Timer warningPolltimer = new Timer();
	//timestamp of last measurement, set current UTC time on startup
	Timestamp lastDpCheck = getDbTimeUTC();
	Timestamp lastWarningCheck = getDbTimeUTC();

	
	//poll interval in ms, default 60s
	private int pollIntervalDp = POLL_INTERVAL;
	private int pollIntervalWarining = POLL_INTERVAL;

	/**
	 * start poll service
	 */
	public void start() {
		//create EventPoller
		dpPolltimer.schedule( new EventPollerDp(), 0, pollIntervalDp );
		warningPolltimer.schedule( new EventPollerWarning(), 0, pollIntervalWarining );
		return;
	}
	/**
	 * cancel all timers
	 */
	public void stop() {
		dpPolltimer.cancel();
		warningPolltimer.cancel();
		return;
	}
	
	/**
	 * change the DB polling interval for new dp values
	 * 
	 * @arg pollInterval in milliseconds!
	 */
	public void setPollIntervalDp(int pollInterval) {
		dpPolltimer.cancel();
		pollIntervalDp = pollInterval;
		dpPolltimer.schedule( new EventPollerDp(), 0, pollInterval );
		return;
	}
	
	/**
	 * change the DB polling interval for new dp values
	 * 
	 * @arg pollInterval in milli seconds!
	 */
	public void setPollIntervalWarnings(int pollInterval) {
		warningPolltimer.cancel();
		pollIntervalWarining = pollInterval;
		warningPolltimer.schedule( new EventPollerWarning(), 0, pollInterval );
		return;
	}
	
	/**
	 * polls for new values in the defined POLL_INTERVALL and call notifyDatapoints
	 * UTC time of database server is used!!
	 * @return
	 */
	public void processNewDpValues() {
		Connection connection = null;
		CallableStatement cstmt = null;
		ResultSet rs = null; 
		Timestamp starttime = lastDpCheck;
		Timestamp endtime = getDbTimeUTC();


		try {
			connection = DbPool.getInstance().getConnection();
			cstmt = connection.prepareCall("{CALL getValues(?,?,?)}");
			//request values of all DPs
			cstmt.setString(1, "%");
			///set starttime (current time - POLL_INTERVALL)
			//cstmt.setString(2, "SELECT DATE_SUB(UTC_TIMESTAMP(), INTERVAL " + POLL_INTERVALL/1000 + " SECOND);");
			cstmt.setTimestamp(2, starttime);
			//set endtime (current time)
			cstmt.setTimestamp(3, endtime);
			//set to some future timestamp
			//cstmt.setString(3, "'2050-01-01 12:00:00'");
			cstmt.execute();
			//save endtime for next run
			lastDpCheck = endtime;
			rs = cstmt.getResultSet();
			//process all values
			while (rs.next()) {
				//lastValueTimestamp = rs.getTimestamp("timestamp");
				//check if DP is marked for polling
				if (DpController.getInstance().getDatapoint(rs.getString("datapoint_name")).getPolledFlag()) {
					//notify DP
					notifyDatapoint(rs.getString("datapoint_name"), rs.getTimestamp("timestamp"), rs.getDouble("value")) ;					
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
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
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * polls for new warnings and notifies WarningController
	 * UTC time of database server is used to shift timeframe for requests!!
	 * @return
	 */
	public void processNewWarnings() {
//TODO implement getWarning stored procedure
//		Connection connection = null;
//		CallableStatement cstmt = null;
//		ResultSet rs = null; 
//		Timestamp starttime = lastWarningCheck;
//		Timestamp endtime = getDbTimeUTC();
//		
//		try {
//			connection = DbPool.getInstance().getConnection();
//			//TODO implement right procedure!
//			cstmt = connection.prepareCall("{CALL getWarnings(?,?,?)}");
//			///set starttime (last check)
//			cstmt.setTimestamp(1, starttime);
//			//set endtime (current time)
//			cstmt.setTimestamp(2, endtime);
//			//TODO set all other options
//			cstmt.setString(1, "%");
//	
//			cstmt.execute();
//			//save endtime for next run
//			lastWarningCheck = endtime;
//			rs = cstmt.getResultSet();
//			//process all values
//			while (rs.next()) {
//				//TODO notify WarningController
//				//notifyDatapoint(rs.getString("datapoint_name"), rs.getTimestamp("timestamp"), rs.getDouble("value")) ;
//				WarningDTO warning = new WarningDTO(rs.getInt("wid"), rs.getInt("errorCode"), rs.getInt("priority"), 
//						rs.getString("datapoint_name"), rs.getString("description"), rs.getString("toDo"), rs.getString("source"), rs.getTimestamp("fromTime"), rs.getTimestamp("toTime"));
//				WarningController.getInstance().notifyObservers(warning);
//			}
//			
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} finally {
//
//			try {
//				if (rs != null) {
//					rs.close();
//				}
//				if (cstmt != null) {
//					cstmt.close();
//				}
//				if (connection != null) {
//					connection.close();
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
	}
	
	/**
	 * notify DPs Observer witch new value
	 */
	public void notifyDatapoint(String dpName, Timestamp timestamp, Double newValue) {
		Datapoint dp = DpController.getInstance().getDatapoint(dpName);
		// first, set change flag!
		dp.setChanged();
		// notify observers with new measurement
		dp.notifyObservers(new DpDataDTO(timestamp, newValue));
		return;
	}
	
	/**
	 * get current UTC time of database
	 */
	public Timestamp getDbTimeUTC() {
		Connection connection = null;
		Statement localStatement = null;
		ResultSet rs = null;

		try {
			connection = DbPool.getInstance().getConnection();
			localStatement = connection.createStatement();
			//get current UTC time of database
			rs = localStatement.executeQuery("SELECT UTC_TIMESTAMP();");
			//check if response
			if (rs.first()) {
				return rs.getTimestamp(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {

			try {
				if (rs != null) {
					rs.close();
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
		return null;
	}
	

	//Singleton
	private PollService() {
		super();
	}	
	public static PollService getInstance(){
		if (ref == null) {
			ref = new PollService();
		}
		return ref;
	}
	public static PollService getInstance(int pollIntervalDp, int pollIntervalWarning){
		if (ref == null) {
			ref = new PollService();
			ref.pollIntervalDp = pollIntervalDp;
			ref.pollIntervalWarining = pollIntervalWarning;
		}
		return ref;
	}
	
	/**
	 * Poller thread
	 */
	public class EventPollerDp extends TimerTask{
		 	public void run()  
		  	{
			 //System.out.println( "check for new values" );
			 PollService.getInstance().processNewDpValues();
		  	}
		 	
	}
	/**
	 * Poller thread
	 */
	public class EventPollerWarning extends TimerTask{
		 	public void run()  
		  	{
			 //System.out.println( "check for new warnings" );
			 PollService.getInstance().processNewWarnings();
		  	}
		 	
	}

}
