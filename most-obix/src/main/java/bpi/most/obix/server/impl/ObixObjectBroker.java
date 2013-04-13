package bpi.most.obix.server.impl;

import bpi.most.dto.DpDTO;
import bpi.most.dto.DpDataDTO;
import bpi.most.dto.DpDatasetDTO;
import bpi.most.dto.UserDTO;
import bpi.most.dto.ZoneDTO;
import bpi.most.obix.history.HistoryQueryOutImpl;
import bpi.most.obix.history.HistoryRecordImpl;
import bpi.most.obix.history.HistoryRollupOutImpl;
import bpi.most.obix.history.HistoryRollupRecordImpl;
import bpi.most.obix.objects.Contract;
import bpi.most.obix.objects.Dp;
import bpi.most.obix.objects.DpData;
import bpi.most.obix.objects.List;
import bpi.most.obix.objects.Uri;
import bpi.most.obix.server.IObjectBroker;
import bpi.most.obix.utils.DateUtils;
import bpi.most.service.api.DatapointService;
import bpi.most.service.api.ZoneService;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.ArrayList;
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
@Component
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
    public ObixObjectBroker() {
        this.dpCache = new HashMap<String, DpDTO>();
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
            dataPoint = dpCache.get(dpDto.getName());
        }

        return ObixBrokerUtils.transformDpDTO(dataPoint);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DpData getDpData(UserDTO user, DpDTO dpDto) {
        DpDataDTO data = datapointService.getData(user, dpDto);
        return ObixBrokerUtils.transformDpDataDTO(ObixBrokerUtils.transformDpDTO(dpDto), data);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void addDp(UserDTO user, Dp dp) {
        // not implemented yet

        Uri uri = dp.getHref();
        if (dpCache.containsKey(uri)) {
            // update data in DB (and cache)
            // some day: datapointService.addDp()
        } else {
            // add to DB (and cache)

            //dpCache.put(uri, dp);
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addDpData(UserDTO user, DpDTO dpDto, DpData dpData) {
        datapointService.addData(user, dpDto, ObixBrokerUtils.transformDpData(dpData));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateDp(UserDTO user, Dp encodedDp) {
        // not implemented yet
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List getAllDps() {
        List list = new List(DP_LIST_NAME, new Contract(DP_LIST_CONTRACT_NAME));

        for (DpDTO dpDto : datapointService.getDatapoints()) {
            list.add(ObixBrokerUtils.transformDpDTO(dpDto));
        }
        return list;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HistoryQueryOutImpl getDpData(UserDTO user, DpDTO dpDto, String from, String to) {
        Date fromDate = DateUtils.returnNowOnNull(from);
        Date toDate = DateUtils.returnNowOnNull(to);

        if (fromDate == null || toDate == null) {
            return null;
        }

        DpDTO dp = datapointService.getDatapoint(user, dpDto);

        DpDatasetDTO data = datapointService.getData(user, dp, fromDate, toDate);

        ArrayList<HistoryRecordImpl> records = new ArrayList<HistoryRecordImpl>();

        for (DpDataDTO d : data) {
            records.add(ObixBrokerUtils.transformDpDataDTO(d));
        }

        HistoryQueryOutImpl query = new HistoryQueryOutImpl(records);

        return query;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HistoryRollupOutImpl getDpPeriodicData(UserDTO user, DpDTO dpDto, String from, String to, float period, int mode, int interval) {
        Date fromDate = DateUtils.returnNowOnNull(from);
        Date toDate = DateUtils.returnNowOnNull(to);

        if (fromDate == null || toDate == null) {
            return null;
        }

        DpDatasetDTO data = datapointService.getDataPeriodic(user, dpDto, fromDate, toDate, period, mode);

        ArrayList<HistoryRollupRecordImpl> records = new ArrayList<HistoryRollupRecordImpl>();
        ArrayList<DpData> dpDataList = new ArrayList<DpData>();
        Dp dp = ObixBrokerUtils.transformDpDTO(dpDto);

        long intervalStart = fromDate.getTime();
        long intervalMillis = interval*1000;
        long intervalEnd = intervalStart + intervalMillis;

        for (DpDataDTO d : data) {
            long time = d.getTimestamp().getTime();
            if (time > intervalStart && time <= intervalEnd) {
                dpDataList.add(ObixBrokerUtils.transformDpDataDTO(dp, d));

            } else if (time > intervalEnd) {
                int initialSize = dpDataList.size();
                records.add(ObixBrokerUtils.transformDpDataDTO(dpDataList));

                dpDataList = new ArrayList<DpData>(initialSize);
                intervalStart = intervalEnd;
                intervalEnd += intervalMillis;

                if (time > intervalStart && time <= intervalEnd) {
                    dpDataList.add(ObixBrokerUtils.transformDpDataDTO(dp, d));
                }
            }
        }


        HistoryRollupOutImpl rollupOutput = new HistoryRollupOutImpl(records, fromDate.getTime(), toDate.getTime());

        return rollupOutput;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public bpi.most.obix.objects.Zone getZone(UserDTO user, ZoneDTO zone) {
        return ObixBrokerUtils.transformZoneDTO(zoneService.getZone(user, zone));
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

            list.add(ObixBrokerUtils.transformDpDTO(dp));
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
            list.add(ObixBrokerUtils.transformZoneDTO(zone));
        }

        return list;
    }

}
