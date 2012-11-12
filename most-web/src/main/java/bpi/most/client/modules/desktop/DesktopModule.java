package bpi.most.client.modules.desktop;

import bpi.most.client.mainlayout.MainMenuEntry;
import bpi.most.client.modules.ModuleInterface;
import bpi.most.client.modules.ModuleWidget;

/**
 * 
 * 
 * The Data Class DesktopModule. Defines all the necessary MetaData to run the
 * Desktop module ( Name, URL, Icon, CSS, etc...)
 * 
 * @author sg
 */

public class DesktopModule implements ModuleInterface {
	public final String MODULE_NAME = "desktop";
	public final String MODULE_MENU_ITEM_TEXT = "Desktop";
	public final String MODULE_URL = "#desktop";
	public final String linkInMenuItem = "ui-element-0 dWidget-uid-desktop";
	public final String menuItemId = "ui-link-desktop";
	public final String menuIconClass = "mod-ctrl-anchor";

	public DesktopModule() {

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
		return DesktopModuleWidget.getInstance(this);
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
