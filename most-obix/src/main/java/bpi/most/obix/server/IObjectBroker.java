package bpi.most.obix.server;

import bpi.most.obix.objects.Dp;
import bpi.most.obix.objects.List;
import bpi.most.obix.objects.Uri;
import bpi.most.obix.objects.Zone;

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
     * @param href The uri of the oBix object, which is: /obix/dp/{datapointName}
     *            of the data point to retrieve.
     * @return One oBix object with the {@link Uri} <code>uri</code>,
     *         or <code>null</code>, if the <code>uri</code> is a wrong one.
     *         Dp contains only data, which belongs to Dp, no data point data.
     */
    Dp getDp(Uri href); // = data pointName = {name} /obix/dp/{name}

    /**
     * Incoming HTTP-Request should be: GET /obix/dp/{name}/data
     * @see bpi.most.obix.server.rest.ObixDpResource
     * @see bpi.most.obix.server.rest.impl.ObixDpResourceImpl
     *
     * @param href The uri of the oBix object, which is /obix/dp/{datapointName}
     *            of the data point to retrieve.
     * @return One oBix object with the {@link Uri} <code>uri</code>,
     *         or <code>null</code>, if the <code>uri</code> is a wrong one.
     *         Dp contains all data from all DpData.
     */
    Dp getDpData(Uri href); // = datapointName = {name}

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
     * Incoming HTTP-Request should be: GET /obix/dp/data
     * @see bpi.most.obix.server.rest.ObixDpResource
     * @see bpi.most.obix.server.rest.impl.ObixDpResourceImpl
     *
     * @return All data points and its data, which are cached in the broker.
     */
    List getAllDpData();
    // return list with all data points with data included

    /**
     * Incoming HTTP-Request should be: GET /obix/dp/{name}/data?from={UTC datetime}&to={UTC datetime}
     * @see bpi.most.obix.server.rest.ObixDpResource
     * @see bpi.most.obix.server.rest.impl.ObixDpResourceImpl
     *
     * @param from UTC Time-String, beginning of the retrieving interval
     * @param to UTC Time-String, end of the retrieving interval
     * @return A list of data points + data, which is in the UTC time interval: [from; to].
     */
    List getDpData(String from, String to);
    // return list with all data points + data


    /**
     * Incoming HTTP-Request should be: PUT /obix/dp/{name}
     * @see bpi.most.obix.server.rest.ObixDpResource
     * @see bpi.most.obix.server.rest.impl.ObixDpResourceImpl
     *
     * Adds the decoded Dp and its values to the server.
     *
     * @param dp An instance of Dp, which maybe contains new values
     */
    void addDp(Dp dp);  // TODO

    /**
     * Incoming HTTP-Request should be: GET /obix/zones/{id}
     * @see bpi.most.obix.server.rest.ObixZoneResource
     * @see bpi.most.obix.server.rest.impl.ObixZoneResourceImpl
     *
     * @param href The uri of the zone
     * @return The zone, which contains data points, but the data points
     *         contain no data.
     */
    Zone getDpsForZone(Uri href);
    // return zone with dp inside

    /**
     * Incoming HTTP-Request should be: GET /obix/zones/{id}/data
     * @see bpi.most.obix.server.rest.ObixZoneResource
     * @see bpi.most.obix.server.rest.impl.ObixZoneResourceImpl
     *
     * @param href The uri of the zone
     * @return The zone, which contains data points + their data
     */
    Zone getDpDataForZone(Uri href);
    // return zone with dp inside, which have data inside

    /**
     * Incoming HTTP-Request should be: GET /obix/zones
     * @see bpi.most.obix.server.rest.ObixZoneResource
     * @see bpi.most.obix.server.rest.impl.ObixZoneResourceImpl
     *
     * @return All objects, wrapped in their own zone, but without data
     */
    List getAllZones();
    // return list with all zones, which contain their dp

    /**
     * Incoming HTTP-Request should be: GET /obix/zones/data
     * @see bpi.most.obix.server.rest.ObixZoneResource
     * @see bpi.most.obix.server.rest.impl.ObixZoneResourceImpl
     *
     * @return All objects, wrapped in their own zone, but with data
     */
    List getDpsForAllZones();
    // return list with all zones, which contain their dp, which contain their data

    /**
     * Incoming HTTP-Request should be: GET /obix/zones/{id}/data
     * @see bpi.most.obix.server.rest.ObixZoneResource
     * @see bpi.most.obix.server.rest.impl.ObixZoneResourceImpl
     *
     * @param href The uri of the zone
     * @param from UTC Time-String, beginning of the retrieving interval
     * @param to UTC Time-String, end of the retrieving interval
     * @return A list of data points + data for the zone with the uri
     *         <code>href</code>. The data is in the UTC time interval:
     *         [from; to].
     */
    List getDpForZone(Uri href, String from, String to);

}
