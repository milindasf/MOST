package bpi.most.obix.server;

import bpi.most.dto.DpDTO;
import bpi.most.dto.UserDTO;
import bpi.most.dto.ZoneDTO;
import bpi.most.obix.history.HistoryQueryOutImpl;
import bpi.most.obix.objects.*;

/**
 * Loads and caches all oBix-objects. This broker can be used
 * to retrieve following Data:
 *
 * <li><b>Dp without data:</b> Data points, which contain no data point data</li>
 * <li><b>Dp with data:</b> Data points, which contain data point data</li>
 * <li><b>List:</b> List, which contains all Dps, which contain no data point data</li>
 * <li><b>List:</b> List, which contains all Dps, which contain data point data</li>
 *
 * <li><b>Zone data:</b> A Zone, which contains Dps, which contain no data</li>
 * <li><b>Zone data:</b> A Zone, which contains Dps, which contain data</li>
 * <li><b>List:</b> List, which contains all Zones, which contain Dps, which contain no data point data</li>
 * <li><b>List:</b> List, which contains all Zones, which contain Dps, which contain data point data</li>
 *
 * <li><b>List:</b> List, which contains all Dps in a period of time, which contain data</li>
 *
 * Following data can be set:
 * <li><b>Dp without data:</b> Data point, which contains no data point data</li>
 * <li><b>Dp with data:</b> Data point, which contains data point data</li>
 *
 * @author Alexej Strelzow
 */
public interface IObjectBroker {

    /**
     * Incoming HTTP-Request should be: GET /obix/dp/{name}
     * @see bpi.most.obix.server.rest.ObixDpResource
     * @see bpi.most.obix.server.rest.impl.ObixDpResourceImpl
     *
     * @param user The user who requests it
     * @param dpDto The name of the data point to retrieve, wrapped in a DpDTO object
     *
     * @return One oBix object with the {@link Uri} <code>uri</code>,
     *         or <code>null</code>, if the <code>uri</code> is a wrong one.
     *         Dp contains only data, which belongs to Dp, no data point data.
     */
    Dp getDp(UserDTO user, DpDTO dpDto); // = data pointName = {name} /obix/dp/{name}

    /**
     * Incoming HTTP-Request should be: GET /obix/dp/{name}/data
     * @see bpi.most.obix.server.rest.ObixDpResource
     * @see bpi.most.obix.server.rest.impl.ObixDpResourceImpl
     *
     * @param user The user who requests it
     * @param dpDto The name of the data point to retrieve, wrapped in a DpDTO object
     *
     * @return One oBix object with the {@link Uri} <code>uri</code>,
     *         or <code>null</code>, if the <code>uri</code> is a wrong one.
     *         The latest measurement for the data point.
     */
    DpData getDpData(UserDTO user, DpDTO dpDto); // = datapointName = {name}

    /**
     * Incoming HTTP-Request should be: GET /obix/dp
     * @see bpi.most.obix.server.rest.ObixDpResource
     * @see bpi.most.obix.server.rest.impl.ObixDpResourceImpl
     *
     * @return All data points, which are cached in the broker,
     *         but without the data point data.
     */
    List getAllDps();
    // return list with all data points (Dp)

    /**
     * Incoming HTTP-Request should be: PUT /obix/dp/{name}
     * @see bpi.most.obix.server.rest.ObixDpResource
     * @see bpi.most.obix.server.rest.impl.ObixDpResourceImpl
     *
     * Adds the decoded Dp and its values to the server.
     *
     * @param user The user who requests it
     * @param dp An instance of Dp, which maybe contains new values
     */
    void addDp(UserDTO user, Dp dp);

    void addDpData(UserDTO user, DpDTO dpDto, DpData dpData);

    void updateDp(UserDTO user, Dp encodedDp);

    /**
     * Incoming HTTP-Request should be: GET /obix/dp/{name}/data?from={UTC datetime}&to={UTC datetime}
     * @see bpi.most.obix.server.rest.ObixDpResource
     * @see bpi.most.obix.server.rest.impl.ObixDpResourceImpl
     *
     * @param user The user who requests it
     * @param dpDto The name of the data point to retrieve, wrapped in a DpDTO object
     * @param from UTC Time-String, beginning of the retrieving interval
     * @param to UTC Time-String, end of the retrieving interval
     *
     * @return A list of data points + data, which is in the UTC time interval: [from; to].
     */
    HistoryQueryOutImpl getDpData(UserDTO user, DpDTO dpDto, String from, String to);
    // return list with all data points + data

    List getDpPeriodicData(UserDTO user, DpDTO dpDto, String from, String to, float period, int mode, int type);

    public bpi.most.obix.objects.Zone getZone(UserDTO user, ZoneDTO zone);

    /**
     * Incoming HTTP-Request should be: GET /obix/zones/{id}
     * @see bpi.most.obix.server.rest.ObixZoneResource
     * @see bpi.most.obix.server.rest.impl.ObixZoneResourceImpl
     *
     * @param user The user who requests it
     * @param zone The requested zone
     * @param level The levels within the zone
     * @return The zone, which contains data points, but the data points
     *         contain no data.
     */
    List getDpsForZone(UserDTO user, ZoneDTO zone, int level);
    // return zone with dp inside

    /**
     * Incoming HTTP-Request should be: GET /obix/zones/data
     * @see bpi.most.obix.server.rest.ObixZoneResource
     * @see bpi.most.obix.server.rest.impl.ObixZoneResourceImpl
     *
     * @param user The user who requests it
     * @return All objects, wrapped in their own zone, but with data
     */
    List getHeadZones(UserDTO user);
    // return list with all zones, which contain their dp, which contain their data

}
