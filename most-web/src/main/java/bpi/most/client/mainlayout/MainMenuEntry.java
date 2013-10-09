package bpi.most.client.mainlayout;

import bpi.most.client.modules.ModuleWidget;

/**
 * 
 * The Class MainMenuEntry. A Data class that creates an object which
 * contains information that are necessary to create a
 * MainMenuEntryWidget ( Anchor ), like Modulename, ModuleIcon, CSS
 * Classes.
 * The information comes from the DataModules that implement the
 * ModuleInterface
 * 
 * @author sg
 */
public class MainMenuEntry {

	/** The anchor text */
	private String MODULE_MENU_ITEM_TEXT = "";

	/** The url of the module */
	private String MODULE_URL = "";

	/** The CSS id */
	private String linkInMenuItem = "";

	/** The module widget. */
	private ModuleWidget moduleWidget = null;

	/** The menu item id. (CSS) */
	private String menuItemId = "";

	/** The menu icon class. */
	private String menuIconClass = "";

	/**
	 * Instantiates a new main menu entry.
	 */
	public MainMenuEntry() {

	}

	public String getModuleMenuItemText() {
		return MODULE_MENU_ITEM_TEXT;
	}

	public void setModuleMenuItemText(String moduleMenuItemText) {
		MODULE_MENU_ITEM_TEXT = moduleMenuItemText;
	}

	public String getModuleURL() {
		return MODULE_URL;
	}

	public void setModuleURL(String moduleURL) {
		MODULE_URL = moduleURL;
	}

	public String getLinkInMenuItem() {
		return linkInMenuItem;
	}

	public void setLinkInMenuItem(String linkInMenuItem) {
		this.linkInMenuItem = linkInMenuItem;
	}

	/**
	 * Gets the module widget instance.
	 * 
	 * @return ModuleWidget moduleWidget
	 */
	public ModuleWidget getModuleWidget() {
		return moduleWidget;
	}

	/**
	 * Sets the module widget.
	 * 
	 * @param ModuleWidget
	 *            moduleWidget the new module widget
	 */
	public void setModuleWidget(ModuleWidget moduleWidget) {
		this.moduleWidget = moduleWidget;
	}

	public String getMenuItemId() {
		return menuItemId;
	}

	public void setMenuItemId(String menuItemId) {
		this.menuItemId = menuItemId;
	}

	public String getMenuIconClass() {
		return menuIconClass;
	}

	public void setMenuIconClass(String menuIconClass) {
		this.menuIconClass = menuIconClass;
	}

}
