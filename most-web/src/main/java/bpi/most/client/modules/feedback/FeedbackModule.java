package bpi.most.client.modules.feedback;

import bpi.most.client.mainlayout.MainMenuEntry;
import bpi.most.client.modules.ModuleInterface;
import bpi.most.client.modules.ModuleWidget;

/**
 * 
 * 
 * The Data Class FeedbackModule. Defines all the necessary MetaData to run the
 * Feedback module ( Name, URL, Icon, CSS, etc...)
 * 
 * @author sg
 */
public class FeedbackModule implements ModuleInterface {
	public static final String MODULE_NAME = "person";
	public static final String MODULE_MENU_ITEM_TEXT = "Feedback";
	public static final String MODULE_URL = "#person";
	public static final String LINK_IN_MENU_ITEM = "ui-element-2 dWidget-uid-person";
	public static final String MENU_ITEM_ID = "ui-link-person";
	public static final String MENU_ICON_CLASS = "mod-ctrl-anchor";

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
		return LINK_IN_MENU_ITEM;
	}

	@Override
	public ModuleWidget getModuleWidget() {
		return FeedbackModuleWidget.getInstance(this);
	}

	@Override
	public MainMenuEntry getMainMenuEntry() {
		return null;
	}

	@Override
	public String getMenuItemId() {
		return MENU_ITEM_ID;
	}

	@Override
	public String getMenuIconClass() {
		return MENU_ICON_CLASS;
	}
}
