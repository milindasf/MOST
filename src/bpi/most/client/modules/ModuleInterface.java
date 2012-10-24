package bpi.most.client.modules;

import bpi.most.client.mainlayout.MainMenuEntry;

/**
 * 
 * The Interface ModuleInterface. This interface specifies Methods that every
 * Metamodule/Datamodule has to implement. The methods access ModuleMetaData
 * which are i.e. used to create the Menu Item.
 * 
 * @author sg
 */
public interface ModuleInterface {

	/**
	 * the module name constant - necessary for db queries etc.
	 * 
	 * @return String
	 */
	public String getModuleName();

	/**
	 * The text displayed in the Anchor defining the Menu Item.
	 * 
	 * @return String
	 */
	public String getMenuItemText();

	/**
	 * Url displayed when clicking a module.
	 * 
	 * @return String
	 */
	public String getModuleUrl();

	/**
	 * the CSS class of the header item. needed for DND
	 * 
	 * @return String
	 */
	public String getMenuItemCssClass();

	/**
	 * Gets the main menu entry.
	 * 
	 * @return the main menu entry
	 */
	public MainMenuEntry getMainMenuEntry();

	/**
	 * Gets the module widget. the actual menu entry widget ( anchor )
	 * 
	 * @return the module widget
	 */
	public ModuleWidget getModuleWidget();

	/**
	 * Gets the menu item id. This id is applied to the DOM element
	 * 
	 * @return the menu item id
	 */
	public String getMenuItemId();

	/**
	 * Gets the menu icon class. A css class linking to the ICON of the menu
	 * item
	 * 
	 * @return the menu icon class
	 */
	public String getMenuIconClass();
}
