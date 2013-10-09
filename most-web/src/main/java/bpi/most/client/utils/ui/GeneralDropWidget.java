package bpi.most.client.utils.ui;

import java.util.ArrayList;

import bpi.most.client.utils.dnd.DNDController;
import bpi.most.client.utils.dnd.DragInterface;
import bpi.most.client.utils.dnd.DragWidget;
import bpi.most.client.utils.dnd.DropWidget;
import bpi.most.client.utils.dnd.MostDragEndEvent;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.DragEnterEvent;
import com.google.gwt.event.dom.client.DragEnterHandler;
import com.google.gwt.event.dom.client.DragOverEvent;
import com.google.gwt.event.dom.client.DragOverHandler;
import com.google.gwt.event.dom.client.DragStartEvent;
import com.google.gwt.event.dom.client.DragStartHandler;
import com.google.gwt.event.dom.client.DropEvent;
import com.google.gwt.event.dom.client.DropHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiChild;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

// TODO: Auto-generated Javadoc
/**
 * @author Stefan Glawischnig
 * @version 0.11a
 * 
 *          The DropWidget Class. It's accepting DragWidgets and communicates
 *          therefore with the DNDController, that tells, which widget is
 *          actually dragged.
 * 
 * 
 *          Handles the positioning of the Widgets, after the elements are moved
 *          to a new position to the DOM. This is necessary as we drag Widgets
 *          between different modules. If the widget remains in a module and the
 *          RootLayoutPanel is cleared, the element has no connection to the
 *          handlers of the widget - the widget is no longer referenced to the
 *          element.
 * 
 * 
 *          Likely to be replaced by FlowPanel in the Future.
 * 
 *          UIBINDER EXAMPLE: It is possible to define the DropWidget in plain
 *          Java or in a UIBinder xml template. The template structure for the
 *          import defined as "my" is:
 * 
 *          <my:DropWidget> <my:DragWidget> <my:header> HEADER CONTENT
 *          </my:header> <my:content> WIDGET CONTENT </my:content>
 *          </my:DragWidget> </my:DropWidget>
 * 
 * 
 */
public class GeneralDropWidget extends DropWidget {

	private static final Binder BINDER = GWT.create(Binder.class);

	/**
	 * The DropWidget as a panel. Just that it's not necessary to cast it when
	 * adding a widget
	 */
	private FlowPanel thisDropWidgetAsPanel;

	private ArrayList<String> accepts = new ArrayList<String>();

	private GeneralDropWidget ref = this;

	interface Binder extends UiBinder<Widget, GeneralDropWidget> {
	}

	/**
	 * The drop widget. This is just used for positioning (relative) and
	 * background styling
	 */
	@UiField
	FlowPanel dropWidget;

	/**
	 * The drop widget content. This is the actual PrarentWidget that contains
	 * DragWidgets. When dropping it handles positioning of the Widgets based on
	 * the Elements position
	 */
	@UiField
	FlowPanel dropWidgetContent;

	/**
	 * Instantiates a new drop widget.
	 * 
	 * @param dropWidgetStyleNames
	 *            the drop widget style names for positioning the DropWidget
	 * @param dropWidgetContentStyleNames
	 *            the drop widget content style names for positioning it's
	 *            children - the DragWidgets
	 */
	public GeneralDropWidget(String[] dropWidgetStyleNames,
			String[] dropWidgetContentStyleNames) {

		initWidget(BINDER.createAndBindUi(this));
		if (dropWidgetStyleNames != null) {
			for (int i = 0; i < dropWidgetStyleNames.length; i++) {
				getWidget().addStyleName(dropWidgetStyleNames[i]);
			}
		}
		if (dropWidgetContentStyleNames != null) {
			for (int i = 0; i < dropWidgetContentStyleNames.length; i++) {
				dropWidgetContent.addStyleName(dropWidgetContentStyleNames[i]);
			}
		}
		setThisDropWidgetAsPanel(((FlowPanel) dropWidgetContent));
		dropWidgetContent.getElement().setId(Document.get().createUniqueId());
		DNDController
				.setDropWidgetAsPanel(((FlowPanel) getThisDropWidgetAsPanel()));
		getElement().setId(Document.get().createUniqueId());
		addHandlers();

		registerHighlightElement(getElement());

	}

	/**
	 * Empty DropWidget Constructor without passing StyleNAmes for UIBinder
	 * usage.
	 * 
	 */
	public GeneralDropWidget() {

		initWidget(BINDER.createAndBindUi(this));
		setThisDropWidgetAsPanel(((FlowPanel) dropWidgetContent));
		dropWidgetContent.getElement().setId(Document.get().createUniqueId());
		DNDController
				.setDropWidgetAsPanel(((FlowPanel) getThisDropWidgetAsPanel()));
		getElement().setId(Document.get().createUniqueId());
		addHandlers();
		registerHighlightElement(getElement());
	}

	public GeneralDropWidget(String dropWidgetStyleNames,
			String dropWidgetContentStyleNames) {
		initWidget(BINDER.createAndBindUi(this));
		if (dropWidgetStyleNames != null) {

			getWidget().addStyleName(dropWidgetStyleNames);

		}
		if (dropWidgetContentStyleNames != null) {

			dropWidgetContent.addStyleName(dropWidgetContentStyleNames);

		}
		setThisDropWidgetAsPanel(((FlowPanel) dropWidgetContent));
		dropWidgetContent.getElement().setId(Document.get().createUniqueId());
		DNDController
				.setDropWidgetAsPanel(((FlowPanel) getThisDropWidgetAsPanel()));
		getElement().setId(Document.get().createUniqueId());
		addHandlers();

		registerHighlightElement(getElement());
	}

	/**
	 * UI Child for adding DragWidgets to a DropWidget
	 * 
	 * @param w
	 */
	@UiChild(tagname = "draggable")
	public boolean addDraggable(DragWidget w) {
		dropWidgetContent.add(w);
		return true;
	}

	public void addHandlers() {
		this.addDomHandler(new DragStartHandler() {

			@Override
			public void onDragStart(DragStartEvent event) {

				DNDController.setOriginalParentWidget(dropWidgetContent);
				DNDController.setCurrentParentWidget(dropWidgetContent);
				DNDController.setCurrentParent(ref);
				// int height = DNDController.getDragWidget().getOffsetHeight();
				// int width = DNDController.getDragWidget().getOffsetWidth();

			}
		}, DragStartEvent.getType());
		this.addDomHandler(new DragEnterHandler() {

			@Override
			public void onDragEnter(DragEnterEvent event) {

				DNDController.setCurrentParentWidget(dropWidgetContent);
				DNDController.setCurrentParent(ref);
				// Widget DOM does not get updated - check yourself

			}
		}, DragEnterEvent.getType());
		this.addDomHandler(new DragOverHandler() {

			@Override
			public void onDragOver(DragOverEvent event) {
				try {
				((DragInterface) DNDController.getCurrentDrag()).dragOverParentProcedure(event);
				}
				catch( Exception e) {
					
				}
//				if (DNDController.getCurrentDrag() instanceof DragWidget) {
//					if ((event.getNativeEvent().getClientY() - DNDController
//							.getRelMouseToElemY()) > (DNDController
//							.getCurrentParentWidget().getOffsetHeight() + DNDController
//							.getCurrentParentWidget().getAbsoluteTop())) {
//						DNDController
//								.getCurrentParentWidget()
//								.getElement()
//								.appendChild(
//										DNDController.getCurrentDrag()
//												.getElement());
//						// DNDController.setIndex(0);
//						// DNDController.setBefore(false);
//					}
//				}
				// DNDController.getCurrentParentWidget().getElement().appendChild(DNDController.getDragWidget().getElement());

			}
		}, DragOverEvent.getType());
		/**
		 * handles positioning of the Dragged Widgets, after the Element was
		 * reallocated. Still quite buggy.
		 **/
		this.addDomHandler(new DropHandler() {

			@Override
			public void onDrop(DropEvent event) {
				try {
				((DragInterface) DNDController.getCurrentDrag()).dropProcedure(
						ref, event);
				} catch( Exception e) {
					
				}

			}
		}, DropEvent.getType());
		try {
			DNDController.EVENT_BUS.fireEvent(new MostDragEndEvent());
		} catch (Exception e) {

		}
	}

	/**
	 * Gets the this drop widget as panel, so that it's not always necessary to
	 * cast when adding a widget. Maybe it will be replaced by using the super
	 * add method.
	 * 
	 * @return FlowPanel
	 */
	public FlowPanel getThisDropWidgetAsPanel() {

		return thisDropWidgetAsPanel;
	}

	/**
	 * Gets the current parent = DropWidget.
	 * 
	 * @return DropWidgetWidget
	 */
	public GeneralDropWidget getCurrentParent() {

		return this;
	}

	/**
	 * Returns the actual DragWidget's parent panel.
	 * 
	 * @return FLowPanel
	 */
	public FlowPanel getDropPanel() {

		return this.dropWidgetContent;
	}

	/**
	 * Specifies what kind of elements can be dropped on this DropWidget The
	 * type is specified based on a CSS Class, resp. StyleName. Drop Control is
	 * acomplished via setting the z-index based on this class names
	 * 
	 * @param String
	 */

	public void addDroppableForDragWidgetType(String styleName) {
		getElement().addClassName(styleName);
	}

	/**
	 * Sets the actual DragWidget's parent panel.
	 * 
	 * @param FlowPanel
	 */
	public void setThisDropWidgetAsPanel(FlowPanel thisDropWidgetAsPanel) {

		this.thisDropWidgetAsPanel = thisDropWidgetAsPanel;
	}

	public ArrayList<String> getAccepted() {
		return accepts;
	}

	public void accepts(String s) {
		this.accepts.add(s);
	}

	@Override
	public void registerHighlightElement(Element element) {
		super.registerHighlightElement(element);
	}

	@Override
	public void fireMostDragEndEvent() {
		// TODO Auto-generated method stub

	}

	// public void dropProcedure() {
	//
	// int ccount = DOM.getChildIndex(DNDController
	// .getCurrentParentWidget().getElement(),
	// DNDController.getCurrentDrag().getElement());
	//
	// if (ccount < (DNDController.getCurrentParent()
	// .getDropPanel().getWidgetCount() - 1)) {
	//
	// DNDController
	// .getCurrentParent()
	// .getDropPanel()
	// .insert(DNDController.getCurrentDrag(),
	// (ccount + 1));
	// } else {
	// DNDController.getCurrentParent().getDropPanel()
	// .add(DNDController.getCurrentDrag());
	// }
	//
	// if (DNDController.getOriginalParentWidget() != DNDController
	// .getCurrentParentWidget()) {
	//
	// }
	//
	// }
	//
	// public void dropProcedure(DropWidget dwidget,SensorLabel widget,
	// DropEvent event) {
	// LiveChartModuleWidget.getInstance().addDropHandler(dwidget,event);
	//
	//
	// }

}
