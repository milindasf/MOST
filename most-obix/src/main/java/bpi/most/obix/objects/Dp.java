package bpi.most.obix.objects;

import bpi.most.obix.comparator.DpDataComparator;
import bpi.most.obix.contracts.Datapoint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * @author Alexej Strelzow
 */
public class Dp extends Obj implements Datapoint {

    public static final String OBIX_DP_PREFIX = "/obix/dp/";

    public static final String DATA_POINT_NAME = "dataPointName";
    public static final String TYPE = "type";
    public static final String DESCRIPTION = "description";
    public static final String DP_DATA = "dpData";

    private String dataPointName;
    private String type;
    private String description;
    private java.util.List<DpData> dpData;

    private Uri unit;

    private DpDataComparator dataComparator;

    private boolean showData = false;

    public Dp(String dataPointName, String type, String description) {
        this.dataPointName = dataPointName;
        this.type = type;
        this.description = description;
        dpData = new ArrayList<DpData>();
        dataComparator = new DpDataComparator();

        // lets set that to the default-value
        this.unit = new Uri("obix:units/celsius");

        add(getDatapointName());
        add(getType());
        add(getDescription());
        setIs(new Contract("obix:Datapoint"));
    }

    public Dp(String dataPointName, String type, String description, DpData[] dpData) {
        this(dataPointName, type, description);
        this.dpData.addAll(Arrays.asList(dpData));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Str getDatapointName() {
        return new Str(DATA_POINT_NAME, dataPointName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Str getType() {
        return new Str(TYPE, type);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Str getDescription() {
        return new Str(DESCRIPTION, description);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List getDpData() {
        List list = new List(DP_DATA, new Contract("obix:DatapointData"));
        list.addAll(dpData.toArray(new DpData[dpData.size()]));
        return list;
    }

    public void addDpData(DpData dpData) {
        this.dpData.add(dpData);
        Collections.sort(this.dpData, this.dataComparator);

        if (showData) {
            add(dpData);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getElement() {
        return "dp";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Uri getHref() {
        return new Uri(dataPointName, OBIX_DP_PREFIX+dataPointName);
    }

    public void setShowData(boolean showData) {
        this.showData = showData;

        if (this.showData) {
            if (!dpData.isEmpty()) {
                Collections.sort(this.dpData, this.dataComparator);
                addAll(dpData.toArray(new DpData[dpData.size()]));
            }
        } else {
            for (DpData data : dpData) {
                remove(data);
            }
        }
    }

    public Uri getUnit() {
        return unit;
    }
}
