package bpi.most.obix.server;

import bpi.most.dto.DpDTO;
import bpi.most.dto.UserDTO;
import bpi.most.dto.ZoneDTO;
import bpi.most.obix.history.HistoryQueryOutImpl;
import bpi.most.obix.history.HistoryRollupOutImpl;
import bpi.most.obix.objects.*;

/**
 * Loads and caches all oBix-objects. This broker can be used
 * to retrieve following Data:
 *
 * <li><b>Dp without data:</b> Data points, which contain no data point data</li>
 * <li><b>Dp with data:</b> Data points, which contain data point data</li>
 * <li><b>List:</b> List, which contains all Dps, which contain no data point data</li>
 * <li><b>HistoryQueryOutImpl:</b> Historical data in a certain time frame</li>
 * <li><b>HistoryRollupOutImpl:</b> Historical rollup data in a certain time frame, mode, period and interval</li>
 *
 * <li><b>Zone data:</b> A Zone</li>
 * <li><b>List of Dp from zone:</b> List, which contains data points of a specific zone, with a specific level</li>
 * <li><b>List of Zones:</b> List, which contains the head zones</li>
 *
 * <li><b>List:</b> List, which contains all Dps in a period of time, which contain data</li>
 *
 * Following data can be created:
 * <li><b>Dp without data:</b> Data point, which contains no data point data</li>
 * <li><b>Dp with data:</b> Data point, which contains data point data</li>
 *  <li><b>DpData:</b> Data point data, for a given data point </li>
 *
 * @author Alexej Strelzow
 */
public interface IObjectBroker {

    /**
     * see {@link IObixServer#getDp(bpi.most.dto.UserDTO, bpi.most.dto.DpDTO)}
     *
     * @return The corresponding oBix object
     */
    Dp getDp(UserDTO user, DpDTO dpDto);

    /**
     * see {@link IObixServer#getDpData(bpi.most.dto.UserDTO, bpi.most.dto.DpDTO)}
     *
     * @return The corresponding oBix object
     */
    DpData getDpData(UserDTO user, DpDTO dpDto);

    /**
     * see {@link bpi.most.obix.server.IObixServer#getAllDps()}
     *
     * @return The corresponding oBix object - all data points in a list
     */
    List getAllDps();

    /**
     * see {@link IObixServer#addDp(bpi.most.dto.UserDTO, String)}
     */
    void addDp(UserDTO user, Dp dp);

    /**
     * see {@link IObixServer#addDpData(bpi.most.dto.UserDTO, bpi.most.dto.DpDTO, String)}
     */
    void addDpData(UserDTO user, DpDTO dpDto, DpData dpData);

    /**
     * see {@link IObixServer#updateDp(bpi.most.dto.UserDTO, bpi.most.dto.DpDTO, String)}
     */
    void updateDp(UserDTO user, Dp encodedDp);

    /**
     * see {@link IObixServer#getDpData(bpi.most.dto.UserDTO, bpi.most.dto.DpDTO, String, String)}
     *
     * @return The corresponding oBix object
     */
    HistoryQueryOutImpl getDpData(UserDTO user, DpDTO dpDto, String from, String to);

    /**
     * see {@link IObixServer#getDpPeriodicData(bpi.most.dto.UserDTO, bpi.most.dto.DpDTO, String, String, float, int, int)}
     *
     * @return The corresponding oBix object
     */
    HistoryRollupOutImpl getDpPeriodicData(UserDTO user, DpDTO dpDto, String from, String to, float period, int mode, int interval);

    /**
     * see {@link IObixServer#getZone(bpi.most.dto.UserDTO, bpi.most.dto.ZoneDTO)}
     *
     * @return The corresponding oBix object
     */
    public bpi.most.obix.objects.Zone getZone(UserDTO user, ZoneDTO zone);

    /**
     * see {@link IObixServer#getDpsForZone(bpi.most.dto.UserDTO, bpi.most.dto.ZoneDTO, int)}
     *
     * @return The corresponding oBix object - list of data points
     */
    List getDpsForZone(UserDTO user, ZoneDTO zone, int level);

    /**
     * see {@link IObixServer#getHeadZones(bpi.most.dto.UserDTO)}
     *
     * @return The corresponding oBix object - list of zones
     */
    List getHeadZones(UserDTO user);

}
