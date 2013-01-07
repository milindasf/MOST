package bpi.most.client.utils.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import java.util.Date;

/**
 * A DateTimePickerBox based on {@link TextBox} and a jQuery-Timepicker-Addon.
 * (<a href="https://github.com/trentrichardson/jQuery-Timepicker-Addon"
 * >https://github.com/trentrichardson/jQuery-Timepicker-Addon</a>) <br>
 * <br>
 * Examples can be seen <a
 * href="http://trentrichardson.com/examples/timepicker/" >here</a>. <br>
 * <br>
 * DateTime format: "dd/MM/yyyy HH:mm"
 * 
 * @author mike
 * 
 */
public class DateTimePickerBox extends Composite {

	/**
	 * The actual selected date-time.
	 */
	public Date date;
	/**
	 * The last date-time before the actual date-time was selected.
	 */
	public Date olddate = null;
	/**
	 * Unique ID to identify the date-time-picker text box in the java script
	 * code.
	 */
	private String uniqueID = null;

	/**
	 * Used to set the format of the date in the text box as needed.
	 */
	DateTimeFormat dateformatter = DateTimeFormat.getFormat("dd/MM/yyyy HH:mm");

	private static DateTimePickerBoxUiBinder uiBinder = GWT
			.create(DateTimePickerBoxUiBinder.class);

	interface DateTimePickerBoxUiBinder extends
			UiBinder<Widget, DateTimePickerBox> {
	}

	/**
	 * The text box that is shown with the selected date-time and a
	 * date-time-picker pop-up.
	 */
	@UiField(provided = true)
	TextBox textbox;

	/**
	 * Creates a new instance of the {@link DateTimePickerBox} with the current
	 * date and time as default.
	 */
	public DateTimePickerBox() {
		uniqueID = Document.get().createUniqueId();
		this.date = new Date();
		textbox = new TextBox() {

			@Override
			protected void onLoad() {
				super.onLoad();
				getElement().setId(getID());
				createDateTimePicker(getThis(), getID());
			}

		};
		initWidget(uiBinder.createAndBindUi(this));
		upDateBox();
	}

	/**
	 * Creates a new instance of the {@link DateTimePickerBox} with the given
	 * date and time as default.
	 * 
	 * @param date
	 *            The date used as default value for this
	 *            {@link DateTimePickerBox}.
	 */
	public DateTimePickerBox(Date date) {
		uniqueID = Document.get().createUniqueId();
		this.date = date;
		textbox = new TextBox() {

			@Override
			protected void onLoad() {
				super.onLoad();
				getElement().setId(getID());
				createDateTimePicker(getThis(), getID());
			}

		};
		initWidget(uiBinder.createAndBindUi(this));
		upDateBox();
	}

	/**
	 * Used to get the reference to this object.
	 * 
	 * @return Returns the reference to this object.
	 */
	private Object getThis() {
		return this;
	}

	/**
	 * Get the currently set date.
	 * 
	 * @return Returns the current date.
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * Set the date you want to use as current date. Calls
	 * {@link #onDateChange(Date, Date)}.
	 * 
	 * @param date
	 *            The date you want to use as current date.
	 */
	public void setDate(Date date) {
		this.olddate = this.date;
		this.date = date;
		upDateBox();
		if (this.olddate.compareTo(date) != 0) {
			onDateChange(this.olddate, this.date);
		}
	}

	/**
	 * Set the date you want to use as current date. Calls
	 * {@link #onDateChange(Date, Date)}, if triggerEvent is true.
	 * 
	 * @param date
	 *            The date you want to use as current date.
	 * @param triggerEvent
	 *            Decides if the {@link #onDateChange(Date, Date)} event is
	 *            triggered or not.
	 */
	public void setDate(Date date, boolean triggerEvent) {
		this.olddate = this.date;
		this.date = date;
		upDateBox();
		if (triggerEvent && this.olddate.compareTo(date) != 0) {
			onDateChange(this.olddate, this.date);
		}
	}

	/**
	 * Get the unique id used for the text box.
	 * 
	 * @return Returns unique id.
	 */
	public String getID() {
		return uniqueID;
	}

	/**
	 * Updates the content of the text box to the current date and time.
	 */
	public void upDateBox() {
		textbox.setText(dateformatter.format(date));
	}

	/**
	 * A method that is called when a new date or/and time is selected.
	 * 
	 * @param olddate
	 *            The date that was set before.
	 * @param date
	 *            The newly selected date.
	 */
	public void onDateChange(Date olddate, Date date) {
	}

	/**
	 * Method called every time the datetimepicker-pop-up closes.
	 * 
	 * @param date
	 *            The date and time selected in the datetimepicker-pop-up as
	 *            string in the format "dd/MM/yyyy HH:mm".
	 */
	private void onDTPClose(String date) {
		textbox.setFocus(false);
		setDate(dateformatter.parse(date));
	}

	public void setEnabled(boolean enabled) {
		textbox.setEnabled(enabled);
	}

	/**
	 * JSNI: Create the date-time-picker with the {@link #textbox} as ui
	 * element.
	 * 
	 * @param x
	 *            A reference to this instance of {@link DateTimePickerBox}.
	 * @param id
	 *            The {@link #uniqueID} used for the {@link #textbox} for
	 *            identification.
	 */
	private static native void createDateTimePicker(Object x, String id) /*-{
		var options = new Object();
		options.onClose = function(dateText, inst) {
			x.@bpi.most.client.utils.ui.DateTimePickerBox::onDTPClose(Ljava/lang/String;)(dateText);
		}
		options.changeMonth = true;
		options.changeYear = true;
		options.dateFormat = "dd/mm/yy";
		options.timeFormat = "hh:mm"
		$wnd.$("#" + id).datetimepicker(options);
	}-*/;
}
