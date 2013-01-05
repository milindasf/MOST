package bpi.most.obix.server;

import java.net.URI;

/**
 * The oBIX server acts as a gateway between incoming requests from outside, for
 * example an HTTP server, and the internal object broker. It also translates
 * between XML and java-representation of oBIX objects.
 *
 * @author Alexej Strelzow
 */
public interface IObixServer {

    /**
     * GET /obix/dp/{name}
     *
     * @param href The uri of the oBix object, which is /obix/dp/{datapointName}
     *            of the Datapoint to retrieve.
     * @return One oBix object with the {@link bpi.most.obix.objects.Uri} <code>uri</code>,
     *         or <code>null</code>, if the <code>uri</code> is a wrong one.
     *         Dp contains only data, which belongs to Dp.
     */
    String getDatapoint(URI href); // = datapointName = {name} /obix/dp/{name}

    /**
     * GET /obix/dp/{name}/data
     *
     * @param href The uri of the oBix object, which is /obix/dp/{datapointName}
     *            of the Datapoint to retrieve.
     * @return One oBix object with the {@link bpi.most.obix.objects.Uri} <code>uri</code>,
     *         or <code>null</code>, if the <code>uri</code> is a wrong one.
     *         Dp contains all data from all DpData.
     */
    String getDatapointData(URI href); // = datapointName = {name}

    /**
     * GET /obix/dp
     *
     * @return All data points
     */
    String getAllDatapoints();
    // return list with all data points

    /**
     * GET /obix/zones/{id}
     *
     * @param href The id of the zone
     * @return The zone, which contains data points
     */
    String getDatapointsForZone(URI href); // = {id}
    // return zone with dp inside

    /**
     * GET /obix/zones
     *
     * @return All objects, wrapped in their own zone
     */
    String getDatapointsForAllZones();  // TODO
    // return list with all zones

    /**
     * GET /obix/dp/{name}/data?from={UCT datetime}&to=
     * {UCT datetime}
     *
     * @param from
     * @param to
     * @return
     */
    String getDatapoints(String from, String to);
    // return list with all datapoint data, see DpDataDTO (!)


    /**
     * PUT /obix/dp/{name}
     * <p/>
     * Updates the data for a data point.
     *
     * @param encodedDp An encoded instance of Dp
     */
    void updateDatapoint(String encodedDp);
}
