package bpi.most.server.model.dpvirtual;

import java.util.Date;

import bpi.most.server.model.Datapoint;
import bpi.most.server.model.DpVirtualFactory;
import bpi.most.shared.DpDataDTO;
import bpi.most.shared.DpDatasetDTO;

/**
 * #### add documentation here!!####
 * 
 * Add new Virtual Datapoints to the file META-INF/services/bpi.most.server.DpVirtualFactory
 * The ID must be a unique string in the DB data_source - virtual column
 * Additional virtual dp can be loaded at runtime by copying a jar with the implementation into the server classpath
 * Don't change the name of the DatapointVO here. It will be forced to the DB definition in the DpController
 * @author robert.zach@tuwien.ac.at
 * @parameter virtualDpId Requested virtual dp type
 * @return A Datapoint Instance or null if the requested type (string id) is not support 
 */
public class DpVirtualExample extends DpVirtualFactory {

	private static final int NUMBER_OF_VALUES = 10;
	
	@Override
	public Datapoint getVirtualDp(String virtualDpId, String dpName) {
		// if virtualDpId is yours --> return a Datapoint instance
		if (virtualDpId.equals("example")) {
			return new DpVirtualExampleImplementation(dpName);			
		}
		return null;
	}
	
	
	/*
	 * Inner Class with the Implementation of the DpVirtualExample
	 * Extend DpPhysical if you want to be able to store and fetch values from the DB
	 */
	public class DpVirtualExampleImplementation extends Datapoint {
		/*
		 * Overwrite getValues, getValuesPeriodic, etc.
		 */
		/*
		@Override
		public DatapointDatasetVO getData(Date starttime, Date endtime) {
			- get required information, e.g. getCustomAttr("myVariable");
			- get measurements, e.g. DpController.getInstance().getDatapoint("XXX").
			- calculate something
			- return results
			return XXX;
		}
		*/
		// ---- Your code here -----
		
		public DpVirtualExampleImplementation(String dpName) {
			super(dpName);
			// TODO Auto-generated constructor stub
		}

		

		@Override
		public int addData(DpDataDTO measurement) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public int delData() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public int delData(Date starttime, Date endtime) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public DpDataDTO getData() {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public DpDatasetDTO getData(Date starttime, Date endtime) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public DpDatasetDTO getDataPeriodic(Date starttime, Date endtime,
				Float period, int mode) {
			// TODO Auto-generated method stub
			return null;
		}

		/* (non-Javadoc)
		 * @see bpi.most.server.model.Datapoint#getNumberOfValues(java.util.Date, java.util.Date)
		 */
		@Override
		public int getNumberOfValues(Date starttime, Date endtime) {
			// TODO Auto-generated method stub
			return NUMBER_OF_VALUES;
		}


	}
	
}


