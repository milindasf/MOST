package bpi.most.client.model.eventservice;

import de.novanic.eventservice.client.event.Event;
import de.novanic.eventservice.client.event.listener.RemoteEventListener;

/**
 * The DpChangedListener listen to events which are fired from the server side eventservice
 * from the server (sender) to the client
 *
 * @author Johannes Weber
 */
public abstract class DpChangedListener implements RemoteEventListener {
	/**
	 * Function is called by the EventService
	 */
	/**
     * This Method is called from the EventService
     * @param anEvent {@link de.novanic.eventservice.client.event.Event}
     */
	public void apply(Event anEvent) {
		/*
		 * Check if the incoming Event is from the type DpChangedEvent.
		 * If so, call the corresponding function
		 */
		if(anEvent instanceof DpChangedEvent)
		{
			this.onDpChangedEvent((DpChangedEvent)anEvent);
		}
	}

	/**
	 * This Method gets called when the incomming Event is from the type DpChangedEvent
	 * @param event {@link bpi.most.client.model.eventservice.DpChangedEvent}
	 */
	abstract public void onDpChangedEvent(DpChangedEvent event);
}
