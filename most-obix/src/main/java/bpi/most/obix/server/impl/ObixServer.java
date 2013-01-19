package bpi.most.obix.server.impl;

import bpi.most.dto.DpDTO;
import bpi.most.dto.UserDTO;
import bpi.most.dto.ZoneDTO;
import bpi.most.obix.history.HistoryQueryOutImpl;
import bpi.most.obix.history.HistoryRollupOutImpl;
import bpi.most.obix.io.ObixDecoder;
import bpi.most.obix.io.ObixEncoder;
import bpi.most.obix.objects.Dp;
import bpi.most.obix.objects.DpData;
import bpi.most.obix.objects.List;
import bpi.most.obix.objects.Zone;
import bpi.most.obix.server.IObixServer;
import bpi.most.obix.server.IObjectBroker;

import javax.inject.Inject;

/**
 * The oBIX server acts as a gateway between incoming requests from outside, for
 * example an HTTP server, and the internal object broker. It also translates
 * between XML and java-representation of oBIX objects.
 *
 * @author Alexej Strelzow
 */
public class ObixServer implements IObixServer {

    @Inject
    private IObjectBroker objectBroker;

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDp(UserDTO user, DpDTO dpDto) {
        Dp dp = objectBroker.getDp(user, dpDto);
        if (dp != null) {
            return ObixEncoder.toString(dp);
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDpData(UserDTO user, DpDTO dpDto) {
        DpData dp = objectBroker.getDpData(user, dpDto);
        if (dp != null) {
            return ObixEncoder.toString(dp);
        }
        return null;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void addDp(UserDTO user, String encodedDp) {
        if (encodedDp != null) {
            Dp dp = (Dp) ObixDecoder.fromString(encodedDp);
            if (dp != null) {
                objectBroker.addDp(user, dp);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addDpData(UserDTO user, DpDTO dpDto, String encodedDpData) {
        if (encodedDpData != null) {
            DpData dpData = (DpData) ObixDecoder.fromString(encodedDpData);
            if (dpData != null) {
                objectBroker.addDpData(user, dpDto, dpData);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateDp(UserDTO user, String dpName, String encodedDp) {
        if (encodedDp != null) {
            Dp dp = (Dp) ObixDecoder.fromString(encodedDp);
            if (dp != null) {
                objectBroker.updateDp(user, dp);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getAllDps() {
        List dataPoints = objectBroker.getAllDps();
        if (dataPoints != null) {
            return ObixEncoder.toString(dataPoints);
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDpData(UserDTO user, DpDTO dpDto, String from, String to) {
        HistoryQueryOutImpl queryOutput = objectBroker.getDpData(user, dpDto, from, to);

        return ObixEncoder.toString(queryOutput);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDpPeriodicData(UserDTO user, DpDTO dpDto, String from, String to, float period, int mode, int type) {
        HistoryRollupOutImpl rollupOutput = objectBroker.getDpPeriodicData(user, dpDto, from, to, period, mode, type);

        return ObixEncoder.toString(rollupOutput);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getZone(UserDTO user, ZoneDTO zoneDto) {
        Zone z = objectBroker.getZone(user, zoneDto);
        if (z != null) {
            return ObixEncoder.toString(z);
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDpsForZone(UserDTO user, ZoneDTO zoneDto, int level) {

        List list = objectBroker.getDpsForZone(user, zoneDto, level);
        if (list != null) {
            return ObixEncoder.toString(list);
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getHeadZones(UserDTO user) {
        List dataPoints = objectBroker.getHeadZones(user);
        if (dataPoints != null) {
            return ObixEncoder.toString(dataPoints);
        }
        return null;
    }

}
