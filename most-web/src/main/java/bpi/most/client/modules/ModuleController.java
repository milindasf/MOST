/**
 * remarks: dynamic module initialization via reflections is not possible in this case.
 * because the necessary jre functionality is not available in translated javascript code
 * e.g. class.getfields() etc...
 * one possibility would be to generate the modules on the serverside.
 * 
 * UPDATE checking deferred binding and generators as an gwt alternative to reflections
 * see: http://stackoverflow.com/questions/4195233/can-you-use-java-reflection-api-in-gwt-client
 * 
 */
package bpi.most.client.modules;

import bpi.most.client.mainlayout.MainMenuEntry;
import bpi.most.client.mainlayout.MainMenuWidget;
import bpi.most.client.model.DpController;
import bpi.most.client.model.ZoneController;
import bpi.most.client.rpc.ModuleControllerService;
import bpi.most.client.rpc.ModuleControllerServiceAsync;

import com.google.gwt.core.client.GWT;

/**
 * The Class ModuleController.
 * 
 * This class controls modules of all kinds. A new module is registered at the
 * module controller, which tells the module where it should be displayed.
 * 
 * Adds and removes Modules
 * 
 * @author sg
 */
public final class ModuleController {

	public static final ModuleControllerServiceAsync moduleCtrlService = GWT
			.create(ModuleControllerService.class);

	public static ModuleController ref = null;
	// TODO find right place for DpController
	public static DpController dpcc = DpController.getSingleton();
	public static ZoneController zoneCtrl = ZoneController.getInstance();

	private ModuleController() {
		new ModuleRegistrator();
	}

	/**
	 * Gets the singleton.
	 * 
	 * @return the singleton
	 */
	public static ModuleController getInstance() {

		if (ref == null) {

			ref = new ModuleController();
		}
		return ref;
	}

	public static void registerMainMenuModule(ModuleInterface module) {

		// TODO Permission Check / true - false

		MainMenuEntry entry = new MainMenuEntry();
		entry.setMODULE_URL(module.getModuleUrl());
		entry.setMODULE_MENU_ITEM_TEXT(module.getMenuItemText());
		entry.setLinkInMenuItem(module.getMenuItemCssClass());
		entry.setModuleWidget(module.getModuleWidget());
		entry.setMenuItemId(module.getMenuItemId());
		entry.setMenuIconClass(module.getMenuIconClass());

		MainMenuWidget.getInstance().addMainMenuEntry(entry);

	}

}
