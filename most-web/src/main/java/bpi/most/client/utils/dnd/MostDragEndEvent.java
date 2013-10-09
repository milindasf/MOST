package bpi.most.client.utils.dnd;

import com.google.gwt.event.shared.GwtEvent;

/**
 * The Class MostDragEndEvent. This event gets fired whenever a DNDOperation
 * stops. The DNDController then handles the highlighting off process
 */
public class MostDragEndEvent extends GwtEvent<MostDragEndEventHandler> {

	/** The TYPE. */
	public static final Type<MostDragEndEventHandler> TYPE = new Type<MostDragEndEventHandler>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.google.gwt.event.shared.GwtEvent#getAssociatedType()
	 */
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<MostDragEndEventHandler> getAssociatedType() {
		// TODO Auto-generated method stub
		return TYPE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.google.gwt.event.shared.GwtEvent#dispatch(com.google.gwt.event.shared
	 * .EventHandler)
	 */
	@Override
	protected void dispatch(MostDragEndEventHandler handler) {
		// TODO Auto-generated method stub
		handler.onDragEndEvent(this);
	}

}
