package bpi.most.obix.server;

import bpi.most.dto.DpDTO;
import bpi.most.dto.ZoneDTO;
import bpi.most.obix.objects.*;
import bpi.most.service.api.DatapointService;
import bpi.most.service.api.ZoneService;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Loads and caches all oBix-objects. This broker can be used
 * to retrieve following Data:
 *
 * <li><b>Dp without data:</b> Datapoints, which contain no data</li>
 * <li><b>Dp with data:</b> Datapoints, which contain data</li>
 * <li><b>List:</b> List, which contains all Dps, which contain data</li>
 *
 * <li><b>Zone data:</b> A Zone, which contains Dps, which contain data</li>
 * <li><b>List:</b> List, which contains all Zones, which contain Dps, which contain data</li>
 *
 * <li><b>List:</b> List, which contains all Dps in a period of time, which contain data</li>
 *
 * @author Alexej Strelzow
 */
public class ObixObjectBroker implements IObjectBroker {

    @Inject
    private ZoneService zoneService;

    @Inject
    private DatapointService datapointService;

    private HashMap<Uri, Dp> dpCache;
    private HashMap<Uri, bpi.most.obix.objects.Zone> zoneCache;

    ObixObjectBroker() {
        this.dpCache = new HashMap<Uri, Dp>();
        this.zoneCache = new HashMap<Uri, bpi.most.obix.objects.Zone>();
        loadDatapoints();
    }

    /**
     * Loads all data points -> Dp
     * TODO: Also headZones can have zones -> maybe recursive algorithm
     */
    public void loadDatapoints() {
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

        if (dp != null && showData) {
            dp.setShowData(true);
        }
        return dp;
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
        List list = new List("datapoints", new Contract("obix:dp"));
        Dp[] values = dpCache.values().toArray(new Dp[dpCache.size()]);

        if (showData) {
            for (Dp dp : values)  {
                dp.setShowData(true);
            }
        }

        list.addAll(values);
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
                for (Uri u : (Uri[]) oBixZone.getDatapointURIs().list()) {
                    Dp dp = dpCache.get(u);
                    if (dp != null) {
                        oBixZone.addDp(dp);
                    }
                }
                if (showData) {
                    oBixZone.setShowData(true);
                }
            }

            return oBixZone;
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
        List list = new List("zones", new Contract("obix:zone"));

        for (Uri uri : zoneCache.keySet()) {
            list.add(getAllDpsInternal(showData));
        }

        return list;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Zone getDpForZone(Uri href, String from, String to) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List getDatapoints(String from, String to) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateDatapoint(Dp dp) {
        // TODO Auto-generated method stub

    }

}
