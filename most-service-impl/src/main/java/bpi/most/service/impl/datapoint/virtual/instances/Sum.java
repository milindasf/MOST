package bpi.most.service.impl.datapoint.virtual.instances;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import bpi.most.domain.datapoint.*;
import bpi.most.domain.datapoint.DpDataFinderHibernate;
import bpi.most.infra.db.DbUtils;
import bpi.most.service.impl.datapoint.virtual.VirtualDatapoint;
import bpi.most.service.impl.datapoint.virtual.VirtualDatapointFactory;

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
public class Sum extends VirtualDatapointFactory {

    private static final String VIRTUAL_TYPE = "Sum";
	
	private static final int NUMBER_OF_DATAPOINTS = 10000;
	
	@Override
	public VirtualDatapoint getVirtualDp(Datapoint dpEntity, EntityManager em) {
		// if virtualDpId is yours --> return a Datapoint instance
		if (dpEntity != null && VIRTUAL_TYPE.equals(dpEntity.getVirtual())) {
			VirtualDatapoint vd = new SumImplementation(dpEntity);
			vd.setEntityManager(em);
			return vd;
		}
		return null;
	}

    @Override
    public String getVirtualType() {
        return VIRTUAL_TYPE;
    }
	
	/**
	 * Inner Class with the actual Implementation.
	 */
	public class SumImplementation implements VirtualDatapoint {
		//Custom Attributes
		private static final String ID_DP_TYPE = "dpType";
		//requested type
		String requestedType = null;
		//list of all source datapoints
		List<DatapointVO> sourceDatapoints = new ArrayList<DatapointVO>();
		//helper
		boolean initFinished = false;
		//db entity
		Datapoint dpEntity;
				
		private DatapointFinder datapointFinder;
		private IDatapointDataFinder datapointDataFinder;

	    public SumImplementation(Datapoint dpEntity) {
	    	this.dpEntity = dpEntity;
			//get requested type
			requestedType = DbUtils.getCustomAttr(dpEntity.getCustom_attr(), ID_DP_TYPE);
		}
	    
	    @Override
	    public void setEntityManager(EntityManager em) {
	    	datapointFinder = new DatapointFinder(em);
	    	datapointDataFinder = new DpDataFinderHibernate(em);
	    }
		
		/*
		 * Overwrite getValues, getValuesPeriodic, etc.
		 */

		@Override
		public int addData(DatapointDataVO measurement) {
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
		public DatapointDataVO getData() {
			init();
			List<DatapointDatasetVO> allValues = new ArrayList<DatapointDatasetVO>();
			//loop through all datapoints
			for (DatapointVO child : sourceDatapoints) {
				DatapointDatasetVO dataset = new DatapointDatasetVO();
				dataset.add(datapointDataFinder.getData(child.getName()));
				allValues.add(dataset);
			}
			//sum up and return single DatapointDataVO
			return calculate(allValues).get(0);
		}
		
		@Override
		public DatapointDatasetVO getData(Date starttime, Date endtime) {
			init();
			//TODO not that easy, each change needs to calculate new value
			DatapointDatasetVO result = new DatapointDatasetVO();
			result.add(new DatapointDataVO(new Date(),0.0 ,(float) 1.0));
			result.setDatapointName(dpEntity.getName());
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
		public DatapointDatasetVO getDataPeriodic(Date starttime, Date endtime, Float period, int mode) {
			init();
			List<DatapointDatasetVO> allValues = new ArrayList<DatapointDatasetVO>();
			//loop through all datapoints
			for (DatapointVO child : sourceDatapoints) {
				DatapointDatasetVO dataset = datapointDataFinder.getDataPeriodic(child.getName(), starttime, endtime, period, mode);
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
		public DatapointDatasetVO calculate(List<DatapointDatasetVO> allValues) {
			DatapointDatasetVO result = new DatapointDatasetVO();
			
			//loop all datasets
			for (List<DatapointDataVO> nextDataset : allValues) {
				//loop each dataset and calculate result
				for (int i = 0; i < nextDataset.size(); i++) {
					//add DatapointDataVO if not in result yet
					if (result.size() <= i) {
						result.add(nextDataset.get(i));
					}else {
						//calculate and update result
						DatapointDataVO lastValue = result.get(i);
						DatapointDataVO nextValue = nextDataset.get(i);
						DatapointDataVO newValue = new DatapointDataVO();
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
			
			result.setDatapointName(dpEntity.getName());
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
			for (DatapointVO child : sourceDatapoints) {
				result = result + datapointDataFinder.getNumberOfValues(child.getName(), starttime, endtime); 
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
			for (DatapointVO childDatapoint : datapointFinder.getDpFromSubZones(dpEntity.getZone_idzone(), NUMBER_OF_DATAPOINTS)) {
				//exclude all virtual DPs
				if (!childDatapoint.isVirtual()) {
					if (requestedType != null && childDatapoint.getType() != null && childDatapoint.getType().equals(requestedType)) {
						sourceDatapoints.add(childDatapoint);
					}
				}
			}
			
			initFinished = true;
			return true;
		}
	}
}
