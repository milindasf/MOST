package bpi.most.client.modules;

import bpi.most.client.modules.charts.LiveChartModule;
import bpi.most.client.modules.d3viewer.D3Module;
import bpi.most.client.modules.desktop.DesktopModule;
import bpi.most.client.modules.exporter.DataExportModule;
import bpi.most.client.modules.feedback.FeedbackModule;

/**
 * 
 * 
 * The Class ModuleRegistrator. This class is the central point that
 * instantiates new Modules
 * 
 * @author sg
 */
public class ModuleRegistrator {

	/**
	 * Instantiates a new module registrator.
	 */
	public ModuleRegistrator() {
		registerMainMenuModule(new DesktopModule());
		registerMainMenuModule(new LiveChartModule());
		registerMainMenuModule(new FeedbackModule());
		registerMainMenuModule(new DataExportModule());
		registerMainMenuModule(new D3Module());
	}

	/**
	 * Register main menu module. This method tells the ModuleController to
	 * instantiate a new Module of the type MainMenuModule = Module that has a
	 * main menu entry
	 * 
	 * @param ModuleInterface
	 *            module
	 */
	public void registerMainMenuModule(ModuleInterface module) {
		ModuleController.registerMainMenuModule(module);
	}
}
