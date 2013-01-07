package bpi.most.client.mainlayout;

import bpi.most.client.utils.dnd.DNDController;
import bpi.most.client.utils.dnd.MostDragEndEvent;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.DropEvent;
import com.google.gwt.event.dom.client.DropHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * The Class MainMenuEntryWidget. Creates a MainMenuEntryWidget object which is
 * actually an anchor.
 * 
 * @author sg
 * 
 */
public class MainMenuEntryWidget extends Composite {

	private static MainMenuEntryWidgetUiBinder uiBinder = GWT
			.create(MainMenuEntryWidgetUiBinder.class);

	interface MainMenuEntryWidgetUiBinder extends
			UiBinder<Widget, MainMenuEntryWidget> {
	}

	@UiField
	VerticalPanel vertical;

	@UiField
	Anchor anchor;

	/**
	 * Instantiates a new main menu entry widget. The constructor receives a
	 * Data Object that serves all necessary link information.
	 * 
	 * @param MainMenuEntry
	 *            entry
	 */
	public MainMenuEntryWidget(MainMenuEntry entry) {
		initWidget(uiBinder.createAndBindUi(this));
		// anchor attributes
		anchor.setText(entry.getModuleMenuItemText());
		anchor.setHref(entry.getModuleURL());
		anchor.setTitle(entry.getModuleURL());
		anchor.addStyleName(entry.getMenuIconClass());
		anchor.addStyleName(entry.getMenuItemId());
		vertical.addStyleName(entry.getLinkInMenuItem());
		vertical.getElement().setId(entry.getMenuItemId());
		
		this.addDomHandler(new DropHandler() {
			
			@Override
			public void onDrop(DropEvent event) {
				DNDController.EVENT_BUS.fireEvent(new MostDragEndEvent());				
			}
		}, DropEvent.getType());
		
	}

}
