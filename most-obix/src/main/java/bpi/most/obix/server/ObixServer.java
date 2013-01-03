package bpi.most.obix.server;

import bpi.most.obix.io.ObixDecoder;
import bpi.most.obix.io.ObixEncoder;
import bpi.most.obix.objects.*;

import java.net.URI;

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
    public String getDatapoint(URI href) {
        if (href != null) {
            Uri uri = new Uri(href.toASCIIString());
            Dp dp = objectBroker.getDatapoint(uri);
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
    public String getDatapointData(URI href) {
        if (href != null) {
            Uri uri = new Uri(href.toASCIIString());
            Dp dp = objectBroker.getDatapointData(uri);
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
    public String getAllDatapoints() {
        List dataPoints = objectBroker.getAllDatapoints();
        if (dataPoints != null) {
            return ObixEncoder.toString(dataPoints);
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDatapointsForZone(URI href) {
        if (href != null) {
            Uri uri = new Uri(href.toASCIIString());
            Zone zone = objectBroker.getDatapointsForZone(uri);
            if (zone != null) {
                return ObixEncoder.toString(zone);
            }
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDatapointsForAllZones() {
        List dataPoints = objectBroker.getDatapointsForAllZones();
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
    public void updateDatapoint(String encodedDp) {
        if (encodedDp != null) {
            Dp dp = (Dp) ObixDecoder.fromString(encodedDp);
            if (dp != null) {
                objectBroker.updateDatapoint(dp);
            }
        }
    }

}
