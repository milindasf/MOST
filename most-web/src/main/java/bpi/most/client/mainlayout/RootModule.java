/*
 * 
 */
package bpi.most.client.mainlayout;

import bpi.most.client.login.LoginViewWidget;
import bpi.most.client.modules.ModuleController;
import bpi.most.client.rpc.AuthenticationService;
import bpi.most.client.rpc.AuthenticationServiceAsync;
import bpi.most.client.utils.ComputedStyle;
import bpi.most.client.utils.dnd.DNDController;
import bpi.most.client.utils.dnd.DropWidget;
import bpi.most.client.utils.dnd.MostDragEndEvent;
import bpi.most.client.utils.ui.GeneralDropWidget;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.DropEvent;
import com.google.gwt.event.dom.client.DropHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

import java.util.Iterator;

/**
 * 
 * 
 * The Class RootModule. Defines the overall layout of the application. Uses a
 * DockLayoutPanel
 * 
 * Instantiates the DND event bus
 * 
 * @author sg
 */
public class RootModule extends Composite implements HasWidgets {

    private final AuthenticationServiceAsync authenticationService = GWT.create(AuthenticationService.class);

    private static final Binder BINDER = GWT.create(Binder.class);
	public static Integer windowWidth;
	public static final Integer DROP_WIDGET_WIDTH = 500;
	public static final int WIDTH_OFFSET = 50;

	interface Binder extends UiBinder<Widget, RootModule> {
	}

	@UiField
	static FlowPanel rootNorth;

	@UiField
	static FlowPanel rootSouth;

	@UiField
	static LayoutPanel rootWest;

	@UiField
	static FlowPanel rootCenter;

	@UiField
	Button logoutButton;

	@UiField
	static DockLayoutPanel rootDock;

	@UiField
	static AbsolutePanel boundaryPanel;

	@UiField
	Label helplabel;

	/**
	 * Instantiates a new root module.
	 */
	public RootModule() {

		// TODO header and footer module/widget
		// TODO i18n!!!!!!
		initWidget(BINDER.createAndBindUi(this));

		windowWidth = Window.getClientWidth();

		// Drag and Drop Target Visualization Master Element
//		Element highlightingBackgroundElement = Document.get()
//				.createDivElement();
		FlowPanel highlightingBackgroundWidget = new FlowPanel();
		DNDController.setBackgroundWidget(highlightingBackgroundWidget);
		highlightingBackgroundWidget.addDomHandler(new DropHandler() {
			
			@Override
			public void onDrop(DropEvent event) {
				DNDController.EVENT_BUS.fireEvent(new MostDragEndEvent());				
			}
		}, DropEvent.getType());
		Element highlightingBackgroundElement = highlightingBackgroundWidget.getElement();
		highlightingBackgroundElement.setId("uid-dragonsupermaster");
		Document.get().getBody().insertFirst(highlightingBackgroundElement);
		DNDController.getInstance(highlightingBackgroundElement);
		rootWest.add(MainMenuWidget.getInstance());
		ModuleController.getInstance();
		
		
	}

	@UiHandler("logoutButton")
	void handleClick(ClickEvent e) {

		authenticationService.logout(new AsyncCallback<Boolean>() {

            @Override
            public void onSuccess(Boolean result) {

                RootLayoutPanel.get().clear();
                RootPanel.get().clear();
                // LoginViewWidget.setNull();
                RootPanel.get().add(new LoginViewWidget(new RootModuleCreator()));
            }

            @Override
            public void onFailure(Throwable caught) {

                Window.alert(caught.getMessage());
            }
        });
	}

	public static FlowPanel getRootNorth() {

		return rootNorth;
	}

	public FlowPanel getRootSouth() {

		return rootSouth;
	}

	public LayoutPanel getRootWest() {

		return rootWest;
	}

	public AbsolutePanel getBoundaryPanel() {

		return boundaryPanel;
	}

	public static DockLayoutPanel getRootWidget() {

		return rootDock;
		// return desktop;
	}

	public static LayoutPanel getSidebar() {

		// return sidebar;
		return rootWest;
	}

	public static FlowPanel getRootCenter() {

		return rootCenter;
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

	public static void addNewDropWidgetToPanel(AbsolutePanel panel,
			String styleNames) {
		while (newDropWidgetPossible(panel)) {
			DropWidget drop = new GeneralDropWidget(styleNames, null);
			drop.setWidth(DROP_WIDGET_WIDTH + "px");
			panel.add(drop);
		}
	}

	private static boolean newDropWidgetPossible(AbsolutePanel panel) {
		int width = ComputedStyle
				.getWidth(RootModule.getSidebar().getElement());
		for (int i = 0; i < panel.getWidgetCount(); i++) {
			width += ComputedStyle.getWidth(panel.getWidget(i).getElement());
			width += ComputedStyle.getMarginLeft(panel.getWidget(i)
					.getElement());
			width += ComputedStyle.getMarginRight(panel.getWidget(i)
					.getElement());
			width += ComputedStyle.getPaddingLeft(panel.getWidget(i)
					.getElement());
			width += ComputedStyle.getPaddingRight(panel.getWidget(i)
					.getElement());
		}
		if (windowWidth - width - WIDTH_OFFSET > DROP_WIDGET_WIDTH) {
			return true;
		}
		return false;
	}

	public static void setDropWidgetWidth(Widget widget) {
		widget.setWidth(DROP_WIDGET_WIDTH + "px");
	}

}
