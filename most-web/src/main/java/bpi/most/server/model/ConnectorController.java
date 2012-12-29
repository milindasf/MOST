package bpi.most.server.model;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bpi.most.server.services.User;
import bpi.most.server.utils.DbPool;
import bpi.most.dto.ConnectorDTO;

/**
 * provides information about defined Connectors (Datapoint - Connector - Device/Sensor/Actor)
 * @author robert.zach@tuwien.ac.at
 */
public final class ConnectorController {
	
	private static final Logger LOG = LoggerFactory.getLogger(ConnectorController.class);
	
	private static ConnectorController ref = null;
	

	/**
	 * @return all connections 
	 */
	public List<ConnectorDTO> getConnection() {
		List<ConnectorDTO> result = new LinkedList<ConnectorDTO>();
		Connection connection = null;
		CallableStatement cstmt = null;
		ResultSet rs = null;

		try {
			connection = DbPool.getInstance().getConnection();
			cstmt = connection.prepareCall("{CALL getConnection(?,?,?,?,?)}");
			cstmt.setString(1, null);
			cstmt.setString(2, null);
			cstmt.setString(3, null);
			cstmt.setString(4, null);
			cstmt.setString(5, null);
			cstmt.execute();
			rs = cstmt.getResultSet();
			while (rs.next()) {
				//set worst case values if null.
				double min = rs.getDouble("min");
				min = (rs.wasNull()) ? - Double.MAX_VALUE : min;
				double max = rs.getDouble("max");
				max = (rs.wasNull()) ? Double.MAX_VALUE : max;
				double deadband = rs.getDouble("deadband");
				deadband = (rs.wasNull()) ? 0.0 : deadband;
				int sampleInterval = rs.getInt("sample_interval");
				sampleInterval = (rs.wasNull()) ? 0 : sampleInterval; //0 means not used (null)!
				int minSampleInterval = rs.getInt("sample_interval_min");
				minSampleInterval = (rs.wasNull()) ? 0 : minSampleInterval;
				//create DTO
				result.add(new ConnectorDTO(rs.getInt("number"),rs.getString("datapoint_name"), rs.getString("device_name"), 
						rs.getString("connection_type"), rs.getString("connection_variables"), 
						rs.getBoolean("writeable"), rs.getString("vendor"), rs.getString("model"), rs.getString("unit"), 
						rs.getString("type"), min, max, deadband, sampleInterval, minSampleInterval ));
			}
		} catch (SQLException e) {
			LOG.error("An SQL exception occured", e);
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
	 * 
	 * @param user
	 * @return all defined connections were the user has any permissions
	 */
	public List<ConnectorDTO> getConnection(User user) {
		//TODO implement:
		//A get all head zone
		//B get all connections within these headzones
		//C remove double entries
		//currently, just return all connections
		return getConnection();
	}
	
	/**
	 * @return connections of specific type (opc-da, jdbc, etc.)
	 * TODO implement 
	 */
	public List<ConnectorDTO> getConnection(User user, String connectionType) {
		return null;
		
	}
	
	
	//Singleton
	private ConnectorController() {
		super();
	}
	public static ConnectorController getInstance(){
		if (ref == null) {
			ref = new ConnectorController();
		}
		return ref;
	}
}
