package bpi.most.client.model.eventservice;

import java.util.ArrayList;

import bpi.most.client.model.Datapoint;
import bpi.most.client.model.DpController;
import bpi.most.client.rpc.DpChangedEventService;
import bpi.most.client.rpc.DpChangedEventServiceAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

import de.novanic.eventservice.client.event.RemoteEventService;
import de.novanic.eventservice.client.event.RemoteEventServiceFactory;
import de.novanic.eventservice.client.event.domain.Domain;
import de.novanic.eventservice.client.event.domain.DomainFactory;

/**
 * The EventManager handes all objects which are required for using the GWT EventService
 *
 * @author Johannes Weber
 */
public final class EventManager {
	/**
     * Singleton reference 
     */
	private static EventManager ref = null;
	
	/**
     * Current Datapoints which are in live mode 
     */
	private ArrayList<Datapoint> datapoints = new ArrayList<Datapoint>();
	
	/**
	 * GWT event service
	 */
	public static final DpChangedEventServiceAsync CHANGED_EVENT_SERVICE = GWT.create(DpChangedEventService.class);
	private RemoteEventServiceFactory theEventServiceFactory = RemoteEventServiceFactory.getInstance();;
	private RemoteEventService remoteEventService = theEventServiceFactory.getRemoteEventService();;
	
	/**
	 * Construct the EventManager
	 */
	private EventManager() {
		super();
	}
	
	/**
	 * This Method register a Datapoint to the event listener
	 * @param dp
	 */
	public void registerDp(Datapoint dp) {
		String dpName = dp.getDatapointName();
		
		// check if the given Datapoint is already listening
		if(remoteEventService.getActiveDomains().contains(getDomainByDpName(dpName)))
		{
			return;
		}
		
		/**
		 * @TODO check error handling
		 */
		//start live listening on server
		CHANGED_EVENT_SERVICE.startListening(dpName, new AsyncCallback<Void>(){
			public void onFailure(Throwable caught) {}
			public void onSuccess(Void result) {}
		});
		
		//start listening on event service
		Domain domain = getDomainByDpName(dpName);
		remoteEventService.addListener(domain, new DpChangedListener(){
			public void onDpChangedEvent(DpChangedEvent event)
			{
				Datapoint dp = DpController.getSingleton().getDatapoint(event.getDpName());
				dp.setChanged();
				dp.notifyObservers(event.getDpData());
			}
		});
		
		datapoints.add(dp);
	}
	
	/**
	 * This Method unregister the givben Datapoint from the event listener
	 * @param dp
	 */
	public void unregisterDp(Datapoint dp) {
		String dpName = dp.getDatapointName();
		
		//stop listening on server
		//TODO check error handling
		CHANGED_EVENT_SERVICE.stopListening(dpName, new AsyncCallback<Void>(){
			public void onFailure(Throwable caught) {}
			public void onSuccess(Void result) {}
		});
		
		//stop listening on event service
		Domain domain = getDomainByDpName(dpName);
		remoteEventService.removeListeners(domain);
	}
	
	/**
	 * This Method removes all eventlistener
	 */
	public void cleanListeners()
	{
		for (Datapoint dp : datapoints) {
			if (dp.countObservers() <= 0) {
				unregisterDp(dp);
			}
		}
	}
	
	/**
	 * This Method returns a Domain which is generated by the given dpName
	 */
	private Domain getDomainByDpName(String dpName)
	{
		return DomainFactory.getDomain("dplive_" + dpName);
	}
	
	/**
	 * This Method returns the current EventManager
	 * @return ref
	 */
	public static EventManager getInstance()
	{
		if (ref == null) {
			ref = new EventManager();
		}
		return ref;
	}
}
