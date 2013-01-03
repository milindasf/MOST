package bpi.most.obix.objects;

/**
 * Obix object, which encapsulates:<br>
 * <li>data value with unit, if given (e.g. celsius)</li>
 * <li>timestamp of the sample</li>
 * <li>quality of the data (default = 1)</li>
 *
 * @author Alexej Strelzow
 */
public class DpData extends Obj {

    public static final String ELEMENT_TAG = "dpData";
    public static final String TIMESTAMP = "timestamp";
    public static final String VALUE = "value";
    public static final String QUALITY = "quality";

    public static final String UNKNOWN_DP_NAME = "<unknown>";

    private Dp parent;
    //    private String dp; // == parent
    private long timestamp;
    private double value;
    private double quality = 1;

    private String datapointName;
    private String type;

    /**
     * Constructor, which will be called via reflection if
     * the object gets decoded.
     */
    public DpData() {
        // noop
    }

    public DpData(Dp parent, long timestamp, double value, double quality) {
        if (parent == null) {
            throw new IllegalArgumentException("Parent is not allowed to be null!");
        }

        this.parent = parent;
        this.datapointName = parent.getDatapointName().get();
        this.type = parent.getType().get();

        this.timestamp = timestamp;
        this.value = value;
        this.quality = quality;

        this.setName(this.datapointName + "/data/" + timestamp);

        add(getTimestamp());
        add(getValue());
        add(getQuality());
    }

    // or is this one better? No dependency to parent =)
    public DpData(String datapointName, String type, long timestamp, double value, double quality) {
        this.datapointName = datapointName;
        this.type = type;                   // we should be able to get the unit out of it, right?
        this.timestamp = timestamp;
        this.value = value;
        this.quality = quality;

        add(getTimestamp());
        add(getValue());
        add(getQuality());
    }

    public Abstime getTimestamp() {
        if (timestamp == 0) {
            Obj obj = get(TIMESTAMP);
            if (obj != null) {
                return (Abstime)obj;
            }
        }
        return new Abstime(TIMESTAMP, timestamp);
    }

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
        return ELEMENT_TAG;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Uri getHref() {
        if (this.datapointName == null) {
            if (super.getHref() != null) {
                return super.getHref();
            } else {
                this.datapointName = UNKNOWN_DP_NAME;
            }
        }
        return new Uri(this.datapointName + "/data", Dp.OBIX_DP_PREFIX + this.datapointName + "/data" + timestamp);
    }

}