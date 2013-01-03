package bpi.most.obix.objects;

import bpi.most.obix.comparator.DpDataComparator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * @author Alexej Strelzow
 */
public class Dp extends Obj {

    public static final String OBIX_DP_PREFIX = "/obix/dp/";

    public static final String ELEMENT_TAG = "dp";
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

    /**
     * Constructor, which will be called via reflection if
     * the object gets decoded.
     */
    public Dp() {
        dpData = new ArrayList<DpData>();
        dataComparator = new DpDataComparator();
    }

    public Dp(String dataPointName, String type, String description) {
        this();
        this.dataPointName = dataPointName;
        this.type = type;
        this.description = description;

        // lets set that to the default-value
        this.unit = new Uri("obix:units/celsius");
        this.setName(this.dataPointName);

        add(getDatapointName());
        add(getType());
        add(getDescription());
    }

    public Dp(String dataPointName, String type, String description, DpData[] dpData) {
        this(dataPointName, type, description);
        this.dpData.addAll(Arrays.asList(dpData));
    }

    public Str getDatapointName() {
        if (dataPointName == null) {
            Obj obj = get(DATA_POINT_NAME);
            if (obj != null) {
                return (Str)obj;
            }
        }
        return new Str(DATA_POINT_NAME, dataPointName);
    }

    public Str getType() {
        if (type == null) {
            Obj obj = get(TYPE);
            if (obj != null) {
                return (Str)obj;
            }
        }
        return new Str(TYPE, type);
    }

    public Str getDescription() {
        if (description == null) {
            Obj obj = get(DESCRIPTION);
            if (obj != null) {
                return (Str)obj;
            }
        }
        return new Str(DESCRIPTION, description);
    }

    public DpData[] getDpData() {
        if (this.dpData.isEmpty()) {
            java.util.List<DpData> kids = getKidsByClass(DpData.class);
            this.dpData.addAll(kids);
        }
        return this.dpData.toArray(new DpData[this.dpData.size()]);
    }

    public List getDpDataAsList() {
        if (this.dpData.isEmpty()) {
            java.util.List<DpData> kids = getKidsByClass(DpData.class);
            this.dpData.addAll(kids);
        }

        List list = new List(DP_DATA, new Contract("obix:dpData"));
        list.addAll(dpData.toArray(new DpData[dpData.size()]));
        return list;
    }

    public void addDpData(DpData dpData) {
        this.dpData.add(dpData);
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
        return new Uri(dataPointName, OBIX_DP_PREFIX + dataPointName);
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
