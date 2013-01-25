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
     * Returns the data point as obix encoded string.
     *
     * @param user The User
     * @param dpDto The {@link DpDTO}, which information should be retrieved
     * @return One encoded oBix object with the data point name <code>name</code>,
     *         or <code>null</code>, if the data point does not exist.
     *         Dp contains only data, which belongs to Dp, no data point data.
     */
    String getDp(UserDTO user, DpDTO dpDto);

    /**
     * Incoming HTTP-Request should be: GET /obix/dp/{name}/data
     * @see bpi.most.obix.server.rest.ObixDpResource
     * @see bpi.most.obix.server.rest.impl.ObixDpResourceImpl
     *
     * Returns the last measurement of the data point as obix encoded string.
     *
     * @param user The User
     * @param dpDto The {@link DpDTO}, which last measurement should be retrieved
     * @return One encoded oBix object with the data point name <code>name</code>,
     *         or <code>null</code>, if the data point does not exist.
     *         The latest measurement for the data point.
     */
    String getDpData(UserDTO user, DpDTO dpDto);

    /**
     * Incoming HTTP-Request should be: POST /obix/dp/{name}
     * @see bpi.most.obix.server.rest.ObixDpResource
     * @see bpi.most.obix.server.rest.impl.ObixDpResourceImpl
     *
     * Adds the decoded Dp and its values to the server.
     *
     * @param user The User
     * @param encodedDp An encoded instance of Dp, which maybe contains new values
     */
    void addDp(UserDTO user, String encodedDp);

    /**
     * Incoming HTTP-Request should be: POST /obix/dp/{name}
     * @see bpi.most.obix.server.rest.ObixDpResource
     * @see bpi.most.obix.server.rest.impl.ObixDpResourceImpl
     *
     * Adds the decoded Dp and its values to the server.
     *
     * @param user The User
     * @param dpDto The data point, which should get additional data appended
     * @param encodedDpData An encoded instance of DpData, which contains new values
     *                      to append to the data point dpDto
     */
    void addDpData(UserDTO user, DpDTO dpDto, String encodedDpData);

    /**
     * Incoming HTTP-Request should be: PUT /obix/dp/{name}
     * @see bpi.most.obix.server.rest.ObixDpResource
     * @see bpi.most.obix.server.rest.impl.ObixDpResourceImpl
     *
     * Updates the data point dpDto with obix encoded data.
     *
     * @param user The User
     * @param dpDto The data point, which should get updated
     * @param encodedDp An encoded instance of Dp, which maybe contains new values
     */
    void updateDp(UserDTO user, DpDTO dpDto, String encodedDp);

    /**
     * Incoming HTTP-Request should be: GET /obix/dp
     * @see bpi.most.obix.server.rest.ObixDpResource
     * @see bpi.most.obix.server.rest.impl.ObixDpResourceImpl
     *
     * Returns all data points as an encoded obix string.
     *
     * @return All data points in the data base
     */
    String getAllDps();

    /**
     * Incoming HTTP-Request should be: GET /obix/dp/{name}/data?from={UTC datetime}&to={UTC datetime}
     * @see bpi.most.obix.server.rest.ObixDpResource
     * @see bpi.most.obix.server.rest.impl.ObixDpResourceImpl
     *
     * @param user The User
     * @param dpDto The data point, which data should be retrieved
     * @param from UTC Time-String, beginning of the retrieving interval
     * @param to UTC Time-String, end of the retrieving interval
     * @return An encoded list of data points + data, which is in the UTC time interval: [from; to].
     */
    String getDpData(UserDTO user, DpDTO dpDto, String from, String to);

    /**
     * Incoming HTTP-Request should be: GET /obix/dp/{name}/periodicdata?from={UTC datetime}&to={UTC datetime}
     *                                      &period={period}&mode={mode}&rollupInterval={rollupInterval}
     * @see bpi.most.obix.server.rest.ObixDpResource
     * @see bpi.most.obix.server.rest.impl.ObixDpResourceImpl
     *
     * @param user The User
     * @param dpDto dpDto The data point, which data should be retrieved
     * @param from UTC Time-String, beginning of the retrieving interval
     * @param to UTC Time-String, end of the retrieving interval
     * @param period The period in seconds, e.g. 60 means: give me data [<from>; <to>] in 1 minute intervals
     * @param mode The mode, in which the data should be retrieved
     * @param rollupInterval The grouping interval in seconds, e.g. 60*5 means: give me
     *          data [<from>; <to>] in <period> intervals with the mode <mode> in intervals of
     *          <rollupInterval>, in which the data should be aggregated and so on
     * @return The obix encoded history rollup, which contains a list of aggregated intervals
     */
    String getDpPeriodicData(UserDTO user, DpDTO dpDto, String from, String to, float period, int mode, int rollupInterval);

    /**
     * Incoming HTTP-Request should be: GET /obix/zones/{id}
     * @see bpi.most.obix.server.rest.ObixZoneResource
     * @see bpi.most.obix.server.rest.impl.ObixZoneResourceImpl
     *
     * @param user The User
     * @param zoneDto The zone
     * @return The obix encoded zone
     */
    String getZone(UserDTO user, ZoneDTO zoneDto);

    /**
     * Incoming HTTP-Request should be: GET /obix/zones/{id}?level={level}
     * @see bpi.most.obix.server.rest.ObixZoneResource
     * @see bpi.most.obix.server.rest.impl.ObixZoneResourceImpl
     *
     * @param user The User
     * @param zoneDto The zone, which data should be retrieved
     * @param level The level of detail
     * @return The zone, which contains data points, but the data points
     *         contain no data.
     */
    String getDpsForZone(UserDTO user, ZoneDTO zoneDto, int level);

    /**
     * Incoming HTTP-Request should be: GET /obix/zones
     * @see bpi.most.obix.server.rest.ObixZoneResource
     * @see bpi.most.obix.server.rest.impl.ObixZoneResourceImpl
     *
     * @param user The User
     * @return The head zones as an obix encoded string
     */
    String getHeadZones(UserDTO user);

}
