package bpi.most.server.model;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ServiceLoader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bpi.most.server.utils.DbPool;
import bpi.most.dto.DpDTO;

/**
 * Handles instantiation of requested data points. Caches data points for
 * performance optimization.
 * 
 * @author robert.zach@tuwien.ac.at
 */
public final class DpController {

	private static final Logger LOG = LoggerFactory.getLogger(DpController.class);
	
	private static DpController ref = null;
	ArrayList<Datapoint> datapoints = new ArrayList<Datapoint>();

	private DpController() {
		super();
	}

	public static DpController getInstance() {
		if (ref == null) {
			ref = new DpController();
		}
		return ref;
	}

	/**
	 * Searches for instance of a data point. TODO: keep it small --> only for
	 * currently active zones
	 * 
	 * @param datapointname
	 *            The name of the requested data point.
	 * @return Returns the instance of the data point wit the given name.
	 */
	private Datapoint lookupDatapoint(String datapointname) {
		for (int i = 0; i < datapoints.size(); i++) {
			String datapointnametemp = datapoints.get(i).getDatapointName();
			if (datapointnametemp.equalsIgnoreCase(datapointname)) {
				return datapoints.get(i);
			}
		}
		return null;
	}

	/**
	 * A) Searches for data point in the object cache. <br> B) Instantiates respective
	 * physical or virtual data point.
	 * 
	 * @param dp
	 *            The {@link DpDTO}.
	 * 
	 * @return Returns an instance of a "physical" (DpServer) or "virtual"
	 *          (DpVirtual) data point. Null if not valid.
	 * @see {@link #getDatapoint(String dpName)}
	 */
	public Datapoint getDatapoint(DpDTO dp) {
		return getDatapoint(dp.getName());
	}

	/**
	 * A) Searches for data point in the object cache. <br> B) Instantiates respective
	 * physical or virtual data point.
	 * 
	 * @param dpName
	 *            The name of the data point.
	 * 
	 * @return Returns an instance of a "physical" (DpServer) or "virtual"
	 *          (DpVirtual) data point. Null if not valid.
	 * @see {@link #getDatapoint(DpDTO)}
	 * */
	public Datapoint getDatapoint(String dpName) {
		Datapoint requestedDp = null;
		// ### A - check datapoint object cache
		requestedDp = lookupDatapoint(dpName);
		if (requestedDp != null) {
			return requestedDp;
		} else {
			// check if valid dp (defined in DB)
			if (!isValidDp(dpName)) {
				// no valid datapoint
				return null;
			}
			// ### B
			// valid dp --> check if physical or virtual
			requestedDp = new DpPhysical(dpName);
			if (! requestedDp.isVirtual()){
				//physical dp, add the dp to the local cache and return
				datapoints.add(requestedDp);
				return requestedDp;
			} else {
				// virtual dp --> use service loader to get instance
				ServiceLoader<DpVirtualFactory> virtualDpLoader = ServiceLoader
						.load(DpVirtualFactory.class);
				String virtualDpID = requestedDp.getVirtual();
				// loop through all DpVirtualFactory implementations
				for (DpVirtualFactory virtualDp : virtualDpLoader) {
					requestedDp = virtualDp.getVirtualDp(virtualDpID, dpName);
					if (requestedDp != null) {
						// set the name of the returned dp, since it may not set
						// yet
						requestedDp.setDatapointName(dpName);
						// add the dp to the local dp cache
						datapoints.add(requestedDp);
						return requestedDp;
					}
				}
				// no matching virtual dp found
				return null;
			}
		}
	}

	/**
	 * Checks if a data point with the given name is in the database.
	 * 
	 * @param dpName
	 *            The name of the data point you are looking for.
	 * @return Returns true if a data point with the given name exists, false
	 *         otherwise.
	 */
	public boolean isValidDp(String dpName) {
		boolean result = false;
		Connection connection = null;
		CallableStatement cstmt = null;
		ResultSet rs = null;

		try {
			connection = DbPool.getInstance().getConnection();
			cstmt = connection.prepareCall("{CALL getDatapoint(?)}");
			cstmt.setString(1, dpName);
			cstmt.execute();
			rs = cstmt.getResultSet();
			if (rs.first()) {
				// DatapointVO exists
				result = true;
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
	 * Get a list of {@link DpDTO} of all data points in the database.
	 * 
	 * @return Returns a list of {@link DpDTO} of all data points in the
	 *         database.
	 */
	public ArrayList<DpDTO> getDatapoints() {
		ArrayList<DpDTO> liste = new ArrayList<DpDTO>();

		Connection connection = null;
		CallableStatement cstmt = null;
		ResultSet rs = null;

		try {
			LOG.info("Try getSensors");
			connection = DbPool.getInstance().getConnection();
			cstmt = connection.prepareCall("{CALL getDatapoint(?)}");
			cstmt.setString(1, null);
			cstmt.execute();
			rs = cstmt.getResultSet();
			while (rs.next()) {
				liste.add(new DpDTO(rs.getString("datapoint_name"), rs
						.getString("type"), rs.getString("datapoint_name")));
			}
			LOG.info("Success getSensors");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
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

		return liste;

	}

	/**
	 * Get a list of {@link DpDTO} of all data points in the database that
	 * contains the search string.
	 * 
	 * @param searchstring
	 *            The string to be searched for.
	 * @return Returns a list of {@link DpDTO} of all data points in the
	 *         database that contains the search string.
	 */
	public ArrayList<DpDTO> getDatapoints(String searchstring) {
		ArrayList<DpDTO> liste = new ArrayList<DpDTO>();

		Connection connection = null;
		CallableStatement cstmt = null;
		ResultSet rs = null;

		try {
			LOG.info("Try getSensors");
			connection = DbPool.getInstance().getConnection();
			cstmt = connection.prepareCall("{CALL getDatapoint(?)}");
			cstmt.setString(1, "%" + searchstring + "%");
			cstmt.execute();
			rs = cstmt.getResultSet();
			while (rs.next()) {
				liste.add(new DpDTO(rs.getString("datapoint_name"), rs
						.getString("type"), rs.getString("datapoint_name")));
			}
			LOG.info("Success getSensors");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
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

		return liste;
	}

	/**
	 * Get a list of {@link DpDTO} of all data points in the database that
	 * contains the search string and are in the given zone.
	 * 
	 * @deprecated Maybe replaced in the future with a {@link ZoneDTO} instead
	 *             of a string.
	 * @param searchstring
	 *            The string to be searched for.
	 * @param zone
	 *            The zone in which you want to search.
	 * @return Returns a list of {@link DpDTO} of all data points in the
	 *         database that contains the search string and are in the given
	 *         zone.
	 */
	public ArrayList<DpDTO> getDatapoints(String searchstring, String zone) {
		ArrayList<DpDTO> liste = new ArrayList<DpDTO>();

		Connection connection = null;
		CallableStatement cstmt = null;
		ResultSet rs = null;

		try {
			LOG.info("Try getSensors");
			connection = DbPool.getInstance().getConnection();
			cstmt = connection.prepareCall("{CALL getDatapoint(?)}");
			cstmt.setString(1, "%" + searchstring + "%");
			cstmt.execute();
			rs = cstmt.getResultSet();
			while (rs.next()) {
				// TODO not yet implementet in the way we want it to use; as for
				// now this is an example
				// TODO: compare Zone objects!! not String with zone id
				if (rs.getString("zone_idzone").contentEquals(zone)) {
					liste.add(new DpDTO(rs.getString("datapoint_name"), rs
							.getString("type"), rs.getString("datapoint_name")));
				}
			}
			LOG.info("Success getSensors");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
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
		return liste;
	}

}
