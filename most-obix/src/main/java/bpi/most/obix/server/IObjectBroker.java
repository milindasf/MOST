package bpi.most.obix.server;

import bpi.most.obix.objects.Dp;
import bpi.most.obix.objects.List;
import bpi.most.obix.objects.Uri;
import bpi.most.obix.objects.Zone;

import java.util.HashMap;

public interface IObjectBroker {

    /**
     * Loads all Datapoints, which are in the DB, converts them
     * to oBix objects and finally caches them in a {@link HashMap}.
     */
    void loadDatapoints();

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
     * @param from
     * @param to
     * @return
     */
    List getDatapoints(String from, String to);
    // return list with all datapoint data, see DpDataDTO (!)


    /**
     * PUT /obix/dp/{name}
     * <p/>
     * Update for the data point with the {@link Uri} <code>{name}</code>
     *
     * @param uri The uri of the oBix object, which is the datapointName
     *            of the Datapoint to update.
     */
    void updateDatapoint(Uri uri);

}
