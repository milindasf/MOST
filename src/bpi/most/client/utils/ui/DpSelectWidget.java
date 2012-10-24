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

	public DpSelectWidget() {
		initWidget(uiBinder.createAndBindUi(this));
	//	tabPan.selectTab(0);
//		for (int i = 0; i < tabPan.getWidgetCount(); i++) {
//			final int temp = i;
//			tabPan.getTabWidget(i).addDomHandler(new ClickHandler() {
//				int value = temp;
//				@Override
//				public void onClick(ClickEvent event) {
//					if (value == oldSelection) {
//						if (tabPan.getWidget(value)
//								.isVisible()) {
//							tabPan.getWidget(value)
//									.setVisible(false);
//						} else {
//							tabPan.getWidget(value)
//									.setVisible(true);
//						}
//					} else {
//						for (int i = 0; i < tabPan.getWidgetCount(); i++) {
//							tabPan.getWidget(i).setVisible(true);
//						}
//					}
//					oldSelection = value;
//				}
//			}, ClickEvent.getType());
//		}
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
