package bpi.most.client.modules.d3viewer;

import bpi.most.client.mainlayout.MainMenuEntry;
import bpi.most.client.modules.ModuleInterface;
import bpi.most.client.modules.ModuleWidget;
import com.google.gwt.user.client.ui.AbsolutePanel;

/**
 * 
 * 
 * The Data Class D3Module. Defines all the necessary MetaData to run the 3 D
 * module ( Name, URL, Icon, CSS, etc...)
 * 
 * @author sg
 */
public class D3Module implements ModuleInterface {

	/**
	 * The Constant MODULE_NAME: Needed for a various number of operation,
	 * including menu rendering.
	 */
	public static final String MODULE_NAME = "3d";

	/**
	 * The Constant MODULE_MENU_ITEM_TEXT: Needed for a various number of
	 * operation, including menu rendering.
	 */
	public static final String MODULE_MENU_ITEM_TEXT = "3D";

	/**
	 * The Constant MODULE_URL: Needed for a various number of operation,
	 * including menu rendering.
	 */
	public static final String MODULE_URL = "#3d";

	/**
	 * The static reference to the object. needed for singleton pattern -> dnd
	 * always the same instance of a object
	 */
	public static D3ModuleWidget ref;

	public static final String LINK_IN_MENU_ITEM = "ui-element-4 dWidget-uid-3d";

	public static final String MENU_ITEM_ID = "ui-link-3d";

	public static final String MENU_ICON_CLASS = "mod-ctrl-anchor";

	public AbsolutePanel prepareSubMenuPanel = new AbsolutePanel();

	/**
	 * Instantiates a new d3 module.
	 */
	public D3Module() {

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
		return LINK_IN_MENU_ITEM;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bpi.most.client.modules.ModuleInterface#getModuleWidget()
	 */
	@Override
	public ModuleWidget getModuleWidget() {
		// TODO Auto-generated method stub
		return D3ModuleWidget.getInstance(this);

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
		return MENU_ITEM_ID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bpi.most.client.modules.ModuleInterface#getMenuIconClass()
	 */
	@Override
	public String getMenuIconClass() {
		return MENU_ICON_CLASS;
	}
}
