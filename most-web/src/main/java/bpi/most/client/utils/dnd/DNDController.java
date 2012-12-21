package bpi.most.client.utils.dnd;

import java.util.ArrayList;

import bpi.most.client.GConfig;
import bpi.most.client.utils.ui.GeneralDropWidget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * The Class DNDController.
 * 
 * @author Stefan Glawischnig
 * @author Michael Hönisch
 * @version 0.11a
 * 
 *          The Drag And Drop Controller. All Drag and Drop operations are
 *          passing through this controller. The Dragged widgets and elements
 *          are statically available, so it is possible to clear a panel during
 *          a drag operation and add the dragged item to a new, empty panel.
 * 
 *          The MOST DND functionality is based on Native GWT Drag and Drop. As
 *          this is still very error prone, there is no guarantee for MOST DND
 *          to work properly. So far, it has successfully been tested on
 *          chromium
 * 
 *          MOST DND uses an eventbus to fire and receive drag events all over
 *          the application. If a Drag event is fired the controller handles the
 *          highlighting process.
 * 
 */

@SuppressWarnings("deprecation")
public class DNDController {
	public static final EventBus EVENT_BUS = GWT.create(SimpleEventBus.class);
	/** x coordinate for firefox workaround */
	public static int clientX;

	/** y coordinate for firefox workaround */
	public static int clientY;
	/** drag is set to the current dragged DragWindow */
	private static Widget dragwindow = null;

	/**
	 * dragover is set to the dropable widget, which is currently in the
	 * foreground / focus
	 */
	private static Widget dragover = null;

	/**
	 * dragwidget is set to the current dragged widget, that can be wrapped in a
	 * DragWindow this is needed to get the widget instead of only a text from
	 * getData on a drop
	 */
	private static Widget dragwidget = null;

	/**
	 * dragitem is set to the current dragged widget, that contains any kind of
	 * information needed on drop
	 */
	private static Widget dragitem = null;

	/** The initial parent widget. */
	private static Widget originalParentWidget = null;

	/** current parent widget which is set on drag enter */
	private static Widget currentParentWidget = null;

	/** The drop widget as panel. */
	private static FlowPanel dropWidgetAsPanel = null;

	/** The rel mouse to elem x. (firefox) */
	private static Integer relMouseToElemX;

	/** The rel mouse to elem y. (firefox) */
	private static Integer relMouseToElemY;

	/** The dragon super master. (highlighting) */
	public static Element dragonSuperMaster = null;

	/**
	 * The highlight array list. here all elements that can be highlighted are
	 * stored. Elements can be added via the
	 * {@link registerHighlightElementEvent}
	 */
	private static ArrayList<Element> highlight = null;

	/** The current parent. */
	public static GeneralDropWidget currentParent = null;

	/** The current dragged widget. */
	public static Widget currentDrag = null;

	/** The index. deprecated */
	private static int index = 0;

	/** The before. deprecated */
	private static boolean before = false;

	/** DND Controller reference */
	private static DNDController ref = null;
	private static FlowPanel backgroundWidget = null;
	
	/**
	 * Instantiates a new DND controller.
	 */
	public DNDController() {
		
	}
	public DNDController(final Element highlightingBackgroundElement) {
		
		EVENT_BUS.addHandler(RegisterHighlightElementEvent.TYPE,
				new RegisterHighlightElementHandler() {

					@Override
					public void registerHighlightElement(
							RegisterHighlightElementEvent event) {

						DNDController.addHighlight(event.getHighlightElement());

					}
				});
		
		EVENT_BUS.addHandler(MostDragStartEvent.TYPE,
				new MostDragStartEventHandler() {

					@Override
					public void onDragStartEvent(
							MostDragStartEvent dragStartEvent) {
						// Window.alert(dragStartEvent.getDNDElement().toString());
						Element draggable = dragStartEvent.getDNDElement();
						DNDController.dndHighlightOn(draggable);
						DNDController.showBlankScreen(highlightingBackgroundElement);

						// Window.alert(draggable.getClassName());

					}
				});
		
		EVENT_BUS.addHandler(MostDragEndEvent.TYPE,
				new MostDragEndEventHandler() {

					@Override
					public void onDragEndEvent(MostDragEndEvent event) {

						DNDController.getInstance().hideBlankScreen();
						DNDController.dndHighlightOff();
						DNDController.setCurrentDragNull();
					}
				});
		
		EVENT_BUS.addHandler(DpWidgetDropEvent.TYPE, new DpWidgetDropEventHandler() {
			
			@Override
			public void onDpWidgetDropEvent(DpWidgetDropEvent event) {
				Window.alert("event fired");
			}
		});
	}

	
	/**
	 * Gets the single instance of DNDController.
	 * 
	 * @return single instance of DNDController
	 */
	public static DNDController getInstance(Element highlightingBackgroundElement) {

		if (ref == null) {
			ref = new DNDController(highlightingBackgroundElement);
		}
		return ref;
	}
	public static DNDController getInstance() {

		if (ref == null) {
			ref = new DNDController();
		}
		return ref;
	}

	/**
	 * Gets the dragwindow. - deprecated
	 * 
	 * @return the dragwindow
	 * 
	 */
	public static Widget getDragwindow() {

		return dragwindow;
	}

	/**
	 * Sets the dragwindow. - deprecated
	 * 
	 * @param dragwindow
	 *            the new dragwindow
	 * 
	 */
	public static void setDragwindow(Widget dragwindow) {

		DNDController.dragwindow = dragwindow;
	}

	/**
	 * Sets the dragwindow null. -deprecated
	 */
	public static void setDragwindowNull() {

		DNDController.dragwindow = null;
	}

	/**
	 * Gets the dragover.
	 * 
	 * @return the dragover
	 */
	public static Widget getDragover() {

		return dragover;
	}

	/**
	 * Sets the dragover.
	 * 
	 * @param dragover
	 *            the new dragover
	 */
	public static void setDragover(Widget dragover) {

		DNDController.dragover = dragover;
	}

	/**
	 * Sets the dragover null.
	 */
	public static void setDragoverNull() {

		DNDController.dragover = null;
	}

	/**
	 * Gets the dragwidget.
	 * 
	 * @return the dragwidget
	 */
	public static Widget getDragwidget() {

		return dragwidget;
	}

	/**
	 * Sets the dragwidget.
	 * 
	 * @param dragwidget
	 *            the new dragwidget
	 */
	public static void setDragwidget(Widget dragwidget) {

		DNDController.dragwidget = dragwidget;
	}

	/**
	 * Sets the dragwidget null.
	 */
	public static void setDragwidgetNull() {

		DNDController.dragwidget = null;
	}

	/**
	 * Gets the dragitem.
	 * 
	 * @return the dragitem
	 */
	public static Widget getDragitem() {

		return dragitem;
	}

	/**
	 * Sets the dragitem.
	 * 
	 * @param dragitem
	 *            the new dragitem
	 */
	public static void setDragitem(Widget dragitem) {

		DNDController.dragitem = dragitem;
	}

	/**
	 * Sets the dragitem null.
	 */
	public static void setDragitemNull() {

		DNDController.dragitem = null;
	}

	/**
	 * ********************************* NEW
	 * ****************************************.
	 * 
	 * @return the original parent widget
	 */

	/**
	 * @return the originalParentWidget
	 */
	public static Widget getOriginalParentWidget() {

		return originalParentWidget;
	}

	/**
	 * Sets the original parent widget.
	 * 
	 * @param originalParentWidget
	 *            the originalParentWidget to set
	 */
	public static void setOriginalParentWidget(Widget originalParentWidget) {

		DNDController.originalParentWidget = originalParentWidget;
	}

	/**
	 * Gets the current parent widget.
	 * 
	 * @return the currentParentWidget
	 */
	public static Widget getCurrentParentWidget() {

		return currentParentWidget;
	}

	/**
	 * Sets the current parent widget.
	 * 
	 * @param currentParentWidget
	 *            the currentParentWidget to set
	 */
	public static void setCurrentParentWidget(Widget currentParentWidget) {

		DNDController.currentParentWidget = currentParentWidget;
	}

	/**
	 * Gets the drop widget as panel.
	 * 
	 * @return the dropWidgetAsPanel
	 */
	public static FlowPanel getDropWidgetAsPanel() {

		return dropWidgetAsPanel;
	}

	/**
	 * Sets the drop widget as panel.
	 * 
	 * @param dropWidgetAsPanel
	 *            the dropWidgetAsPanel to set
	 */
	public static void setDropWidgetAsPanel(FlowPanel dropWidgetAsPanel) {

		DNDController.dropWidgetAsPanel = dropWidgetAsPanel;
	}

	/**
	 * Gets the rel mouse to elem x.
	 * 
	 * @return the relMouseToElemX
	 */
	public static Integer getRelMouseToElemX() {

		return relMouseToElemX;
	}

	/**
	 * Gets the rel mouse to elem y.
	 * 
	 * @return the relMouseToElemY
	 */
	public static Integer getRelMouseToElemY() {

		return relMouseToElemY;
	}

	/**
	 * Sets the rel mouse to elem x.
	 * 
	 * @param relMouseToElemX
	 *            the relMouseToElemX to set
	 */
	public static void setRelMouseToElemX(int relMouseToElemX) {

		DNDController.relMouseToElemX = relMouseToElemX;
	}

	/**
	 * Sets the rel mouse to elem y.
	 * 
	 * @param relMouseToElemY
	 *            the relMouseToElemY to set
	 */
	public static void setRelMouseToElemY(int relMouseToElemY) {

		DNDController.relMouseToElemY = relMouseToElemY;
	}



	/**
	 * Adds the highlight.
	 * 
	 * @param d
	 *            the d
	 */
	public static void addHighlight(Element d) {

		DNDController.getHighlight().add(d);

	}

	/**
	 * Gets the highlight.
	 * 
	 * @return the highlight
	 */
	public static ArrayList<Element> getHighlight() {

		if (highlight == null) {
			highlight = new ArrayList<Element>();
		}
		return highlight;
	}


	/**
	 * Show blank screen.
	 * 
	 * @param d
	 *            the d
	 */
	public static void showBlankScreen(Element d) {

		d.addClassName(GConfig.getUiDragPrefix() + GConfig.getBlankScreen());

	}

	/**
	 * Hide blank screen.
	 */
	public void hideBlankScreen() {

		// Window.alert("ts");
		try{
			Document.get()
				.getElementById("uid-dragonsupermaster")
				.removeClassName(
						GConfig.getUiDragPrefix() + GConfig.getBlankScreen());
		} catch(Exception e)
		{
			
		}
	}

	/**
	 * Dnd highlight on.
	 * 
	 * @param draggable
	 *            the draggable
	 */
	public static void dndHighlightOn(Element draggable) {
		
		draggable.addClassName(GConfig.getUiDragPrefix()
				+ GConfig.getDndHighlightDragged());
		getCurrentDrag().addStyleName(
				GConfig.getUiDragPrefix() + GConfig.getDndHighlightDragged());

		for (int i = 0; i < getHighlight().size(); i++) {
			String[] highlightClasses;
			highlightClasses = DNDController.getHighlight().get(i)
					.getClassName().split(" ");
			for (int j = 0; j < highlightClasses.length; j++) {
				
				if (DNDController.getCurrentDrag().getStyleName()
						.contains(highlightClasses[j])) {
					
					DNDController
							.getHighlight()
							.get(i)
							.addClassName(
									GConfig.getUiDragPrefix()
											+ GConfig.getDndHighlightDropable());
					

				}
			}
			highlightClasses = null;
		}
	}

	/**
	 * Dnd highlight off.
	 */
	public static void dndHighlightOff() {
		try {
		getCurrentDrag().removeStyleName(
				GConfig.getUiDragPrefix() + GConfig.getDndHighlightDragged());

		for (int i = 0; i < getHighlight().size(); i++) {

			DNDController
					.getHighlight()
					.get(i)
					.removeClassName(
							GConfig.getUiDragPrefix()
									+ GConfig.getDndHighlightDropable());
		}
		} catch (Exception e) {
			
		}

	}

	/**
	 * Gets the current parent.
	 * 
	 * @return the currentParent
	 */
	public static GeneralDropWidget getCurrentParent() {

		return currentParent;
	}

	/**
	 * Sets the current parent.
	 * 
	 * @param currentParent
	 *            the currentParent to set
	 */
	public static void setCurrentParent(GeneralDropWidget currentParent) {

		DNDController.currentParent = currentParent;
	}

	/**
	 * Gets the current drag.
	 * 
	 * @return the currentDrag
	 */
	public static Widget getCurrentDrag() {

		return currentDrag;
	}

	/**
	 * Sets the current drag.
	 * 
	 * @param currentDrag
	 *            the currentDrag to set
	 */
	public static void setCurrentDrag(Widget currentDrag) {

		DNDController.currentDrag = currentDrag;
	}

	/**
	 * Sets the current drag null.
	 */
	public static void setCurrentDragNull() {
		DNDController.setCurrentDrag(null);
	}

	/**
	 * Gets the index.
	 * 
	 * @return the index
	 */
	public static int getIndex() {

		return index;
	}

	/**
	 * Checks if is before.
	 * 
	 * @return the before
	 */
	public static boolean isBefore() {

		return before;
	}

	/**
	 * Sets the index.
	 * 
	 * @param index
	 *            the index to set
	 */
	public static void setIndex(int index) {

		DNDController.index = index;
	}

	/**
	 * Sets the before.
	 * 
	 * @param before
	 *            the before to set
	 */
	public static void setBefore(boolean before) {

		DNDController.before = before;
	}
	public static FlowPanel getBackgroundWidget() {
		return backgroundWidget;
	}
	public static void setBackgroundWidget(FlowPanel backgroundWidget) {
		DNDController.backgroundWidget = backgroundWidget;
	}

}
