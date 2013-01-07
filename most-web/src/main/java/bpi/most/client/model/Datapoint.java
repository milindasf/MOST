package bpi.most.client.model;

import bpi.most.client.model.eventservice.EventManager;
import bpi.most.client.rpc.DpChangedEventService;
import bpi.most.client.rpc.DpChangedEventServiceAsync;
import bpi.most.client.utils.Observable;
import bpi.most.client.utils.Observer;
import bpi.most.dto.DpDataDTO;
import bpi.most.dto.DpDatasetDTO;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.Date;

/**
 * don't use synchronous methods on the client side
 */
public class Datapoint extends Observable {
	/**
	 * identifier of the datapoint
	 */
	private String datapointName;
	
	public static final DpChangedEventServiceAsync EVENT_SERVICE = GWT.create(DpChangedEventService.class);
	
	/**
	 * constructors
	 */
	public Datapoint() {
		super();
	}

	public Datapoint(String datapointName) {
		super();
		this.datapointName = datapointName;
	}

	/**
	 * Add a new measurement 
	 * @param measurement
	 * @return int
	 */
	public int addData(DpDataDTO measurement) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	/**
	 * provide setChanged public.
	 * Required for DpPollService to mark DPs changed.
	 */
	public void setChanged() {
		super.setChanged();
	}
	
	/**
	 * Add observer and start the live service
	 */
	public void addObserver(Observer observer){

		EventManager eventManager = EventManager.getInstance();
		eventManager.registerDp(this);
								
		super.addObserver(observer);
	}
	
	/**
	 * Deletes an observer from the set of observers of this object.
	 * @param observer
	 */
	public void deleteObserver(Observer observer){
		if(countObservers() <= 1)
		{
			EventManager eventManager = EventManager.getInstance();
			eventManager.unregisterDp(this);
		}
		super.deleteObserver(observer);
	}
	
	/**
	 * Clears the observer list so that this object no longer has any observers.
	 * Attention: Deletes all observers
	 */
	public void deleteObservers(){ 
		EventManager eventManager = EventManager.getInstance();
		eventManager.unregisterDp(this);
		super.deleteObservers();
	}
	
	public int delData() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int delData(Date starttime, Date endtime) {
		// TODO Auto-generated method stub
		return 0;
	}

	public DpDataDTO getData() {
		// TODO implement method for getting last point of the datapoint
		return null;
	}

	public DpDatasetDTO getData(Date starttime, Date endtime) {
		// TODO Auto-generated method stub
		return null;
	}

	// asynchronous method of getData(starttime, endtime)
	public DpDatasetDTO getData(Date starttime, Date endtime,
			final DatapointHandler dpHandler) {
		// set corresponding dp reference in dpHandler
		dpHandler.setDatapoint(this);
		// start RPC
		DpController.DP_SERVICE.getData(getDatapointName(), starttime, endtime,
				new AsyncCallback<DpDatasetDTO>() {

					@Override
					public void onSuccess(DpDatasetDTO result) {
						// put code for data preprocessing here
						dpHandler.onSuccess(result);
					}

					@Override
					public void onFailure(Throwable caught) {
						// TODO: implement better error handling
						Window.alert("getData(Date starttime, Date endtime) RPC-Error");
					}
				});

		return null;
	}

	/**
	 * A method used to get data between a start and end time with a specific
	 * period. This can be useful if the normal amount of data you get with the
	 * selected start and end time is too much.
	 * 
	 * @param starttime
	 *            Start time of the period you want the data.
	 * @param endtime
	 *            End time of the period you want the data.
	 * @param period
	 *            The period in seconds, with which you want the data.
	 * @param dpHandler
	 *            A custom Handler, with which you can handle the asynchronous
	 *            RPC call.
	 * @return Returns a DatapointDatasetVO with the values between start and end time
	 *         with the selected period.
	 */
	public DpDatasetDTO getDataPeriodic(Date starttime, Date endtime,
			Float period, final DatapointHandler dpHandler) {
		// set corresponding dp reference in dpHandler
		dpHandler.setDatapoint(this);
		// start RPC
		DpController.DP_SERVICE.getDataPeriodic(getDatapointName(), starttime,
				endtime, period, new AsyncCallback<DpDatasetDTO>() {

					@Override
					public void onSuccess(DpDatasetDTO result) {
						dpHandler.onSuccess(result);
					}

					@Override
					public void onFailure(Throwable caught) {
						// TODO: implement better error handling
						Window.alert("getDataPeriodic(getDatapointName(), starttime, endtime, period) RPC-Error");
					}
				});
		return null;
	}

	// getters and setters
	public String getDatapointName() {
		return datapointName;
	}

	// FIXME: add support for datapoint name change (change name in DB, etc.)
	public void setDatapointName(String datapointName) {
		this.datapointName = datapointName;
	}

	/**
	 * Get the number of values of the datapoint between the start and end time.
	 * 
	 * @param starttime
	 *            The start time of the time frame you want to know how many
	 *            values are in.
	 * @param endtime
	 *            The end time of the time frame you want to know how many
	 *            values are in.
	 * @param dpHandler
	 *            A custom Handler, with which you can handle the asynchronous
	 *            RPC call.
	 * @return The number of values in the time frame between start and end
	 *         time.
	 */
	public Integer getNumberOfValues(Date starttime, Date endtime,
			final DatapointHandler dpHandler) {
		// set corresponding dp reference in dpHandler
		dpHandler.setDatapoint(this);
		// start RPC
		DpController.DP_SERVICE.getNumberOfValues(getDatapointName(), starttime,
				endtime, new AsyncCallback<Integer>() {

					@Override
					public void onSuccess(Integer result) {
						dpHandler.onSuccess(result);
					}

					@Override
					public void onFailure(Throwable caught) {
						// TODO: implement better error handling
						Window.alert("getNumberOfValues(getDatapointName(), starttime, endtime) RPC-Error");
					}
				});
		return null;
	}

	public String getAccuracy() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getCustomAttr(String myAttr) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getUnit() {
		// TODO Auto-generated method stub
		return null;
	}

	public Double getMin() {
		// TODO Auto-generated method stub
		return null;
	}

	public Double getMax() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getType() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getMathOperations() {
		// TODO Auto-generated method stub
		return null;
	}

	public Double getDeadband() {
		// TODO Auto-generated method stub
		return null;
	}

	public Double getSampleInterval() {
		// TODO Auto-generated method stub
		return null;
	}

	public Double getSampleIntervalMin() {
		// TODO Auto-generated method stub
		return null;
	}

	public Double getWatchdog() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getDeviceName() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getModel() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getVendor() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getVirtual() {
		// TODO Auto-generated method stub
		return null;
	}

}
