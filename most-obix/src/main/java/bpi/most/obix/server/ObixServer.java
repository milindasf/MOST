package bpi.most.obix.server;

import bpi.most.obix.io.ObixDecoder;
import bpi.most.obix.io.ObixEncoder;
import bpi.most.obix.objects.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;

public class ObixServer implements IObixServer {

    private IObjectBroker objectBroker;

    public ObixServer() {

    }

    @Override
    public String getDatapoint(URI href) {
        Uri uri = new Uri(href.toASCIIString());
        Dp dp = objectBroker.getDatapoint(uri);
        if (dp != null) {
            return ObixEncoder.toString(dp);
        }
        return null;
    }

    @Override
    public String getDatapointData(URI href) {
        Uri uri = new Uri(href.toASCIIString());
        Dp dp = objectBroker.getDatapointData(uri);
        if (dp != null) {
            return ObixEncoder.toString(dp);
        }
        return null;
    }

    @Override
    public String getAllDatapoints() {
        List dataPoints = objectBroker.getAllDatapoints();
        if (dataPoints != null) {
            return ObixEncoder.toString(dataPoints);
        }
        return null;
    }

    @Override
    public String getDatapointsForZone(URI href) {
        Uri uri = new Uri(href.toASCIIString());
        Zone zone = objectBroker.getDatapointsForZone(uri);
        if (zone != null) {
            return ObixEncoder.toString(zone);
        }
        return null;
    }

    @Override
    public String getDatapointsForAllZones() {
        List dataPoints = objectBroker.getDatapointsForAllZones();
        if (dataPoints != null) {
            return ObixEncoder.toString(dataPoints);
        }
        return null;
    }

    @Override
    public String getDatapoints(String from, String to) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void updateDatapoint(URI href) {
        Uri uri = new Uri(href.toASCIIString());

    }

}
