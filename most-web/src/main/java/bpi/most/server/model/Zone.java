package bpi.most.server.model;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bpi.most.domain.connector.ConnectorFinder;
import bpi.most.server.utils.DbPool;
import bpi.most.shared.ZoneDTO;

/**
 * DAO of a zone used for grouping datapoints.
 * 
 * Currently, independent zone grouping is implemented using the MOST-DB.
 * Therefore, a Datapoint can only be linked to ONE zone. Zones can contain any
 * number of subzones. TODO: implement zone DAO for an IFC file. Handle IfcZone,
 * IfcSpatialZone, IfcSpace, etc. as zone. Use GlobalId as identifier.
 * 
 * @author robert.zach@tuwien.ac.at
 */
public class Zone {
	
    private static final Logger LOG = LoggerFactory.getLogger(Zone.class);
	
	int zoneId; // unique - pk

	/**
	 * constructors
	 */
	public Zone(int zoneId) {
		super();
		this.zoneId = zoneId;
	}

	/**
	 * @return a ZoneEntity (serializable) object of the respective Zone. null
	 *         if not found.
	 */
	public ZoneDTO getZoneDTO() {
		// get zone info from db
		Connection connection = null;
		CallableStatement cstmt = null;
		ResultSet rs = null;

		try {
			connection = DbPool.getInstance().getConnection();
			cstmt = connection.prepareCall("{CALL getZoneParameters(?)}");
			cstmt.setInt(1, getZoneId());
			cstmt.execute();
			rs = cstmt.getResultSet();
			// check if valid
			if (rs.next()) {
				// return ZoneEntity
				return new ZoneDTO(rs.getInt("idzone"), rs.getString("name"),
						rs.getString("description"), rs.getString("country"),
						rs.getString("state"), rs.getString("county"),
						rs.getString("city"), rs.getString("building"),
						rs.getString("floor"), rs.getString("room"),
						rs.getDouble("area"), rs.getDouble("volume"));
			}
		} catch (SQLException e) {
			LOG.error("An exception occured while calling stored procedure 'getZoneParameters'", e);
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
				LOG.error("An exception occured while calling 'close'", e);
			}
		}
		return null;
	}

	/**
	 * TODO: prevent loops, done in the db? Returns subzones which are contained
	 * in a given number of sublevels of a zone
	 * 
	 * @param sublevels
	 *            level of returned sublevels sublevels = 0 --> 0 subzones -->
	 *            empty result sublevels = 1 --> return "next level" of subzones
	 *            sublevels = 2 --> return "two levels" of subzones ...
	 *            sublevels = null --> return "all level" of subzones
	 * @return List of subzones.
	 */
	public List<Zone> getSubzones(int sublevels) {
		// TODO: use better fitting List type
		List<Zone> result = new ArrayList<Zone>();

		// get zone info from db
		Connection connection = null;
		CallableStatement cstmt = null;
		ResultSet rs = null;

		try {
			connection = DbPool.getInstance().getConnection();
			cstmt = connection.prepareCall("{CALL getSubzones(?,?)}");
			cstmt.setInt(1, getZoneId());
			cstmt.setInt(2, sublevels);
			cstmt.execute();
			LOG.info(cstmt.toString());
			rs = cstmt.getResultSet();
			// add all subzones to resultset
			while (rs.next()) {
				ZoneController zoneCtrl = ZoneController.getInstance();
				result.add(zoneCtrl.getZone(rs.getInt("zone")));
				// TODO: zoneCtrl.getZone calls DB, prevent this double calls by
				// instatiating zones here
				// //check if zone is cached
				// Zone cachedZone =
				// zoneCtrl.lookupZoneInCache(rs.getInt("idzone"));
				// if ( cachedZone != null ) {
				// result.add(cachedZone);
				// }else {
				// //create zone
				// cachedZone = new Zone(rs.getInt("idzone"));
				// cachedZones.add(cachedZone);
				// result.add(cachedZone);
				// }

			}
		} catch (SQLException e) {
			LOG.error("An exception occured while calling stored procedure 'getSubzones'", e);
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
	 * not implemented yet Links a zone as a subzone of this zone
	 * 
	 * @param subzone
	 *            which should be linked to this zone
	 * @return
	 */
	public boolean addSubzone(Zone subzone) {
		return false;
	}

	/**
	 * Returns Datapoints which are contained in a given number of sublevels of
	 * a zone
	 * 
	 * @param sublevels
	 *            level of returned datapoints sublevels = 0 --> return
	 *            datapoints of this zone sublevels = 1 --> return datapoints of
	 *            this and "next level" zone ...
	 * @return List of {@link Datapoint} that are in the zone/subzones
	 */
	public List<Datapoint> getDatapoints(int sublevel) {
		// TODO: use better fitting List type
		List<Datapoint> result = new ArrayList<Datapoint>();

		// get zone info from db
		Connection connection = null;
		CallableStatement cstmt = null;
		ResultSet rs = null;

		try {
			connection = DbPool.getInstance().getConnection();
			cstmt = connection
					.prepareCall("{CALL getDatapointsInSubzones(?,?)}");
			cstmt.setInt(1, getZoneId());
			cstmt.setInt(2, sublevel);
			cstmt.execute();
			LOG.info(cstmt.toString());
			rs = cstmt.getResultSet();
			// add all datapoints to resultset
			while (rs.next()) {
				DpController dpCtrl = DpController.getInstance();
				Datapoint requestedDp = dpCtrl.getDatapoint(rs
						.getString("datapoint_name"));
				// check if valid
				if (requestedDp != null) {
					result.add(requestedDp);
				}

			}
		} catch (SQLException e) {
			LOG.error("An exception occured while calling stored procedure 'getDatapointsInSubzones'", e);
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
	 * @return List of {@link Datapoint} with the requested type that are in the
	 *         zone or subzone
	 */
	public List<Datapoint> getDatapoints(int sublevel, String dpType) {
		List<Datapoint> result = new ArrayList<Datapoint>();
		for (Datapoint datapoint : getDatapoints(sublevel)) {
			if (datapoint.getType() != null && datapoint.getType().equals(dpType)) {
				result.add(datapoint);
			}
		}
		return result;
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
			cstmt = connection.prepareCall("{CALL getZoneParameters(?)}");
			cstmt.setInt(1, getZoneId());
			cstmt.execute();
			rs = cstmt.getResultSet();
			// check if valid
			if (rs.next()) {
				columnContent = rs.getDouble(requestedColumn);
			}
		} catch (SQLException e) {
			LOG.error("An exception occured while calling stored procedure 'getZoneParameters'", e);
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
	 * @return String of requested column
	 */
	protected String getColumnString(String requestedColumn) {
		String columnContent = null;
		Connection connection = null;
		CallableStatement cstmt = null;
		ResultSet rs = null;

		try {
			connection = DbPool.getInstance().getConnection();
			cstmt = connection.prepareCall("{CALL getZoneParameters(?)}");
			cstmt.setInt(1, zoneId);
			cstmt.execute();
			rs = cstmt.getResultSet();
			if (rs.next()) {
				columnContent = rs.getString(requestedColumn);
			}
		} catch (SQLException e) {
			LOG.error("An exception occured while calling stored procedure 'getZoneParameters'", e);
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

	public int getZoneId() {
		return zoneId;
	}

	public String getName() {
		return getColumnString("name");
	}

	public String getDescription() {
		return getColumnString("description");
	}

	public String getCountry() {
		return getColumnString("country");
	}

	public String getState() {
		return getColumnString("state");
	}

	public String getCounty() {
		return getColumnString("county");
	}

	public String getCity() {
		return getColumnString("city");
	}

	public String getBuilding() {
		return getColumnString("building");
	}

	public String getFloor() {
		return getColumnString("floor");
	}

	public String getRoom() {
		return getColumnString("room");
	}

	public Double getArea() {
		return getColumnDouble("area");
	}

	public Double getVolume() {
		return getColumnDouble("volume");
	}

}
