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

public class InternalConditionsFormWidget extends FormWidget {

	@UiField(provided = true)
	DropTextbox conddp = new DropTextbox(new String[] { "dropWidget",
			"dWidget-uid-export" });

	@UiField
	ListBox compare;

	@UiField
	Button save;

	@UiField
	Button revert;

	private static InternalConditionsFormWidgetUiBinder uiBinder = GWT
			.create(InternalConditionsFormWidgetUiBinder.class);

	interface InternalConditionsFormWidgetUiBinder extends
			UiBinder<Widget, InternalConditionsFormWidget> {
	}

	public InternalConditionsFormWidget(Filter filter) {
		super(filter);
		initWidget(uiBinder.createAndBindUi(this));
	}

	private void saveData() {
		((InternalConditionsFilter) getFilter()).condition = conddp.getText();
		((InternalConditionsFilter) getFilter()).compare = compare
				.getValue(compare.getSelectedIndex());
	}

	@Override
	protected void onLoad() {
		conddp.setText(((InternalConditionsFilter) getFilter()).condition);
		if (((InternalConditionsFilter) getFilter()).compare
				.equalsIgnoreCase("smallerThan")) {
			compare.setSelectedIndex(0);
		} else if (((InternalConditionsFilter) getFilter()).compare
				.equalsIgnoreCase("greaterThan")) {
			compare.setSelectedIndex(1);
		} else if (((InternalConditionsFilter) getFilter()).compare
				.equalsIgnoreCase("equalTo")) {
			compare.setSelectedIndex(2);
		} else if (((InternalConditionsFilter) getFilter()).compare
				.equalsIgnoreCase("notEqualTo")) {
			compare.setSelectedIndex(3);
		} else if (((InternalConditionsFilter) getFilter()).compare
				.equalsIgnoreCase("smallerOrEqual")) {
			compare.setSelectedIndex(4);
		} else if (((InternalConditionsFilter) getFilter()).compare
				.equalsIgnoreCase("greaterOrEqual")) {
			compare.setSelectedIndex(5);
		}

		super.onLoad();
	}

	@Override
	protected void onUnload() {
		// TODO maybe nothing todo, but it's possible to safe all the changes
		// that are made to the filter, but currently it's planed that there is
		// a
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
