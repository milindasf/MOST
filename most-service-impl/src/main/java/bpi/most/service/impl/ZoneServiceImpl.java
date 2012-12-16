// TODO weix: refactor

package bpi.most.service.impl;

import bpi.most.domain.user.User;
import bpi.most.domain.zone.Zone;
import bpi.most.service.api.ZoneService;
import bpi.most.service.impl.db.DbPool;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
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
import java.util.LinkedHashMap;
import java.util.Map;


import static bpi.most.service.impl.utils.DbUtils.prepareSearchParameter;

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

    private int cacheSize = 100;
    private LinkedHashMap<Integer, Zone> cachedZones = new LinkedHashMap<Integer, Zone>(cacheSize, 0.7f, true) {
        @Override
        protected boolean removeEldestEntry(Map.Entry<Integer, Zone> eldest) {
            return size() > cacheSize;
        }
    };


    /**
     * searches for instance of zone in cache
     * @return return Zone if cached, null if emtpy
     */
    public Zone lookupZoneInCache(int zoneId) {
        return cachedZones.get(zoneId);
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

            log.debug("Fetching zones with id {}", zoneId);
            // noinspection unchecked
            try{
                List<Zone> zoneList = ((Session) em.getDelegate()).createSQLQuery("{CALL getZoneParameters(:zoneId)}")
                        .setParameter("zoneId", zoneId)
                        .setReadOnly(true)
                        .setResultTransformer(Transformers.aliasToBean(Zone.class))
                        .list();

                if(zoneList.size() == 0){
                    return null;
                }else{
                    result = zoneList.get(0);
                    cachedZones.put(result.getZoneId(), result);
                    return result;
                }
            }catch(HibernateException e){
                return null;
            }
        }
    }

    /**
     * Search for a zone using a String search pattern
     * @param searchPattern String search pattern
     * @return List of Zones matching the searchPattern, null if empty
     */
    @Override
    public List<Zone> getZone(String searchPattern) {
        //get zone info from db
        log.debug("Fetching zones with searchPattern {}", searchPattern);
        try{
            List<Zone> zoneList = ((Session) em.getDelegate()).createSQLQuery("{CALL getZoneParametersSearch(:p,:searchPattern)}")
                    .setParameter("p", null)
                    .setParameter("searchPattern", searchPattern)
                    .setReadOnly(true)
                    .setResultTransformer(Transformers.aliasToBean(Zone.class))
                    .list();

            if(zoneList.size() == 0){
                return null;
            }else{
                for (Zone zone : zoneList) {
                    cachedZones.put(zone.getZoneId(), zone);
                }
                return zoneList;
            }
        }catch(HibernateException e){
            return null;
        }
    }

    /**
     * @return A List of all Zones which have no parent ("highest" Zones).
     */
    @Override
    public List<Zone> getHeadZones() {
        return getHeadZones(null);
    }

    /**
     * @param user
     * @return A List of all highest Zones were the user still has any permissions (Zone with no parents were the user has any permissions).
     */
    @Override
    public List<Zone> getHeadZones(User user) {
        List<Zone> zoneList = new ArrayList<Zone>();
        String username = null;

        if(user != null){
            username = user.getName();
        }

        //get zone info from db
        log.debug("Fetching zones with user {}", username);
        try{
            // The stored procedure returns a list of all zones ids that have no superzones
            // The stored procedure returns the highest zones for that the given user (a list of Integers).
            List<Integer> zoneIDs = ((Session) em.getDelegate()).createSQLQuery("{CALL getHeadzones(:user)}")
                    .setParameter("user", username)
                    .setReadOnly(true)
                    .list();

            // Retrieve the corresponding zone for each zoneId in the result
            for(Integer zoneId:zoneIDs){
                Zone zone = getZone(zoneId);
                if(zone != null){
                    zoneList.add(zone);
                }
            }

            if(zoneList.size() != 0){
                for (Zone zone : zoneList) {
                    cachedZones.put(zone.getZoneId(), zone);
                }
            }

            return zoneList;

        }catch(HibernateException e){
            return new ArrayList<Zone>();
        }
    }

}
