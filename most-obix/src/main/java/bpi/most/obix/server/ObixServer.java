package bpi.most.obix.server;

import bpi.most.obix.io.ObixDecoder;
import bpi.most.obix.io.ObixEncoder;
import bpi.most.obix.objects.Dp;
import bpi.most.obix.objects.List;
import bpi.most.obix.objects.Uri;
import bpi.most.obix.objects.Zone;

/**
 * The oBIX server acts as a gateway between incoming requests from outside, for
 * example an HTTP server, and the internal object broker. It also translates
 * between XML and java-representation of oBIX objects.
 *
 * @author Alexej Strelzow
 */
public class ObixServer implements IObixServer {

    private IObjectBroker objectBroker;

    public ObixServer() {
        if (objectBroker == null) {
            objectBroker = new ObixObjectBroker();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDp(String name) {
        if (name != null && !name.isEmpty()) {
            Uri uri = new Uri(Dp.OBIX_DP_PREFIX + name);
            Dp dp = objectBroker.getDp(uri);
            if (dp != null) {
                return ObixEncoder.toString(dp);
            }
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDpData(String name) {
        if (name != null && !name.isEmpty()) {
            Uri uri = new Uri(Dp.OBIX_DP_PREFIX + name);
            Dp dp = objectBroker.getDpData(uri);
            if (dp != null) {
                return ObixEncoder.toString(dp);
            }
        }
        return null;
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
    public String getAllDpData() {
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
    public String getDpsForZone(int id) {
        Uri uri = new Uri(Zone.OBIX_ZONE_PREFIX + id);
        Zone zone = objectBroker.getDpsForZone(uri);
        if (zone != null) {
            return ObixEncoder.toString(zone);
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDpDataForZone(int id) {
        Uri uri = new Uri(Zone.OBIX_ZONE_PREFIX + id);
        Zone zone = objectBroker.getDpDataForZone(uri);
        if (zone != null) {
            return ObixEncoder.toString(zone);
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDpForZone(int id, String from, String to) {
        Uri uri = new Uri(Zone.OBIX_ZONE_PREFIX + id);
        Zone zone = objectBroker.getDpsForZone(uri);
        if (zone != null) {
            return ObixEncoder.toString(zone);
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDpsForAllZones() {
        List dataPoints = objectBroker.getDpsForAllZones();
        if (dataPoints != null) {
            return ObixEncoder.toString(dataPoints);
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getAllZones() {
        List dataPoints = objectBroker.getAllZones();
        if (dataPoints != null) {
            return ObixEncoder.toString(dataPoints);
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDatapoints(String from, String to) {
        List dataPoints = objectBroker.getDatapoints(from, to);
        if (dataPoints != null) {
            return ObixEncoder.toString(dataPoints);
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addDp(String encodedDp) {
        if (encodedDp != null) {
            Dp dp = (Dp) ObixDecoder.fromString(encodedDp);
            if (dp != null) {
                objectBroker.addDp(dp);
            }
        }
    }

}
