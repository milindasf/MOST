/*
 * This code licensed to public domain
 */
package bpi.most.obix.objects;


/**
 * Ref models a reference object.
 *
 * @author Brian Frank
 * @version $Revision$ $Date$
 * @creation 30 Mar 06
 */
public class Ref
        extends Obj {

    /**
     * Construct named Ref with specified and and href.
     */
    public Ref(String name, Uri href) {
        super(name);
        setHref(href);
    }

    /**
     * Construct named Ref.
     */
    public Ref(String name) {
        super(name);
    }

    /**
     * Construct unnamed Ref.
     */
    public Ref() {
    }

////////////////////////////////////////////////////////////////
// Obj
////////////////////////////////////////////////////////////////

    /**
     * Return "ref".
     */
    public String getElement() {
        return "ref";
    }

    /**
     * Debug to string is href
     */
    public final String toString() {
        Uri href = getHref();
        if (href == null) {
			return "null";
		}
        return href.toString();
    }

}
