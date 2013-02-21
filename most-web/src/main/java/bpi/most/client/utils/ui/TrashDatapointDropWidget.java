package bpi.most.client.utils.ui;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;

import bpi.most.client.utils.dnd.DpWidgetDropEvent;
import bpi.most.client.utils.dnd.DpWidgetDropEventHandler;

/**
 * The TrashDatapointDropWidget class. It's a helper class and should be used to
 * easily remove data points from the {@link CollectDatapointDropWidget} to
 * which it belongs.
 * 
 * @author mike
 * 
 */
public class TrashDatapointDropWidget extends GeneralDropWidget {

	/**
	 * The {@link CollectDatapointDropWidget} to which this widget belongs.
	 */
	CollectDatapointDropWidget cddWidget = null;

	/**
	 * An optional button to remove all data points from the
	 * {@link CollectDatapointDropWidget} to which it belongs.
	 */
	Button removeAll = new Button("remove all");

	/**
	 * Creates a new TrashDatapointDropWidget.
	 * 
	 */
	public TrashDatapointDropWidget() {
		super();
		initTrashDPWidget(false);
	}

	/**
	 * Creates a new TrashDatapointDropWidget.
	 * 
	 * @param dropWidgetStyleNames
	 *            The drop widget style names for positioning the DropWidget.
	 * @param dropWidgetContentStyleNames
	 *            The drop widget content style names for positioning it's
	 *            children - the DragWidgets.
	 */
	public TrashDatapointDropWidget(String dropWidgetStyleNames, String dropWidgetContentStyleNames) {
		super(dropWidgetStyleNames, dropWidgetContentStyleNames);
		initTrashDPWidget(false);
	}

	/**
	 * Creates a new TrashDatapointDropWidget.
	 * 
	 * @param dropWidgetStyleNames
	 *            The drop widget style names for positioning the DropWidget.
	 * @param dropWidgetContentStyleNames
	 *            The drop widget content style names for positioning it's
	 *            children - the DragWidgets.
	 */
	public TrashDatapointDropWidget(String[] dropWidgetStyleNames, String[] dropWidgetContentStyleNames) {
		super(dropWidgetStyleNames, dropWidgetContentStyleNames);
		initTrashDPWidget(false);
	}

	/**
	 * Creates a new TrashDatapointDropWidget.
	 * 
	 * @param collector
	 *            If true, a new {@link CollectDatapointDropWidget} will be
	 *            created an this this widget bound to it.
	 */
	public TrashDatapointDropWidget(boolean collector) {
		super();
		initTrashDPWidget(collector);
	}

	/**
	 * Creates a new TrashDatapointDropWidget.
	 * 
	 * @param dropWidgetStyleNames
	 *            The drop widget style names for positioning the DropWidget.
	 * @param dropWidgetContentStyleNames
	 *            The drop widget content style names for positioning it's
	 *            children - the DragWidgets.
	 * @param collector
	 *            If true, a new {@link CollectDatapointDropWidget} will be
	 *            created an this this widget bound to it.
	 */
	public TrashDatapointDropWidget(String dropWidgetStyleNames, String dropWidgetContentStyleNames, boolean collector) {
		super(dropWidgetStyleNames, dropWidgetContentStyleNames);
		initTrashDPWidget(collector);
	}

	/**
	 * Creates a new TrashDatapointDropWidget.
	 * 
	 * @param dropWidgetStyleNames
	 *            The drop widget style names for positioning the DropWidget.
	 * @param dropWidgetContentStyleNames
	 *            The drop widget content style names for positioning it's
	 *            children - the DragWidgets.
	 * @param collector
	 *            If true, a new {@link CollectDatapointDropWidget} will be
	 *            created an this this widget bound to it.
	 */
	public TrashDatapointDropWidget(String[] dropWidgetStyleNames, String[] dropWidgetContentStyleNames, boolean collector) {
		super(dropWidgetStyleNames, dropWidgetContentStyleNames);
		initTrashDPWidget(collector);
	}

	/**
	 * Creates a new TrashDatapointDropWidget.
	 * 
	 * @param cddWidget
	 *            The {@link CollectDatapointDropWidget} you want to bind this
	 *            widget to.
	 */
	public TrashDatapointDropWidget(CollectDatapointDropWidget cddWidget) {
		super();
		initTrashDPWidget(false);
		this.cddWidget = cddWidget;
	}

	/**
	 * Creates a new TrashDatapointDropWidget.
	 * 
	 * @param dropWidgetStyleNames
	 *            The drop widget style names for positioning the DropWidget.
	 * @param dropWidgetContentStyleNames
	 *            The drop widget content style names for positioning it's
	 *            children - the DragWidgets.
	 * @param cddWidget
	 *            The {@link CollectDatapointDropWidget} you want to bind this
	 *            widget to.
	 */
	public TrashDatapointDropWidget(String dropWidgetStyleNames, String dropWidgetContentStyleNames, CollectDatapointDropWidget cddWidget) {
		super(dropWidgetStyleNames, dropWidgetContentStyleNames);
		initTrashDPWidget(false);
		this.cddWidget = cddWidget;
	}

	/**
	 * Creates a new TrashDatapointDropWidget.
	 * 
	 * @param dropWidgetStyleNames
	 *            The drop widget style names for positioning the DropWidget.
	 * @param dropWidgetContentStyleNames
	 *            The drop widget content style names for positioning it's
	 *            children - the DragWidgets.
	 * @param cddWidget
	 *            The {@link CollectDatapointDropWidget} you want to bind this
	 *            widget to.
	 */
	public TrashDatapointDropWidget(String[] dropWidgetStyleNames, String[] dropWidgetContentStyleNames,
			CollectDatapointDropWidget cddWidget) {
		super(dropWidgetStyleNames, dropWidgetContentStyleNames);
		initTrashDPWidget(false);
		this.cddWidget = cddWidget;
	}

	/**
	 * Method to initialize this widget with all necessary parameters.
	 * 
	 * @param collector
	 *            If true, a new {@link CollectDatapointDropWidget} will be
	 *            created an this this widget bound to it.
	 */
	private void initTrashDPWidget(boolean collector) {
		addHandler(new DpWidgetDropEventHandler() {

			@Override
			public void onDpWidgetDropEvent(DpWidgetDropEvent event) {
				if (cddWidget != null) {
					if (event.getDraggable() instanceof DpWidget) {
						cddWidget.removeDpWidget((DpWidget) event.getDraggable());
					}
				}
			}
		}, DpWidgetDropEvent.getTYPE());
		addDomHandler(new DoubleClickHandler() {

			@Override
			public void onDoubleClick(DoubleClickEvent event) {
				if (Window.confirm("Delete all collected data points?")) {
					if (cddWidget != null) {
						cddWidget.removeAllDpWidgets();
					}
				}
			}
		}, DoubleClickEvent.getType());
		removeAll.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (cddWidget != null) {
					cddWidget.removeAllDpWidgets();
				}
			}
		});
		getThis().getDropPanel().add(removeAll);
		removeAll.getElement().getStyle().setWidth(100, Unit.PCT);
		enableRemoveAllButton(false);
		if (collector) {
			cddWidget = new CollectDatapointDropWidget(getStyleName(), null, this);
		}
	}

	/**
	 * Get the instance of this widget.
	 * 
	 * @return Returns the instance of this widget.
	 */
	public TrashDatapointDropWidget getThis() {
		return this;
	}

	/**
	 * Get the {@link CollectDatapointDropWidget} to which this widget is bound.
	 * 
	 * @return Returns the {@link CollectDatapointDropWidget} to which this
	 *         widget is bound.
	 */
	public CollectDatapointDropWidget getCDDWidget() {
		return cddWidget;
	}

	/**
	 * Set the {@link CollectDatapointDropWidget} to which this widget should be
	 * bound.
	 * 
	 * @param cddWidget
	 *            The {@link CollectDatapointDropWidget} to which this widget
	 *            should be bound.
	 */
	public void setCDDWidget(CollectDatapointDropWidget cddWidget) {
		this.cddWidget = cddWidget;
	}

	/**
	 * Enable and disable the "remove all" button in this widget.
	 * 
	 * @param enable
	 *            If true, the button will be enabled, otherwise it will be
	 *            disabled.
	 */
	public void enableRemoveAllButton(boolean enable) {
		removeAll.setVisible(enable);
	}

}
