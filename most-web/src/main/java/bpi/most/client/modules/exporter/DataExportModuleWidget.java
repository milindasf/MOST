package bpi.most.client.modules.exporter;

import bpi.most.client.modules.ModuleInterface;
import bpi.most.client.modules.ModuleWidget;
import bpi.most.client.utils.ui.DpSearchWidget;
import bpi.most.client.utils.ui.DpSelectWidget;
import bpi.most.client.utils.ui.ZoneBrowseWidget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * The Class DataExportModule.
 * 
 * @author sg
 */
public final class DataExportModuleWidget extends ModuleWidget {

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

	/**
	 * The static reference to the object. needed for singleton pattern -> dnd
	 * always the same instance of a object
	 */
	public static DataExportModuleWidget ref;

	public static String linkInMenuItem = "ui-link-export";

	private static final Binder binder = GWT.create(Binder.class);

	/**
	 * The unique module panel. everything is rendered in this panel. originally
	 * included to ensure dnd functionality.
	 */
	@UiField
	AbsolutePanel demPanel;
	
	ZoneBrowseWidget zbw;
	DpSearchWidget dpsw;
	int oldSelection;

	interface Binder extends UiBinder<Widget, DataExportModuleWidget> {
	}

	private DataExportModuleWidget(ModuleInterface module) {
		super(module);
		initWidget(binder.createAndBindUi(this));
		demPanel.add(new DpSelectWidget());
		
//		RootModule
//				.addNewDropWidgetToPanel(demPanel,
//						"dropWidget dropWidget-desktopModule dWidget-uid-desktop floatLeft");
//		for (int i = 0; i < demPanel.getWidgetCount(); i++) {
//			RootModule.setDropWidgetWidth(demPanel.getWidget(i));
//		}
	}

	/**
	 * Gets the single instance of DataExportModule.
	 * 
	 * @return single instance of DataExportModule
	 */
	public static DataExportModuleWidget getInstance(ModuleInterface module) {
		if (ref == null) {
			ref = new DataExportModuleWidget(module);
		}
		return ref;
	}

	public String getLinkInMenuItem() {

		return linkInMenuItem;
	}

	public void setLinkInMenuItem(String linkInMenuItem) {

		DataExportModuleWidget.linkInMenuItem = linkInMenuItem;
	}

}
