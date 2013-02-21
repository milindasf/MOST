package bpi.most.client.utils.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

public class TimeSelectWidget extends Composite {
	
	@UiField
	ListBox hourValue;
	
	@UiField
	ListBox minuteValue;
	
	private int hour = 0;
	private int minute = 0;
	private int timeInMinute = 0;

	private static TimeWidgetUiBinder uiBinder = GWT
			.create(TimeWidgetUiBinder.class);

	interface TimeWidgetUiBinder extends UiBinder<Widget, TimeSelectWidget> {
	}

	public TimeSelectWidget() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public TimeSelectWidget(int hour, int minute) {
		initWidget(uiBinder.createAndBindUi(this));
		this.hour = hour;
		this.minute = minute;
		hourValue.setItemSelected(hour, true);
		minuteValue.setItemSelected(minute, true);
		timeInMinute = (this.hour * 60) + this.minute;
	}
	
	@UiHandler("hourValue")
	void hourChangeHandler(ChangeEvent e) {
		this.hour = hourValue.getSelectedIndex();
		timeInMinute = (this.hour * 60) + this.minute;
	}
	
	@UiHandler("minuteValue")
	void minuteChangeHandler(ChangeEvent e) {
		this.minute = minuteValue.getSelectedIndex();
		timeInMinute = (this.hour * 60) + this.minute;
	}

	public int getHour() {
		return hour;
	}

	public void setHour(int hour) {
		this.hour = hour;
		timeInMinute = (this.hour * 60) + this.minute;
		hourValue.setItemSelected(this.hour, true);
	}

	public int getMinute() {
		return minute;
	}

	public void setMinute(int minute) {
		this.minute = minute;
		timeInMinute = (this.hour * 60) + this.minute;
		minuteValue.setItemSelected(this.minute, true);
	}

	public int getTimeInMinute() {
		return timeInMinute;
	}

	public void setTimeInMinute(int timeInMinute) {
		this.timeInMinute = timeInMinute;
		this.hour = timeInMinute / 60;
		this.minute = timeInMinute % 60;
		hourValue.setItemSelected(this.hour, true);
		minuteValue.setItemSelected(this.minute, true);
	}
	
}
