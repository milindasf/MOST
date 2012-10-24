package bpi.most.client.utils.dnd;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.Composite;

public abstract class DropWidget extends Composite implements DropInterface {
	
	public DropWidget() {
		
	}
	
	public void registerHighlightElement(Element element) {
		DNDController.EVENT_BUS.fireEvent(new RegisterHighlightElementEvent(
				element));
	}
	public void fireMostDragEndEvent() {
		DNDController.EVENT_BUS.fireEvent(new MostDragEndEvent());
	}
	public boolean addDraggable(DragWidget widget) {
		return true;
	}


}
