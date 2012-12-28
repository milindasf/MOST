package bpi.most.obix.server;

import java.util.HashMap;

import bpi.most.obix.Dp;
import bpi.most.obix.DpData;
import bpi.most.obix.List;
import bpi.most.obix.Uri;

public interface IObjectBroker {

	/**
	 * Loads all Datapoints, which are in the DB, converts them
	 * to oBix objects and finally caches them in a {@link HashMap}. 
	 */
	void loadDatapoints();
	
	/**
	 * GET /obix/dp/{name}
	 * 
	 * @param uri The uri of the oBix object, which is the datapointName
	 * 	of the Datapoint to retrieve.
	 * @return One oBix object with the {@link Uri} <code>uri</code>,
	 * 	or <code>null</code>, if the <code>uri</code> is a wrong one.
	 */
	Dp getDatapoint(String uri); // = datapointName = {name}
	
	/**
	 * GET /obix/dp/{name}/data
	 * 
	 * @param uri The uri of the oBix object, which is the datapointName
	 * 	of the Datapoint to retrieve.
	 * @return One oBix object with the {@link Uri} <code>uri</code>,
	 * 	or <code>null</code>, if the <code>uri</code> is a wrong one.
	 */
	DpData getDatapointData(String uri); // = datapointName = {name}
	
	/**
	 * GET /obix/dp
	 * 
	 * @return All data points
	 */
	List getAllDatapoints();
	// return list with all data points
	
	/**
	 * GET /obix/zones/{id}
	 * 
	 * @param zone The id of the zone
	 * @return The zone, which contains data points
	 */
	List getDatapointsForZone(String zone); // = {id}
	// return list with zones
	
	/**
	 * GET /obix/zones
	 * 
	 * @return All objects, wrapped in their own zone
	 */
	List getDatapointsForAllZones();
	// return list with all zones
	
	/**
	 * GET /obix/dp/{name}/data?from={UCT datetime}&to=
	 * 	{UCT datetime}
	 * 
	 * @param from
	 * @param to
	 * @return
	 */
	List getDatapoints(String from, String to); 
	// return list with all datapoint data, see DpDataDTO (!)
	
	
	/**
	 * PUT /obix/dp/{name}
	 * 
	 * Update for the data point with the {@link Uri} <code>{name}</code>
	 * 
	 * @param uri The uri of the oBix object, which is the datapointName
	 * 	of the Datapoint to update.
	 */
	void updateDatapoint(String uri);
	
}
