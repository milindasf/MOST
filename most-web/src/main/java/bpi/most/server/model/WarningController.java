package bpi.most.server.model;

import bpi.most.dto.DpDTO;
import bpi.most.dto.WarningDTO;

import java.util.Date;
import java.util.List;
import java.util.Observable;

/**
 * Handles warnings of datapoints
 * Currently only "global" observing of warnings is possible
 * @author robert.zach@tuwien.ac.at
 */
public final class WarningController extends Observable {
	private static WarningController ref = null;
 
	/**
	 * called by the PollService
	 * @param warning
	 */
	public void notifyObservers(WarningDTO warning) {
		setChanged();
		super.notifyObservers(warning);
	}
	
	/**
	 * @return all Warnings
	 */
	public List<WarningDTO> getWarnings() {
		return null;
	}
	public List<WarningDTO> getWarnings(Date from) {
		return null;
	}
	public List<WarningDTO> getWarnings(Date from, Date to) {
		return null;
	}
	public List<WarningDTO> getWarnings(String dpName) {
		return null;
	}
	public List<WarningDTO> getWarnings(DpDTO dp) {
		return null;
	}
	//TODO define other combinations
	
	/**
	 * Searches for requested warnings, argument is ignored if null
	 * @param starttime
	 * @param endtime
	 * @param dpName
	 * @param errorCode
	 * @param priority include everything with >= requested priority
	 * @param zone everything in this zone or any subzone
	 * @param source
	 * @return
	 */
	public List<WarningDTO> getWarnings(Date starttime, Date endtime,String dpName,int errorCode,int priority,int zone, String source) {
		return null;
	}

	
	//Singleton
	private WarningController() {
		super();
	}	
	public static WarningController getInstance(){
		if (ref == null) {
			ref = new WarningController();
		}
		return ref;
	}
	
}
