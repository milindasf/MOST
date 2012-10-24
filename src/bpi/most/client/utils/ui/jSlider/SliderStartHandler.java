package bpi.most.client.utils.ui.jSlider;

import com.google.gwt.core.client.JsArrayInteger;
import com.google.gwt.user.client.Event;

public interface SliderStartHandler extends SliderHandler {
	
	void onStart(Event event, int value, JsArrayInteger values);
	
}
