package bpi.most.client.utils.ui;

import bpi.most.client.utils.dnd.DNDController;
import bpi.most.client.utils.dnd.DpWidgetDropEvent;
import bpi.most.client.utils.dnd.DragInterface;
import bpi.most.client.utils.dnd.DropWidget;
import bpi.most.client.utils.dnd.MostDragEndEvent;
import bpi.most.client.utils.dnd.MostDragStartEvent;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.event.dom.client.DragEndEvent;
import com.google.gwt.event.dom.client.DragEndHandler;
import com.google.gwt.event.dom.client.DragOverEvent;
import com.google.gwt.event.dom.client.DragOverHandler;
import com.google.gwt.event.dom.client.DragStartEvent;
import com.google.gwt.event.dom.client.DragStartHandler;
import com.google.gwt.event.dom.client.DropEvent;
import com.google.gwt.event.dom.client.DropHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

public class DpWidget extends HTML implements DragInterface {
	private String type;
	private String name;
	private String description;
	
	public DpWidget(final String name, String type) {
		this(name);
		this.type = type;
	}
	public DpWidget(final String name, String type, String description) {
		this(name, type);
		this.description = description;
		setTitle(description);
	}
	public DpWidget(final String name) {
		super(name);
		this.name = name;
		this.description = name;
		setWidth("100px");
		getElement().setPropertyBoolean("draggable", true);
		getElement().getStyle().setDisplay(Display.INLINE_BLOCK);
		getElement().setDraggable(Element.DRAGGABLE_TRUE);
		
		//TODO set DpWidget style and let the modules chose which styles to "support" (-->dropable)
		addStyleName("gwt-SensorLabel dWidget-uid-livechart dWidget-uid-3d");
		setTitle(name);
		addDragStartHandler(new DragStartHandler() {

			@Override
			public void onDragStart(DragStartEvent event) {
				event.setData("mytext", name);		//setting "text" here causes url redirects on drops. use "mytext" or something else
				event.getDataTransfer().setDragImage(getElement(), 10, 10);
				registerDragWidget(getThis());
				fireMostDragStartEvent(event);
				DNDController.setDragitem(getThis());

				// firefox workaround as ff doesn't receive the event
				// coordinates
				if (event.getNativeEvent().getClientX() >= getAbsoluteLeft()) {
					DNDController.setRelMouseToElemX(event.getNativeEvent()
							.getClientX() - getAbsoluteLeft());
				}
				if (event.getNativeEvent().getClientY() >= getAbsoluteTop()) {
					DNDController.setRelMouseToElemY(event.getNativeEvent()
							.getClientY() - getAbsoluteTop());
				}
			}
		});
		//TODO Why di I need this handlers here??? 
		addDomHandler(new DragOverHandler() {

			@Override
			public void onDragOver(DragOverEvent event) {
				

			}
		}, DragOverEvent.getType());
		addDomHandler(new DragEndHandler() {

			@Override
			public void onDragEnd(DragEndEvent event) {
				event.preventDefault();
				event.stopPropagation();
				fireMostDragEndEvent();

			}
		}, DragEndEvent.getType());
		addDomHandler(new DropHandler() {
			
			@Override
			public void onDrop(DropEvent event) {
				event.preventDefault();
				event.stopPropagation();	
				fireMostDragEndEvent();
			}
		}, DropEvent.getType());

	}

	public DpWidget getThis() {
		return this;
	}
	
	@Override
	public void dropProcedure(DropWidget dwidget, DropEvent event) {
		event.preventDefault();
		((Widget)event.getSource()).fireEvent(new DpWidgetDropEvent(getThis()));
		
//		fireEvent(new DpWidgetDropEvent(getThis()));
		//DNDController.EVENT_BUS.fireEvent(new DpWidgetDropEvent(getThis()));
		
	}

	@Override
	public void dragOverParentProcedure(DragOverEvent event) {
		event.preventDefault();
	}
	public void registerDragWidget(Widget widget) {
//		Widget copy = new DpWidget(((DpWidget)widget).name);
//		DNDController.setCurrentDrag(copy);
		DNDController.setCurrentDrag(widget);
	}

	public void fireMostDragEndEvent() {
		DNDController.EVENT_BUS.fireEvent(new MostDragEndEvent());
	}
	public void fireMostDragStartEvent(DragStartEvent event) {
		DNDController.EVENT_BUS.fireEvent(new MostDragStartEvent(this
				.getElement()));
	}

	@Override
	public boolean isDroppable() {
		return false;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
