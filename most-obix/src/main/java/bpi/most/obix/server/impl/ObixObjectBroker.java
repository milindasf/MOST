package bpi.most.obix.server.impl;

import bpi.most.dto.DpDTO;
import bpi.most.dto.DpDataDTO;
import bpi.most.dto.UserDTO;
import bpi.most.dto.ZoneDTO;
import bpi.most.obix.objects.*;
import bpi.most.obix.server.IObjectBroker;
import bpi.most.service.api.DatapointService;
import bpi.most.service.api.ZoneService;

import javax.inject.Inject;
import java.util.Date;
import java.util.HashMap;

/**
 * Loads and caches all oBix-objects. This broker can be used
 * to retrieve following Data:
 *
 * <li><b>Dp without data:</b> Data points, which contain no data point data</li>
 * <li><b>Dp with data:</b> Data points, which contain data point data</li>
 * <li><b>List:</b> List, which contains all Dps, which contain no data point data</li>
 * <li><b>List:</b> List, which contains all Dps, which contain data point data</li>
 *
 * <li><b>Zone data:</b> A Zone, which contains Dps, which contain no data</li>
 * <li><b>Zone data:</b> A Zone, which contains Dps, which contain data</li>
 * <li><b>List:</b> List, which contains all Zones, which contain Dps, which contain no data point data</li>
 * <li><b>List:</b> List, which contains all Zones, which contain Dps, which contain data point data</li>
 *
 * <li><b>List:</b> List, which contains all Dps in a period of time, which contain data</li>
 *
 * Following data can be set:
 * <li><b>Dp without data:</b> Data point, which contains no data point data</li>
 * <li><b>Dp with data:</b> Data point, which contains data point data</li>
 *
 * @author Alexej Strelzow
 */
public class ObixObjectBroker implements IObjectBroker {

    @Inject
    private ZoneService zoneService;

    @Inject
    private DatapointService datapointService;

    /** data point name to Dp */
    private HashMap<String, DpDTO> dpCache;

    public static final String DP_LIST_NAME = "datapoints";
    public static final String DP_LIST_CONTRACT_NAME = "obix:dp";

    public static final String DP_DATA_LIST_NAME = "dpData";
    public static final String DP_DATA_LIST_CONTRACT_NAME = "obix:dpData";

    public static final String ZONE_LIST_NAME = "zones";
    public static final String ZONE_LIST_CONTRACT_NAME = "obix:zone";

    /**
     * Constructor, which initializes the broker with data points
     * and zones, which are stored in the DB.
     */
    ObixObjectBroker() {
        this.dpCache = new HashMap<String, DpDTO>();
    }

    private Dp transformDpDTO(DpDTO dpDto) {
        return new Dp(dpDto.getName(), dpDto.getType(), dpDto.getDescription());
    }

    private DpData transformDpDataDTO(Dp dp, DpDataDTO dpDataDto) {
        return new DpData(dp, dpDataDto.getTimestamp().getTime(), dpDataDto.getValue(), dpDataDto.getQuality());
    }

    private Zone transformZoneDTO(ZoneDTO zoneDTO) {
        Zone zone = new Zone(zoneDTO.getZoneId(), zoneDTO.getName());
        zone.setArea(zoneDTO.getArea());
        zone.setBuilding(zoneDTO.getBuilding());
        zone.setCity(zoneDTO.getCity());
        zone.setCountry(zoneDTO.getCountry());
        zone.setCounty(zoneDTO.getCounty());
        zone.setDescription(zoneDTO.getDescription());
        zone.setFloor(zoneDTO.getFloor());
        zone.setRoom(zoneDTO.getRoom());
        zone.setState(zoneDTO.getState());

        return zone;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Dp getDp(UserDTO user, DpDTO dpDto) {
        DpDTO dataPoint = null;

        if (!dpCache.containsKey(dpDto.getName())) {
            dataPoint = datapointService.getDatapoint(user, dpDto);
            dpCache.put(dataPoint.getName(), dataPoint);
        } else {
            dataPoint = dpCache.get(dataPoint.getName());
        }

        return transformDpDTO(dataPoint);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DpData getDpData(UserDTO user, DpDTO dpDto) {
        DpDataDTO data = datapointService.getData(user, dpDto);
        return transformDpDataDTO(transformDpDTO(dpDto), data);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List getAllDps() {
        List list = new List(DP_LIST_NAME, new Contract(DP_LIST_CONTRACT_NAME));

        for (DpDTO dpDto : datapointService.getDatapoints()) {
            list.add(transformDpDTO(dpDto));
        }
        return list;
    }



    /**
     * {@inheritDoc}
     */
    @Override
    public List getDpData(UserDTO user, DpDTO dpDto, String from, String to) {
        Date fromDate = null;
        Date toDate = null;
        // TODO: ASE fromDate = DateUtils.returnNowOnNull(from);
        // TODO: ASE toDate = DateUtils.returnNowOnNull(to);
        if (fromDate == null || toDate == null) {
            return null;
        }

        List list = new List(DP_DATA_LIST_NAME, new Contract(DP_DATA_LIST_CONTRACT_NAME));

        for (DpDataDTO data : datapointService.getData(user, dpDto, fromDate, toDate)) {
            list.add(transformDpDataDTO(getDp(user, dpDto), data));
        }

        return list;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addDp(UserDTO user, Dp dp) {
        Uri uri = dp.getHref();
        if (dpCache.containsKey(uri)) {
            // update data in DB (and cache)
            // TODO: datapointService.addData()
        } else {
            // add to DB (and cache)

            //dpCache.put(uri, dp);
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public bpi.most.obix.objects.Zone getZone(UserDTO user, ZoneDTO zone) {
        return transformZoneDTO(zoneService.getZone(user, zone));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List getDpsForZone(UserDTO user, ZoneDTO zone, int level) {
        List list = new List(DP_LIST_NAME, new Contract(DP_LIST_CONTRACT_NAME));

        for (DpDTO dp : zoneService.getDatapoints(user, zone, level)) {
            if (!dpCache.containsKey(dp.getName())) {
                dpCache.put(dp.getName(), dp);
            }

            list.add(transformDpDTO(dp));
        }

        return list;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List getHeadZones(UserDTO user) {
        List list = new List(ZONE_LIST_NAME, new Contract(ZONE_LIST_CONTRACT_NAME));

        for (ZoneDTO zone : zoneService.getHeadZones(user)) {
            list.add(transformZoneDTO(zone));
        }

        return list;
    }

}
