package bpi.most.client.utils.ui;

import java.util.ArrayList;

import bpi.most.client.utils.dnd.DNDController;
import bpi.most.client.utils.dnd.DpWidgetDropEvent;
import bpi.most.client.utils.dnd.DpWidgetDropEventHandler;
import bpi.most.client.utils.dnd.DragInterface;
import bpi.most.client.utils.dnd.DropWidget;
import bpi.most.client.utils.dnd.MostDragEndEvent;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.DragOverEvent;
import com.google.gwt.event.dom.client.DragOverHandler;
import com.google.gwt.event.dom.client.DropEvent;
import com.google.gwt.event.dom.client.DropHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.uibinder.client.UiField;

public class DropTextbox extends DropWidget {

	@UiField
	TextBox textBox;

	private ArrayList<String> accepts = new ArrayList<String>();

	private DropTextbox ref = this;

	private static DropTextboxUiBinder uiBinder = GWT
			.create(DropTextboxUiBinder.class);

	interface DropTextboxUiBinder extends UiBinder<Widget, DropTextbox> {
	}

	public DropTextbox() {
		initWidget(uiBinder.createAndBindUi(this));
		getElement().setId(Document.get().createUniqueId());
		addHandlers();
		registerHighlightElement(getElement());
	}

	public DropTextbox(String[] dropWidgetStyleNames) {

		initWidget(uiBinder.createAndBindUi(this));
		if (dropWidgetStyleNames != null) {
			for (int i = 0; i < dropWidgetStyleNames.length; i++) {
				getWidget().addStyleName(dropWidgetStyleNames[i]);
			}
		}

		getElement().setId(Document.get().createUniqueId());
		addHandlers();
		registerHighlightElement(getElement());

	}
	
	public DropTextbox(String dropWidgetStyleNames) {
		initWidget(uiBinder.createAndBindUi(this));
		if (dropWidgetStyleNames != null) {

			getWidget().addStyleName(dropWidgetStyleNames);

		}
		getElement().setId(Document.get().createUniqueId());
		addHandlers();
		registerHighlightElement(getElement());
	}

	public void addHandlers() {
		
		this.addDomHandler(new DragOverHandler() {

			@Override
			public void onDragOver(DragOverEvent event) {
				try {
					((DragInterface) DNDController.getCurrentDrag())
							.dragOverParentProcedure(event);
				} catch (Exception e) {

				}

			}
		}, DragOverEvent.getType());
		/**
		 * handles positioning of the Dragged Widgets, after the Element was
		 * reallocated. Still quite buggy.
		 **/
		this.addDomHandler(new DropHandler() {

			@Override
			public void onDrop(DropEvent event) {
				try {
					((DragInterface) DNDController.getCurrentDrag())
							.dropProcedure(ref, event);
				} catch (Exception e) {

				}

			}
		}, DropEvent.getType());
		try {
			DNDController.EVENT_BUS.fireEvent(new MostDragEndEvent());
		} catch (Exception e) {

		}
		addHandler(new DpWidgetDropEventHandler() {

			@Override
			public void onDpWidgetDropEvent(DpWidgetDropEvent event) {
				if (event.getDraggable() instanceof DpWidget) {
					textBox.setText(((DpWidget)event.getDraggable()).getName());
				}
			}
		}, DpWidgetDropEvent.getTYPE());
		textBox.addFocusHandler(new FocusHandler() {
			
			@Override
			public void onFocus(FocusEvent event) {
				textBox.selectAll();
			}
		});
	}
	
	public ArrayList<String> getAccepted() {
		return accepts;
	}

	public void accepts(String s) {
		this.accepts.add(s);
	}
	
	public String getText(){
		return textBox.getText();
	}
	
	public void setText(String text){
		textBox.setText(text);
	}

}
