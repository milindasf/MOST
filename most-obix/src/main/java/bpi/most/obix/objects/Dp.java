package bpi.most.obix.objects;

import bpi.most.obix.contracts.Datapoint;

/**
 * @author Alexej Strelzow
 */
public class Dp extends Obj implements Datapoint {

    public static final String DATA_POINT_NAME = "dataPointName";
    public static final String TYPE = "type";
    public static final String DESCRIPTION = "description";
    public static final String DP_DATA = "dpData";

    private String dataPointName;
    private String type;
    private String description;
    private DpData[] dpData;

    public Dp(String dataPointName, String type, String description) {
        this.dataPointName = dataPointName;
        this.type = type;
        this.description = description;
    }

    public Dp(String dataPointName, String type, String description, DpData[] dpData) {
        this(dataPointName, type, description);
        this.dpData = dpData;
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

    @Override
    public List getDpData() {
        List list = new List(DP_DATA, new Contract("obix:DatapointData"));
        list.addAll(dpData);
        return list;
    }

//	public void setDpData(List dpData) {
//		this.dpData = dpData;
//	}

}
