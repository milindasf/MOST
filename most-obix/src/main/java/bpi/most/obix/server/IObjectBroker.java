package bpi.most.obix.server;

import bpi.most.obix.objects.Dp;
import bpi.most.obix.objects.DpData;
import bpi.most.obix.objects.List;
import bpi.most.obix.objects.Uri;

import java.net.URI;
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
     * @param href The uri of the oBix object, which is the datapointName
     *            of the Datapoint to retrieve.
     * @return One oBix object with the {@link Uri} <code>uri</code>,
     *         or <code>null</code>, if the <code>uri</code> is a wrong one.
     */
    Dp getDatapoint(URI href); // = datapointName = {name}

    /**
     * GET /obix/dp/{name}/data
     *
     * @param href The uri of the oBix object, which is the datapointName
     *            of the Datapoint to retrieve.
     * @return One oBix object with the {@link Uri} <code>uri</code>,
     *         or <code>null</code>, if the <code>uri</code> is a wrong one.
     */
    List getDatapointData(URI href); // = datapointName = {name}

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
    List getDatapointsForZone(URI href); // = {id}
    // return list with zones

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
    void updateDatapoint(String uri);

}
