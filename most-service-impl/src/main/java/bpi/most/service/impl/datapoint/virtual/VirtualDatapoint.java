package bpi.most.service.impl.datapoint.virtual;

import java.util.Date;

import javax.persistence.EntityManager;

import bpi.most.domain.datapoint.DatapointDataVO;
import bpi.most.domain.datapoint.DatapointDatasetVO;

/**
 * Virtual datapoint interface
 * 
 * @author Christoph Lauscher
 */
public interface VirtualDatapoint {
	
	public void setEntityManager(EntityManager em);

	/**
	 * store a measurement of the datapoint
	 * 
	 * @return 1 = inserted; < 0 constraints violated or procedure error
	 */
	public int addData(DatapointDataVO data);

	/**
	 * Deletes all stored data of datapoint. Use with caution!!
	 */
	public int delData();

	/**
	 * Deletes data of a given timeslot
	 */
	public int delData(Date starttime, Date endtime);

	/**
	 * @return The latest value in Monitoring DB as DatapointDataVO, null if empty
	 */
	public DatapointDataVO getData();

	/**
	 * @return A DatapointDatasetVO from start- to endtime, null if empty
	 */
	public DatapointDatasetVO getData(Date starttime, Date endtime);

	/**
	 * @return a DatapointDatasetVO from start- to endtime, null if empty
	 */
	public DatapointDatasetVO getDataPeriodic(Date starttime, Date endtime, Float period, int mode);

	/**
	 * A method used to get the number of values in a specific time frame.
	 * 
	 * @param starttime
	 *            Start time of the time frame you want to know how many values
	 *            are in.
	 * @param endtime
	 *            End time of the time frame you want to know how many values
	 *            are in.
	 * @return Number of values in the specific time frame between start and end
	 *         time.
	 */
	public int getNumberOfValues(Date starttime, Date endtime);
}
