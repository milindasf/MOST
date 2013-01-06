package bpi.most.obix.server;

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
     * @param name The name of the oBix object, which is the last part of: /obix/dp/{datapointName}
     *            of the Datapoint to retrieve.
     * @return One oBix object with the {@link bpi.most.obix.objects.Uri} <code>uri</code>,
     *         or <code>null</code>, if the <code>uri</code> is a wrong one.
     *         Dp contains only data, which belongs to Dp.
     */
    String getDp(String name); // = datapointName = {name} /obix/dp/{name}

    /**
     * GET /obix/dp/{name}/data
     *
     * @param name The name of the oBix object, which is the last part of: /obix/dp/{datapointName}
     *            of the Datapoint to retrieve.
     * @return One oBix object with the {@link bpi.most.obix.objects.Uri} <code>uri</code>,
     *         or <code>null</code>, if the <code>uri</code> is a wrong one.
     *         Dp contains all data from all DpData.
     */
    String getDpData(String name); // = datapointName = {name}

    /**
     * GET /obix/dp
     *
     * @return All data points
     */
    String getAllDps();
    // return list with all data points

    /**
     * GET /obix/dp/data
     *
     * @return All data points
     */
    String getAllDpData();
    // return list with all data points with data included

    /**
     * GET /obix/zones/{id}
     *
     * @param id The id of the zone
     * @return The zone, which contains data points
     */
    String getDpsForZone(int id); // = {id}
    // return zone with dp inside

    /**
     * GET /obix/zones/{id}/data
     *
     * @param id The id of the zone
     * @return The zone, which contains data points
     */
    String getDpDataForZone(int id); // = {id}
    // return zone with dp inside

    /**
     * GET /obix/zones
     *
     * @return All objects, wrapped in their own zone
     */
    String getDpsForAllZones();  // TODO
    // return list with all zones

    /**
     * GET /obix/zones/data
     *
     * @return All objects, wrapped in their own zone
     */
    String getAllZones();  // TODO
    // return list with all zones


    String getDpForZone(int id, String from, String to);

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
    void updateDp(String encodedDp);
}
