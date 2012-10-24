package bpi.most.client.utils.dnd;

import com.google.gwt.event.dom.client.DragStartEvent;
import com.google.gwt.event.dom.client.DragStartHandler;

public interface DNDDragStartInterface extends DragStartHandler{

	void onDragStart(DragStartEvent event);
	void onDragStartDND(DragStartEvent event);
}
