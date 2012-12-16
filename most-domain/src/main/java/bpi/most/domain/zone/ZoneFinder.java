package bpi.most.domain.zone;

import bpi.most.domain.user.User;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

/**
 * Finds {@link Zone} domain objects from a given {@link javax.persistence.EntityManager}.
 *
 * @author Lukas Weichselbaum
 */
public class ZoneFinder {

    private static final Logger log = LoggerFactory.getLogger(ZoneFinder.class);

    private final EntityManager em;

    public ZoneFinder(EntityManager em) {
        this.em = em;
    }

    /**
     * TODO: May change type of zoneID to support IfcGloballyUniqueId IDs of IFC
     * @param zoneId Unique ID of the zone
     * @return Requested Zone, null if not valid
     */
    public Zone getZone(int zoneId) {
        //get zone info from db

        log.debug("Fetching zones with id {}", zoneId);
        try{
            // noinspection unchecked
            List<Zone> zoneList = ((Session) em.getDelegate()).createSQLQuery("{CALL getZoneParameters(:zoneId)}")
                    .addEntity(Zone.class)
                    .setParameter("zoneId", zoneId)
                    .setReadOnly(true)
                    .list();

            if(zoneList.size() == 0){
                return null;
            }else{
                return zoneList.get(0);
            }
        }catch(HibernateException e){
            log.debug(e.getStackTrace().toString());
            return null;
        }
    }

    /**
     * Search for a zone using a String search pattern
     * @param searchPattern String search pattern
     * @return List of Zones matching the searchPattern, null if empty
     */
    public List<Zone> getZone(String searchPattern) {
        //get zone info from db
        log.debug("Fetching zones with searchPattern {}", searchPattern);
        try{
            // noinspection unchecked
            List<Zone> zoneList = ((Session) em.getDelegate()).createSQLQuery("{CALL getZoneParametersSearch(:p,:searchPattern)}")
                    .addEntity(Zone.class)
                    .setParameter("p", null)
                    .setParameter("searchPattern", searchPattern)
                    .setReadOnly(true)
                    .list();

            if(zoneList.size() == 0){
                return null;
            }else{
                return zoneList;
            }
        }catch(HibernateException e){
            log.debug(e.getStackTrace().toString());
            return null;
        }
    }

    /**
     * @param user
     * @return A List of all highest Zones were the user still has any permissions (Zone with no parents were the user has any permissions).
     */
    public List<Integer> getHeadZoneIds(User user) {
        String username = null;

        if(user != null){
            username = user.getName();
        }

        //get zone info from db
        log.debug("Fetching zones with user {}", username);
        try{
            // If user is null the stored procedure returns a list of all zones ids that have no superzones
            // otherwise the stored procedure returns the highest zones for that the given user (a list of Integers).
            // noinspection unchecked
            List<Integer> zoneIDs = ((Session) em.getDelegate()).createSQLQuery("{CALL getHeadzones(:user)}")
                    .setParameter("user", username)
                    .setReadOnly(true)
                    .list();

            return zoneIDs;

        }catch(HibernateException e){
            log.debug(e.getStackTrace().toString());
            return new ArrayList<Integer>();
        }
    }

}
