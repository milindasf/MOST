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

    public static final String TIMESTAMP = "timestamp";
    public static final String VALUE = "value";
    public static final String QUALITY = "quality";

    private Dp parent;
    private long timestamp;
    private double value;
    private double quality = 1;

    public DpData(Dp parent, long timestamp, double value, double quality) {
        this.parent = parent;
        this.timestamp = timestamp;
        this.value = value;
        this.quality = quality;

        add(getTimestamp());
        add(getValue());
        add(getQuality());
        setIs(new Contract("obix:DatapointData"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Abstime getTimestamp() {
        return new Abstime(TIMESTAMP, timestamp);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Real getValue() {
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
        return new Uri(parent.getDatapointName().get()+"/data", Dp.OBIX_DP_PREFIX+parent.getDatapointName().get()+"/data");
    }

}
