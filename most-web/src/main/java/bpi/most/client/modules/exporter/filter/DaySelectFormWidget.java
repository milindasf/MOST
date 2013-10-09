package bpi.most.client.modules.exporter.filter;


import bpi.most.client.utils.ui.TimeSelectWidget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

public class DaySelectFormWidget extends FormWidget {
	
	@UiField
	CheckBox monday;
	@UiField
	CheckBox montime;
	@UiField
	FlowPanel mon;
	@UiField
	TimeSelectWidget monfrom;
	@UiField
	TimeSelectWidget monto;
	
	@UiField
	CheckBox tuesday;
	@UiField
	CheckBox tuetime;
	@UiField
	FlowPanel tue;
	@UiField
	TimeSelectWidget tuefrom;
	@UiField
	TimeSelectWidget tueto;
	
	@UiField
	CheckBox wednesday;
	@UiField
	CheckBox wedtime;
	@UiField
	FlowPanel wed;
	@UiField
	TimeSelectWidget wedfrom;
	@UiField
	TimeSelectWidget wedto;
	
	@UiField
	CheckBox thursday;
	@UiField
	CheckBox thutime;
	@UiField
	FlowPanel thu;
	@UiField
	TimeSelectWidget thufrom;
	@UiField
	TimeSelectWidget thuto;
	
	@UiField
	CheckBox friday;
	@UiField
	CheckBox fritime;
	@UiField
	FlowPanel fri;
	@UiField
	TimeSelectWidget frifrom;
	@UiField
	TimeSelectWidget frito;
	
	@UiField
	CheckBox saturday;
	@UiField
	CheckBox sattime;
	@UiField
	FlowPanel sat;
	@UiField
	TimeSelectWidget satfrom;
	@UiField
	TimeSelectWidget satto;
	
	@UiField
	CheckBox sunday;
	@UiField
	CheckBox suntime;
	@UiField
	FlowPanel sun;
	@UiField
	TimeSelectWidget sunfrom;
	@UiField
	TimeSelectWidget sunto;
	
	@UiField
	Button save;
	@UiField
	Button revert;

	private static DaySelectFormWidgetUiBinder uiBinder = GWT
			.create(DaySelectFormWidgetUiBinder.class);

	interface DaySelectFormWidgetUiBinder extends
			UiBinder<Widget, DaySelectFormWidget> {
	}

	public DaySelectFormWidget(Filter filter) {
		super(filter);
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	protected void onLoad() {
		// TODO load all the data from the filter into the forms
		if(((DaySelectFilter)getFilter()).days.get("Monday")){
			monday.setValue(true, true);
			if(((DaySelectFilter)getFilter()).daystime.get("Monday")){
				montime.setValue(true, true);
			} else {
				montime.setValue(false, true);
			}
		} else {
			monday.setValue(false, true);
		}
		if(((DaySelectFilter)getFilter()).days.get("Tuesday")){
			tuesday.setValue(true, true);
			if(((DaySelectFilter)getFilter()).daystime.get("Tuesday")){
				tuetime.setValue(true, true);
			} else {
				tuetime.setValue(false, true);
			}
		} else {
			tuesday.setValue(false, true);
		}
		if(((DaySelectFilter)getFilter()).days.get("Wednesday")){
			wednesday.setValue(true, true);
			if(((DaySelectFilter)getFilter()).daystime.get("Wednesday")){
				wedtime.setValue(true, true);
			} else {
				wedtime.setValue(false, true);
			}
		} else {
			wednesday.setValue(false, true);
		}
		if(((DaySelectFilter)getFilter()).days.get("Thursday")){
			thursday.setValue(true, true);
			if(((DaySelectFilter)getFilter()).daystime.get("Thursday")){
				thutime.setValue(true, true);
			} else {
				thutime.setValue(false, true);
			}
		} else {
			thursday.setValue(false, true);
		}
		if(((DaySelectFilter)getFilter()).days.get("Friday")){
			friday.setValue(true, true);
			if(((DaySelectFilter)getFilter()).daystime.get("Friday")){
				fritime.setValue(true, true);
			} else {
				fritime.setValue(false, true);
			}
		} else {
			friday.setValue(false, true);
		}
		if(((DaySelectFilter)getFilter()).days.get("Saturday")){
			saturday.setValue(true, true);
			if(((DaySelectFilter)getFilter()).daystime.get("Saturday")){
				sattime.setValue(true, true);
			} else {
				sattime.setValue(false, true);
			}
		} else {
			saturday.setValue(false, true);
		}
		if(((DaySelectFilter)getFilter()).days.get("Sunday")){
			sunday.setValue(true, true);
			if(((DaySelectFilter)getFilter()).daystime.get("Sunday")){
				suntime.setValue(true, true);
			} else {
				suntime.setValue(false, true);
			}
		} else {
			sunday.setValue(false, true);
		}
		monfrom.setTimeInMinute(((DaySelectFilter)getFilter()).monFromInMinute);
		monto.setTimeInMinute(((DaySelectFilter)getFilter()).monToInMinute);
		
		tuefrom.setTimeInMinute(((DaySelectFilter)getFilter()).tueFromInMinute);
		tueto.setTimeInMinute(((DaySelectFilter)getFilter()).tueToInMinute);
		
		wedfrom.setTimeInMinute(((DaySelectFilter)getFilter()).wedFromInMinute);
		wedto.setTimeInMinute(((DaySelectFilter)getFilter()).wedToInMinute);
		
		thufrom.setTimeInMinute(((DaySelectFilter)getFilter()).thuFromInMinute);
		thuto.setTimeInMinute(((DaySelectFilter)getFilter()).thuToInMinute);
		
		frifrom.setTimeInMinute(((DaySelectFilter)getFilter()).friFromInMinute);
		frito.setTimeInMinute(((DaySelectFilter)getFilter()).friToInMinute);
		
		satfrom.setTimeInMinute(((DaySelectFilter)getFilter()).satFromInMinute);
		satto.setTimeInMinute(((DaySelectFilter)getFilter()).satToInMinute);
		
		sunfrom.setTimeInMinute(((DaySelectFilter)getFilter()).sunFromInMinute);
		sunto.setTimeInMinute(((DaySelectFilter)getFilter()).sunToInMinute);
		super.onLoad();
	}

	@Override
	protected void onUnload() {
		// TODO maybe nothing todo, but it's possible to safe all the changes
		// that are made to the filter, but currently it's planed that there is a
		// save button and all unsaved changes are lost
		super.onUnload();
	}
	
	private void saveData() {
		if (monday.getValue()){
			((DaySelectFilter)getFilter()).days.put("Monday", true);
			if (montime.getValue()){
				((DaySelectFilter)getFilter()).daystime.put("Monday", true);
			} else {
				((DaySelectFilter)getFilter()).daystime.put("Monday", false);
			}
		} else {
			((DaySelectFilter)getFilter()).days.put("Monday", false);
			((DaySelectFilter)getFilter()).daystime.put("Monday", false);
		}
		if (tuesday.getValue()){
			((DaySelectFilter)getFilter()).days.put("Tuesday", true);
			if (tuetime.getValue()){
				((DaySelectFilter)getFilter()).daystime.put("Tuesday", true);
			} else {
				((DaySelectFilter)getFilter()).daystime.put("Tuesday", false);
			}
		} else {
			((DaySelectFilter)getFilter()).days.put("Tuesday", false);
			((DaySelectFilter)getFilter()).daystime.put("Tuesday", false);
		}
		if (wednesday.getValue()){
			((DaySelectFilter)getFilter()).days.put("Wednesday", true);
			if (wedtime.getValue()){
				((DaySelectFilter)getFilter()).daystime.put("Wednesday", true);
			} else {
				((DaySelectFilter)getFilter()).daystime.put("Wednesday", false);
			}
		} else {
			((DaySelectFilter)getFilter()).days.put("Wednesday", false);
			((DaySelectFilter)getFilter()).daystime.put("Wednesday", false);
		}
		if (thursday.getValue()){
			((DaySelectFilter)getFilter()).days.put("Thursday", true);
			if (thutime.getValue()){
				((DaySelectFilter)getFilter()).daystime.put("Thursday", true);
			} else {
				((DaySelectFilter)getFilter()).daystime.put("Thursday", false);
			}
		} else {
			((DaySelectFilter)getFilter()).days.put("Thursday", false);
			((DaySelectFilter)getFilter()).daystime.put("Thursday", false);
		}
		if (friday.getValue()){
			((DaySelectFilter)getFilter()).days.put("Friday", true);
			if (fritime.getValue()){
				((DaySelectFilter)getFilter()).daystime.put("Friday", true);
			} else {
				((DaySelectFilter)getFilter()).daystime.put("Friday", false);
			}
		} else {
			((DaySelectFilter)getFilter()).days.put("Friday", false);
			((DaySelectFilter)getFilter()).daystime.put("Friday", false);
		}
		if (saturday.getValue()){
			((DaySelectFilter)getFilter()).days.put("Saturday", true);
			if (sattime.getValue()){
				((DaySelectFilter)getFilter()).daystime.put("Saturday", true);
			} else {
				((DaySelectFilter)getFilter()).daystime.put("Saturday", false);
			}
		} else {
			((DaySelectFilter)getFilter()).days.put("Saturday", false);
			((DaySelectFilter)getFilter()).daystime.put("Saturday", false);
		}
		if (sunday.getValue()){
			((DaySelectFilter)getFilter()).days.put("Sunday", true);
			if (suntime.getValue()){
				((DaySelectFilter)getFilter()).daystime.put("Sunday", true);
			} else {
				((DaySelectFilter)getFilter()).daystime.put("Sunday", false);
			}
		} else {
			((DaySelectFilter)getFilter()).days.put("Sunday", false);
			((DaySelectFilter)getFilter()).daystime.put("Sunday", false);
		}
		
		if (monto.getTimeInMinute() < monfrom.getTimeInMinute()){
			Window.alert("The second time cannot be earlier than the first!");
		} else {
			((DaySelectFilter)getFilter()).monFromInMinute = monfrom.getTimeInMinute();
			((DaySelectFilter)getFilter()).monToInMinute = monto.getTimeInMinute();
		}
		if (tueto.getTimeInMinute() < tuefrom.getTimeInMinute()){
			Window.alert("The second time cannot be earlier than the first!");
		} else {
			((DaySelectFilter)getFilter()).tueFromInMinute = tuefrom.getTimeInMinute();
			((DaySelectFilter)getFilter()).tueToInMinute = tueto.getTimeInMinute();
		}
		if (wedto.getTimeInMinute() < wedfrom.getTimeInMinute()){
			Window.alert("The second time cannot be earlier than the first!");
		} else {
			((DaySelectFilter)getFilter()).wedFromInMinute = wedfrom.getTimeInMinute();
			((DaySelectFilter)getFilter()).wedToInMinute = wedto.getTimeInMinute();
		}
		if (thuto.getTimeInMinute() < thufrom.getTimeInMinute()){
			Window.alert("The second time cannot be earlier than the first!");
		} else {
			((DaySelectFilter)getFilter()).thuFromInMinute = thufrom.getTimeInMinute();
			((DaySelectFilter)getFilter()).thuToInMinute = thuto.getTimeInMinute();
		}
		if (frito.getTimeInMinute() < frifrom.getTimeInMinute()){
			Window.alert("The second time cannot be earlier than the first!");
		} else {
			((DaySelectFilter)getFilter()).friFromInMinute = frifrom.getTimeInMinute();
			((DaySelectFilter)getFilter()).friToInMinute = frito.getTimeInMinute();
		}
		if (satto.getTimeInMinute() < satfrom.getTimeInMinute()){
			Window.alert("The second time cannot be earlier than the first!");
		} else {
			((DaySelectFilter)getFilter()).satFromInMinute = satfrom.getTimeInMinute();
			((DaySelectFilter)getFilter()).satToInMinute = satto.getTimeInMinute();
		}
		if (sunto.getTimeInMinute() < sunfrom.getTimeInMinute()){
			Window.alert("The second time cannot be earlier than the first!");
		} else {
			((DaySelectFilter)getFilter()).sunFromInMinute = sunfrom.getTimeInMinute();
			((DaySelectFilter)getFilter()).sunToInMinute = sunto.getTimeInMinute();
		}		
	}
	
	@UiHandler("save")
	void saveClick(ClickEvent e) {
		saveData();
	}

	@UiHandler("revert")
	void revertClick(ClickEvent e) {
		onLoad();
	}
		
	@UiHandler("monday")
	void onMonValueChange(ValueChangeEvent<Boolean> event) {
		if(event.getValue()){
			
		} else {
			montime.setValue(false, true);
		}
	}
	
	@UiHandler("tuesday")
	void onTueValueChange(ValueChangeEvent<Boolean> event) {
		if(event.getValue()){
			
		} else {
			tuetime.setValue(false, true);
		}
	}
	
	@UiHandler("wednesday")
	void onWedValueChange(ValueChangeEvent<Boolean> event) {
		if(event.getValue()){
			
		} else {
			wedtime.setValue(false, true);
		}
	}
	
	@UiHandler("thursday")
	void onThuValueChange(ValueChangeEvent<Boolean> event) {
		if(event.getValue()){
			
		} else {
			thutime.setValue(false, true);
		}
	}
	
	@UiHandler("friday")
	void onFriValueChange(ValueChangeEvent<Boolean> event) {
		if(event.getValue()){
			
		} else {
			fritime.setValue(false, true);
		}
	}
	
	@UiHandler("saturday")
	void onSatValueChange(ValueChangeEvent<Boolean> event) {
		if(event.getValue()){
			
		} else {
			sattime.setValue(false, true);
		}
	}
	
	@UiHandler("sunday")
	void onSunValueChange(ValueChangeEvent<Boolean> event) {
		if(event.getValue()){
			
		} else {
			suntime.setValue(false, true);
		}
	}
	
	@UiHandler("montime")
	void onMonTimeValueChange(ValueChangeEvent<Boolean> event) {
		if(event.getValue()){
			mon.setVisible(true);
		} else {
			mon.setVisible(false);
		}
	}
	
	@UiHandler("tuetime")
	void onTueTimeValueChange(ValueChangeEvent<Boolean> event) {
		if(event.getValue()){
			tue.setVisible(true);
		} else {
			tue.setVisible(false);
		}
	}
	
	@UiHandler("wedtime")
	void onWedTimeValueChange(ValueChangeEvent<Boolean> event) {
		if(event.getValue()){
			wed.setVisible(true);
		} else {
			wed.setVisible(false);
		}
	}
	
	@UiHandler("thutime")
	void onThuTimeValueChange(ValueChangeEvent<Boolean> event) {
		if(event.getValue()){
			thu.setVisible(true);
		} else {
			thu.setVisible(false);
		}
	}
	
	@UiHandler("fritime")
	void onFriTimeValueChange(ValueChangeEvent<Boolean> event) {
		if(event.getValue()){
			fri.setVisible(true);
		} else {
			fri.setVisible(false);
		}
	}
	
	@UiHandler("sattime")
	void onSatTimeValueChange(ValueChangeEvent<Boolean> event) {
		if(event.getValue()){
			sat.setVisible(true);
		} else {
			sat.setVisible(false);
		}
	}
	
	@UiHandler("suntime")
	void onSunTimeValueChange(ValueChangeEvent<Boolean> event) {
		if(event.getValue()){
			sun.setVisible(true);
		} else {
			sun.setVisible(false);
		}
	}

}
