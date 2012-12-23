package bpi.most.client.modules.d3viewer;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.TextResource;

/**
 * 
 * 
 * The Interface BimSurferInjection. Defines methods for injecting all necessary
 * classes for the BIM surfer
 * 
 * @author sg
 * @see D3ModuleWidget
 */
public interface BimSurferInjection extends ClientBundle {

	// Scenejs
	/**
	 * Scenejs math.
	 * 
	 * @return the text resource
	 */
	@Source("scenejs/scenejs.math.js")
	TextResource scenejsMath();

	/**
	 * Scenejs math extra.
	 * 
	 * @return the text resource
	 */
	@Source("scenejs/scenejs.math.extra.js")
	TextResource scenejsMathExtra();

	/**
	 * Scenejs.
	 * 
	 * @return the text resource
	 */
	@Source("scenejs/scenejs.js")
	TextResource scenejs();

	/**
	 * Touch events for ff 10. absoluter pfusch
	 * remove as soon as android is supported.
	 * 
	 * @return the text resource
	 */
	@Source("bimsurfer/ff_touch.js")
	TextResource ffTouch();
	
	// BimSurfer
	/**
	 * Bim surfer.
	 * 
	 * @return the text resource
	 */
	@Source("bimsurfer/app.js")
	TextResource bimSurfer();

}
