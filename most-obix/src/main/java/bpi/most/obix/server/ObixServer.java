package bpi.most.obix.server;

import bpi.most.obix.objects.Err;
import bpi.most.obix.objects.Obj;
import bpi.most.obix.io.ObixDecoder;
import bpi.most.obix.io.ObixEncoder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;

public class ObixServer implements IObixServer {
    private IObjectBroker objectBroker;

    public ObixServer() {
        char[] inC = {'a'};
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        objectBroker = new ObixObjectBroker();


        try {
            new HTTPServer(80, this);
            while (inC[0] != 'q')
                in.read(inC);
        } catch (IOException ioex) {
            ioex.printStackTrace();
            System.exit(1);
        }

        System.exit(0);
    }


    public String readObj(URI href, String user) {
        System.out.println("read request on: " + href.getPath());
//		Obj o = _objectBroker.pullObj(new Uri(href.toASCIIString()));
//		o = ObjectFilter.filterObject(o, 0);
//		return ObixEncoder.toString(o);

        return null;
    }

    public String writeObj(URI href, String xmlStream) {
        Obj input = ObixDecoder.fromString(xmlStream);

        try {
//			_objectBroker.pushObj(new Uri(href.toASCIIString()), input, false);
        } catch (Exception ex) {
            Err e = new Err("Error writing object to network" + ex.getMessage());
            return ObixEncoder.toString(e);
        }

        return xmlStream;
    }

    public String invokeOp(URI href, String xmlStream) {
        Obj input = ObixDecoder.fromString(xmlStream);

        try {
            // do nothing
        } catch (Exception ex) {
            Err e = new Err("Error invoking operation" + ex.getMessage());
            ex.printStackTrace();
            return ObixEncoder.toString(e);
        }

        return ObixEncoder.toString(new Obj());
    }

    /**
     * Starts a new oBIX server.
     *
     * @param args No command line parameters available.
     */
    public static void main(String[] args) {
        new ObixServer();
    }

}
