package bpi.most.client.modules.feedback;

import bpi.most.client.mainlayout.RootModule;
import bpi.most.client.modules.ModuleInterface;
import bpi.most.client.modules.ModuleWidget;
import bpi.most.client.rpc.ModuleControllerService;
import bpi.most.client.rpc.ModuleControllerServiceAsync;
import bpi.most.client.rpc.PersonModuleService;
import bpi.most.client.rpc.PersonModuleServiceAsync;
import bpi.most.client.utils.ui.GeneralDragWidget;
import bpi.most.client.utils.ui.GeneralDropWidget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitHandler;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * The feedback module
 * 
 * @author sg
 */
public class FeedbackModuleWidget extends ModuleWidget {

	public final ModuleControllerServiceAsync moduleCtrlService = GWT
			.create(ModuleControllerService.class);

	// public static VerticalPanel panel = new VerticalPanel();
	public static AbsolutePanel panel = new AbsolutePanel();
	public static FeedbackModuleWidget ref;
	private static final Binder binder = GWT.create(Binder.class);
	public final PersonModuleServiceAsync personModuleService = GWT
			.create(PersonModuleService.class);
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
	@UiField
	GeneralDragWidget drag2;
	@UiField
	GeneralDragWidget drag3;
	@UiField
	GeneralDragWidget drag4;

	interface Binder extends UiBinder<Widget, FeedbackModuleWidget> {
	}

	private FeedbackModuleWidget(ModuleInterface module) {
		super(module);
		initWidget(binder.createAndBindUi(this));
		RootModule.addNewDropWidgetToPanel(mPMmPanel, "dropWidget dropWidget-desktopModule dWidget-uid-desktop floatLeft");
		for(int i = 0; i < mPMmPanel.getWidgetCount(); i++) {			
			RootModule.setDropWidgetWidth(mPMmPanel.getWidget(i));		
		}
		form.setAction(GWT.getModuleBaseURL() + "person");
		form.setMethod(FormPanel.METHOD_POST);

		form.setAction(GWT.getModuleBaseURL() + "person");
		form.setMethod(FormPanel.METHOD_POST);

		form.setAction(GWT.getModuleBaseURL() + "person");
		form.setMethod(FormPanel.METHOD_POST);

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

	@UiHandler("save")
	void handleClick(ClickEvent e) {

		// TODO validation
		form.submit();
	}

	public static FeedbackModuleWidget getInstance(ModuleInterface module) {
		if (ref == null) {
			ref = new FeedbackModuleWidget(module);
		}
		return ref;
	}
}
