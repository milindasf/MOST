package bpi.most.client.modules.exporter.filter;

import bpi.most.client.utils.ui.DateTimePickerBox;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class TimePeriodFormWidget extends FormWidget {

	@UiField
	DateTimePickerBox startdate;

	@UiField
	DateTimePickerBox enddate;

	@UiField
	TextBox period;

	@UiField
	RadioButton mode1;

	@UiField
	RadioButton mode2;

	@UiField
	RadioButton mode3;

	@UiField
	CheckBox anaToBin;

	@UiField
	Button save;

	@UiField
	Button revert;

	private static TimePeriodFormWidgetUiBinder uiBinder = GWT
			.create(TimePeriodFormWidgetUiBinder.class);

	interface TimePeriodFormWidgetUiBinder extends
			UiBinder<Widget, TimePeriodFormWidget> {
	}

	public TimePeriodFormWidget(Filter filter) {
		super(filter);
		initWidget(uiBinder.createAndBindUi(this));
		getElement().getStyle().setPadding(0.0, Unit.PX);
	}

	@UiHandler("save")
	void saveClick(ClickEvent e) {
		saveData();
	}

	@UiHandler("revert")
	void revertClick(ClickEvent e) {
		onLoad();
	}

	@Override
	protected void onLoad() {
		// TODO load all the data from the filter into the forms
		startdate.setDate(((TimePeriodFilter) getFilter()).startdate, false);
		enddate.setDate(((TimePeriodFilter) getFilter()).enddate, false);
		period.setText(Integer
				.toString(((TimePeriodFilter) getFilter()).period));
		if (((TimePeriodFilter) getFilter()).mode == 1) {
			mode1.setValue(true, true);
		} else if (((TimePeriodFilter) getFilter()).mode == 2) {
			mode2.setValue(true, true);
		} else if (((TimePeriodFilter) getFilter()).mode == 3) {
			mode3.setValue(true, true);
		} else {
			// unallowed value, set to default value 1
			((TimePeriodFilter) getFilter()).mode = 1;
			mode1.setValue(true, true);
		}
		if (((TimePeriodFilter) getFilter()).anaToBin) {
			anaToBin.setValue(true, true);
		} else {
			anaToBin.setValue(false, true);
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

	private void saveData() {
		int periodvalue;
		if (mode1.getValue() == false && mode2.getValue() == false && mode3.getValue() == false){
			Window.alert("An error occured whit the radio buttons!");
			return;
		}
		if(period.getText().isEmpty()){
			periodvalue = 0;
			period.setText(Integer.toString(0));
		} else {
			try {
			    periodvalue = Integer.parseInt(period.getText());
			}
			catch(NumberFormatException nFE) {
			   Window.alert("Period is not an integer value!");
			   return;
			}
		}
		if (enddate.getDate().before(startdate.getDate())){
			Window.alert("End date is before start date!");
			return;
		} else {
			((TimePeriodFilter) getFilter()).startdate = startdate.getDate();
			((TimePeriodFilter) getFilter()).enddate = enddate.getDate();
		}
		
		((TimePeriodFilter) getFilter()).period = periodvalue;
		if (mode1.getValue()){
			((TimePeriodFilter) getFilter()).mode = 1;
		} else if (mode2.getValue()){
			((TimePeriodFilter) getFilter()).mode = 2;
		} else if (mode3.getValue()){
			((TimePeriodFilter) getFilter()).mode = 3;
		} else {
			// this should never happen
			Window.alert("An error occured whit the radio buttons!");
		}
		((TimePeriodFilter) getFilter()).anaToBin = anaToBin.getValue();
	}
}
