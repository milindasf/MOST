package bpi.most.client.utils.dnd;

import com.google.gwt.event.dom.client.DragOverEvent;
import com.google.gwt.event.dom.client.DropEvent;

public interface DragInterface {
	
//	public void fireMostDragStartEvent(DragStartEvent event);
//	public void dragOverProcedure(DragWidget widget);
//	public void registerDragWidget(Widget widget);
//	public void fireMostDragEndEvent(DragEndEvent event);
	void dropProcedure(DropWidget dropOnMe, DropEvent event);
	void dragOverParentProcedure(DragOverEvent event);
	boolean isDroppable();
}
