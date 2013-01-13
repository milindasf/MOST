package bpi.most.domain.datapoint;

import bpi.most.dto.DpDataDTO;
import org.hibernate.Session;
import org.hibernate.transform.ResultTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import static bpi.most.infra.db.DbUtils.findIndex;

/**
 * Finds {@link DatapointDataVO}s from a given {@link javax.persistence.EntityManager}.
 *
 * @author Christoph Lauscher
 */
public class DatapointDataFinder {

    private static final Logger LOG = LoggerFactory.getLogger(DatapointDataFinder.class);

    private final EntityManager em;

    public DatapointDataFinder(EntityManager em) {
        this.em = em;
    }

    public DatapointDataVO getData(String dpName) {
        LOG.debug("Fetching datapoint data: {}", dpName);
        // noinspection unchecked
        List<DatapointDataVO> result = ((Session) em.getDelegate()).createSQLQuery("{CALL getValues(:name, null, null)}")
                .setParameter("name", dpName)
                .setReadOnly(true)
                .setResultTransformer(new DatapointDataVOResultTransformer())
                .list();

        if(result.size() > 0){
            return result.get(0);
        }
        return null;
    }

    public DatapointDatasetVO getData(String dpName, Date starttime, Date endtime) {
        LOG.debug("Fetching datapoint data: {}", dpName);
        if (starttime.before(endtime)) {
	        // noinspection unchecked
	        List<DatapointDataVO> result = ((Session) em.getDelegate()).createSQLQuery("{CALL getValues(:name, :start, :end)}")
	                .setParameter("name", dpName)
	                .setParameter("start", new java.sql.Timestamp(starttime.getTime()))
	                .setParameter("end", new java.sql.Timestamp(endtime.getTime()))
	                .setReadOnly(true)
	                .setResultTransformer(new DatapointDataVOResultTransformer())
	                .list();
	        
			if(result.size() > 0){
				DatapointDatasetVO ret = new DatapointDatasetVO();
				ret.addAll(result);
				return ret;
	        }
		}
        return null;
    }

    public DatapointDatasetVO getDataPeriodic(String dpName, Date starttime, Date endtime, Float period, int mode) {
        LOG.debug("Fetching datapoint data: {}", dpName);
        if (starttime.before(endtime)) {
	        // noinspection unchecked
	        List<DatapointDataVO> result = ((Session) em.getDelegate())
	        		.createSQLQuery("{CALL getValuesPeriodic(:name, :start, :end, :period, :mode)}")
	                .setParameter("name", dpName)
	                .setParameter("start", new java.sql.Timestamp(starttime.getTime()))
	                .setParameter("end", new java.sql.Timestamp(endtime.getTime()))
	                .setParameter("period", period)
	                .setParameter("mode", mode)
	                .setReadOnly(true)
	                .setResultTransformer(new DatapointDataVOResultTransformer())
	                .list();
	        
			if(result.size() > 0){
				DatapointDatasetVO ret = new DatapointDatasetVO();
				ret.addAll(result);
				return ret;
	        }
		}
        return null;
    }

    public Integer getNumberOfValues(String dpName, Date starttime, Date endtime) {
        LOG.debug("Fetching datapoint data: {}", dpName);
        if (starttime.before(endtime)) {
	        // noinspection unchecked
	        List<Integer> result = ((Session) em.getDelegate()).createSQLQuery("{CALL getNumberOfValues(:name, :start, :end)}")
	                .setParameter("name", dpName)
	                .setParameter("start", new java.sql.Timestamp(starttime.getTime()))
	                .setParameter("end", new java.sql.Timestamp(endtime.getTime()))
	                .setReadOnly(true)
	                .setResultTransformer(new IntegerResultTransformer())
	                .list();
	        
			if(result.size() > 0){
				return result.get(0);
	        }
		}
        return null;
    }
    
	public int addData(String dpName, DpDataDTO measurement) {
		int result = 0;
		if (measurement != null) {
			// call addData SP
	        // noinspection unchecked
	        List<Integer> retval = ((Session) em.getDelegate()).createSQLQuery("{CALL addData(:name, :timestamp, :value)}")
	                .setParameter("name", dpName)
	                .setParameter("timestamp", new java.sql.Timestamp(measurement.getTimestamp().getTime()))
	                .setParameter("value", measurement.getValue())
	                .setReadOnly(true)
	                .setResultTransformer(new IntegerResultTransformer())
	                .list();
	        
	        if(retval.size() > 0){
	        	result = retval.get(0);
	        }
		}
		// TODO ASE notify observers if value was inserted
		if (result == 1) {
			//super.addData(measurement);
		}
		return result;
	}

	/**
	 * Deletes all data of datapoint. Use with caution!!
	 */
	public int delData(String dpName) {
		int result = 0;

		// call emptyDatapoint SP
		// noinspection unchecked
        List<Integer> retval = ((Session) em.getDelegate()).createSQLQuery("{CALL emptyDatapoint(:name)}")
                .setParameter("name", dpName)
                .setReadOnly(true)
                .setResultTransformer(new IntegerResultTransformer())
                .list();
        
        if(retval.size() > 0){
        	result = retval.get(0);
        }
		return result;
	}

	/**
	 * Deletes data of given timeslot
	 */
	public int delData(String dpName, Date starttime, Date endtime) {
		int result = 0;

		// call emtpyDatapointTimeslot SP
		// noinspection unchecked
        List<Integer> retval = ((Session) em.getDelegate()).createSQLQuery("{CALL emptyDatapointTimeslot(:name, :start, :end)}")
                .setParameter("name", dpName)
                .setParameter("start", new java.sql.Timestamp(starttime.getTime()))
                .setParameter("end", new java.sql.Timestamp(endtime.getTime()))
                .setReadOnly(true)
                .setResultTransformer(new IntegerResultTransformer())
                .list();
        
        if(retval.size() > 0){
        	result = retval.get(0);
        }
		return result;
	}

    /**
     * Transforms a result of the stored procedure getValues() into a {@link DatapointDataVO}.
     */
    private static class DatapointDataVOResultTransformer implements ResultTransformer {

        private boolean initialized = false;
        private int timestampIndex;
        private int valueIndex;
        private int qualityIndex;

        @Override
        public DatapointDataVO transformTuple(Object[] tuple, String[] aliases) {
            if (!initialized) {
                initializeIndexes(aliases);
            }
            return new DatapointDataVO((Date) tuple[timestampIndex], 
            						   (Double) tuple[valueIndex], 
            						   new Float(qualityIndex != -1 ? (Double) tuple[qualityIndex] : 1));
        }

        private void initializeIndexes(String[] aliases) {
        	timestampIndex = findIndex(aliases, "timestamp");
        	valueIndex = findIndex(aliases, "value");
        	try {
        		qualityIndex = findIndex(aliases, "quality");
        	} catch(PersistenceException e){
        		qualityIndex = -1;
        	}
            initialized = true;
        }

        @Override
        public List transformList(List collection) {
            return collection;
        }
    }

    /**
     * Transforms a result of a stored procedure into an integer number.
     */
    private static class IntegerResultTransformer implements ResultTransformer {

        @Override
        public Integer transformTuple(Object[] tuple, String[] aliases) {
            return ((BigInteger) tuple[0]).intValue();
        }

        @Override
        public List transformList(List collection) {
            return collection;
        }
    }
}
