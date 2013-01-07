package bpi.most.client.modules.feedback;

import bpi.most.client.mainlayout.RootModule;
import bpi.most.client.utils.ui.GeneralDragWidget;
import bpi.most.client.utils.ui.GeneralDropWidget;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitHandler;

public class newPMDataset extends Composite {

	public static final String MODULE_NAME = "newAP";
	public static final String MODULE_MENU_ITEM_TEXT = "Neues Arbeitsplatzprotokoll";
	public static final String MODULE_URL = "#newAP";
	public static final String MODULE_ICON = "icon_newAP.jpg";
	public static String linkInMenuItem = "ui-link-pmdataset";
	public static newPMDataset ref;

	private static final Binder BINDER = GWT.create(Binder.class);
	@UiField
	Button save;
	@UiField
	FormPanel form;
	
	@UiField
	AbsolutePanel mPMmPanel;
	// @UiField(provided=true)
	// DropWidget dropField1;
	@UiField
	GeneralDropWidget dropField1;

	@UiField
	GeneralDragWidget drag1;

	public static FlowPanel test = RootModule.getRootCenter();

	interface Binder extends UiBinder<Widget, newPMDataset> {
	}
	public newPMDataset() {
		
	}
	public newPMDataset(final FlowPanel sidebar, final FlowPanel rootCenter) {

		// dropField1 = new DropWidget(new String[] {
		// "dropWidget-desktopModule",
		// "red", "floatLeft" }, new String[] { "dropWidget-content" });

		initWidget(BINDER.createAndBindUi(this));

		// DragWidget drag = new DragWidget(pmLufttemperatur, null, null, null);
		// drag.addDragWidgetType(GConfig.getUiDragPrefix() + getModuleName());

		// dropField1.getThisDropWidgetAsPanel().add(drag);
		// dropField1.addDroppableForDragWidgetType(GConfig.getUiDragPrefix() +
		// getModuleName());

//		DNDHighlightElement t1 = new DNDHighlightElement(
//				dropField1.getElement());
//		t1.accepts(GConfig.getUiDragPrefix() + getModuleName());
//		DNDController.addHighlightElement(t1);

//		drag1.addDragWidgetType(GConfig.getUiDragPrefix() + getModuleName());

//		addRightClickProcedure(rcPanel, sidebar, rootCenter);
		form.setAction(GWT.getModuleBaseURL() + "person");
		form.setMethod(FormPanel.METHOD_POST);

		// form.setEncoding(FormPanel.METHOD_POST);
		form.addSubmitHandler(new SubmitHandler() {

			@Override
			public void onSubmit(SubmitEvent event) {

				// Window.alert(""+form.);
				// TODO Auto-generated method stub
			}
		});
		form.addSubmitCompleteHandler(new SubmitCompleteHandler() {

			@Override
			public void onSubmitComplete(SubmitCompleteEvent event) {

				Window.alert("" + event.getResults());
				// TODO Auto-generated method stub
			}
		});
	}
	/*public class Sliders extends AbstractSliderDemo {

		  public Sliders() {
		    super("Sliders");
		  }

		  public String getName() {
		    return "Default Functionality";
		  }

		  public void setupDemoElement(Element demo) {
		    $("#slider", demo).as(Ui).slider();
		  }
		}*/
    
//	public void addRightClickProcedure(final RightClickFlowPanel p,
//			FlowPanel sidebar, FlowPanel rootCenter) {
//
//		final RightClickPanelDefault_Add popup = new RightClickPanelDefault_Add(
//				this, sidebar, rootCenter);
//		popup.setAutoHideEnabled(true);
//		popup.addCloseHandler(new CloseHandler<PopupPanel>() {
//
//			@Override
//			public void onClose(CloseEvent<PopupPanel> event) {
//
//				p.removeStyleName("border-red");
//				// TODO Auto-generated method stub
//			}
//		});
//		p.addClickHandler(new RightClickFlowPanelInterface() {
//
//			@Override
//			public void onClick(ClickEvent event) {
//
//				// TODO Auto-generated method stub
//			}
//
//			@Override
//			public void onRightClick(Widget sender, Event event) {
//
//				p.addStyleName("border-red");
//				popup.setPopupPosition(DOM.eventGetClientX(event),
//						DOM.eventGetClientY(event));
//				popup.show();
//				// TODO Auto-generated method stub
//			}
//
//			@Override
//			public void onClick(Widget sender, Event event) {
//
//				// TODO Auto-generated method stub
//			}
//		});
//	}

	@UiHandler("save")
	void handleClick(ClickEvent e) {

		// TODO validation
		form.submit();
	}

	public static newPMDataset getSingleton(FlowPanel sidebar,
			FlowPanel rootCenter) {

		if (ref == null) {
			ref = new newPMDataset(sidebar, rootCenter);
		}
		return ref;
	}

//	@Override
//	public String getModuleName() {
//
//		// TODO Auto-generated method stub
//		return MODULE_NAME;
//	}
//
//	@Override
//	public String getModuleMenuItemText() {
//
//		// TODO Auto-generated method stub
//		return MODULE_MENU_ITEM_TEXT;
//	}
//
//	@Override
//	public String getModuleUrl() {
//
//		// TODO Auto-generated method stub
//		return MODULE_URL;
//	}
//
//	public Composite getModuleObj() {
//
//		Composite m = this;
//		return m;
//	}
//
//	@Override
//	public String getModuleIconName() {
//
//		// TODO Auto-generated method stub
//		return MODULE_ICON;
//	}
//
//	public boolean isRightClickSet() {
//
//		return true;
//	}
//
//	public FlowPanel rcPanel() {
//
//		return null;
//	}
//
//	@Override
//	public AbsolutePanel getPanel() {
//
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public AbsolutePanel getDropPanel() {
//
//		// TODO Auto-generated method stub
//		return mPMmPanel;
//	}

	/**
	 * @return the linkInMenuItem
	 */
	public String getLinkInMenuItem() {

		return linkInMenuItem;
	}

	/**
	 * @param linkInMenuItem
	 *            the linkInMenuItem to set
	 */
	public void setLinkInMenuItem(String linkInMenuItem) {

		newPMDataset.linkInMenuItem = linkInMenuItem;
	}
	
}
