package bpi.most.client.model.eventservice;

import bpi.most.dto.DpDataDTO;
import de.novanic.eventservice.client.event.Event;

/**
 * The DpChangedEvent handles the changed Datapoint measurement
 * from the server (sender) to the client
 *
 * @author Johannes Weber
 */
public class DpChangedEvent implements Event {
	public static final long serialVersionUID = 1L;
	/**
	 * The Name of the Datapoint
	 */
	public String dpName;
	/**
	 * The changed Datapoint measurement
	 */
	public DpDataDTO dpData;
	
	/**
     * Empty constructor
     */
	public DpChangedEvent() {};
	
	/**
     * This Method sets the Datapoint name
     * @param dpName Name of the changed Datapoint
     */
	public void setDpName(String dpName)
	{
		this.dpName = dpName;
	}
	
	/**
	 * This method returns the name of the changed Datapoint
	 * @return dpName
	 */
	public String getDpName()
	{
		return this.dpName;
	}
	
	/**
	 * This method set the single measuremet data from the changed Datapoint
	 * @return dpDataDTO
	 */
	public void setDpDataDTO(DpDataDTO dpDataDTO)
	{
		this.dpData = dpDataDTO;
	}

	/**
	 * This method returns the measurement data from the changed Datapoint
	 * @return dpData
	 */
	public DpDataDTO getDpData()
	{
		return this.dpData;
	}
}
