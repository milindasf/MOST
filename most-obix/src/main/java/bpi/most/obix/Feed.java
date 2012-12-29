/*
 * This code licensed to public domain
 */
package bpi.most.obix;


/**
 * Feed models a event feed topic.
 *
 * @author Brian Frank
 * @version $Revision$ $Date$
 * @creation 30 Mar 06
 */
public class Feed
        extends Obj {

////////////////////////////////////////////////////////////////
// Construction
////////////////////////////////////////////////////////////////

    /**
     * Construct named Feed with in and of contracts.
     */
    public Feed(String name, Contract in, Contract of) {
        super(name);
        setIn(in);
        setOf(of);
    }

    /**
     * Construct named Feed.
     */
    public Feed(String name) {
        this(name, null, null);
    }

    /**
     * Construct unnamed Feed.
     */
    public Feed() {
        this(null, null, null);
    }

////////////////////////////////////////////////////////////////
// Feed
////////////////////////////////////////////////////////////////

    /**
     * Get input contract.
     */
    public Contract getIn() {
        return in;
    }

    /**
     * Set the input contract.
     */
    public void setIn(Contract in) {
        this.in = (in != null) ? in : Contract.Obj;
    }

    /**
     * Get of contract.
     */
    public Contract getOf() {
        return of;
    }

    /**
     * Set the of contract.
     */
    public void setOf(Contract of) {
        this.of = (of != null) ? of : Contract.Obj;
    }

////////////////////////////////////////////////////////////////
// Obj
////////////////////////////////////////////////////////////////

    /**
     * Return "feed".
     */
    public String getElement() {
        return "feed";
    }

////////////////////////////////////////////////////////////////
// Fields
////////////////////////////////////////////////////////////////

    private Contract in;
    private Contract of;

}
