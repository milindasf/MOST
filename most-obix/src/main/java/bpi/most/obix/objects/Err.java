/*
 * This code licensed to public domain
 */
package bpi.most.obix.objects;


/**
 * Err models an error object.
 *
 * @author Brian Frank
 * @version $Revision$ $Date$
 * @creation 30 Mar 06
 */
public class Err
        extends Obj {

    /**
     * Construct named Err.
     */
    public Err(String name) {
        super(name);
    }

    /**
     * Construct unnamed Err.
     */
    public Err() {
    }

    /**
     * Return "err".
     */
    public String getElement() {
        return "err";
    }

    /**
     * Format the error for human display.
     */
    public String format() {
        // TODO - displayName, is, etc
        dump();
        return this.toString();
    }

}
