package bpi.most.client.modules.exporter.filter;


import bpi.most.client.utils.ui.DropTextbox;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

public class ExternalConditionsFormWidget extends FormWidget {
	
	@UiField(provided = true)
	DropTextbox conddp1 = new DropTextbox(new String[] { "dropWidget",
			"dWidget-uid-export" });
	
	@UiField(provided = true)
	DropTextbox conddp2 = new DropTextbox(new String[] { "dropWidget",
			"dWidget-uid-export" });
	
	@UiField
	ListBox compare;
	
	@UiField
	Button save;

	@UiField
	Button revert;


	private static ExternalConditionsFormWidgetUiBinder uiBinder = GWT
			.create(ExternalConditionsFormWidgetUiBinder.class);

	interface ExternalConditionsFormWidgetUiBinder extends
			UiBinder<Widget, ExternalConditionsFormWidget> {
	}

	public ExternalConditionsFormWidget(Filter filter) {
		super(filter);
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	private void saveData() {
		((ExternalConditionsFilter) getFilter()).condition1 = conddp1.getText();
		((ExternalConditionsFilter) getFilter()).condition2 = conddp2.getText();
		((ExternalConditionsFilter) getFilter()).compare = compare
				.getValue(compare.getSelectedIndex());
	}

	@Override
	protected void onLoad() {
		conddp1.setText(((ExternalConditionsFilter) getFilter()).condition1);
		conddp2.setText(((ExternalConditionsFilter) getFilter()).condition2);
		if (((ExternalConditionsFilter) getFilter()).compare
				.equalsIgnoreCase("smallerThan")) {
			compare.setSelectedIndex(0);
		} else if (((ExternalConditionsFilter) getFilter()).compare
				.equalsIgnoreCase("greaterThan")) {
			compare.setSelectedIndex(1);
		} else if (((ExternalConditionsFilter) getFilter()).compare
				.equalsIgnoreCase("equalTo")) {
			compare.setSelectedIndex(2);
		} else if (((ExternalConditionsFilter) getFilter()).compare
				.equalsIgnoreCase("notEqualTo")) {
			compare.setSelectedIndex(3);
		} else if (((ExternalConditionsFilter) getFilter()).compare
				.equalsIgnoreCase("smallerOrEqual")) {
			compare.setSelectedIndex(4);
		} else if (((ExternalConditionsFilter) getFilter()).compare
				.equalsIgnoreCase("greaterOrEqual")) {
			compare.setSelectedIndex(5);
		}
		super.onLoad();
	}

	@Override
	protected void onUnload() {
		// TODO maybe nothing todo, but it's possible to safe all the changes
		// that are made to the filter, but currently it's planed that there is a
		// save button and all unsaved changes are lost
		super.onUnload();
	}
	
	@UiHandler("save")
	void saveClick(ClickEvent e) {
		saveData();
	}

	@UiHandler("revert")
	void revertClick(ClickEvent e) {
		onLoad();
	}

}
