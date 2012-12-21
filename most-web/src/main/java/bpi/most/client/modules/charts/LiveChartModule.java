package bpi.most.client.modules.charts;

import bpi.most.client.mainlayout.MainMenuEntry;
import bpi.most.client.modules.ModuleInterface;
import bpi.most.client.modules.ModuleWidget;

/**
 * 
 * 
 * The Data Class LiveChartModule. Defines all the necessary MetaData to run the
 * livechartmodule ( Name, URL, Icon, CSS, etc...)
 * 
 * @author sg
 */

public class LiveChartModule implements ModuleInterface {

	public static final String MODULE_NAME = "livechart";

	public static final String MODULE_MENU_ITEM_TEXT = "Charts";

	public static final String MODULE_URL = "#liveChart";

	public static final String linkInMenuItem = "ui-element-1 dWidget-uid-livechart ui-link-livechart";

	public static final String menuItemId = "ui-link-livechart";

	public static final String menuIconClass = "mod-ctrl-anchor";

	public LiveChartModule() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bpi.most.client.modules.ModuleInterface#getModuleName()
	 */
	@Override
	public String getModuleName() {
		// TODO Auto-generated method stub
		return MODULE_NAME;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bpi.most.client.modules.ModuleInterface#getMenuItemText()
	 */
	@Override
	public String getMenuItemText() {
		// TODO Auto-generated method stub
		return MODULE_MENU_ITEM_TEXT;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bpi.most.client.modules.ModuleInterface#getModuleUrl()
	 */
	@Override
	public String getModuleUrl() {
		// TODO Auto-generated method stub
		return MODULE_URL;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bpi.most.client.modules.ModuleInterface#getMenuItemCssClass()
	 */
	@Override
	public String getMenuItemCssClass() {
		// TODO Auto-generated method stub
		return linkInMenuItem;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bpi.most.client.modules.ModuleInterface#getModuleWidget()
	 */
	@Override
	public ModuleWidget getModuleWidget() {
		// TODO Auto-generated method stub
		return LiveChartModuleWidget.getInstance(this);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bpi.most.client.modules.ModuleInterface#getMainMenuEntry()
	 */
	@Override
	public MainMenuEntry getMainMenuEntry() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bpi.most.client.modules.ModuleInterface#getMenuItemId()
	 */
	@Override
	public String getMenuItemId() {
		return menuItemId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bpi.most.client.modules.ModuleInterface#getMenuIconClass()
	 */
	@Override
	public String getMenuIconClass() {
		return menuIconClass;
	}

}
