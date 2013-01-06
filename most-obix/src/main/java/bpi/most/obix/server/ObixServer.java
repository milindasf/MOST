package bpi.most.obix.server;

import bpi.most.obix.io.ObixDecoder;
import bpi.most.obix.io.ObixEncoder;
import bpi.most.obix.objects.Dp;
import bpi.most.obix.objects.List;
import bpi.most.obix.objects.Uri;
import bpi.most.obix.objects.Zone;

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
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateDp(String encodedDp) {
        if (encodedDp != null) {
            Dp dp = (Dp) ObixDecoder.fromString(encodedDp);
            if (dp != null) {
                objectBroker.updateDatapoint(dp);
            }
        }
    }

}
