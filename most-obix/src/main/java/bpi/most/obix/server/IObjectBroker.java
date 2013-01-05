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
    Dp getDatapoint(Uri href); // = datapointName = {name} /obix/dp/{name}

    /**
     * GET /obix/dp/{name}/data
     *
     * @param href The uri of the oBix object, which is /obix/dp/{datapointName}
     *            of the Datapoint to retrieve.
     * @return One oBix object with the {@link Uri} <code>uri</code>,
     *         or <code>null</code>, if the <code>uri</code> is a wrong one.
     *         Dp contains all data from all DpData.
     */
    Dp getDatapointData(Uri href); // = datapointName = {name}

    /**
     * GET /obix/dp
     *
     * @return All data points
     */
    List getAllDatapoints();
    // return list with all data points

    /**
     * GET /obix/zones/{id}
     *
     * @param href The id of the zone
     * @return The zone, which contains data points
     */
    Zone getDatapointsForZone(Uri href); // = {id}
    // return zone with dp inside

    /**
     * GET /obix/zones
     *
     * @return All objects, wrapped in their own zone
     */
    List getDatapointsForAllZones();
    // return list with all zones

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
