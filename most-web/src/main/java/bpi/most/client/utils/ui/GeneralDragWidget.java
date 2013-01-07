package bpi.most.client.utils.ui;

import bpi.most.client.utils.dnd.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiChild;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * 
 * 
 * The Class DragWidget. The DragWidget is Composite though maybe it will be
 * changed to FlowPanel as just FlowPanel functionality is used. It can wrap any
 * content that is of type Widget. The DND functionality works mostly on Element
 * level. Uses just native GWT functionality, no libraries included - should
 * work similar to igoogle dnd + including highlighting functionality: Highlight
 * droppable areas, dim the rest.
 * 
 * 
 * Positioning is solved moving as much logic to the Browser floating Engine.
 * Alternative solution to absolute positioning
 * 
 * + better performance + no z-index issues + easier to organize
 * 
 * - floating unhandy when droppable containers are to large.
 * 
 * TODO: error handling check if there are better ways (performance) to improve
 * the solution caching
 * 
 * UIBINDER EXAMPLE: It is possible to define the DragWidget in plain Java or in
 * a UIBinder xml template. The template structure for the import defined as
 * "my" is:
 * 
 * <my:DragWidget> <my:header> HEADER CONTENT </my:header> <my:content> WIDGET
 * CONTENT </my:content> </my:DragWidget>
 * 
 * @author Stefan Glawischnig
 * @version 0.11a
 * 
 */
public class GeneralDragWidget extends DragWidget implements DragInterface {

	private static final Binder BINDER = GWT.create(Binder.class);
	
	private static final int HORIZONTAL_OFFSET = 10;
	private static final int VERTICAL_OFFSET = 10;

	/**
	 * The widget that is actually dragged. all status updates and handlers are
	 * based on this one.
	 */

	public GeneralDragWidget ref = this;

	interface Binder extends UiBinder<Widget, GeneralDragWidget> {
	}

	/**
	 * The head panel that is used for actual DND interaction with the mouse
	 * (show mouse pointer
	 */
	@UiField
	FlowPanel draggin;

	/** The content panel, that actually wraps the widget that has to be dragged */
	@UiField
	FlowPanel cont;

	/**
	 * The parent panel, that wraps the head and content panel. Basically it's
	 * just used for styling and keeping everything together
	 */
	@UiField
	FlowPanel dragParent;

	/**
	 * Instantiates a new drag widget. It is possible to pass String arrays with
	 * class names for styling
	 * 
	 * @param w
	 *            the wrapped widget
	 * @param widgetStyleNames
	 *            style names for the DragWidget. Necessary for floating and
	 *            styling
	 * @param contentStyleNames
	 *            the style names for the content div. eg. content bg
	 * @param dragElementStyleNames
	 *            the drag element style names for the header. eg. header bg
	 *            image
	 */
	public GeneralDragWidget(Widget w, String[] widgetStyleNames,
			String[] contentStyleNames, String[] dragElementStyleNames) {

		initWidget(BINDER.createAndBindUi(this));
		if (widgetStyleNames != null) {
			for (int i = 0; i < widgetStyleNames.length; i++) {
				getWidget().addStyleName(widgetStyleNames[i]);
			}
		}
		if (contentStyleNames != null) {
			for (int i = 0; i < contentStyleNames.length; i++) {
				cont.addStyleName(contentStyleNames[i]);
			}
		}
		if (dragElementStyleNames != null) {
			for (int i = 0; i < dragElementStyleNames.length; i++) {
				draggin.addStyleName(dragElementStyleNames[i]);
			}
		}

		getElement().setId(Document.get().createUniqueId());
		addHandlers();
		cont.add(w);
	}

	/**
	 * Empty DragWidget Constructor Preferred for usage in UIBinder
	 */
	public GeneralDragWidget() {
		super();
		initWidget(BINDER.createAndBindUi(this));

		getElement().setId(Document.get().createUniqueId());
		addHandlers();
	}

	/**
	 * DragWidget Constructor for code usage without passing css class names.
	 * 
	 * @param w
	 */
	public GeneralDragWidget(Widget w) {
		super(w);
		initWidget(BINDER.createAndBindUi(this));

		getElement().setId(Document.get().createUniqueId());
		addHandlers();
		cont.add(w);

	}

	/**
	 * Defines UI Header Child for UIBinder usage. Content gets added to the
	 * header Panel. (Drag and Drop functionality)
	 * 
	 * @param header
	 */
	@UiChild(tagname = "header")
	public void addHeader(HorizontalPanel header) {
		draggin.add(header);
	}

	/**
	 * Defines UI COntent CHild for UIBinder usage. Content gets added to the
	 * content panel.
	 * 
	 * @param content
	 */
	@UiChild(tagname = "content")
	public void addContent(FlowPanel content) {
		cont.add(content);
	}

	/**
	 * The handlers on the Header Divs are used for the DND action. The reason
	 * for this workaround is to keep the content editable and just bind the DND
	 * handlers to the header, which has no other functionality
	 **/
	public void addHandlers() {

		draggin.asWidget().addDomHandler(new MouseOverHandler() {

			@Override
			public void onMouseOver(MouseOverEvent event) {

				draggin.getElement().setPropertyBoolean("draggable", false);
				getElement().setPropertyBoolean("draggable", true);
			}
		}, MouseOverEvent.getType());
		draggin.asWidget().addDomHandler(new MouseOutHandler() {

			@Override
			public void onMouseOut(MouseOutEvent event) {

				getElement().setPropertyBoolean("draggable", false);
				draggin.getElement().setPropertyBoolean("draggable", true);
			}
		}, MouseOutEvent.getType());

		getWidget().addDomHandler(new DragStartHandler() {

			@Override
			public void onDragStart(DragStartEvent event) {

				event.setData("dragId", "drag");
				event.getDataTransfer().setDragImage(getElement(), HORIZONTAL_OFFSET, VERTICAL_OFFSET);
				registerDragWidget(ref);
				fireMostDragStartEvent(event);

				// firefox workaround as ff doesn't receive the event
				// coordinates
				if (event.getNativeEvent().getClientX() >= getAbsoluteLeft()) {
					DNDController.setRelMouseToElemX(event.getNativeEvent()
							.getClientX() - getAbsoluteLeft());
				}
				if (event.getNativeEvent().getClientY() >= getAbsoluteTop()) {
					DNDController.setRelMouseToElemY(event.getNativeEvent()
							.getClientY() - getAbsoluteTop());
				}

			}

		}, DragStartEvent.getType());

		getWidget().addDomHandler(new DragOverHandler() {

			@Override
			public void onDragOver(DragOverEvent event) {
				dragOverProcedure(event);

			}
		}, DragOverEvent.getType());
		getWidget().addDomHandler(new DragEndHandler() {

			@Override
			public void onDragEnd(DragEndEvent event) {
				fireMostDragEndEvent();

			}
		}, DragEndEvent.getType());
		
		getWidget().addDomHandler(new DropHandler() {
			
			@Override
			public void onDrop(DropEvent event) {
				DNDController.EVENT_BUS.fireEvent(new MostDragEndEvent());
				
			}
		}, DropEvent.getType());

	}

	/**
	 * specifies the Draggable Type of this element via a stylename This
	 * styleName is used for identifying possible droppables and for droppable
	 * control via z-index
	 * 
	 * @param String
	 */
	public void addDragWidgetType(String styleName) {
		getElement().addClassName(styleName);
	}

	
	public void dragOverProcedure(DragOverEvent event) {

		/**
		 * loop through all child elements of the current parent and check where
		 * to insert the dragged element
		 **/
		for (int i = 0; i < DNDController.getCurrentParentWidget().getElement()
				.getChildCount(); i++) {
			/** check if it's the right parent **/
			if (DNDController
					.getCurrentParentWidget()
					.getElement()
					.getChild(i)
					.toString()
					.contains(
							getWidget().getElement().getId().toString().trim())) {
				/** specify where to insert the dragged element **/
				if ((event.getNativeEvent().getClientY() - DNDController
						.getRelMouseToElemY()) < (getWidget().getAbsoluteTop() + getWidget()
						.getOffsetHeight() / 2.5)) {
					Document.get()
							.getElementById(
									DNDController.getCurrentParentWidget()
											.getElement().getId())
							.insertBefore(
									DNDController.getCurrentDrag().getElement(),
									getWidget().getElement());
					// DNDController.setIndex(i);
					// DNDController.setBefore(true);
				} else {
					Document.get()
							.getElementById(
									DNDController.getCurrentParentWidget()
											.getElement().getId())
							.insertAfter(
									DNDController.getCurrentDrag().getElement(),
									getWidget().getElement());
					// DNDController.setIndex(i);
					// DNDController.setBefore(false);
				}
			}
		}
	}

	@Override
	public void dropProcedure(DropWidget dropOnMe, DropEvent event) {
		int ccount = DOM.getChildIndex(DNDController.getCurrentParentWidget()
				.getElement(), DNDController.getCurrentDrag().getElement());

		if (ccount < (DNDController.getCurrentParent().getDropPanel()
				.getWidgetCount() - 1)) {

			DNDController.getCurrentParent().getDropPanel()
					.insert(DNDController.getCurrentDrag(), (ccount + 1));
		} else {
			DNDController.getCurrentParent().getDropPanel()
					.add(DNDController.getCurrentDrag());
		}
	}

	@Override
	public void dragOverParentProcedure(DragOverEvent event) {
		if ((event.getNativeEvent().getClientY() - DNDController
				.getRelMouseToElemY()) > (DNDController
				.getCurrentParentWidget().getOffsetHeight() + DNDController
				.getCurrentParentWidget().getAbsoluteTop())) {
			DNDController.getCurrentParentWidget().getElement()
					.appendChild(DNDController.getCurrentDrag().getElement());
			// DNDController.setIndex(0);
			// DNDController.setBefore(false);
		}
	}
}
