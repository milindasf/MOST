package bpi.most.obix.server;

import bpi.most.domain.datapoint.DatapointVO;
import bpi.most.obix.objects.*;
import bpi.most.service.api.DatapointService;
import bpi.most.service.api.ZoneService;
import bpi.most.domain.zone.Zone;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;

public class ObixObjectBroker implements IObjectBroker {

    private static final String OBIX_DP_PREFIX = "/obix/dp/";
    private static final String OBIX_ZONE_PREFIX = "/obix/zones/";

    //@Inject
    private ZoneService zoneService;

    //@Inject
    private DatapointService datapointService;

    private HashMap<Uri, Dp> dpCache;
    private HashMap<Uri, bpi.most.obix.objects.Zone> zoneCache;

    ObixObjectBroker() {
        this.dpCache = new HashMap<Uri, Dp>();
        this.zoneCache = new HashMap<Uri, bpi.most.obix.objects.Zone>();
    }

    @Override
    public void loadDatapoints() {
        final java.util.List<Zone> headZones = zoneService.getHeadZones();
        for (Zone zone : headZones) {

            int id = zone.getZoneId();
            String name = zone.getName();
            Uri zoneUri = new Uri(OBIX_ZONE_PREFIX + id);
            bpi.most.obix.objects.Zone oBixZone = new bpi.most.obix.objects.Zone(id, name);

            for (DatapointVO point : datapointService.getDatapoints(null, String.valueOf(id))) {
                String pointName = point.getName();
                Uri uri = new Uri(OBIX_DP_PREFIX + pointName);
                dpCache.put(uri, new Dp(pointName, point.getType(), point.getDescription()));
                oBixZone.addURI(uri);
            }
            zoneCache.put(zoneUri, oBixZone);
        }
    }

    @Override
    public Dp getDatapoint(URI href) {
        Uri uri = new Uri(href.toASCIIString());
        return dpCache.get(uri);
    }

    @Override
    public List getDatapointData(URI href) {
        Uri uri = new Uri(href.toASCIIString());
        Dp dp = dpCache.get(uri);
        if (dp != null) {
            dp.getDpData();
        }
        return null;
    }

    @Override
    public List getAllDatapoints() {
        // TODO
        List list = new List("datapoints", new Contract("obix:Datapoint"));
        list.addAll(dpCache.values().toArray(new Dp[dpCache.size()]));
        return list;
    }

    @Override
    public List getDatapointsForZone(URI href) {
        Uri uri = new Uri(href.toASCIIString());
        if (zoneCache.containsKey(uri)) {
            java.util.List<Dp> dpList = new ArrayList<Dp>();

            for (Uri u : (Uri[]) zoneCache.get(uri).getDatapointURIs().list()) {
                if (dpCache.containsKey(u)) {
                    dpList.add(dpCache.get(u));
                }
            }

            List list = new List("datapoints", new Contract("obix:Datapoint"));
            list.addAll(dpList.toArray(new Dp[dpList.size()]));
            return list;
        }
        return null;
    }

    @Override
    public List getDatapointsForAllZones() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List getDatapoints(String from, String to) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void updateDatapoint(String uri) {
        // TODO Auto-generated method stub

    }

}
