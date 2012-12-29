/*
 * This code licensed to public domain
 */
package bpi.most.obix.objects;

/**
 * Op is used to represent operation objects.
 *
 * @author Brian Frank
 * @version $Revision$ $Date$
 * @creation 27 Apr 05
 */
public class Op
        extends Obj {

    private Contract in;
    private Contract out;

    /**
     * Construct named Op with specified input and output parameters.
     */
    public Op(String name, Contract in, Contract out) {
        super(name);
        setIn(in);
        setOut(out);
    }

    /**
     * Construct named Op.
     */
    public Op(String name) {
        this(name, null, null);
    }

    /**
     * Construct unnamed Op.
     */
    public Op() {
        this(null, null, null);
    }

////////////////////////////////////////////////////////////////
// Op
////////////////////////////////////////////////////////////////

    /**
     * Get the input argument contract.
     */
    public Contract getIn() {
        return in;
    }

    /**
     * Set operations input contract.
     */
    public void setIn(Contract in) {
        this.in = (in != null) ? in : Contract.Obj;
    }

    /**
     * Get output contract.
     */
    public Contract getOut() {
        return out;
    }

    /**
     * Set the operatikons output contract.
     */
    public void setOut(Contract out) {
        this.out = (out != null) ? out : Contract.Obj;
    }

////////////////////////////////////////////////////////////////
// Obj
////////////////////////////////////////////////////////////////

    /**
     * Include signature in display string if display
     * attribute is unspecified.
     */
    public String toDisplayString() {
        if (getDisplay() != null) return getDisplay();

        String base;
        if (getIs() != null && getIs().size() > 0) base = getIs().toString();
        else base = "obix:" + getElement();

        return base + "(in=\"" + in + "\" out=\"" + out + "\")";
    }

    /**
     * Return "op".
     */
    public String getElement() {
        return "op";
    }

}
