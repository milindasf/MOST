package bpi.most.client.modules.exporter.filter;

import bpi.most.client.utils.dnd.DNDController;
import bpi.most.client.utils.dnd.DragInterface;
import bpi.most.client.utils.dnd.DragWidget;
import bpi.most.client.utils.dnd.DropWidget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DragEndEvent;
import com.google.gwt.event.dom.client.DragEndHandler;
import com.google.gwt.event.dom.client.DragOverEvent;
import com.google.gwt.event.dom.client.DragOverHandler;
import com.google.gwt.event.dom.client.DragStartEvent;
import com.google.gwt.event.dom.client.DragStartHandler;
import com.google.gwt.event.dom.client.DropEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiChild;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Button;
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
 * @author mike
 * 
 */
public class ExportOptionsDragWidget extends DragWidget implements
		DragInterface {

	private static final Binder binder = GWT.create(Binder.class);

	/**
	 * The widget that is actually dragged. all status updates and handlers are
	 * based on this one.
	 */

	public ExportOptionsDragWidget ref = this;

	interface Binder extends UiBinder<Widget, ExportOptionsDragWidget> {
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

	@UiField
	Button closeButton;

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
	public ExportOptionsDragWidget(Widget w, String[] widgetStyleNames,
			String[] contentStyleNames, String[] dragElementStyleNames) {

		initWidget(binder.createAndBindUi(this));
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
	public ExportOptionsDragWidget() {
		super();
		initWidget(binder.createAndBindUi(this));

		getElement().setId(Document.get().createUniqueId());
		addHandlers();
	}

	/**
	 * DragWidget Constructor for code usage without passing css class names.
	 * 
	 * @param w
	 */
	public ExportOptionsDragWidget(Widget w) {
		super(w);
		initWidget(binder.createAndBindUi(this));

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
				event.getDataTransfer().setDragImage(getElement(), 10, 10);
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

		closeButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				removeFromParent();
			}
		});

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

		if (((DragInterface) DNDController.getCurrentDrag()).isDroppable()) {
			/**
			 * loop through all child elements of the current parent and check
			 * where to insert the dragged element
			 **/
			for (int i = 0; i < DNDController.getCurrentParentWidget()
					.getElement().getChildCount(); i++) {
				/** check if it's the right parent **/
				if (DNDController
						.getCurrentParentWidget()
						.getElement()
						.getChild(i)
						.toString()
						.contains(
								getWidget().getElement().getId().toString()
										.trim())) {
					/** specify where to insert the dragged element **/
					if ((event.getNativeEvent().getClientY() - DNDController
							.getRelMouseToElemY()) < (getWidget()
							.getAbsoluteTop() + getWidget().getOffsetHeight() / 2.5)) {
						Document.get()
								.getElementById(
										DNDController.getCurrentParentWidget()
												.getElement().getId())
								.insertBefore(
										DNDController.getCurrentDrag()
												.getElement(),
										getWidget().getElement());
						// DNDController.setIndex(i);
						// DNDController.setBefore(true);
					} else {
						Document.get()
								.getElementById(
										DNDController.getCurrentParentWidget()
												.getElement().getId())
								.insertAfter(
										DNDController.getCurrentDrag()
												.getElement(),
										getWidget().getElement());
						// DNDController.setIndex(i);
						// DNDController.setBefore(false);
					}
				} else {
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

		if (DNDController.getOriginalParentWidget() != DNDController
				.getCurrentParentWidget()) {

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
	
	public FilterWidget getFilter(){
		for(int i = 0;i< cont.getWidgetCount();i++){
			if (cont.getWidget(i) instanceof FilterWidget){
				return (FilterWidget) cont.getWidget(i);
			}
		}
		return null;
	}

}
