package bpi.most.service.impl;

import bpi.most.domain.datapoint.DatapointFinder;
import bpi.most.domain.datapoint.DatapointVO;
import bpi.most.domain.user.User;
import bpi.most.domain.zone.Zone;
import bpi.most.domain.zone.ZoneFinder;
import bpi.most.dto.DpDTO;
import bpi.most.dto.UserDTO;
import bpi.most.dto.ZoneDTO;
import bpi.most.service.api.ZoneService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;

/**
 * Implementation of {@link bpi.most.service.api.ZoneService}.
 *
 * @author Lukas Weichselbaum
 */
@Service
public class ZoneServiceImpl implements ZoneService {

    private static final int CACHE_SIZE = 100;
    private static final float LOAD_FACTOR = 0.7f;

    @PersistenceContext(unitName = "most")
    private EntityManager em;

    private ZoneFinder zoneFinder;
    private DatapointFinder datapointFinder;

    @PostConstruct
    protected void init() {
        zoneFinder = new ZoneFinder(em);
        datapointFinder = new DatapointFinder(em);
    }

    private int cacheSize = CACHE_SIZE;
    private Map<Integer, Zone> cachedZones = new LinkedHashMap<Integer, Zone>(cacheSize, LOAD_FACTOR, true) {
        @Override
        protected boolean removeEldestEntry(Map.Entry<Integer, Zone> eldest) {
            return size() > cacheSize;
        }
    };

    /**
     * reset the cache
     */
    @Override
    public void resetCache() {
        cachedZones.clear();
    }

    /**
     * searches for instance of zone in cache
     *
     * @return return Zone if cached, null if emtpy
     */
    Zone lookupZoneInCache(int zoneId) {
        return cachedZones.get(zoneId);
    }

    /**
     * add zone object to the cache
     *
     * @param zone Zone object which should be cached
     */
    private void cacheZone(Zone zone) {
        if (zone != null) {
            cachedZones.put(zone.getZoneId(), zone);
        }
    }

    /**
     * @param zone Zone object
     * @return returns the respective Zone object, zoneID is used as identifier
     */
    @Override
    @Transactional
    public Zone getZone(ZoneDTO zone) {
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
    @Transactional
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
     * returns a single {@link ZoneDTO} which is identified by the given
     * {@link ZoneDTO#getName()}. this is used so that clients can fetch a
     * fully filled DpDTO object by only having the datapoints name.
     *
     * @param user
     * @param zoneDto
     * @return
     */
    @Override
    @Transactional
    public ZoneDTO getZone(UserDTO user, ZoneDTO zoneDto) {
        ZoneDTO result = null;
        Zone requestedZone = zoneFinder.getZone(zoneDto.getZoneId());
        //TODO move permission definition
        if (requestedZone != null) {
            user.hasPermission(requestedZone, DpDTO.Permissions.READ);
            result = requestedZone.getDTO();
        }

        return result;
    }

    /**
     * Search for a zone using a String search pattern
     *
     * @param searchPattern String search pattern
     * @return List of Zones matching the searchPattern, null if empty
     */
    @Override
    @Transactional
    public List<ZoneDTO> getZone(String searchPattern) {
        List<ZoneDTO> resultDTOs = null;
        List<Zone> results = zoneFinder.getZone(searchPattern);
        if (results != null) {
            resultDTOs = new ArrayList<ZoneDTO>();
            for (Zone result : results) {
                cacheZone(result);
                resultDTOs.add(result.getDTO());
            }
        }
        return resultDTOs;
    }

    /**
     * @return A List of all Zones which have no parent ("highest" Zones).
     */
    @Override
    @Transactional
    public List<ZoneDTO> getHeadZones() {
        return getHeadZones(null);
    }

    /**
     * @param userDTO
     * @return A List of all highest Zones were the user still has any permissions (Zone with no parents were the user has any permissions).
     */
    @Override
    @Transactional
    public List<ZoneDTO> getHeadZones(UserDTO userDTO) {
        User user = null;

        if (userDTO != null) {
            user = new User();
            user.setName(userDTO.getUserName());
        }
        List<ZoneDTO> resultDTOs = new ArrayList<ZoneDTO>();
        List<Integer> results = zoneFinder.getHeadZoneIds(user);

        // Retrieve the corresponding zone for each zoneId in the result
        for (Integer zoneId : results) {
            resultDTOs.add(getZone(zoneId).getDTO());
        }

        return resultDTOs;
    }


    /**
     * adds permission check
     *
     * @return A List of ZoneDTOs of requested subzones
     */
    @Override
    @Transactional
    public List<ZoneDTO> getSubzones(UserDTO user, ZoneDTO zoneEntity, int sublevels) {
        List<ZoneDTO> result = new ArrayList<ZoneDTO>();
        Zone requestedZone = zoneFinder.getZone(zoneEntity.getZoneId());

        //TODO move permission definition
        user.hasPermission(requestedZone, DpDTO.Permissions.READ);

        List<Integer> subzoneIds = zoneFinder.getSubZoneIds(zoneEntity.getZoneId(), sublevels);
        if (subzoneIds != null) {
            //convert to DTOs
            for (Integer subzoneId : subzoneIds) {
                result.add(getZone(subzoneId).getDTO());
            }
        }
        return result;
    }

    /**
     */
    @Override
    @Transactional
    public List<DpDTO> getDatapoints(UserDTO user, ZoneDTO zoneEntity, int sublevels) {
        List<DpDTO> result = new ArrayList<DpDTO>();
        Zone requestedZone = zoneFinder.getZone(zoneEntity.getZoneId());
        //TODO move permission definition
        user.hasPermission(requestedZone, DpDTO.Permissions.READ);

        List<DatapointVO> datapoints = datapointFinder.getDpFromSubZones(zoneEntity.getZoneId(), sublevels);
        if (datapoints != null) {
            //convert to DTOs
            for (DatapointVO iterateDp : datapoints) {
                result.add(iterateDp.getDTO());
            }
        }
        return result;
    }

}
