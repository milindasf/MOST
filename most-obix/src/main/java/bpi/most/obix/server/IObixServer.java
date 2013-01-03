package bpi.most.obix.server;

import java.net.URI;

/**
 * The oBIX server acts as a gateway between incoming requests from outside, for
 * example an HTTP server, and the internal object broker. It also translates
 * between XML and java-representation of oBIX objects. In the future, it will
 * be the place, where user administration and permission based degradation via
 * the object filter will be realised.
 *
 * @author Matthias Neugschwandtner
 */
public interface IObixServer {
    /**
     * The object specified via the URI is read by pulling it from the object broker, if the
     * user has the right to do so.
     *
     * @param href URI of the object to be read.
     * @param user Name of the user who wants to read the object.
     * @return XML representation of the object to be read.
     */
    String readObj(URI href, String user);

    /**
     * The object specified via the URI is written by pushing it to the object
     * broker.
     *
     * @param href      URI of the object to be written
     * @param xmlStream XML representation of the object to be written.
     * @return XML representation of the written object.
     */
    String writeObj(URI href, String xmlStream);

    /**
     * The operation specified via the URI is invoked by pushing it to the
     * object broker.
     *
     * @param href      URI of the operation to be invoked.
     * @param xmlStream Parameters of the operation to be invoked.
     * @return XML representation of the output parameters of the operation.
     */
    String invokeOp(URI href, String xmlStream);
}
