// TODO weix: refactor

package bpi.most.service.impl;

import bpi.most.domain.user.User;
import bpi.most.domain.zone.Zone;
import bpi.most.service.api.ZoneService;
import bpi.most.service.impl.db.DbPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of {@link bpi.most.service.api.ZoneService}.
 *
 * @author Lukas Weichselbaum
 */
@Service
public class ZoneServiceImpl implements ZoneService {

    private static final Logger log = LoggerFactory.getLogger(ZoneServiceImpl.class);

    @PersistenceContext(unitName = "most")
    private EntityManager em;

    @Inject
    private DbPool dbPool;

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
     * @param zone Zone object
     * @return returns the respective Zone object, zoneID is used as identifier
     */
    @Override
    public Zone getZone(Zone zone) {
        return getZone(zone.getZoneId());
    }

    /**
     * TODO: May change type of zoneID to support IfcGloballyUniqueId IDs of IFC
     * @param zoneId Unique ID of the zone
     * @return Requested Zone, null if not valid
     */
    @Override
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
                connection = dbPool.getDataSource().getConnection();
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
    @Override
    public List<Zone> getZone(String searchPattern) {
        List<Zone> result = new ArrayList<Zone>();

        //get zone info from db
        Connection connection = null;
        CallableStatement cstmt = null;
        ResultSet rs = null;

        try {
            connection = dbPool.getDataSource().getConnection();
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
    @Override
    public List<Zone> getHeadZones() {
        List<Zone> result = new ArrayList<Zone>();

        //get zone info from db
        Connection connection = null;
        CallableStatement cstmt = null;
        ResultSet rs = null;

        try {
            connection = dbPool.getDataSource().getConnection();
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
    @Override
    public List<Zone> getHeadZones(User user) {
        List<Zone> result = new ArrayList<Zone>();

        //get zone info from db
        Connection connection = null;
        CallableStatement cstmt = null;
        ResultSet rs = null;

        try {
            connection = dbPool.getDataSource().getConnection();
            cstmt = connection.prepareCall("{CALL getHeadzones(?)}");
            cstmt.setString(1, user.getName());
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

}
