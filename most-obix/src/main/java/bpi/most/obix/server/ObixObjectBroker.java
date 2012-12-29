package bpi.most.obix.server;

import bpi.most.domain.datapoint.DatapointVO;
import bpi.most.obix.objects.Dp;
import bpi.most.obix.objects.DpData;
import bpi.most.obix.objects.List;
import bpi.most.obix.objects.Uri;
import bpi.most.service.api.DatapointService;
import bpi.most.service.api.ZoneService;

import java.util.HashMap;

public class ObixObjectBroker implements IObjectBroker {

    private static final String OBIX_DP_PREFIX = "/obix/dp/";
    private static final String OBIX_ZONE_PREFIX = "/obix/zones/";

    //@Inject
    private ZoneService zoneService;

    //@Inject
    private DatapointService datapointService;

    private HashMap<Uri, Dp> dpCache;
    private HashMap<Uri, List> zoneCache;

    ObixObjectBroker() {
        this.dpCache = new HashMap<Uri, Dp>();
        this.zoneCache = new HashMap<Uri, List>();
    }

    @Override
    public void loadDatapoints() {
        //final java.util.List<Zone> headZones = zoneService.getHeadZones();
        //for (Zone zone : headZones) {

        //}

        for (DatapointVO point : datapointService.getDatapoints()) {
            String name = point.getName();
            Uri uri = new Uri(OBIX_DP_PREFIX + name);
            dpCache.put(uri, new Dp(name, point.getType(), point.getDescription()));
            // TODO extend service, to get DpData somehow!
        }

    }

    @Override
    public Dp getDatapoint(String uri) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public DpData getDatapointData(String uri) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List getAllDatapoints() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List getDatapointsForZone(String zone) {
        // TODO Auto-generated method stub
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
