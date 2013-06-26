package bpi.most.vdp.instance;

import bpi.most.domain.datapoint.Datapoint;
import bpi.most.domain.datapoint.DatapointDataVO;
import bpi.most.domain.datapoint.DatapointDatasetVO;
import bpi.most.service.api.DatapointService;
import bpi.most.service.impl.datapoint.virtual.VirtualDatapoint;
import bpi.most.service.impl.datapoint.virtual.VirtualDatapointFactory;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: harald
 * Date: 28.05.13
 * Time: 11:28
 * To change this template use File | Settings | File Templates.
 */
public class RandomDataDatapoint extends VirtualDatapointFactory {

    private static final int NUMBER_OF_VALUES = 10;
    private static final String VIRTUAL_TYPE = "randomDataVdp";

    @Override
    public VirtualDatapoint getVirtualDp(Datapoint dpEntity, EntityManager em) {
        // if virtualDpId is yours --> return a Datapoint instance
        if (dpEntity != null && VIRTUAL_TYPE.equals(dpEntity.getVirtual())) {
            return new DpVirtualExampleImplementation();
        }
        return null;
    }

    @Override
    public String getVirtualType(){
        return VIRTUAL_TYPE;
    }

    /*
     * Inner Class with the Implementation of the DpVirtualExample
     * Extend DpPhysical if you want to be able to store and fetch values from the DB
     */
    public class DpVirtualExampleImplementation implements VirtualDatapoint {

        @Inject
        private DatapointService dpService;


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
            System.out.println("have datapoint service: " + (dpService != null));
            return new DatapointDataVO(new Date(), Math.random()*10);
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
