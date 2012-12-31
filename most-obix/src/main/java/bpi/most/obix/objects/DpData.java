package bpi.most.obix.objects;

import bpi.most.obix.contracts.DatapointData;

/**
 * Obix object, which holds:<br>
 * <li>data value with unit, if given (e.g. celsius)</li>
 * <li>timestamp of the sample</li>
 * <li>quality of the data (default = 1)</li>
 *
 * @author Alexej Strelzow
 */
public class DpData extends Obj implements DatapointData {

    public static final String DP = "dp";
    public static final String TIMESTAMP = "timestamp";
    public static final String VALUE = "value";
    public static final String QUALITY = "quality";

    private Dp parent;
    //    private String dp; // == parent
    private long timestamp;
    private double value;
    private double quality = 1;

    /**
     * Constructor, which will be called via reflection if
     * the object gets decoded.
     */
    public DpData() {
        // noop
    }

    public DpData(Dp parent, long timestamp, double value, double quality) {
        this.parent = parent;
//    	this.dp = parent.getDatapointName().get();
        this.timestamp = timestamp;
        this.value = value;
        this.quality = quality;

        parent.add(this);

//        add(getDp()); // only interesting if we send data only (-> no link to parent)
        add(getTimestamp());
        add(getValue());
        add(getQuality());
//        setIs(new Contract("obix:DatapointData"));
    }

//    public Str getDp() {
//    	if (dp == null) {
//    		Obj obj = get(DP);
//    		if (obj != null) {
//    			dp = ((Str)obj).get();
//    		}
//    	}
//        return new Str(DP, dp);
//    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Abstime getTimestamp() {
        if (timestamp == 0) {
            Obj obj = get(TIMESTAMP);
            if (obj != null) {
                return (Abstime)obj;
            }
        }
        return new Abstime(TIMESTAMP, timestamp);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Real getValue() {
        if (value == 0.0d) {
            Obj obj = get(VALUE);
            if (obj != null) {
                return (Real)obj;
            }
        }

        Real real = new Real(VALUE, value);
        Uri unit = parent.getUnit();
        if (unit != null) {
            real.setUnit(unit);
        }

        return real;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Real getQuality() {
        if (quality == 0.0d) {
            Obj obj = get(QUALITY);
            if (obj != null) {
                return (Real)obj;
            }
        }

        return new Real(QUALITY, quality);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getElement() {
        return "dpData";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Uri getHref() {
        if (parent == null) {
            return super.getHref();
        }
        return new Uri(parent.getDatapointName().get()+"/data", Dp.OBIX_DP_PREFIX+parent.getDatapointName().get()+"/data");
    }

//    void removeDp() {
//    	remove(getDp());
//    }

}
