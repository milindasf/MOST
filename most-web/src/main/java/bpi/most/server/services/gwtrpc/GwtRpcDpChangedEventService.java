package bpi.most.server.services.gwtrpc;

import java.util.ArrayList;
import java.util.Date;
import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import bpi.most.client.model.eventservice.DpChangedEvent;
import bpi.most.client.rpc.DpChangedEventService;
import bpi.most.server.model.Datapoint;
import bpi.most.server.model.DpController;
import bpi.most.server.services.DatapointService;
import bpi.most.server.services.User;
import bpi.most.shared.DpDTO;
import bpi.most.shared.DpDataDTO;
import de.novanic.eventservice.client.event.domain.Domain;
import de.novanic.eventservice.client.event.domain.DomainFactory;
import de.novanic.eventservice.service.RemoteEventServiceServlet;

/**
 * The GwtRpcDpChangedEventService handle the attached datapoints which should be updated
 * after a new measurement is assigned
 *
 * @TODO move this function to DatapointService
 * @author Johannes Weber
 */
public class GwtRpcDpChangedEventService extends RemoteEventServiceServlet
implements DpChangedEventService {
	/**
	 * Serial Version ID needed for XML-RPC calls
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * The event generator timer
	 */
	@SuppressWarnings("unused")
	private static Timer myEventGeneratorTimer;
	
	/**
	 * List of the current attached Datapoints and TODO Event Observers
	 */
	private ArrayList<Datapoint> activeEventListeners = new ArrayList<Datapoint>();
	
	private static final Logger LOG = Logger.getLogger(GwtRpcDpChangedEventService.class);

	/**
	 * This Method starts listening for new measurement data
	 * in this case, the measurement is generyted by that method
	 * @param dpName
	 */
	public void startListening(String dpName) {
		User user;
		// get user of session
		HttpSession session = this.getThreadLocalRequest().getSession(true);
		user = (User) session.getAttribute("mostUser");
		
		LOG.debug("start listening for " + dpName);
		
		//TODO don't access model here!!! Only use generic service classes!!! --> remember something else...
		Datapoint dataPoint = DpController.getInstance().getDatapoint(dpName);
		if(!activeEventListeners.contains(dataPoint))
		{
			//create DpChangedEventListener
			DpChangedEventListener eventListener = new DpChangedEventListener() {
				public void update(Observable o, Object arg) {
					Datapoint targetDatapoint = (Datapoint) o;
					DpDataDTO measurement = (DpDataDTO) arg;
					String dpName = targetDatapoint.getDatapointName();
					
					DpChangedEvent changedEvent = new DpChangedEvent();
					changedEvent.setDpName(targetDatapoint.getDatapointName());
					changedEvent.setDpDataDTO(measurement);
					LOG.debug("gwteventservice event on " + dpName);
					
					Domain domain = getDomainByDpName(dpName);
					addEvent(domain, changedEvent);
				}
			};
			
			// attach the observer
			if (DatapointService.getInstance().addDpObserver(user, new DpDTO(dpName), eventListener)) {
				//remember active event listeners TODO and DpChangedEventListener
				activeEventListeners.add(dataPoint);
				LOG.debug("add observer for " + dpName);
				
				/* uncomment to generate new values every 2 seconds on all datapoints which start listening
				// push test data to the client 
				if(myEventGeneratorTimer == null) {
		            myEventGeneratorTimer = new Timer();
		            myEventGeneratorTimer.schedule(new ServerMessageGeneratorTimerTask(), 0, 2000);
		        }
		        */
			}
		}
	}
	
	/**
	 * removes the event observer
	 */
	public void stopListening(String dpName) {
		@SuppressWarnings("unused")
		User user;
		// get user of session
		HttpSession session = this.getThreadLocalRequest().getSession(true);
		user = (User) session.getAttribute("mostUser");

		//TODO get observer of respective DP and -->
		//DatapointService.getInstance().deleteDpObserver(user, new DpDTO(dpName), observer);
	}
	
	/**
	 * Get Domain for given DatapointVO name
	 */
	/**
	 * This Method generate and returns the Domain depended on the provided dpName
	 * @param dpName
	 */
	private Domain getDomainByDpName(String dpName)
	{
		return DomainFactory.getDomain("dplive_" + dpName);
	}
	
	/**
	 * This Class generates & stores new measurements for a attached Datapoint
	 */
	@SuppressWarnings("unused")
	private class ServerMessageGeneratorTimerTask extends TimerTask
    {
        public void run() {
        	for(Datapoint dataPoint : activeEventListeners)
        	{
        		Date timestamp = new Date();
            	Double min = 0.1;
            	Double max = 100.0;
            	Double randValue = min + (int)(Math.random() * ((max - min) + 1));
            	DpDataDTO measurement = new DpDataDTO(timestamp, randValue);
            	
            	dataPoint.setChanged();
            	dataPoint.notifyObservers(measurement);
        	}
        }
    }
}
