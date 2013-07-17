package bpi.most.vdp.instance;

import bpi.most.domain.datapoint.Datapoint;
import bpi.most.domain.datapoint.DatapointDataVO;
import bpi.most.domain.datapoint.DatapointDatasetVO;
import bpi.most.service.api.DatapointService;
import bpi.most.service.impl.datapoint.virtual.VirtualDatapoint;
import bpi.most.service.impl.datapoint.virtual.VirtualDatapointFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private static final Logger LOG = LoggerFactory.getLogger(RandomDataDatapoint.class);
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

        private static final int RANDOM_VALUE_COUNT = 10;

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
            LOG.debug("returning random data");
            return new DatapointDataVO(new Date(), getRandomValueBetween10And20());
        }

        @Override
        public DatapointDatasetVO getData(Date starttime, Date endtime) {
            LOG.debug("returning random dataset");
            DatapointDatasetVO dataset = new DatapointDatasetVO();

            long diff = starttime.getTime() - endtime.getTime();

            for (int i = 0; i < RANDOM_VALUE_COUNT; i++){
                // create timestamps so that the datavalues are equally distributed
                // over the requested time range
                Date date = new Date(diff / (RANDOM_VALUE_COUNT - 1) * i + starttime.getTime());
                dataset.add(new DatapointDataVO(date, getRandomValueBetween10And20())) ;
            }
            return dataset;
        }

        @Override
        public DatapointDatasetVO getDataPeriodic(Date starttime, Date endtime,
                                                  Float period, int mode) {
            /**
             * TODO call data preprocessing module when it exists
             */

            return null;
        }

        /* (non-Javadoc)
         * @see bpi.most.server.model.Datapoint#getNumberOfValues(java.util.Date, java.util.Date)
         */
        @Override
        public int getNumberOfValues(Date starttime, Date endtime) {
            return RANDOM_VALUE_COUNT;
        }

        private double getRandomValueBetween10And20(){
            int min = 10, max = 20;
            return min + (int)(Math.random() * ((max - min) + 1));
        }
    }
}
