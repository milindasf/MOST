package bpi.most.client.utils.dnd;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.shared.GwtEvent;

/**
 * The Class registerHighlightElementEvent. This event gets fired whenever a
 * DropWidget is initiated. The Drop WIdget gets added to the DNDCOntroller
 * which is necessary to start the highlighting procedure. Actually every kind
 * of widget can function as a drop element, because the DNDController just
 * works with the DOM Element.
 */
public class RegisterHighlightElementEvent extends
		GwtEvent<RegisterHighlightElementHandler> {

	/** The TYPE. */
	public static final Type<RegisterHighlightElementHandler> TYPE = new Type<RegisterHighlightElementHandler>();

	/** The highlight element. */
	private final Element highlightElement;

	/**
	 * Instantiates a new register highlight element event.
	 * 
	 * @param h
	 *            the h
	 */
	public RegisterHighlightElementEvent(Element h) {
		this.highlightElement = h;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.google.gwt.event.shared.GwtEvent#getAssociatedType()
	 */
	@Override
	public Type<RegisterHighlightElementHandler> getAssociatedType() {
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
	protected void dispatch(RegisterHighlightElementHandler handler) {
		// TODO Auto-generated method stub
		handler.registerHighlightElement(this);
	}

	/**
	 * Gets the highlight element.
	 * 
	 * @return the highlight element
	 */
	public Element getHighlightElement() {
		return highlightElement;
	}

}
