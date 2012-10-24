package bpi.most.client.utils.ui.jSlider;

import com.google.gwt.core.client.JsArrayInteger;
import com.google.gwt.user.client.Event;

public interface SliderChangeHandler {
	
	void onChange(Event event, int value, JsArrayInteger values, boolean originalEvent);

}
