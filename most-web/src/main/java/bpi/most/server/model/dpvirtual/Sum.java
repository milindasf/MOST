package bpi.most.server.model.dpvirtual;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import bpi.most.server.model.Datapoint;
import bpi.most.server.model.DpVirtualFactory;
import bpi.most.server.model.Zone;
import bpi.most.server.model.ZoneController;
import bpi.most.dto.DpDataDTO;
import bpi.most.dto.DpDatasetDTO;

/**
 * ID = "Sum"
 * The class Sum sums up all physical sensor data (no virtual dps!) within its zone.
 * If dpType is provided, only the defined types are summed up.
 * 
 * The following custom attributes can be provided:
 * - A) (optional) dpType - String of datapoints type that should be summed up
 * 
 * Example:
 * dpType power;
 * 
 * @author robert.zach@tuwien.ac.at
 * @parameter virtualDpId = "Sum"
 * @return A Datapoint Instance or null if the requested type (string id) is not support 
 */
public class Sum extends DpVirtualFactory {
	
	private static final int NUMBER_OF_DATAPOINTS = 10000;
	
	@Override
	public Datapoint getVirtualDp(String virtualDpId, String dpName) {
		// if virtualDpId is yours --> return a Datapoint instance
		if (virtualDpId.equals("Sum")) {
			return new SumImplementation(dpName);			
		}
		return null;
	}
	
	
	/**
	 * Inner Class with the actual Implementation.
	 */
	public class SumImplementation extends Datapoint {
		//Custom Attributes
		private static final String ID_DP_TYPE = "dpType";
		//requested type
		String requestedType = null;
		ZoneController zoneCtrl = null;
		Zone myZone = null;
		//list of all source datapoints
		List<Datapoint> sourceDatapoints = new ArrayList<Datapoint>();
		//helper
		boolean initFinished = false;
				
		public SumImplementation(String dpName) {
			super(dpName);
			//get requested type
			requestedType = getCustomAttr(ID_DP_TYPE);
			//get required DPs
			zoneCtrl = ZoneController.getInstance();
			myZone = getZone();
		}

		
		/*
		 * Overwrite getValues, getValuesPeriodic, etc.
		 */

		@Override
		public int addData(DpDataDTO measurement) {
			// not supported
			return 0;
		}

		@Override
		public int delData() {
			// not supported
			return 0;
		}

		@Override
		public int delData(Date starttime, Date endtime) {
			// not supported
			return 0;
		}

		@Override
		public DpDataDTO getData() {
			init();
			List<DpDatasetDTO> allValues = new ArrayList<DpDatasetDTO>();
			//loop through all datapoints
			for (Datapoint child : sourceDatapoints) {
				DpDatasetDTO dataset = new DpDatasetDTO();
				dataset.add(child.getData());
				allValues.add(dataset);
			}
			//sum up and return single DatapointDataVO
			return calculate(allValues).get(0);
		}
		
		@Override
		public DpDatasetDTO getData(Date starttime, Date endtime) {
			init();
			//TODO not that easy, each change needs to calculate new value
			DpDatasetDTO result = new DpDatasetDTO();
			result.add(new DpDataDTO(new Date(),0.0 ,(float) 1.0));
			result.setDatapointName(getDatapointName());
			return result;
					
//			List<DpDatasetDTO> allValues = new ArrayList<DpDatasetDTO>();
//			//loop through all datapoints
//			for (Datapoint child : sourceDatapoints) {
//				allValues.add(child.getData(starttime, endtime));
//			}
//			//sum up
//			return calculate(allValues);
		}

		@Override
		public DpDatasetDTO getDataPeriodic(Date starttime, Date endtime, Float period, int mode) {
			init();
			List<DpDatasetDTO> allValues = new ArrayList<DpDatasetDTO>();
			//loop through all datapoints
			for (Datapoint child : sourceDatapoints) {
				DpDatasetDTO dataset = null;
				dataset = child.getDataPeriodic(starttime, endtime, period, mode);
				if (dataset != null) {
					allValues.add(dataset);
				}
			}
			//sum up
			return calculate(allValues);
		}

		/**
		 * Sums up each row of a number of DpDatasets
		 * works only if number of DatapointDataVO in DpDatasets is the same!
		 * @return A Dataset
		 */
		public DpDatasetDTO calculate(List<DpDatasetDTO> allValues) {
			DpDatasetDTO result = new DpDatasetDTO();
			
			//loop all datasets
			for (DpDatasetDTO nextDataset : allValues) {
				//loop each dataset and calculate result
				for (int i = 0; i < nextDataset.size(); i++) {
					//add DatapointDataVO if not in result yet
					if (result.size() <= i) {
						result.add(nextDataset.get(i));
					}else {
						//calculate and update result
						DpDataDTO lastValue = result.get(i);
						DpDataDTO nextValue = nextDataset.get(i);
						DpDataDTO newValue = new DpDataDTO();
						//### calculate start
						//sum up if quality is >= 0.25
						if (nextValue.getQuality() >= 0.25) {
							// value
							newValue.setValue(lastValue.getValue() + nextValue.getValue());
							// timestamp
							//set newer timestamp
							if (lastValue.getTimestamp().getTime() < nextValue.getTimestamp().getTime()) {
								newValue.setTimestamp(nextValue.getTimestamp());
							}else {
								newValue.setTimestamp(lastValue.getTimestamp());
							}
							// quality
							//set smaller quality
							if (lastValue.getQuality() < nextValue.getQuality()) {
								newValue.setQuality(lastValue.getQuality());
							}else {
								newValue.setQuality(nextValue.getQuality());
							}
							//### calculate end
							// store resulting DatapointDataVO
							result.set(i, newValue);
						}
					}
				}
			}
			
			result.setDatapointName(getDatapointName());
			return result;
		}


		
		/**
		 * @see bpi.most.server.model.Datapoint#getNumberOfValues(java.util.Date, java.util.Date)
		 * @return sum of all number of values
		 */
		public int getNumberOfValues(Date starttime, Date endtime) {
			init();
			int result = 0;
			//loop through all datapoints
			for (Datapoint child : sourceDatapoints) {
				result = result + child.getNumberOfValues(starttime, endtime); 
			}
			return result;
		}
		
		/**
		 * needs to be done outside of the contructor to prevent loops 
		 * @return
		 */
		private boolean init() {
			if (initFinished) {
				return true;
			}
			//get all DPs within requested zone
			//TODO replace 10000
			if (requestedType == null) {
				for (Datapoint childDatapoint : myZone.getDatapoints(NUMBER_OF_DATAPOINTS)) {
					//exclude all virtual DPs
					if (! childDatapoint.isVirtual()) {
						sourceDatapoints.add(childDatapoint);
					}
				}
			}else{
				for (Datapoint childDatapoint : myZone.getDatapoints(NUMBER_OF_DATAPOINTS, requestedType)) {
					//exclude all virtual DPs
					if (! childDatapoint.isVirtual()) {
						sourceDatapoints.add(childDatapoint);
					}
				}
			}
			
			initFinished = true;
			return true;
		}
		
		
	}
	
}


