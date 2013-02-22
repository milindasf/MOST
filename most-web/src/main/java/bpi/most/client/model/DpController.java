package bpi.most.client.model;

import java.util.ArrayList;

import bpi.most.client.rpc.DatapointService;
import bpi.most.client.rpc.DatapointServiceAsync;

import com.google.gwt.core.client.GWT;

public class DpController {

	private ArrayList<Datapoint> datapoints = new ArrayList<Datapoint>();
	private static DpController ref = null;
	
	public static final DatapointServiceAsync DP_SERVICE = GWT
		      .create(DatapointService.class);
	
	public DpController() {
		super();
	}
	
	public static DpController getSingleton(){
		if (ref == null) {
			ref = new DpController();
		}
		return ref;
	}
	
	private Datapoint lookupDatapoint(String datapointname) {
		for (int i = 0; i < datapoints.size(); i++) {
			String datapointnametemp = datapoints.get(i).getDatapointName();
			if (datapointnametemp.equalsIgnoreCase(datapointname)) {
				return datapoints.get(i);
			}
		}
		return null;
	}

	public Datapoint getDatapoint(String datapointname) {
		Datapoint temp = lookupDatapoint(datapointname);
		if (temp != null) {
			return temp;
		} else {
			temp = new Datapoint(datapointname);
			datapoints.add(temp);
		}
		return temp;
	}


}
