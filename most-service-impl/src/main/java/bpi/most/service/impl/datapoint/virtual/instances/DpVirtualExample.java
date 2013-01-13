package bpi.most.service.impl.datapoint.virtual.instances;

import java.util.Date;

import javax.persistence.EntityManager;

import bpi.most.domain.datapoint.Datapoint;
import bpi.most.domain.datapoint.DatapointDataVO;
import bpi.most.domain.datapoint.DatapointDatasetVO;
import bpi.most.service.impl.datapoint.virtual.VirtualDatapoint;
import bpi.most.service.impl.datapoint.virtual.VirtualDatapointFactory;

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
public class DpVirtualExample extends VirtualDatapointFactory {

	private static final int NUMBER_OF_VALUES = 10;
	
	@Override
	public VirtualDatapoint getVirtualDp(Datapoint dpEntity, EntityManager em) {
		// if virtualDpId is yours --> return a Datapoint instance
		if (dpEntity != null && "example".equals(dpEntity.getVirtual())) {
			return new DpVirtualExampleImplementation();
		}
		return null;
	}
	
	/*
	 * Inner Class with the Implementation of the DpVirtualExample
	 * Extend DpPhysical if you want to be able to store and fetch values from the DB
	 */
	public class DpVirtualExampleImplementation implements VirtualDatapoint {
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
		
		@Override
		public void setEntityManager(EntityManager em) {
			// TODO Auto-generated method stub
		}

		@Override
		public int addData(DatapointDataVO measurement) {
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
		public DatapointDataVO getData() {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public DatapointDatasetVO getData(Date starttime, Date endtime) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public DatapointDatasetVO getDataPeriodic(Date starttime, Date endtime,
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
