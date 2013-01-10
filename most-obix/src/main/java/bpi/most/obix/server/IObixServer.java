package bpi.most.obix.server;

import bpi.most.dto.DpDTO;
import bpi.most.dto.UserDTO;
import bpi.most.dto.ZoneDTO;

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
     * @param dpDto The name of the oBix object, which is: /obix/dp/{datapointName}
     *            encapsulated in an DpDTO object.
     * @return One encoded oBix object with the data point name <code>name</code>,
     *         or <code>null</code>, if the data point does not exist.
     *         Dp contains only data, which belongs to Dp, no data point data.
     */
    String getDp(UserDTO user, DpDTO dpDto); // = datapointName = {name} /obix/dp/{name}

    /**
     * Incoming HTTP-Request should be: GET /obix/dp/{name}/data
     * @see bpi.most.obix.server.rest.ObixDpResource
     * @see bpi.most.obix.server.rest.impl.ObixDpResourceImpl
     *
     * @param dpDto The name of the oBix object, which is: /obix/dp/{datapointName}
     *            encapsulated in an DpDTO object.
     * @return One encoded oBix object with the data point name <code>name</code>,
     *         or <code>null</code>, if the data point does not exist.
     *         The latest measurement for the data point.
     */
    String getDpData(UserDTO user, DpDTO dpDto); // = datapointName = {name}

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
     * Incoming HTTP-Request should be: GET /obix/dp/{name}/data?from={UTC datetime}&to={UTC datetime}
     * @see bpi.most.obix.server.rest.ObixDpResource
     * @see bpi.most.obix.server.rest.impl.ObixDpResourceImpl
     *
     * @param dpDto The name of the oBix object, which is: /obix/dp/{datapointName}
     *            encapsulated in an DpDTO object.
     * @param from UTC Time-String, beginning of the retrieving interval
     * @param to UTC Time-String, end of the retrieving interval
     * @return An encoded list of data points + data, which is in the UTC time interval: [from; to].
     */
    String getDpData(UserDTO user, DpDTO dpDto, String from, String to);
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
    void addDp(UserDTO user, String encodedDp);  // TODO


    String getZone(UserDTO user, ZoneDTO zone);

    /**
     * Incoming HTTP-Request should be: GET /obix/zones/{id}
     * @see bpi.most.obix.server.rest.ObixZoneResource
     * @see bpi.most.obix.server.rest.impl.ObixZoneResourceImpl
     *
     * @param id The id of the zone
     * @return The zone, which contains data points, but the data points
     *         contain no data.
     */
    String getDpsForZone(UserDTO user, ZoneDTO zoneDto, int level); // = {id}
    // return zone with dp inside

    /**
     * Incoming HTTP-Request should be: GET /obix/zones
     * @see bpi.most.obix.server.rest.ObixZoneResource
     * @see bpi.most.obix.server.rest.impl.ObixZoneResourceImpl
     *
     * @return All objects, wrapped in their own zone, but without data
     */
    String getHeadZones(UserDTO user);
    // return list with all zones

}
