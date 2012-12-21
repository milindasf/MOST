package bpi.most.server.model.dpvirtual;

import java.util.Date;

import bpi.most.server.model.Datapoint;
import bpi.most.server.model.DpController;
import bpi.most.server.model.DpVirtualFactory;
import bpi.most.shared.DpDataDTO;
import bpi.most.shared.DpDatasetDTO;

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
public class RadiatorHeatPower extends DpVirtualFactory {

	@Override
	public Datapoint getVirtualDp(String virtualDpId, String dpName) {
		// if virtualDpId is yours --> return a Datapoint instance
		if (virtualDpId.equals("RadiatorHeatPower")) {
			return new RadiatorHeatPowerImplementation(dpName);			
		}
		return null;
	}
	
	
	/**
	 * Inner Class with the actual Implementation.
	 */
	public class RadiatorHeatPowerImplementation extends Datapoint {
		//Custom Attributes
		private static final String IDdpMeanTemp = "dpMeanTemp";
		private static final String IDstandartHeatOutput = "standartHeatOutput";
		private static final String IDdpRoomTemp = "dpRoomTemp";
		DpController dpct = null;
		private DpDatasetDTO defaultRoomTemp; 
		
		public RadiatorHeatPowerImplementation(String dpName) {
			super(dpName);
			//get required DPs
			dpct = DpController.getInstance();
			//create default room temp Dataset - set to 20C
			defaultRoomTemp = new  DpDatasetDTO();
			defaultRoomTemp.add(new DpDataDTO(new Date(), 20.0));
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
			//get required DPs
			Datapoint dpMeanTemp = dpct.getDatapoint(getCustomAttr(IDdpMeanTemp));
			//dpRoomTemp is null if not defined
			Datapoint dpRoomTemp = dpct.getDatapoint(getCustomAttr(IDdpRoomTemp));
			
			//put last value in Dataset
			DpDatasetDTO meanRadiatorTemp = new DpDatasetDTO();
			meanRadiatorTemp.add(dpMeanTemp.getData());
			
			if (dpRoomTemp == null) {
				return calculateHeatPower(meanRadiatorTemp, defaultRoomTemp).get(0);
			}else {
				DpDatasetDTO roomTemp = new DpDatasetDTO();
				roomTemp.add(dpRoomTemp.getData());
				return calculateHeatPower(meanRadiatorTemp, roomTemp).get(0);
			}
		}
		
		@Override
		public DpDatasetDTO getData(Date starttime, Date endtime) {
			//get required DPs
			Datapoint dpMeanTemp = dpct.getDatapoint(getCustomAttr(IDdpMeanTemp));
			//dpRoomTemp is null if not defined
			Datapoint dpRoomTemp = dpct.getDatapoint(getCustomAttr(IDdpRoomTemp));
			
			if (dpRoomTemp == null) {
				return calculateHeatPower(dpMeanTemp.getData(starttime, endtime), defaultRoomTemp);
			}else {
				return calculateHeatPower(dpMeanTemp.getData(starttime, endtime), dpRoomTemp.getData(starttime, endtime));
			}
		}

		@Override
		public DpDatasetDTO getDataPeriodic(Date starttime, Date endtime, Float period, int mode) {
			//get required DPs
			Datapoint dpMeanTemp = dpct.getDatapoint(getCustomAttr(IDdpMeanTemp));
			//dpRoomTemp is null if not defined
			Datapoint dpRoomTemp = dpct.getDatapoint(getCustomAttr(IDdpRoomTemp));
			
			if (dpRoomTemp == null) {
				return calculateHeatPower(dpMeanTemp.getDataPeriodic(starttime, endtime, period, mode), defaultRoomTemp);
			}else {
				return calculateHeatPower(dpMeanTemp.getDataPeriodic(starttime, endtime, period, mode), dpRoomTemp.getDataPeriodic(starttime, endtime, period, mode));
			} 
		}

		/**
		 * Actual calculation of the heat power
		 * Includes smaller quality value of meanRadiatorTemp and roomTemp in returned Dataset
		 * @return A Dataset of the calculated heat power
		 */
		public DpDatasetDTO calculateHeatPower(DpDatasetDTO meanRadiatorTemp, DpDatasetDTO roomTemp) {
			DpDatasetDTO result = new DpDatasetDTO();
			DpDataDTO matchingRoomTemp;
			//set dp name in Resultset
			result.setDatapointName(getDatapointName());
			int standartHeatOutput = Integer.valueOf(getCustomAttr(IDstandartHeatOutput));
			
			//return empty Dataset if not all arguments are valid
			if (meanRadiatorTemp.isEmpty() || roomTemp.isEmpty()) {
				return result;
			}
						
			//calculate power value for each radiator meanTemp measurement
			for (DpDataDTO meanTempData : meanRadiatorTemp) {
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
				power = 0.0062 * java.lang.Math.pow(diffTemp, 1.2998) * standartHeatOutput;
				//set smaller quality value
				if (meanTempData.getQuality() < matchingRoomTemp.getQuality())
					result.add(new DpDataDTO(meanTempData.getTimestamp(), power, meanTempData.getQuality()));
				else
					result.add(new DpDataDTO(meanTempData.getTimestamp(), power, matchingRoomTemp.getQuality()));
			}
			return result;
		}


		
		/**
		 * @see bpi.most.server.model.Datapoint#getNumberOfValues(java.util.Date, java.util.Date)
		 */
		public int getNumberOfValues(Date starttime, Date endtime) {
			return dpct.getDatapoint(getCustomAttr(IDdpMeanTemp)).getNumberOfValues(starttime, endtime);
		}
		
		
	}
	
}


