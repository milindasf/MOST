package bpi.most.service.impl.datapoint.virtual.instances;

import java.util.Date;

import javax.persistence.EntityManager;

import bpi.most.domain.datapoint.Datapoint;
import bpi.most.domain.datapoint.DatapointDataFinder;
import bpi.most.domain.datapoint.DatapointDataVO;
import bpi.most.domain.datapoint.DatapointDatasetVO;
import bpi.most.domain.datapoint.DatapointFinder;
import bpi.most.domain.datapoint.DatapointVO;
import bpi.most.infra.db.DbUtils;
import bpi.most.service.impl.datapoint.virtual.VirtualDatapoint;
import bpi.most.service.impl.datapoint.virtual.VirtualDatapointFactory;

/**
 * ID = "RadiatorHeatPower"
 * RadiatorHeatPower calculates the heat power of a radiator based on the
 * - A) mean radiator temperature
 * - B) the standard heat output of the radiator (based on the respective radiator size!! --> [W] ), e.g. 1694 W for a common radiator with 1m length 
 * - C) (optional) the room temperature of the respective zone (20C is used if not provided).
 * 
 * The following custom attributes should be provided:
 * - A) dpMeanTemp - String of dp name
 * - B) standartHeatOutput - Integer, [W]
 * - C) dpRoomTemp - String of dp name
 * Example:
 * dpMeanTemp tem1; standartHeatOutput 1694; dpRoomTemp tem2;
 * 
 * @author robert.zach@tuwien.ac.at
 * @parameter virtualDpId = "RadiatorHeatPower"
 * @return A Datapoint Instance or null if the requested type (string id) is not support 
 */
public class RadiatorHeatPower extends VirtualDatapointFactory {

    private static final String VIRTUAL_TYPE = "RadiatorHeatPower";

	private static final double ROOM_TEMP_CELSIUS = 20.0;
	private static final double HEAT_POWER_COEFF1 = 0.0062;
	private static final double HEAT_POWER_COEFF2 = 1.2998;
	
	@Override
	public VirtualDatapoint getVirtualDp(Datapoint dpEntity, EntityManager em) {
		// if virtualDpId is yours --> return a Datapoint instance
		if (dpEntity != null && VIRTUAL_TYPE.equals(dpEntity.getVirtual())) {
			VirtualDatapoint vd = new RadiatorHeatPowerImplementation(dpEntity);
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
	public class RadiatorHeatPowerImplementation implements VirtualDatapoint {
		//Custom Attributes
		private static final String ID_DP_MEAN_TEMP = "dpMeanTemp";
		private static final String ID_STANDART_HEAT_OUTPUT = "standartHeatOutput";
		private static final String ID_DP_ROOM_TEMP = "dpRoomTemp";
		private DatapointFinder datapointFinder;
		private DatapointDataFinder datapointDataFinder;
		private DatapointDatasetVO defaultRoomTemp; 
		//db entity
		Datapoint dpEntity;
		
		public RadiatorHeatPowerImplementation(Datapoint dpEntity) {
			this.dpEntity = dpEntity;
			//create default room temp Dataset - set to 20C
			defaultRoomTemp = new DatapointDatasetVO();
			defaultRoomTemp.add(new DatapointDataVO(new Date(), ROOM_TEMP_CELSIUS));
		}
	    
	    @Override
	    public void setEntityManager(EntityManager em) {
	    	datapointFinder = new DatapointFinder(em);
	    	datapointDataFinder = new DatapointDataFinder(em);
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
			//get required DPs
			DatapointVO dpMeanTemp = datapointFinder.getDatapoint(DbUtils.getCustomAttr(dpEntity.getCustom_attr(), ID_DP_MEAN_TEMP));
			//dpRoomTemp is null if not defined
			DatapointVO dpRoomTemp = datapointFinder.getDatapoint(DbUtils.getCustomAttr(dpEntity.getCustom_attr(), ID_DP_ROOM_TEMP));
			
			//put last value in Dataset
			DatapointDatasetVO meanRadiatorTemp = new DatapointDatasetVO();
			meanRadiatorTemp.add(datapointDataFinder.getData(dpMeanTemp.getName()));
			
			if (dpRoomTemp == null) {
				return calculateHeatPower(meanRadiatorTemp, defaultRoomTemp).get(0);
			}else {
				DatapointDatasetVO roomTemp = new DatapointDatasetVO();
				roomTemp.add(datapointDataFinder.getData(dpRoomTemp.getName()));
				return calculateHeatPower(meanRadiatorTemp, roomTemp).get(0);
			}
		}
		
		@Override
		public DatapointDatasetVO getData(Date starttime, Date endtime) {
			//get required DPs
			DatapointVO dpMeanTemp = datapointFinder.getDatapoint(DbUtils.getCustomAttr(dpEntity.getCustom_attr(), ID_DP_MEAN_TEMP));
			//dpRoomTemp is null if not defined
			DatapointVO dpRoomTemp = datapointFinder.getDatapoint(DbUtils.getCustomAttr(dpEntity.getCustom_attr(), ID_DP_ROOM_TEMP));
			
			if (dpRoomTemp == null) {
				return calculateHeatPower(datapointDataFinder.getData(dpMeanTemp.getName(), starttime, endtime), defaultRoomTemp);
			}else {
				return calculateHeatPower(datapointDataFinder.getData(dpMeanTemp.getName(), starttime, endtime), datapointDataFinder.getData(dpRoomTemp.getName(), starttime, endtime));
			}
		}

		@Override
		public DatapointDatasetVO getDataPeriodic(Date starttime, Date endtime, Float period, int mode) {
			//get required DPs
			DatapointVO dpMeanTemp = datapointFinder.getDatapoint(DbUtils.getCustomAttr(dpEntity.getCustom_attr(), ID_DP_MEAN_TEMP));
			//dpRoomTemp is null if not defined
			DatapointVO dpRoomTemp = datapointFinder.getDatapoint(DbUtils.getCustomAttr(dpEntity.getCustom_attr(), ID_DP_ROOM_TEMP));
			
			if (dpRoomTemp == null) {
				return calculateHeatPower(datapointDataFinder.getDataPeriodic(dpMeanTemp.getName(), starttime, endtime, period, mode), defaultRoomTemp);
			}else {
				return calculateHeatPower(datapointDataFinder.getDataPeriodic(dpMeanTemp.getName(), starttime, endtime, period, mode), datapointDataFinder.getDataPeriodic(dpRoomTemp.getName(), starttime, endtime, period, mode));
			} 
		}

		/**
		 * Actual calculation of the heat power
		 * Includes smaller quality value of meanRadiatorTemp and roomTemp in returned Dataset
		 * @return A Dataset of the calculated heat power
		 */
		public DatapointDatasetVO calculateHeatPower(DatapointDatasetVO meanRadiatorTemp, DatapointDatasetVO roomTemp) {
			DatapointDatasetVO result = new DatapointDatasetVO();
			DatapointDataVO matchingRoomTemp;
			//set dp name in Resultset
			result.setDatapointName(dpEntity.getName());
			int standartHeatOutput = Integer.valueOf(DbUtils.getCustomAttr(dpEntity.getCustom_attr(), ID_STANDART_HEAT_OUTPUT));
			
			//return empty Dataset if not all arguments are valid
			if (meanRadiatorTemp.isEmpty() || roomTemp.isEmpty()) {
				return result;
			}
						
			//calculate power value for each radiator meanTemp measurement
			for (DatapointDataVO meanTempData : meanRadiatorTemp) {
				Double power = null;
				Double diffTemp = null;
				//### get matching room temperature
				//use roomTemp measurement before or equal to the respective meanTemp measurement timestamp
				matchingRoomTemp = roomTemp.getDataBeforeOrEqual(meanTempData.getTimestamp());
				//if no data before or equal use after, only required for special case where you have no measurements in the beginning
				if (matchingRoomTemp == null) {
					matchingRoomTemp = roomTemp.getDataAfter(meanTempData.getTimestamp());
				}
				
				//### calc diff. temp.
				diffTemp = meanTempData.getValue() - matchingRoomTemp.getValue();
				//negative values caused by measurement faults are set to 0
				if (diffTemp < 0.0) {
					diffTemp = 0.0;
				}
				//calc heat power - see documentation
				power = HEAT_POWER_COEFF1 * java.lang.Math.pow(diffTemp, HEAT_POWER_COEFF2) * standartHeatOutput;
				//set smaller quality value
				if (meanTempData.getQuality() < matchingRoomTemp.getQuality()) {
					result.add(new DatapointDataVO(meanTempData.getTimestamp(), power, meanTempData.getQuality()));
				} else {
					result.add(new DatapointDataVO(meanTempData.getTimestamp(), power, matchingRoomTemp.getQuality()));
				}
			}
			return result;
		}
		
		/**
		 * @see bpi.most.server.model.Datapoint#getNumberOfValues(java.util.Date, java.util.Date)
		 */
		public int getNumberOfValues(Date starttime, Date endtime) {
			DatapointVO dp = datapointFinder.getDatapoint(DbUtils.getCustomAttr(dpEntity.getCustom_attr(), ID_DP_MEAN_TEMP));
			return datapointDataFinder.getNumberOfValues(dp.getName(), starttime, endtime);
		}
	}
}
