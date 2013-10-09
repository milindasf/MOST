package bpi.most.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Is an ArrayList of DatapointDataVO (single measurements).
 * FIXME Implement some Hashlist for performance optimization during searches in the DatapointDatasetVO
 * FIXME Research of overhead code (getDataBefore(), etc. ) is transfered over the wire (client - server) each time? If so, create static helper Class.
 * @author robert.zach@tuwien.ac.at
 */
public class DpDatasetDTO extends ArrayList<DpDataDTO> implements Serializable{
	private static final long serialVersionUID = -981884783836456778L;
	private String datapointName = null;
	
	public DpDatasetDTO() {
		super();
	}
	public DpDatasetDTO(String datapointName) {
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
	 * @return A DatapointDataVO before the respective timestamp, null if nothing before timstamp
	 */
	public DpDataDTO getDataBefore(Date timestamp) {
		//FIXME: date sorted arraylist is assumed right now, implement compare --> sort
		DpDataDTO result = null;
		for (DpDataDTO iterateData : this) {
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
	 * @return A DatapointDataVO before or equal the respective timestamp, null if nothing before timstamp
	 */
	public DpDataDTO getDataBeforeOrEqual(Date timestamp) {
		//FIXME: date sorted arraylist is assumed right now, implement compare --> sort
		DpDataDTO result = null;
		for (DpDataDTO iterateData : this) {
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
	 * @return A DatapointDatasetVO (all measurements) before the respective timestamp, emtpy Dataset if nothing is before timestamp
	 */
	public DpDatasetDTO getDatasetBefore(Date timestamp) {
		//FIXME: date sorted arraylist is assumed right now, implement compare --> sort
		DpDatasetDTO result = new DpDatasetDTO(getDatapointName());
		
		for (DpDataDTO iterateData : this) {
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
	 * @return A DatapointDatasetVO (all measurements) before or equal the respective timestamp, emtpy Dataset if nothing is before timestamp
	 */
	public DpDatasetDTO getDatasetBeforeOrEqual(Date timestamp) {
		//FIXME: date sorted arraylist is assumed right now, implement compare --> sort
		DpDatasetDTO result = new DpDatasetDTO(getDatapointName());
		
		for (DpDataDTO iterateData : this) {
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
	 * @return A DatapointDataVO after the respective timestamp, null if nothing after timstamp
	 */
	public DpDataDTO getDataAfter(Date timestamp) {		
		//FIXME: date sorted arraylist is assumed right now, implement compare --> sort
		DpDataDTO result = null;
		for (DpDataDTO iterateData : this) {
			if (iterateData.getTimestamp().after(timestamp)) {
				//return first value after timestamp
				return iterateData;
			}
		}
		return result;
	}
	/**
	 * @return A DatapointDataVO after or equal the respective timestamp, null if nothing after timstamp
	 */
	public DpDataDTO getDataAfterOrEqual(Date timestamp) {		
		//FIXME: date sorted arraylist is assumed right now, implement compare --> sort
		DpDataDTO result = null;
		for (DpDataDTO iterateData : this) {
			//after or equal date
			if (iterateData.getTimestamp().compareTo(timestamp) >= 0) {
				//return first value after timestamp
				return iterateData;
			}
		}
		return result;
	}
	/**
	 * @return A DatapointDatasetVO (all measurements) before the respective timestamp, emtpy Dataset if nothing is before timestamp
	 */
	public DpDatasetDTO getDatasetAfter(Date timestamp) {
		//FIXME: date sorted arraylist is assumed right now, implement compare --> sort
		DpDatasetDTO result = new DpDatasetDTO(getDatapointName());
		
		for (DpDataDTO iterateData : this) {
			if (iterateData.getTimestamp().after(timestamp)) {
				//add everything after timestamp
				result.add(iterateData);
			}
		}
		return result;
	}
	/**
	 * @return A DatapointDatasetVO (all measurements) before the respective timestamp, emtpy Dataset if nothing is before timestamp
	 */
	public DpDatasetDTO getDatasetAfterOrEqual(Date timestamp) {
		//FIXME: date sorted arraylist is assumed right now, implement compare --> sort
		DpDatasetDTO result = new DpDatasetDTO(getDatapointName());
		
		for (DpDataDTO iterateData : this) {
			//after or equal date
			if (iterateData.getTimestamp().compareTo(timestamp) >= 0) {
				//add everything after timestamp
				result.add(iterateData);
			}
		}
		return result;
	}

}
