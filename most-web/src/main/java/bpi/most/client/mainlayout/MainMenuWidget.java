package bpi.most.client.mainlayout;

import bpi.most.client.utils.dnd.DNDController;
import bpi.most.client.utils.dnd.RegisterHighlightElementEvent;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.dom.client.DragOverEvent;
import com.google.gwt.event.dom.client.DragOverHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.StackLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * 
 * The Class MainMenuWidget. The MainMenuWidget is a StackLayoutPanel which
 * forms the Main Menu of the Application. The Widget implements a singleton
 * 
 * @author sg
 */
public class MainMenuWidget extends Composite {

	private static MainMenuWidgetUiBinder uiBinder = GWT
			.create(MainMenuWidgetUiBinder.class);
	private static MainMenuWidget ref = null;

	interface MainMenuWidgetUiBinder extends UiBinder<Widget, MainMenuWidget> {
	}

	/** The actual UI element. */
	@UiField
	StackLayoutPanel menu;

	/**
	 * Instantiates a new main menu widget.
	 */
	public MainMenuWidget() {
		initWidget(uiBinder.createAndBindUi(this));

		menu.addSelectionHandler(new SelectionHandler<Integer>() {

			// add an unique id to every element on active
			@Override
			public void onSelection(SelectionEvent<Integer> event) {

				final String activeName = "ui-element-active";
				for (int i = 0; i < menu.getWidgetCount(); i++) {
					if (menu.getHeaderWidget(i).getParent().getElement()
							.getAttribute("class").contains(activeName)) {
						menu.getHeaderWidget(i).getParent()
								.removeStyleName(activeName);
					}
				}
				menu.getHeaderWidget(menu.getVisibleIndex()).getParent()
						.addStyleName(activeName);

			}
		});

	}

	/**
	 * Adds the main menu entry. Instantiates a new MainMenuEntryWidget, that is
	 * a VerticalPanel containing an Anchor and adds it to the StackLayoutPanel
	 * 
	 * @see bpi.most.client.mainlayout.MainMenuEntryWidget
	 * @see bpi.most.client.mainlayout.MainMenuEntry
	 * 
	 * @param MainMenuEntry
	 *            entry
	 * @return true, if successful
	 */
	public boolean addMainMenuEntry(MainMenuEntry entry) {

		MainMenuEntryWidget mainMenuEntryWidget = new MainMenuEntryWidget(entry);

		// clickhandler to load the new menu
		registerClickHandler(mainMenuEntryWidget.anchor,
				entry.getModuleWidget());

		// necessary for triggering the first click
		AbsolutePanel panel = new AbsolutePanel();

		// add new item to menu
		menu.add(panel, mainMenuEntryWidget, 6);

		// trigger click when first module loaded to initialize application
		if (menu.getWidgetIndex(panel) == 0) {
			mainMenuEntryWidget.anchor.getParent().addStyleName(
					"ui-element-active");
			NativeEvent ev = Document.get().createClickEvent(1, 1, 1, 1, 1,
					false, false, false, false);
			DomEvent.fireNativeEvent(ev, mainMenuEntryWidget.anchor);
		}

		registerMouseOverHandler(
				menu.getHeaderWidget(menu.getWidgetIndex(panel)).getParent(),
				mainMenuEntryWidget);

		return true;
	}

	public void registerMouseOverHandler(Widget highlightWidget,
			MainMenuEntryWidget mainMenuEntryWidget) {
		final Widget _highlightWidget = highlightWidget;
		final Widget _verticalPanel = mainMenuEntryWidget.vertical;
		final Widget _anchor = mainMenuEntryWidget.anchor;

		// add a new highlight element for each menu entry
		DNDController.EVENT_BUS.fireEvent(new RegisterHighlightElementEvent(
				_highlightWidget.getElement()));

		// handler for triggering menu action on mouse over
		// _highlightWidget.getParent().addDomHandler(new DragOverHandler() {
		//
		// @Override
		// public void onDragOver(DragOverEvent event) {
		// NativeEvent e = Document.get().createClickEvent(1, 1, 1, 1, 1,
		// false, false, false, false);
		// DomEvent.fireNativeEvent(e, _highlightWidget);
		//
		// }
		// }, DragOverEvent.getType());

		// handler for loading new module on drag operation
		_verticalPanel.addDomHandler(new DragOverHandler() {

			@Override
			public void onDragOver(DragOverEvent event) {
				try {
					NativeEvent e = Document.get().createClickEvent(1, 1, 1, 1,
							1, false, false, false, false);
					DomEvent.fireNativeEvent(e, _anchor);

				} catch (Exception e) {

				}

			}
		}, DragOverEvent.getType());
		_verticalPanel.addDomHandler(new MouseOverHandler() {

			@Override
			public void onMouseOver(MouseOverEvent event) {
				try {
					if (DNDController.getCurrentDrag() != null) {
						NativeEvent e = Document.get().createClickEvent(1, 1,
								1, 1, 1, false, false, false, false);
						DomEvent.fireNativeEvent(e, _anchor);
					}
				} catch (Exception e) {

				}
			}
		}, MouseOverEvent.getType());

	}

	/**
	 * Gets the single instance of MainMenuWidget.
	 * 
	 * @return single instance of MainMenuWidget
	 */
	public static MainMenuWidget getInstance() {
		if (ref == null) {
			ref = new MainMenuWidget();
		}
		return ref;
	}

	/**
	 * Register click handler.
	 * 
	 * @param anchor
	 *            the heading of the menu item
	 * @param module
	 *            the module which will be used
	 */
	private void registerClickHandler(Anchor anchor, final Composite module) {
		final String moduleUrl = anchor.getTitle().substring(1);

		anchor.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				RootModule.rootCenter.clear();
				RootModule.rootCenter.add(module);
//				try {
//					String[] url = Window.Location.getHref().split("#");
//					if (!url[url.length - 1].toString().trim()
//							.equals(moduleUrl.toString().trim())) {
//                        //TODO
//					}
//				} catch (Exception e) {
//
//				}
			}
		});
	}

}
