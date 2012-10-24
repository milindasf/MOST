package bpi.most.client.utils.dnd;

import com.google.gwt.event.dom.client.DragStartEvent;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public abstract class DragWidget extends Composite {

	public DragWidget() {
	}

	public DragWidget(Widget w) {
	}

	public void fireMostDragStartEvent(DragStartEvent event) {
		DNDController.EVENT_BUS.fireEvent(new MostDragStartEvent(this
				.getElement()));
	}
//
//	public abstract DragWidget dragOverProcedure(DragWidget widget,
//			DragOverEvent event);
//
//	public abstract SensorLabel dragOverProcedure(SensorLabel widget,
//			DragOverEvent event);

	public void registerDragWidget(Widget widget) {
		DNDController.setCurrentDrag(widget);
	}

	public void fireMostDragEndEvent() {
		DNDController.EVENT_BUS.fireEvent(new MostDragEndEvent());
	}
	
	public boolean isDroppable() {
		return true;
	}
//	public abstract void dropProcedure();

}
