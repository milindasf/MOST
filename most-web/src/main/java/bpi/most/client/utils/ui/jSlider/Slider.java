package bpi.most.client.utils.ui.jSlider;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayInteger;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Widget;

/**
 * A wrapper for the jquery-ui slider.
 * 
 * @see <a href="http://jqueryui.com/demos/slider/"
 *      >http://jqueryui.com/demos/slider/</a>
 * 
 * @author mike
 * 
 */
public class Slider extends Widget {

	private String id = null;
	private boolean rendered = false;
	private JSONObject options = new JSONObject();

	/**
	 * Create a new slider with default options.
	 */
	public Slider() {
		this.id = Document.get().createUniqueId();
		setDefaultOptions();
		Element div = DOM.createDiv();
		setElement(div);
		div.setId(this.id);
	}

	/**
	 * Create a new slider with default option, but set minimum and maximum
	 * value.
	 * 
	 * @param min
	 *            The minimum value of the slider.
	 * @param max
	 *            The maximum value of the slider.
	 */
	public Slider(int min, int max) {
		this.id = Document.get().createUniqueId();
		setDefaultOptions();
		options.put("min", new JSONNumber(min));
		options.put("max", new JSONNumber(max));
		options.put("value", new JSONNumber(min));
		Element div = DOM.createDiv();
		setElement(div);
		div.setId(this.id);
	}

	/**
	 * Create a new slider with default option, but set minimum and maximum
	 * value. You can also make it vertical.
	 * 
	 * @param min
	 *            The minimum value of the slider.
	 * @param max
	 *            The maximum value of the slider.
	 * @param vertical
	 *            If {@link true} the slider is vertical, horizontal otherwise.
	 */
	public Slider(int min, int max, boolean vertical) {
		this.id = Document.get().createUniqueId();
		setDefaultOptions();
		if (vertical) {
			options.put("orientation", new JSONString("vertical"));
		}
		options.put("min", new JSONNumber(min));
		options.put("max", new JSONNumber(max));
		options.put("value", new JSONNumber(min));
		Element div = DOM.createDiv();
		setElement(div);
		div.setId(this.id);
	}

	/**
	 * Create a new slider with default options and give it your own element id.
	 * 
	 * @param id
	 *            The element id you want this slider to have.
	 */
	public Slider(String id) {
		this.id = id;
		setDefaultOptions();
		Element div = DOM.createDiv();
		setElement(div);
		div.setId(this.id);
	}

	/**
	 * Create a new slider with default option, give it your own element id and
	 * set minimum and maximum value.
	 * 
	 * @param id
	 *            The element id you want this slider to have.
	 * @param min
	 *            The minimum value of the slider.
	 * @param max
	 *            The maximum value of the slider.
	 */
	public Slider(String id, int min, int max) {
		this.id = id;
		setDefaultOptions();
		options.put("min", new JSONNumber(min));
		options.put("max", new JSONNumber(max));
		options.put("value", new JSONNumber(min));
		Element div = DOM.createDiv();
		setElement(div);
		div.setId(this.id);
	}

	/**
	 * Create a new slider with default option, give it your own element id and
	 * set minimum and maximum value. You can also make it vertical.
	 * 
	 * @param id
	 *            The element id you want this slider to have.
	 * @param min
	 *            The minimum value of the slider.
	 * @param max
	 *            The maximum value of the slider.
	 * @param vertical
	 *            If {@link true} the slider is vertical, horizontal otherwise.
	 */
	public Slider(String id, int min, int max, boolean vertical) {
		this.id = id;
		setDefaultOptions();
		if (vertical) {
			options.put("orientation", new JSONString("vertical"));
		}
		options.put("min", new JSONNumber(min));
		options.put("max", new JSONNumber(max));
		options.put("value", new JSONNumber(min));
		Element div = DOM.createDiv();
		setElement(div);
		div.setId(this.id);
	}

	public void addSliderChangeHandler(SliderChangeHandler handler) {
		JavaScriptObject obj = createChangeFunction(getThis(), handler);
		options.put("change", new JSONObject(obj));
		if (isRendered()) {
			setNativeOptions(getId(), options.getJavaScriptObject());
		}
	}

	public void addSliderCreateHandler(SliderCreateHandler handler) {
		JavaScriptObject obj = createCreateFunction(getThis(), handler);
		options.put("create", new JSONObject(obj));
		if (isRendered()) {
			setNativeOptions(getId(), options.getJavaScriptObject());
		}
	}

	public void addSliderSlideHandler(SliderSlideHandler handler) {
		JavaScriptObject obj = createSlideFunction(getThis(), handler);
		options.put("slide", new JSONObject(obj));
		if (isRendered()) {
			setNativeOptions(getId(), options.getJavaScriptObject());
		}
	}

	public void addSliderStartHandler(SliderStartHandler handler) {
		JavaScriptObject obj = createStartFunction(getThis(), handler);
		options.put("start", new JSONObject(obj));
		if (isRendered()) {
			setNativeOptions(getId(), options.getJavaScriptObject());
		}
	}

	public void addSliderStopHandler(SliderStopHandler handler) {
		JavaScriptObject obj = createStopFunction(getThis(), handler);
		options.put("stop", new JSONObject(obj));
		if (isRendered()) {
			setNativeOptions(getId(), options.getJavaScriptObject());
		}
	}

	public void enable() {
		options.put("disabled", JSONBoolean.getInstance(false));
		if (isRendered()) {
			setNativeOptions(getId(), options.getJavaScriptObject());
		}
	}

	public void disable() {
		options.put("disabled", JSONBoolean.getInstance(true));
		if (isRendered()) {
			setNativeOptions(getId(), options.getJavaScriptObject());
		}
	}

	public void setAnimate(boolean bool) {
		options.put("animate", JSONBoolean.getInstance(bool));
		if (isRendered()) {
			setNativeOptions(getId(), options.getJavaScriptObject());
		}
	}

	public void setAnimate(String speed) {
		if (speed.equalsIgnoreCase("slow")) {
			options.put("animate", new JSONString("slow"));
		} else if (speed.equalsIgnoreCase("normal")) {
			options.put("animate", new JSONString("normal"));
		} else if (speed.equalsIgnoreCase("fast")) {
			options.put("animate", new JSONString("fast"));
		}
		if (isRendered()) {
			setNativeOptions(getId(), options.getJavaScriptObject());
		}
	}

	public void setAnimate(int speed) {
		options.put("animate", new JSONNumber(speed));
		if (isRendered()) {
			setNativeOptions(getId(), options.getJavaScriptObject());
		}
	}

	public void setMax(int max) {
		options.put("max", new JSONNumber(max));
		if (isRendered()) {
			setNativeOptions(getId(), options.getJavaScriptObject());
		}
	}

	public void setMin(int min) {
		options.put("min", new JSONNumber(min));
		if (isRendered()) {
			setNativeOptions(getId(), options.getJavaScriptObject());
		}
	}

	public void setHorizontal() {
		options.put("orientation", new JSONString("horizontal"));
		if (isRendered()) {
			setNativeOptions(getId(), options.getJavaScriptObject());
		}
	}

	public void setVertical() {
		options.put("orientation", new JSONString("vertical"));
		if (isRendered()) {
			setNativeOptions(getId(), options.getJavaScriptObject());
		}
	}

	// disabled because of error when using range
	// public void setRange(boolean bool) {
	// options.put("range", JSONBoolean.getInstance(bool));
	// if (isRendered()) {
	// setNativeOptions(getId(), options.getJavaScriptObject());
	// }
	// }

	public void setRange(String range) {
		if (range.equalsIgnoreCase("min")) {
			options.put("range", new JSONString("min"));
		} else if (range.equalsIgnoreCase("max")) {
			options.put("range", new JSONString("max"));
		}
		if (isRendered()) {
			setNativeOptions(getId(), options.getJavaScriptObject());
		}
	}

	public void setStep(int step) {
		options.put("step", new JSONNumber(step));
		if (isRendered()) {
			setNativeOptions(getId(), options.getJavaScriptObject());
		}
	}

	public void setValue(int value) {
		options.put("value", new JSONNumber(value));
		if (isRendered()) {
			setNativeOptions(getId(), options.getJavaScriptObject());
		}
	}

	public int getValue() {
		return getNativeValue(getId());
	}

	// disabled because of error when using range
	// public void setValues(Number[] array) {
	// JSONArray jarray = new JSONArray();
	// for (int i = 0; i < array.length; i++) {
	// jarray.set(i, new JSONNumber((Double) array[i]));
	// }
	// // Window.alert(jarray.toString());
	// options.put("values", jarray);
	// if (isRendered()) {
	// setNativeOptions(getId(), options.getJavaScriptObject());
	// }
	// }

	@Override
	protected void onLoad() {
		setRendered(true);
		createSlider(getId(), this.options.getJavaScriptObject());
		super.onLoad();
	}

	@Override
	protected void onUnload() {
		options.put("value", new JSONNumber(getNativeValue(getId())));
		destroySlider(this.id);
		setRendered(false);
		super.onUnload();
	}

	public String getId() {
		return this.id;
	}

	private Slider getThis() {
		return this;
	}

	private static native void destroySlider(String id) /*-{
		$wnd.$("#" + id).slider("destroy");
	}-*/;

	private static native void setNativeOptions(String id,
			JavaScriptObject options) /*-{
		$wnd.$("#" + id).slider("destroy");
		$wnd.$("#" + id).slider(options);
	}-*/;

	private void updateOptionValues(int value, JsArrayInteger values) {
		options.put("value", new JSONNumber(getNativeValue(getId())));
	}

	private static native JavaScriptObject createChangeFunction(Slider x,
			SliderChangeHandler handler) /*-{
		var func = function(event, ui) {
			var has = event.originalEvent ? true : false;
			x.@bpi.most.client.utils.ui.jSlider.Slider::updateOptionValues(ILcom/google/gwt/core/client/JsArrayInteger;)(ui.value,ui.values);
			handler.@bpi.most.client.utils.ui.jSlider.SliderChangeHandler::onChange(Lcom/google/gwt/user/client/Event;ILcom/google/gwt/core/client/JsArrayInteger;Z)(event,ui.value,ui.values,has);
		}
		return func;
	}-*/;

	private static native JavaScriptObject createCreateFunction(Slider x,
			SliderCreateHandler handler) /*-{
		var func = function(event, ui) {
			x.@bpi.most.client.utils.ui.jSlider.Slider::updateOptionValues(ILcom/google/gwt/core/client/JsArrayInteger;)(ui.value,ui.values);
			handler.@bpi.most.client.utils.ui.jSlider.SliderCreateHandler::onCreate(Lcom/google/gwt/user/client/Event;)(event);
		}
		return func;
	}-*/;

	private static native JavaScriptObject createSlideFunction(Slider x,
			SliderSlideHandler handler) /*-{
		var func = function(event, ui) {
			x.@bpi.most.client.utils.ui.jSlider.Slider::updateOptionValues(ILcom/google/gwt/core/client/JsArrayInteger;)(ui.value,ui.values);
			handler.@bpi.most.client.utils.ui.jSlider.SliderSlideHandler::onSlide(Lcom/google/gwt/user/client/Event;ILcom/google/gwt/core/client/JsArrayInteger;)(event,ui.value,ui.values);
		}
		return func;
	}-*/;

	private static native JavaScriptObject createStartFunction(Slider x,
			SliderStartHandler handler) /*-{
		var func = function(event, ui) {
			x.@bpi.most.client.utils.ui.jSlider.Slider::updateOptionValues(ILcom/google/gwt/core/client/JsArrayInteger;)(ui.value,ui.values);
			handler.@bpi.most.client.utils.ui.jSlider.SliderStartHandler::onStart(Lcom/google/gwt/user/client/Event;ILcom/google/gwt/core/client/JsArrayInteger;)(event,ui.value,ui.values);
		}
		return func;
	}-*/;

	private static native JavaScriptObject createStopFunction(Slider x,
			SliderStopHandler handler) /*-{
		var func = function(event, ui) {
			x.@bpi.most.client.utils.ui.jSlider.Slider::updateOptionValues(ILcom/google/gwt/core/client/JsArrayInteger;)(ui.value,ui.values);
			handler.@bpi.most.client.utils.ui.jSlider.SliderStopHandler::onStop(Lcom/google/gwt/user/client/Event;ILcom/google/gwt/core/client/JsArrayInteger;)(event,ui.value,ui.values);
		}
		return func;
	}-*/;

	private static native void createSlider(String id, JavaScriptObject options) /*-{
		$wnd.$("#" + id).slider(options);
	}-*/;

	private boolean isRendered() {
		return rendered;
	}

	private void setRendered(boolean rendered) {
		this.rendered = rendered;
	}

	private void setDefaultOptions() {
		options.put("disabled", JSONBoolean.getInstance(false));
		options.put("animate", JSONBoolean.getInstance(false));
		options.put("max", new JSONNumber(100));
		options.put("min", new JSONNumber(0));
		options.put("value", new JSONNumber(0));
		options.put("orientation", new JSONString("horizontal"));
		options.put("range", JSONBoolean.getInstance(false));
		options.put("step", new JSONNumber(1));
		// disabled because of error when using range
		// JSONArray temp = new JSONArray();
		// temp.set(0, new JSONNumber(0));
		// options.put("values", temp);
	}

	public String getOption(String option) {
		return getNativeOption(getId(), option);
	}

	private static native String getNativeOption(String id, String option) /*-{
		return $wnd.$("#" + id).slider("option", option);
	}-*/;

	private static native int getNativeValue(String id) /*-{
		return $wnd.$("#" + id).slider("option", "value");
	}-*/;
}
