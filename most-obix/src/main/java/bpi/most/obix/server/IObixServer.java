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
     * Incoming HTTP-Request should be: GET /obix/dp/{name}
     * @see bpi.most.obix.server.rest.ObixDpResource
     * @see bpi.most.obix.server.rest.impl.ObixDpResourceImpl
     *
     * @param name The name of the oBix object, which is: /obix/dp/{datapointName}
     *            of the data point to retrieve.
     * @return One encoded oBix object with the data point name <code>name</code>,
     *         or <code>null</code>, if the data point does not exist.
     *         Dp contains only data, which belongs to Dp, no data point data.
     */
    String getDp(String name); // = datapointName = {name} /obix/dp/{name}

    /**
     * Incoming HTTP-Request should be: GET /obix/dp/{name}/data
     * @see bpi.most.obix.server.rest.ObixDpResource
     * @see bpi.most.obix.server.rest.impl.ObixDpResourceImpl
     *
     * @param name The name of the oBix object, which is: /obix/dp/{datapointName}
     *            of the data point to retrieve.
     * @return One encoded oBix object with the data point name <code>name</code>,
     *         or <code>null</code>, if the data point does not exist.
     *         Dp contains all data from all DpData.
     */
    String getDpData(String name); // = datapointName = {name}

    /**
     * Incoming HTTP-Request should be: GET /obix/dp
     * @see bpi.most.obix.server.rest.ObixDpResource
     * @see bpi.most.obix.server.rest.impl.ObixDpResourceImpl
     *
     * @return All data points, which are cached in the broker,
     *         encoded, but without the data point data.
     */
    String getAllDps();
    // return list with all data points

    /**
     * Incoming HTTP-Request should be: GET /obix/dp/data
     * @see bpi.most.obix.server.rest.ObixDpResource
     * @see bpi.most.obix.server.rest.impl.ObixDpResourceImpl
     *
     * @return All data points and its data, which are cached in the broker encoded.
     */
    String getAllDpData();
    // return list with all data points with data included

    /**
     * Incoming HTTP-Request should be: GET /obix/dp/{name}/data?from={UTC datetime}&to={UTC datetime}
     * @see bpi.most.obix.server.rest.ObixDpResource
     * @see bpi.most.obix.server.rest.impl.ObixDpResourceImpl
     *
     * @param from UTC Time-String, beginning of the retrieving interval
     * @param to UTC Time-String, end of the retrieving interval
     * @return An encoded list of data points + data, which is in the UTC time interval: [from; to].
     */
    String getDpData(String from, String to);
    // return list with all datapoint data, see DpDataDTO (!)


    /**
     * Incoming HTTP-Request should be: PUT /obix/dp/{name}
     * @see bpi.most.obix.server.rest.ObixDpResource
     * @see bpi.most.obix.server.rest.impl.ObixDpResourceImpl
     *
     * Adds the decoded Dp and its values to the server.
     *
     * @param encodedDp An encoded instance of Dp, which maybe contains new values
     */
    void addDp(String encodedDp);  // TODO

    /**
     * Incoming HTTP-Request should be: GET /obix/zones/{id}
     * @see bpi.most.obix.server.rest.ObixZoneResource
     * @see bpi.most.obix.server.rest.impl.ObixZoneResourceImpl
     *
     * @param id The id of the zone
     * @return The zone, which contains data points, but the data points
     *         contain no data.
     */
    String getDpsForZone(int id); // = {id}
    // return zone with dp inside

    /**
     * Incoming HTTP-Request should be: GET /obix/zones/{id}/data
     * @see bpi.most.obix.server.rest.ObixZoneResource
     * @see bpi.most.obix.server.rest.impl.ObixZoneResourceImpl
     *
     * @param id The id of the zone
     * @return The zone, which contains data points + their data
     */
    String getDpDataForZone(int id); // = {id}
    // return zone with dp inside

    /**
     * Incoming HTTP-Request should be: GET /obix/zones
     * @see bpi.most.obix.server.rest.ObixZoneResource
     * @see bpi.most.obix.server.rest.impl.ObixZoneResourceImpl
     *
     * @return All objects, wrapped in their own zone, but without data
     */
    String getAllZones();
    // return list with all zones

    /**
     * Incoming HTTP-Request should be: GET /obix/zones/data
     * @see bpi.most.obix.server.rest.ObixZoneResource
     * @see bpi.most.obix.server.rest.impl.ObixZoneResourceImpl
     *
     * @return All objects, wrapped in their own zone, but with data
     */
    String getDpsForAllZones();
    // return list with all zones

    /**
     * Incoming HTTP-Request should be: GET /obix/zones/{id}/data
     * @see bpi.most.obix.server.rest.ObixZoneResource
     * @see bpi.most.obix.server.rest.impl.ObixZoneResourceImpl
     *
     * @param id The id of the zone
     * @param from UTC Time-String, beginning of the retrieving interval
     * @param to UTC Time-String, end of the retrieving interval
     * @return A list of data points + data for the zone with the uri
     *         <code>href</code>. The data is in the UTC time interval:
     *         [from; to].
     */
    String getDpForZone(int id, String from, String to);

}
