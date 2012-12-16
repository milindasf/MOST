package bpi.most.service.impl;

import bpi.most.domain.user.User;
import bpi.most.domain.zone.Zone;
import bpi.most.domain.zone.ZoneFinder;
import bpi.most.service.api.ZoneService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

    private ZoneFinder zoneFinder;

    @PostConstruct
    protected void init() {
        zoneFinder = new ZoneFinder(em);
    }

    private int cacheSize = 100;
    private LinkedHashMap<Integer, Zone> cachedZones = new LinkedHashMap<Integer, Zone>(cacheSize, 0.7f, true) {
        @Override
        protected boolean removeEldestEntry(Map.Entry<Integer, Zone> eldest) {
            return size() > cacheSize;
        }
    };

    /**
     * reset the cache
     */
    void resetCache() {
        cachedZones.clear();
    }

    /**
     * searches for instance of zone in cache
     * @return return Zone if cached, null if emtpy
     */
    public Zone lookupZoneInCache(int zoneId) {
        return cachedZones.get(zoneId);
    }

    /**
     * add zone object to the cache
     * @param zone Zone object which should be cached
     */
    private void cacheZone(Zone zone){
        if(zone != null){
            cachedZones.put(zone.getZoneId(), zone);
        }
    }

    /**
     * @param zone Zone object
     * @return returns the respective Zone object, zoneID is used as identifier
     */
    @Override
    public Zone getZone(Zone zone) {
        Zone result = lookupZoneInCache(zone.getZoneId());
        if (result != null) {
            return result;
        } else {
            result = zoneFinder.getZone(zone.getZoneId());
            cacheZone(result);
            return result;
        }
    }

    /**
     * @param zoneId Unique ID of the zone
     * @return Requested Zone, null if not valid
     */
    @Override
    public Zone getZone(int zoneId) {
        Zone result = lookupZoneInCache(zoneId);
        if (result != null) {
            return result;
        } else {
            result = zoneFinder.getZone(zoneId);
            cacheZone(result);
            return result;
        }
    }

    /**
     * Search for a zone using a String search pattern
     * @param searchPattern String search pattern
     * @return List of Zones matching the searchPattern, null if empty
     */
    @Override
    public List<Zone> getZone(String searchPattern) {
        List<Zone> results = zoneFinder.getZone(searchPattern);
        if(results != null){
            for (Zone result : results) {
                cacheZone(result);
            }
        }
        return results;
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
        List<Zone> zones = new ArrayList<Zone>();
        List<Integer> results = zoneFinder.getHeadZoneIds(user);

        // Retrieve the corresponding zone for each zoneId in the result
        for(Integer zoneId:results){
            Zone zone = getZone(zoneId);
            if(zone != null){
                zones.add(zone);
                cachedZones.put(zone.getZoneId(), zone);
            }
        }

        return zones;
    }
}
