package bpi.most.client.utils.dnd;

import bpi.most.client.utils.dnd.MostDragStartEventHandler;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.shared.GwtEvent;

// TODO: Auto-generated Javadoc
/**
 * The Class MostDragStartEvent. This event gets fired whenever a DND Operation
 * starts, e.g. DragWidget, SensorLabel etc. Initiates the DNDCOntroller
 * highlighting process
 */
public class MostDragStartEvent extends GwtEvent<MostDragStartEventHandler> {

	/** The TYPE. */
	public static final Type<MostDragStartEventHandler> TYPE = new Type<MostDragStartEventHandler>();

	/** The draggable. */
	private final Element draggable;

	/**
	 * Instantiates a new most drag start event.
	 * 
	 * @param draggable
	 *            the draggable
	 */
	public MostDragStartEvent(Element draggable) {
		this.draggable = draggable;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.google.gwt.event.shared.GwtEvent#getAssociatedType()
	 */
	@Override
	public Type<MostDragStartEventHandler> getAssociatedType() {
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
	protected void dispatch(MostDragStartEventHandler handler) {
		handler.onDragStartEvent(this);
	}

	/**
	 * Gets the dND element.
	 * 
	 * @return the dND element
	 */
	public Element getDNDElement() {
		return draggable;
	}

}
