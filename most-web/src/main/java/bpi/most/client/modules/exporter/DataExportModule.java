package bpi.most.client.modules.exporter;

import bpi.most.client.mainlayout.MainMenuEntry;
import bpi.most.client.modules.ModuleInterface;
import bpi.most.client.modules.ModuleWidget;

import com.google.gwt.user.client.ui.AbsolutePanel;

/**
 * 
 * 
 * The Data Class DataExportModule. Defines all the necessary MetaData to run
 * the Data Exporter ( Name, URL, Icon, CSS, etc...)
 * 
 * @author sg
 */

public class DataExportModule implements ModuleInterface {
	
	 
	/**
	 * The Constant MODULE_NAME: Needed for a various number of operation,
	 * including menu rendering.
	 */
	public static final String MODULE_NAME = "export";

	/**
	 * The Constant MODULE_MENU_ITEM_TEXT: Needed for a various number of
	 * operation, including menu rendering.
	 */
	public static final String MODULE_MENU_ITEM_TEXT = "Export";

	/**
	 * The Constant MODULE_URL: Needed for a various number of operation,
	 * including menu rendering.
	 */
	public static final String MODULE_URL = "#export";
	public static final String linkInMenuItem = "ui-element-3 dWidget-uid-export";
	public static final String menuItemId = "ui-link-export";
	public static final String menuIconClass = "mod-ctrl-anchor";
	public AbsolutePanel prepareSubMenuPanel = new AbsolutePanel();

	public DataExportModule() {
		
	
	}

	@Override
	public String getModuleName() {
		return MODULE_NAME;
	}

	@Override
	public String getMenuItemText() {
		return MODULE_MENU_ITEM_TEXT;
	}

	@Override
	public String getModuleUrl() {
		return MODULE_URL;
	}

	@Override
	public String getMenuItemCssClass() {
		return linkInMenuItem;
	}

	@Override
	public ModuleWidget getModuleWidget() {
		return DataExportModuleWidget.getInstance(this);
	}

	@Override
	public MainMenuEntry getMainMenuEntry() {
		return null;
	}

	@Override
	public String getMenuItemId() {
		return menuItemId;
	}

	@Override
	public String getMenuIconClass() {
		return menuIconClass;
	}

}
