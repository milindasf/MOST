package bpi.most.client.utils.ui;

import java.util.ArrayList;

import bpi.most.dto.DpDTO;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.Widget;

public class DpCollectorWidget extends Composite {

	private static DpCollectorWidgetUiBinder uiBinder = GWT
			.create(DpCollectorWidgetUiBinder.class);

	@UiField(provided = true)
	CollectDatapointDropWidget cddw;

	@UiField(provided = true)
	TrashDatapointDropWidget tddw;

	@UiField
	TabPanel tabPan;

	int oldSelection = 0;

	interface DpCollectorWidgetUiBinder extends
			UiBinder<Widget, DpCollectorWidget> {
	}

	public DpCollectorWidget() {
		cddw = new CollectDatapointDropWidget(true);
		tddw = cddw.getTrashWidget();
		cddw.addStyleName("cddw");
		tddw.addStyleName("tddw");
		initWidget(uiBinder.createAndBindUi(this));
		tabPan.selectTab(0, true);
	}

	public DpCollectorWidget(String[] dropWidgetStyleNames,
			String[] dropWidgetContentStyleNames) {
		cddw = new CollectDatapointDropWidget(dropWidgetStyleNames,
				dropWidgetContentStyleNames, true);
		tddw = cddw.getTrashWidget();
		cddw.addStyleName("cddw");
		tddw.addStyleName("tddw");
		initWidget(uiBinder.createAndBindUi(this));
		tabPan.selectTab(0, true);
	}

	@UiHandler("tabPan")
	void onTabSelection(SelectionEvent<Integer> event) {
		if (oldSelection == event.getSelectedItem()) {
			if (tabPan.getWidget(event.getSelectedItem()).isVisible()) {
				tabPan.getWidget(event.getSelectedItem()).setVisible(false);
			} else {
				tabPan.getWidget(event.getSelectedItem()).setVisible(true);
			}
		} else {
			for (int i = 0; i < tabPan.getWidgetCount(); i++) {
				tabPan.getWidget(i).setVisible(true);
			}
		}
		oldSelection = event.getSelectedItem();
	}
	
	/**
	 * Get a list of all {@link DpWidget} they are in this widget.
	 * 
	 * @return Returns a list of all {@link DpWidget} they are in this widget.
	 */
	public ArrayList<DpWidget> getDpWidgets() {
		return cddw.getDpWidgets();
	}
	
	/**
	 * Get a list of all data points in this widget as {@link DpDTO}.
	 * 
	 * @return Returns a list of all data points in this widget as {@link DpDTO}
	 *         .
	 */
	public ArrayList<DpDTO> getDatapoints() {
		return cddw.getDatapoints();
	}

	public CollectDatapointDropWidget getCollectDatapointDropWidget() {
		return cddw;
	}

	public TrashDatapointDropWidget getTrashDatapointDropWidget() {
		return tddw;
	}

}
