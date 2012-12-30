/*
 * This code licensed to public domain
 */
package bpi.most.obix.objects;

/**
 * Int models a 64-bit integer number
 *
 * @author Brian Frank
 * @version $Revision$ $Date$
 * @creation 27 Apr 05
 */
public class Int
        extends Val {

    /**
     * Default min facet is Long.MIN_VALUE
     */
    public static final long MIN_DEFAULT = Long.MIN_VALUE;

    /**
     * Default max facet is Long.MAX_VALUE
     */
    public static final long MAX_DEFAULT = Long.MAX_VALUE;

    private long val;
    private long min = MIN_DEFAULT;
    private long max = MAX_DEFAULT;
    private Uri unit = null;

    /**
     * Construct named Int with specified value.
     */
    public Int(String name, long val) {
        super(name);
        set(val);
    }

    /**
     * Construct named Int with value of 0.
     */
    public Int(String name) {
        super(name);
        set(0);
    }

    /**
     * Construct unnamed Int with specified value.
     */
    public Int(long val) {
        set(val);
    }

    /**
     * Construct unnamed Int with value of 0.
     */
    public Int() {
        set(0);
    }

////////////////////////////////////////////////////////////////
// Int
////////////////////////////////////////////////////////////////

    /**
     * Get value as a long.
     */
    public long get() {
        return val;
    }

    /**
     * Set value.
     */
    public void set(long val) {
        this.val = val;
    }

////////////////////////////////////////////////////////////////
// Val
////////////////////////////////////////////////////////////////

    /**
     * Return "int".
     */
    public String getElement() {
        return "int";
    }

    /**
     * Return if specified Val has equivalent int value.
     */
    public boolean valEquals(Val that) {
        if (that instanceof Int) {
			return ((Int) that).val == val;
		}
        return false;
    }

    /**
     * Compares this object with the specified object for
     * order. Returns a negative integer, zero, or a positive
     * integer as this object is less than, equal to, or greater
     * than the specified object.
     */
    public int compareTo(Object that) {
        long a = val;
        long b = ((Int) that).val;
        if (a == b) {
			return 0;
		}
        if (a < b) {
			return -1;
		} else {
			return 1;
		}
    }

    /**
     * Encode the value as a string
     */
    public String encodeVal() {
        return String.valueOf(val);
    }

    /**
     * Decode the value from a string.
     */
    public void decodeVal(String val)
            throws Exception {
        this.val = Long.parseLong(val);
    }

    /**
     * Encode the value as a Java code literal to pass to the constructor.
     */
    public String encodeJava() {
        return String.valueOf(val) + "L";
    }

////////////////////////////////////////////////////////////////
// Facets
////////////////////////////////////////////////////////////////

    /**
     * Get the min facet or MIN_DEFAULT if unspecified.
     */
    public long getMin() {
        return min;
    }

    /**
     * Set the min facet.
     */
    public void setMin(long min) {
        this.min = min;
    }

    /**
     * Get the max facet or MAX_DEFAULT if unspecified.
     */
    public long getMax() {
        return max;
    }

    /**
     * Set the max facet.
     */
    public void setMax(long max) {
        this.max = max;
    }

    /**
     * Get the unit facet or null if unspecified.
     */
    public Uri getUnit() {
        return unit;
    }

    /**
     * Set the unit facet.
     */
    public void setUnit(Uri unit) {
        this.unit = unit;
    }

}
