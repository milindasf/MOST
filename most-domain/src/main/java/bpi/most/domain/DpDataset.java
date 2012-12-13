/* TODO: Refactor! */

package bpi.most.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Is an ArrayList of DpData (single measurements).
 * FIXME Implement some Hashlist for performance optimization during searches in the DpDataset
 * FIXME Research of overhead code (getDataBefore(), etc. ) is transfered over the wire (client - server) each time? If so, create static helper Class.
 * @author robert.zach@tuwien.ac.at
 */
public class DpDataset extends ArrayList<DpData> implements Serializable{
	private static final long serialVersionUID = -981884783836456778L;
	private String datapointName = null;
	
	public DpDataset() {
		super();
	}
	public DpDataset(String datapointName) {
		super();
		this.datapointName = datapointName;
	}
	
	public String getDatapointName() {
		return datapointName;
	}
	public void setDatapointName(String datapointName) {
		this.datapointName = datapointName;
	}
	
	
	
	//#### Search methods #####
	/**
	 * @return A DpData before the respective timestamp, null if nothing before timstamp  
	 */
	public DpData getDataBefore(Date timestamp) {
		//FIXME: date sorted arraylist is assumed right now, implement compare --> sort
		DpData result = null;
		for (DpData iterateData : this) {
			if (iterateData.getTimestamp().after(timestamp)) {
				//return previous measurement
				return result;
			}
			//remember last measurement
			result = iterateData;
		}
		return result;
	}
	/**
	 * @return A DpData before or equal the respective timestamp, null if nothing before timstamp  
	 */
	public DpData getDataBeforeOrEqual(Date timestamp) {
		//FIXME: date sorted arraylist is assumed right now, implement compare --> sort
		DpData result = null;
		for (DpData iterateData : this) {
			//after or equal date
			if (iterateData.getTimestamp().compareTo(timestamp) >= 0 ) {
				//return previous measurement
				return result;
			}
			//remember last measurement
			result = iterateData;
		}
		return result;
	}
	/**
	 * @return A DpDataset (all measurements) before the respective timestamp, emtpy Dataset if nothing is before timestamp  
	 */
	public DpDataset getDatasetBefore(Date timestamp) {
		//FIXME: date sorted arraylist is assumed right now, implement compare --> sort
		DpDataset result = new DpDataset(getDatapointName());
		
		for (DpData iterateData : this) {
			if (iterateData.getTimestamp().after(timestamp)) {
				//return Dataset with all previous measurements
				return result;
			}
			//add last measurement to requested Dataset
			result.add(iterateData);
		}
		return result;
	}
	/**
	 * @return A DpDataset (all measurements) before or equal the respective timestamp, emtpy Dataset if nothing is before timestamp  
	 */
	public DpDataset getDatasetBeforeOrEqual(Date timestamp) {
		//FIXME: date sorted arraylist is assumed right now, implement compare --> sort
		DpDataset result = new DpDataset(getDatapointName());
		
		for (DpData iterateData : this) {
			//after or equal date
			if (iterateData.getTimestamp().compareTo(timestamp) >= 0) {
				//return Dataset with all previous measurements
				return result;
			}
			//add last measurement to requested Dataset
			result.add(iterateData);
		}
		return result;
	}
	
	/**
	 * @return A DpData after the respective timestamp, null if nothing after timstamp  
	 */
	public DpData getDataAfter(Date timestamp) {
		//FIXME: date sorted arraylist is assumed right now, implement compare --> sort
		DpData result = null;
		for (DpData iterateData : this) {
			if (iterateData.getTimestamp().after(timestamp)) {
				//return first value after timestamp
				return iterateData;
			}
		}
		return result;
	}
	/**
	 * @return A DpData after or equal the respective timestamp, null if nothing after timstamp  
	 */
	public DpData getDataAfterOrEqual(Date timestamp) {
		//FIXME: date sorted arraylist is assumed right now, implement compare --> sort
		DpData result = null;
		for (DpData iterateData : this) {
			//after or equal date
			if (iterateData.getTimestamp().compareTo(timestamp) >= 0) {
				//return first value after timestamp
				return iterateData;
			}
		}
		return result;
	}
	/**
	 * @return A DpDataset (all measurements) before the respective timestamp, emtpy Dataset if nothing is before timestamp  
	 */
	public DpDataset getDatasetAfter(Date timestamp) {
		//FIXME: date sorted arraylist is assumed right now, implement compare --> sort
		DpDataset result = new DpDataset(getDatapointName());
		
		for (DpData iterateData : this) {
			if (iterateData.getTimestamp().after(timestamp)) {
				//add everything after timestamp
				result.add(iterateData);
			}
		}
		return result;
	}
	/**
	 * @return A DpDataset (all measurements) before the respective timestamp, emtpy Dataset if nothing is before timestamp  
	 */
	public DpDataset getDatasetAfterOrEqual(Date timestamp) {
		//FIXME: date sorted arraylist is assumed right now, implement compare --> sort
		DpDataset result = new DpDataset(getDatapointName());
		
		for (DpData iterateData : this) {
			//after or equal date
			if (iterateData.getTimestamp().compareTo(timestamp) >= 0) {
				//add everything after timestamp
				result.add(iterateData);
			}
		}
		return result;
	}

}
