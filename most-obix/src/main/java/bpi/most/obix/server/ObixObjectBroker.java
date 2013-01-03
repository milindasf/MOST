package bpi.most.obix.server;

import bpi.most.dto.DpDTO;
import bpi.most.dto.ZoneDTO;
import bpi.most.obix.objects.Contract;
import bpi.most.obix.objects.Dp;
import bpi.most.obix.objects.List;
import bpi.most.obix.objects.Uri;
import bpi.most.service.api.DatapointService;
import bpi.most.service.api.ZoneService;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;

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

    @Override
    public Dp getDatapoint(Uri href) {
        Dp dp = dpCache.get(href);
        dp.setShowData(false);

        return dp;
    }

    @Override
    public Dp getDatapointData(Uri href) {
        Dp dp = dpCache.get(href);

        if (dp != null) {
            dp.setShowData(true);
            return dp;
        }
        return null;
    }

    @Override
    public List getAllDatapoints() {
        List list = new List("datapoints", new Contract("obix:dp"));
        Dp[] values = dpCache.values().toArray(new Dp[dpCache.size()]);

        for (Dp dp : values)  {
            dp.setShowData(true);
        }

        list.addAll(values);
        return list;
    }

    @Override
    public bpi.most.obix.objects.Zone getDatapointsForZone(Uri href) {

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
                oBixZone.setShowData(true);
            }

           return oBixZone;
        }
        return null;
    }

    @Override
    public List getDatapointsForAllZones() {
        List list = new List("zones", new Contract("obix:zone"));

        for (Uri uri : zoneCache.keySet()) {
            list.add(getDatapointsForZone(uri));
        }

        return list;
    }

    @Override
    public List getDatapoints(String from, String to) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void updateDatapoint(Uri uri) {
        // TODO Auto-generated method stub

    }

}
