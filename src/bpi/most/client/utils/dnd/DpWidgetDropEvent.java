package bpi.most.client.utils.dnd;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.ui.Widget;

/**
 * The Class MostDragEndEvent. This event gets fired whenever a DNDOperation
 * stops. The DNDController then handles the highlighting off process
 */
public class DpWidgetDropEvent extends GwtEvent<DpWidgetDropEventHandler> {

	public DpWidgetDropEvent(Widget draggable) {
		this.draggable = draggable;
	}

	/** The TYPE. */
	public static final Type<DpWidgetDropEventHandler> TYPE = new Type<DpWidgetDropEventHandler>();

	private final Widget draggable;
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.google.gwt.event.shared.GwtEvent#getAssociatedType()
	 */
	@Override
	public Type<DpWidgetDropEventHandler> getAssociatedType() {
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
	protected void dispatch(DpWidgetDropEventHandler handler) {
		// TODO Auto-generated method stub
		handler.onDpWidgetDropEvent(this);
	}

	public Widget getDraggable() {
		return draggable;
	}

	public static Type<DpWidgetDropEventHandler> getTYPE() {
		return TYPE;
	}
	
	

}
