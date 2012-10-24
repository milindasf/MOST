/**
 * 
 */
package bpi.most.server.model;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bpi.most.server.services.User;
import bpi.most.server.utils.DbPool;
import bpi.most.shared.ZoneDTO;


/**
 * @author robert.zach@tuwien.ac.at
 */
public class ZoneController {

	private static ZoneController ref;
	List<Zone> cachedZones = new ArrayList<Zone>();

	/**
	 * searches for instance of zone in cache
	 * TODO: keep it small --> only for currently active zones
	 * @return return Zone if cached, null if emtpy
	 */
	public Zone lookupZoneInCache(int zoneId) {
		//TODO: implement searchable datastructure
		for (int i = 0; i < cachedZones.size(); i++) {
			if (cachedZones.get(i).getZoneId() == zoneId) {
				return cachedZones.get(i);
			}
		}
		return null;
	}

	/**
	 * @param zone ZoneDTO object
	 * @return returns the respective Zone object, zoneID is used as identifier
	 */
	public Zone getZone(ZoneDTO zone) {
		return getZone(zone.getZoneId());
	}
	
	/**
	 * TODO: May change type of zoneID to support IfcGloballyUniqueId IDs of IFC 
	 * @param zoneID Unique ID of the zone
	 * @return Requested Zone, null if not valid
	 */
	public Zone getZone(int zoneId) {
		Zone result = lookupZoneInCache(zoneId);
		if (result != null) {
			return result;
		} else {
			//get zone info from db
			Connection connection = null;
			CallableStatement cstmt = null;
			ResultSet rs = null;
			
			try {
				connection = DbPool.getInstance().getConnection();
				cstmt = connection.prepareCall("{CALL getZoneParameters(?)}");
				cstmt.setInt(1, zoneId);
				cstmt.execute();
				rs = cstmt.getResultSet();
				//check if valid
				if (rs.next()) {
					//create zone, add to cache and return
					result = new Zone(rs.getInt("idzone"));
					cachedZones.add(result);
					return result;
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
		return null;
	}
	
	/**
	 * Search for a zone using a String search pattern
	 * @param searchPattern String search pattern
	 * @return List of Zones matching the searchPattern, null if empty
	 */
	public List<Zone> getZone(String searchPattern) {
		List<Zone> result = new ArrayList<Zone>(); 
		
		//get zone info from db
		Connection connection = null;
		CallableStatement cstmt = null;
		ResultSet rs = null;
		
		try {
			connection = DbPool.getInstance().getConnection();
			cstmt = connection.prepareCall("{CALL getZoneParametersSearch(?,?)}");
			//set first parameter to NULL --> search all parameters
			cstmt.setString(1, "NULL");
			cstmt.setString(2, searchPattern);
			cstmt.execute();
			rs = cstmt.getResultSet();
			//add all zones to resultset
			while (rs.next()) {
				//check if zone is cached
				Zone cachedZone = lookupZoneInCache(rs.getInt("idzone"));
				if ( cachedZone != null ) {
					result.add(cachedZone);
				}else {
					//create zone, add to cache and return
					cachedZone = new Zone(rs.getInt("idzone"));
					cachedZones.add(cachedZone);
					result.add(cachedZone);
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
		
		return result;
	}
	
	/**
	 * @return A List of all Zones which have no parent ("highest" Zones). 
	 */
	public List<Zone> getHeadZones() {
		List<Zone> result = new ArrayList<Zone>(); 
		
		//get zone info from db
		Connection connection = null;
		CallableStatement cstmt = null;
		ResultSet rs = null;
		
		try {
			connection = DbPool.getInstance().getConnection();
			cstmt = connection.prepareCall("{CALL getHeadzones(?)}");
			cstmt.setString(1, null);
			cstmt.execute();
			rs = cstmt.getResultSet();
			//add all zones to resultset
			while (rs.next()) {
				//check if zone is cached
				Zone cachedZone = lookupZoneInCache(rs.getInt(1));
				if ( cachedZone != null ) {
					result.add(cachedZone);
				}else {
					//create zone, add to cache and return
					cachedZone = new Zone(rs.getInt(1));
					cachedZones.add(cachedZone);
					result.add(cachedZone);
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
		return result;
	}
	
	/**
	 * @param user
	 * @return A List of all highest Zones were the user still has any permissions (Zone with no parents were the user has any permissions). 
	 */
	public List<Zone> getHeadZones(User user) {
		List<Zone> result = new ArrayList<Zone>(); 
		
		//get zone info from db
		Connection connection = null;
		CallableStatement cstmt = null;
		ResultSet rs = null;
		
		try {
			connection = DbPool.getInstance().getConnection();
			cstmt = connection.prepareCall("{CALL getHeadzones(?)}");
			cstmt.setString(1, user.getUserName());
			cstmt.execute();
			System.out.println(cstmt.toString());
			rs = cstmt.getResultSet();
			//add all zones to resultset
			while (rs.next()) {
				//check if zone is cached
				Zone cachedZone = lookupZoneInCache(rs.getInt(1));
				if ( cachedZone != null ) {
					result.add(cachedZone);
				}else {
					//create zone, add to cache and return
					cachedZone = new Zone(rs.getInt(1));
					cachedZones.add(cachedZone);
					result.add(cachedZone);
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
		return result;
	}
	
	/**
	 * not implemented yet
	 * Adds a new zone to the MOST-DB (BIM)
	 * @param newZone
	 * @return
	 */
	public boolean addZone(Zone newZone) {
		return false;
	}
	
	
	// Singleton
	private ZoneController() {
		super();
	}
	public static ZoneController getInstance() {
		if (ref == null) {
			ref = new ZoneController();
		}
		return ref;
	}
	
}
