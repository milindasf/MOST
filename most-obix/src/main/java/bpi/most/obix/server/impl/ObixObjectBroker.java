package bpi.most.obix.server.impl;

import bpi.most.dto.DpDTO;
import bpi.most.dto.ZoneDTO;
import bpi.most.obix.objects.*;
import bpi.most.obix.objects.List;
import bpi.most.obix.server.IObjectBroker;
import bpi.most.service.api.DatapointService;
import bpi.most.service.api.ZoneService;

import javax.inject.Inject;
import java.util.*;

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

    /** Obj-href (= Uri) to Dp */
    private HashMap<Uri, Dp> dpCache;
    /** Obj-href (= Uri) to Zone */
    private HashMap<Uri, bpi.most.obix.objects.Zone> zoneCache;

    public static final String DP_LIST_NAME = "datapoints";
    public static final String DP_LIST_CONTRACT_NAME = "obix:dp";

    public static final String ZONE_LIST_NAME = "zones";
    public static final String ZONE_LIST_CONTRACT_NAME = "obix:zone";

    /**
     * Constructor, which initializes the broker with data points
     * and zones, which are stored in the DB.
     */
    ObixObjectBroker() {
        this.dpCache = new HashMap<Uri, Dp>();
        this.zoneCache = new HashMap<Uri, bpi.most.obix.objects.Zone>();
        loadDatapoints();
    }

    /**
     * Loads all data points -> Dp
     * TODO: Also headZones can have zones -> maybe recursive algorithm
     */
    private final void loadDatapoints() {
        final java.util.List<ZoneDTO> headZones = zoneService.getHeadZones();

        for (ZoneDTO zone : headZones) {
            int id = zone.getZoneId();
            String name = zone.getName();
            Uri zoneUri = new Uri(bpi.most.obix.objects.Zone.OBIX_ZONE_PREFIX + id);
            bpi.most.obix.objects.Zone oBixZone = new bpi.most.obix.objects.Zone(id, name);

            for (DpDTO point : datapointService.getDatapoints(null, String.valueOf(id))) {
                String pointName = point.getName();
                Uri uri = new Uri(Dp.OBIX_DP_PREFIX + pointName);
                dpCache.put(uri, new Dp(pointName, point.getType(), point.getDescription()));
                oBixZone.addURI(uri);
            }
            zoneCache.put(zoneUri, oBixZone);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Dp getDp(Uri href) {
        return getDpInternal(href, false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Dp getDpData(Uri href) {
        return getDpInternal(href, true);
    }

    private Dp getDpInternal(Uri href, boolean showData) {
        Dp dp = dpCache.get(href);

        if (dp != null) {
            Dp clone = dp.clone();
            if (showData) {
                clone.setShowData(true);
                return clone;
            }
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List getAllDps() {
        return getAllDpsInternal(false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List getAllDpData() {
        return getAllDpsInternal(true);
    }

    private List getAllDpsInternal(boolean showData) {
        if (dpCache.isEmpty()) {
            return null;
        }

        List list = new List(DP_LIST_NAME, new Contract(DP_LIST_CONTRACT_NAME));
        Dp[] values = dpCache.values().toArray(new Dp[dpCache.size()]);

        for (Dp dp : values)  {
            Dp clone = dp.clone();
            if (showData) {
                clone.setShowData(true);
            }
            list.add(clone);
        }
        return list;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public bpi.most.obix.objects.Zone getDpsForZone(Uri href) {
        return getDpDataForZoneInternal(href, false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Zone getDpDataForZone(Uri href) {
        return getDpDataForZoneInternal(href, true);
    }

    private bpi.most.obix.objects.Zone getDpDataForZoneInternal(Uri href, boolean showData) {
        if (zoneCache.containsKey(href)) {
            java.util.List<Dp> dpList = new ArrayList<Dp>();
            bpi.most.obix.objects.Zone oBixZone = zoneCache.get(href);

            if (oBixZone != null) {
                bpi.most.obix.objects.Zone zoneClone = oBixZone.clone();
                for (Uri u : (Uri[]) oBixZone.getDatapointURIs().list()) {
                    Dp dp = dpCache.get(u);
                    if (dp != null) {
                        Dp cpClone = dp.clone();
                        zoneClone.addDp(cpClone);
                    }
                }
                if (showData) {
                    zoneClone.setShowData(true);
                }
                return zoneClone;
            }
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List getDpsForAllZones() {
        return getAllZonesInternal(true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List getAllZones() {
        return getAllZonesInternal(false);
    }

    private List getAllZonesInternal(boolean showData) {
        List list = new List(ZONE_LIST_NAME, new Contract(ZONE_LIST_CONTRACT_NAME));

        for (Uri uri : zoneCache.keySet()) {
            list.add(getDpDataForZoneInternal(uri, showData));
        }
        return list;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List getDpForZone(Uri href, String from, String to) {
        Date fromDate = null;
        Date toDate = null;
        // TODO: ASE fromDate = DateUtils.returnNowOnNull(from);
        // TODO: ASE toDate = DateUtils.returnNowOnNull(to);
        if (fromDate == null || toDate == null) {
            return null;
        }

        long fromMillis = fromDate.getTime();
        long toMillis =  toDate.getTime();
        List list = new List(DP_LIST_NAME, new Contract(DP_LIST_CONTRACT_NAME));

        bpi.most.obix.objects.Zone oBixZone = zoneCache.get(href);

        if (oBixZone != null) {
            for (Uri u : (Uri[]) oBixZone.getDatapointURIs().list()) {
                Dp dp = dpCache.get(u);
                if (dp != null) {
                    Dp dpClone = dp.clone();
                    for (DpData data : dp.getDpData()) {
                        long dataMillis = data.getTimestamp().getMillis();
                        if (dataMillis >= fromMillis && dataMillis <= toMillis) {
                            dpClone.addDpData(data.clone(dpClone));
                        }
                    }
                    dpClone.setShowData(true);
                    list.add(dpClone);
                }
            }
            return list;
        }

        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List getDpData(String from, String to) {
        Date fromDate = null;
        Date toDate = null;
        // TODO: ASE fromDate = DateUtils.returnNowOnNull(from);
        // TODO: ASE toDate = DateUtils.returnNowOnNull(to);
        if (fromDate == null || toDate == null) {
            return null;
        }

        long fromMillis = fromDate.getTime();
        long toMillis =  toDate.getTime();
        List list = new List(DP_LIST_NAME, new Contract(DP_LIST_CONTRACT_NAME));

        for (Dp dp : dpCache.values()) {
            Dp dpClone = dp.clone();
            for (DpData data : dp.getDpData()) {
                long dataMillis = data.getTimestamp().getMillis();
                if (dataMillis >= fromMillis && dataMillis <= toMillis) {
                    dpClone.addDpData(data.clone(dpClone));
                }
            }
            dpClone.setShowData(true);
            list.add(dpClone);
        }
        return list;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addDp(Dp dp) {
        Uri uri = dp.getHref();
        if (dpCache.containsKey(uri)) {
            // update data in DB (and cache)
            // TODO: datapointService.addData()
        } else {
            // add to DB (and cache)

            //dpCache.put(uri, dp);
        }

    }

}
