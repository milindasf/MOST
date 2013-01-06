package bpi.most.obix.server;

import bpi.most.obix.objects.Dp;
import bpi.most.obix.objects.List;
import bpi.most.obix.objects.Uri;
import bpi.most.obix.objects.Zone;

public interface IObjectBroker {

    /**
     * GET /obix/dp/{name}
     *
     * @param href The uri of the oBix object, which is /obix/dp/{datapointName}
     *            of the Datapoint to retrieve.
     * @return One oBix object with the {@link Uri} <code>uri</code>,
     *         or <code>null</code>, if the <code>uri</code> is a wrong one.
     *         Dp contains only data, which belongs to Dp.
     */
    Dp getDp(Uri href); // = datapointName = {name} /obix/dp/{name}

    /**
     * GET /obix/dp/{name}/data
     *
     * @param href The uri of the oBix object, which is /obix/dp/{datapointName}
     *            of the Datapoint to retrieve.
     * @return One oBix object with the {@link Uri} <code>uri</code>,
     *         or <code>null</code>, if the <code>uri</code> is a wrong one.
     *         Dp contains all data from all DpData.
     */
    Dp getDpData(Uri href); // = datapointName = {name}

    /**
     * GET /obix/dp
     *
     * @return All data points
     */
    List getAllDps();
    // return list with all data points

    /**
     * GET /obix/dp/data
     *
     * @return All data points
     */
    List getAllDpData();
    // return list with all data points with data included

    /**
     * GET /obix/zones/{id}
     *
     * @param href The id of the zone
     * @return The zone, which contains data points
     */
    Zone getDpsForZone(Uri href); // = {id}
    // return zone with dp inside

    /**
     * GET /obix/zones/{id}/data
     *
     * @param href The id of the zone
     * @return The zone, which contains data points
     */
    Zone getDpDataForZone(Uri href); // = {id}
    // return zone with dp inside

    /**
     * GET /obix/zones
     *
     * @return All objects, wrapped in their own zone
     */
    List getDpsForAllZones();
    // return list with all zones

    /**
     * GET /obix/zones/data
     *
     * @return All objects, wrapped in their own zone
     */
    List getAllZones();  // TODO
    // return list with all zones

    Zone getDpForZone(Uri href, String from, String to);

    /**
     * GET /obix/dp/{name}/data?from={UCT datetime}&to=
     * {UCT datetime}
     *
     * @param from Begin Timestamp
     * @param to End Timestamp
     * @return A List of objects, which are in this period of time
     */
    List getDatapoints(String from, String to);
    // return list with all datapoint data, see DpDataDTO (!)


    /**
     * PUT /obix/dp/{name}
     * <p/>
     * Updates the data for a data point.
     *
     * @param dp An instance of Dp, which contains new values
     */
    void updateDatapoint(Dp dp);

}
