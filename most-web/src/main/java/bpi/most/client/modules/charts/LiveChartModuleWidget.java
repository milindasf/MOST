package bpi.most.client.modules.charts;

import java.util.Date;

import bpi.most.client.mainlayout.RootModule;
import bpi.most.client.modules.ModuleInterface;
import bpi.most.client.modules.ModuleWidget;
import bpi.most.client.utils.dnd.DNDController;
import bpi.most.client.utils.dnd.DpWidgetDropEvent;
import bpi.most.client.utils.dnd.DpWidgetDropEventHandler;
import bpi.most.client.utils.dnd.DropWidget;
import bpi.most.client.utils.dnd.MostDragEndEvent;
import bpi.most.client.utils.ui.DeleteableDragWidget;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.DragOverEvent;
import com.google.gwt.event.dom.client.DragOverHandler;
import com.google.gwt.event.dom.client.DropEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Widget;

public final class LiveChartModuleWidget extends ModuleWidget {

	public static LiveChartModuleWidget ref;
	
	private static LCMUiBinder uiBinder = GWT.create(LCMUiBinder.class);

	interface LCMUiBinder extends UiBinder<Widget, LiveChartModuleWidget> {
	}

	@UiField
	AbsolutePanel dockRoot;

	/**
	 * The default time to start a chart wrapper with.
	 */
	public static final Date DTPB = new Date(1304589600000L);

	private LiveChartModuleWidget() {
		super();
	}

	private LiveChartModuleWidget(ModuleInterface module) {
		super(module);
		initWidget(uiBinder.createAndBindUi(this));
		RootModule.addNewDropWidgetToPanel(dockRoot,
				"dropWidget dWidget-uid-desktop floatLeft");
		for (int i = 0; i < dockRoot.getWidgetCount(); i++) {
			final Widget temp = dockRoot.getWidget(i);
			RootModule.setDropWidgetWidth(dockRoot.getWidget(i));
			temp.addHandler(new DpWidgetDropEventHandler() {
				private DropWidget relatedDropWidget = (DropWidget) temp;  
				@Override
				public void onDpWidgetDropEvent(DpWidgetDropEvent event) {
					ChartWrapper cwtemp = new ChartWrapper(DTPB);
					DeleteableDragWidget drag = new DeleteableDragWidget(
							cwtemp, new String[] { "dWidget-uid-livechart",
									"dWidget-uid-desktop" }, null, null);	
					cwtemp.dropEventHandler();
					relatedDropWidget.addDraggable(drag);
					DNDController.EVENT_BUS.fireEvent(new MostDragEndEvent());
				}

			}, DpWidgetDropEvent.TYPE);
		}

		dockRoot.addDomHandler(new DragOverHandler() {
			public void onDragOver(DragOverEvent event) {
				DNDController.clientX = event.getNativeEvent().getClientX();
				DNDController.clientY = event.getNativeEvent().getClientY();
			}
		}, DragOverEvent.getType());

	}

	public void addDropHandler(DropWidget dwidget, DropEvent event) {
		event.preventDefault();
		event.stopPropagation();
	}

	public AbsolutePanel getDockRoot() {
		return dockRoot;
	}

	public static LiveChartModuleWidget getInstance() {
		if (ref == null) {
			ref = new LiveChartModuleWidget();
		}
		return ref;
	}

	public static LiveChartModuleWidget getInstance(ModuleInterface module) {
		if (ref == null) {
			ref = new LiveChartModuleWidget(module);
		}
		return ref;
	}

}
