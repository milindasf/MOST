package bpi.most.client.modules.desktop;

import java.util.Iterator;

import bpi.most.client.mainlayout.RootModule;
import bpi.most.client.modules.ModuleInterface;
import bpi.most.client.modules.ModuleWidget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * The DesktopModule.
 * 
 * @author sg
 */
public final class DesktopModuleWidget extends ModuleWidget implements HasWidgets {

	private static final Binder binder = GWT.create(Binder.class);

	public static DesktopModuleWidget ref = null;

	@UiField
	public AbsolutePanel mDmPanel;

	interface Binder extends UiBinder<Widget, DesktopModuleWidget> {
	}

	private DesktopModuleWidget(ModuleInterface module) {
		super(module);
		initWidget(binder.createAndBindUi(this));
		RootModule.addNewDropWidgetToPanel(mDmPanel, "dropWidget dropWidget-desktopModule dWidget-uid-desktop floatLeft");
		for(int i = 0; i < mDmPanel.getWidgetCount(); i++) {			
			RootModule.setDropWidgetWidth(mDmPanel.getWidget(i));		
		}
		
		
		
		// DNDController.getDragonmaster().get(DNDController.getDragonmaster().size()-1).addSlave(DesktopModule.getInstance().getLinkInMenuItem());
		// DNDController.addDragonmaster(dragonmaster);

	}

	public static DesktopModuleWidget getInstance(ModuleInterface module) {

		if (ref == null) {
			ref = new DesktopModuleWidget(module);
		}
		return ref;
	}

	public AbsolutePanel getDropPanel() {

		return mDmPanel;
	}

	@Override
	public void add(Widget w) {
	}

	@Override
	public void clear() {
	}

	@Override
	public Iterator<Widget> iterator() {
		return null;
	}

	@Override
	public boolean remove(Widget w) {
		return false;
	}
	
	protected void onLoad() {
		
		
		

		super.onLoad();
	}

}
