package bpi.most.client.utils.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.Widget;

public class DpSelectWidget extends Composite {

	@UiField
	TabPanel tabPan;

	int oldSelection = 0;

	private static DpSelectWidgetUiBinder uiBinder = GWT
			.create(DpSelectWidgetUiBinder.class);

	interface DpSelectWidgetUiBinder extends UiBinder<Widget, DpSelectWidget> {
	}

	// TODO when highlighting is reworked, create another constructor with
	// StyleNames for the DpWidget that should be added in the different select
	// widgets.
	public DpSelectWidget() {
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

}
