package bpi.most.client.modules;

import com.google.gwt.user.client.ui.Composite;

/**
 * 
 * 
 * The Class ModuleWidget. An abstract class to specify that a module of type
 * ModuleWidget must receive a ModuleInterface in the constructor and implement
 * an getInstance method.
 * 
 * The getInstance method is not required though, as a module can possible not
 * be a singleton.
 * 
 * @author sg
 */

public abstract class ModuleWidget extends Composite {

	/**
	 * Instantiates a new module widget.
	 * 
	 * @param module
	 *            : a Metamodule class that implements the ModuleInterface.
	 */
	protected ModuleWidget(ModuleInterface module) {

	}
	protected ModuleWidget () {
		
	}

	/**
	 * Gets the single instance of ModuleWidget.
	 * 
	 * @param m
	 *            : instance of a ModuleInterface Object = a new Module
	 * @return single instance of ModuleWidget
	 */
	public static ModuleWidget getInstance(ModuleInterface m) {
		return null;

	}
}
