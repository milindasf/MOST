package bpi.most.client.utils.ui;

import java.util.ArrayList;

import bpi.most.client.utils.dnd.DpWidgetDropEvent;
import bpi.most.client.utils.dnd.DpWidgetDropEventHandler;
import bpi.most.dto.DpDTO;

/**
 * The CollectDatapointDropWidget class. It's for collecting {@link DpWidget}
 * from other sources like the ZoneBrowser, the DpSearchWidget or the 3D module.
 * When the {@link DpWidget} is dropped over the CollectDatapointDropWidget it
 * creates a copy of it (unless it's already present) and can so collect all the
 * data points you want to use for further tasks.
 * 
 * @author mike
 * 
 */
public class CollectDatapointDropWidget extends GeneralDropWidget {

	/**
	 * The {@link TrashDatapointDropWidget} that belongs to this
	 * CollectDatapointDropWidget. It's not absolutely necessary, but it's a
	 * easy way to remove DpWidgets from this Widget.
	 * 
	 */
	TrashDatapointDropWidget trashDPWidget = null;

	/**
	 * Create a new CollectDatapointDropWidget.
	 */
	public CollectDatapointDropWidget() {
		super();
		initCDDWidget(false);
	}

	/**
	 * Create a new CollectDatapointDropWidget.
	 * 
	 * @param dropWidgetStyleNames
	 *            The drop widget style names for positioning the DropWidget.
	 * @param dropWidgetContentStyleNames
	 *            The drop widget content style names for positioning it's
	 *            children - the DragWidgets.
	 */
	public CollectDatapointDropWidget(String dropWidgetStyleNames,
			String dropWidgetContentStyleNames) {
		super(dropWidgetStyleNames, dropWidgetContentStyleNames);
		initCDDWidget(false);
	}

	/**
	 * Create a new CollectDatapointDropWidget.
	 * 
	 * @param dropWidgetStyleNames
	 *            The drop widget style names for positioning the DropWidget.
	 * @param dropWidgetContentStyleNames
	 *            The drop widget content style names for positioning it's
	 *            children - the DragWidgets.
	 */
	public CollectDatapointDropWidget(String[] dropWidgetStyleNames,
			String[] dropWidgetContentStyleNames) {
		super(dropWidgetStyleNames, dropWidgetContentStyleNames);
		initCDDWidget(false);
	}

	/**
	 * Create a new CollectDatapointDropWidget.
	 * 
	 * @param trash
	 *            If true, a {@link TrashDatapointDropWidget} will be created
	 *            and bound to this widget.
	 */
	public CollectDatapointDropWidget(boolean trash) {
		super();
		initCDDWidget(trash);
	}

	/**
	 * Create a new CollectDatapointDropWidget.
	 * 
	 * @param dropWidgetStyleNames
	 *            The drop widget style names for positioning the DropWidget.
	 * @param dropWidgetContentStyleNames
	 *            The drop widget content style names for positioning it's
	 *            children - the DragWidgets.
	 * @param trash
	 *            If true, a {@link TrashDatapointDropWidget} will be created
	 *            and bound to this widget.
	 */
	public CollectDatapointDropWidget(String dropWidgetStyleNames,
			String dropWidgetContentStyleNames, boolean trash) {
		super(dropWidgetStyleNames, dropWidgetContentStyleNames);
		initCDDWidget(trash);
	}

	/**
	 * Create a new CollectDatapointDropWidget.
	 * 
	 * @param dropWidgetStyleNames
	 *            The drop widget style names for positioning the DropWidget.
	 * @param dropWidgetContentStyleNames
	 *            The drop widget content style names for positioning it's
	 *            children - the DragWidgets.
	 * @param trash
	 *            If true, a {@link TrashDatapointDropWidget} will be created
	 *            and bound to this widget.
	 */
	public CollectDatapointDropWidget(String[] dropWidgetStyleNames,
			String[] dropWidgetContentStyleNames, boolean trash) {
		super(dropWidgetStyleNames, dropWidgetContentStyleNames);
		initCDDWidget(trash);
	}

	/**
	 * Create a new CollectDatapointDropWidget.
	 * 
	 * @param trashWidget
	 *            A {@link TrashDatapointDropWidget} you want to bind to this
	 *            widget.
	 */
	public CollectDatapointDropWidget(TrashDatapointDropWidget trashWidget) {
		super();
		initCDDWidget(false);
		this.trashDPWidget = trashWidget;
	}

	/**
	 * Create a new CollectDatapointDropWidget.
	 * 
	 * @param dropWidgetStyleNames
	 *            The drop widget style names for positioning the DropWidget.
	 * @param dropWidgetContentStyleNames
	 *            The drop widget content style names for positioning it's
	 *            children - the DragWidgets.
	 * @param trashWidget
	 *            A {@link TrashDatapointDropWidget} you want to bind to this
	 *            widget.
	 */
	public CollectDatapointDropWidget(String dropWidgetStyleNames,
			String dropWidgetContentStyleNames,
			TrashDatapointDropWidget trashWidget) {
		super(dropWidgetStyleNames, dropWidgetContentStyleNames);
		initCDDWidget(false);
		this.trashDPWidget = trashWidget;
	}

	/**
	 * Create a new CollectDatapointDropWidget.
	 * 
	 * @param dropWidgetStyleNames
	 *            The drop widget style names for positioning the DropWidget.
	 * @param dropWidgetContentStyleNames
	 *            The drop widget content style names for positioning it's
	 *            children - the DragWidgets.
	 * @param trashWidget
	 *            A {@link TrashDatapointDropWidget} you want to bind to this
	 *            widget.
	 */
	public CollectDatapointDropWidget(String[] dropWidgetStyleNames,
			String[] dropWidgetContentStyleNames,
			TrashDatapointDropWidget trashWidget) {
		super(dropWidgetStyleNames, dropWidgetContentStyleNames);
		initCDDWidget(false);
		this.trashDPWidget = trashWidget;
	}

	/**
	 * Method to initialize this widget with all necessary parameters.
	 * 
	 * @param trash
	 *            If true, a {@link TrashDatapointDropWidget} will be created
	 *            and bound to this widget.
	 */
	private void initCDDWidget(boolean trash) {
		addHandler(new DpWidgetDropEventHandler() {

			@Override
			public void onDpWidgetDropEvent(DpWidgetDropEvent event) {
				if (event.getDraggable() instanceof DpWidget) {
					addDpWidget((DpWidget) event.getDraggable(), true);
				}
			}
		}, DpWidgetDropEvent.getTYPE());
		if (trash) {
			trashDPWidget = new TrashDatapointDropWidget(getStyleName(), null,
					this);
		}
	}

	/**
	 * Get the instance of this widget.
	 * 
	 * @return Returns the instance of this widget.
	 */
	public CollectDatapointDropWidget getThis() {
		return this;
	}

	/**
	 * Set the {@link TrashDatapointDropWidget} that should be bound to this
	 * widget.
	 * 
	 * @param trashWidget
	 *            The {@link TrashDatapointDropWidget} that should be bound to
	 *            this widget.
	 */
	public void setTrashWidget(TrashDatapointDropWidget trashWidget) {
		this.trashDPWidget = trashWidget;
	}

	/**
	 * Get the {@link TrashDatapointDropWidget} that is bound to this widget.
	 * 
	 * @return Returns the {@link TrashDatapointDropWidget} that is binded to
	 *         this widget.
	 */
	public TrashDatapointDropWidget getTrashWidget() {
		return trashDPWidget;
	}

	/**
	 * Get a list of all {@link DpWidget} they are in this widget.
	 * 
	 * @return Returns a list of all {@link DpWidget} they are in this widget.
	 */
	public ArrayList<DpWidget> getDpWidgets() {
		ArrayList<DpWidget> dplist = new ArrayList<DpWidget>();
		for (int i = 0; i < getThis().getDropPanel().getWidgetCount(); i++) {
			if (getThis().getDropPanel().getWidget(i) instanceof DpWidget) {
				dplist.add((DpWidget) getThis().getDropPanel().getWidget(i));
			}
		}
		return dplist;
	}

	/**
	 * Get a list of all data points in this widget as {@link DpDTO}.
	 * 
	 * @return Returns a list of all data points in this widget as {@link DpDTO}
	 *         .
	 */
	public ArrayList<DpDTO> getDatapoints() {
		ArrayList<DpDTO> dplist = new ArrayList<DpDTO>();
		for (int i = 0; i < getThis().getDropPanel().getWidgetCount(); i++) {
			if (getThis().getDropPanel().getWidget(i) instanceof DpWidget) {
				DpWidget temp = (DpWidget) getThis().getDropPanel()
						.getWidget(i);
				dplist.add(new DpDTO(temp.getName(), temp.getType(), temp
						.getDescription()));
			}
		}
		return dplist;
	}

	/**
	 * Add a {@link DpWidget} to this widget, unless it's already present.
	 * 
	 * @param dpwidget
	 *            The {@link DpWidget} you want to add.
	 * @param copy
	 *            If true, the {@link DpWidget} will be copied. If false, it
	 *            will be removed from it's source and added to this widget.
	 */
	public void addDpWidget(DpWidget dpwidget, boolean copy) {
		boolean alreadyPresent = false;
		for (int i = 0; i < getThis().getDropPanel().getWidgetCount(); i++) {
			if (getThis().getDropPanel().getWidget(i) instanceof DpWidget) {
				if (((DpWidget) getThis().getDropPanel().getWidget(i))
						.getName().contentEquals(dpwidget.getName())) {
					alreadyPresent = true;
				}
			}
		}
		if (alreadyPresent == false) {
			if (copy) {
				getThis().getDropPanel().add(
						new DpWidget(dpwidget.getName(), dpwidget.getType(),
								dpwidget.getDescription()));
			} else {
				getThis().getDropPanel().add(dpwidget);
			}
		}

	}

	/**
	 * Add a {@link DpWidget} to this widget, unless it's already present. This
	 * method uses a {@link DpDTO} as source.
	 * 
	 * @param dpdto
	 *            The {@link DpDTO} you want to add to this widget. It will be
	 *            converted to a {@link DpWidget}.
	 */
	public void addDpWidget(DpDTO dpdto) {
		boolean alreadyPresent = false;
		for (int i = 0; i < getThis().getDropPanel().getWidgetCount(); i++) {
			if (getThis().getDropPanel().getWidget(i) instanceof DpWidget) {
				if (((DpWidget) getThis().getDropPanel().getWidget(i))
						.getName().contentEquals(dpdto.getName())) {
					alreadyPresent = true;
				}
			}
		}
		if (alreadyPresent == false) {
			getThis().getDropPanel().add(
					new DpWidget(dpdto.getName(), dpdto.getType(), dpdto
							.getDescription()));
		}

	}

	/**
	 * Remove a {@link DpWidget} from this widget.
	 * 
	 * @param dpwidget
	 *            The {@link DpWidget} you want to remove from this widget.
	 * @return Returns the {@link DpWidget} that is removed.
	 */
	public DpWidget removeDpWidget(DpWidget dpwidget) {
		DpWidget temp = null;
		for (int i = 0; i < getThis().getDropPanel().getWidgetCount(); i++) {
			if (getThis().getDropPanel().getWidget(i) instanceof DpWidget) {
				if (((DpWidget) getThis().getDropPanel().getWidget(i))
						.getName().contentEquals(dpwidget.getName())) {
					temp = (DpWidget) getThis().getDropPanel().getWidget(i);
					getThis().getDropPanel().getWidget(i).removeFromParent();
				}
			}
		}
		return temp;
	}

	/**
	 * Remove a {@link DpWidget} from this widget with a {@link DpDTO} as
	 * source.
	 * 
	 * @param dpdto
	 *            The {@link DpDTO} that is used as source to remove a
	 *            {@link DpWidget} from this widget.
	 * @return Returns the {@link DpWidget} that is removed.
	 */
	public DpWidget removeDpWidget(DpDTO dpdto) {
		DpWidget temp = null;
		for (int i = 0; i < getThis().getDropPanel().getWidgetCount(); i++) {
			if (getThis().getDropPanel().getWidget(i) instanceof DpWidget) {
				if (((DpWidget) getThis().getDropPanel().getWidget(i))
						.getName().contentEquals(dpdto.getName())) {
					temp = (DpWidget) getThis().getDropPanel().getWidget(i);
					getThis().getDropPanel().getWidget(i).removeFromParent();
				}
			}
		}
		return temp;
	}

	/**
	 * Remove a {@link DpWidget} from this widget.
	 * 
	 * @param name
	 *            The name of the data point as source to remove a
	 *            {@link DpWidget} from this widget.
	 * @return Returns the {@link DpWidget} that is removed.
	 */
	public DpWidget removeDpWidget(String name) {
		DpWidget temp = null;
		for (int i = 0; i < getThis().getDropPanel().getWidgetCount(); i++) {
			if (getThis().getDropPanel().getWidget(i) instanceof DpWidget) {
				if (((DpWidget) getThis().getDropPanel().getWidget(i))
						.getName().contentEquals(name)) {
					temp = (DpWidget) getThis().getDropPanel().getWidget(i);
					getThis().getDropPanel().getWidget(i).removeFromParent();
				}
			}
		}
		return temp;
	}

	/**
	 * Remove all the {@link DpWidget} from this widget and returns them as a
	 * list.
	 * 
	 * @return Returns all {@link DpWidget} they are removed from this widget as
	 *         a list.
	 */
	public ArrayList<DpWidget> removeAllDpWidgets() {
		ArrayList<DpWidget> dplist = new ArrayList<DpWidget>();
		for (int i = 0; i < getThis().getDropPanel().getWidgetCount(); i++) {
			if (getThis().getDropPanel().getWidget(i) instanceof DpWidget) {
				dplist.add((DpWidget) getThis().getDropPanel().getWidget(i));
			}
		}
		getThis().getDropPanel().clear();
		return dplist;
	}

	/**
	 * Lookup if a {@link DpWidget} is already present.
	 * 
	 * @param dpwidget
	 *            The {@link DpWidget} you want to lookup.
	 * @return Returns true if the {@link DpWidget} is already present, false
	 *         otherwise.
	 */
	public boolean lookupDpWidget(DpWidget dpwidget) {
		for (int i = 0; i < getThis().getDropPanel().getWidgetCount(); i++) {
			if (getThis().getDropPanel().getWidget(i) instanceof DpWidget) {
				if (((DpWidget) getThis().getDropPanel().getWidget(i))
						.getName().contentEquals(dpwidget.getName())) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Lookup if a {@link DpWidget} is already present.
	 * 
	 * @param dpdto
	 *            The {@link DpDTO} used as source for the lookup.
	 * @return Returns true if the {@link DpWidget} is already present, false
	 *         otherwise.
	 */
	public boolean lookupDpWidget(DpDTO dpdto) {
		for (int i = 0; i < getThis().getDropPanel().getWidgetCount(); i++) {
			if (getThis().getDropPanel().getWidget(i) instanceof DpWidget) {
				if (((DpWidget) getThis().getDropPanel().getWidget(i))
						.getName().contentEquals(dpdto.getName())) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Lookup if a {@link DpWidget} is already present.
	 * 
	 * @param name
	 *            The name of the data point that is used as source for the
	 *            lookup.
	 * @return Returns true if the {@link DpWidget} is already present, false
	 *         otherwise.
	 */
	public boolean lookupDpWidget(String name) {
		for (int i = 0; i < getThis().getDropPanel().getWidgetCount(); i++) {
			if (getThis().getDropPanel().getWidget(i) instanceof DpWidget) {
				if (((DpWidget) getThis().getDropPanel().getWidget(i))
						.getName().contentEquals(name)) {
					return true;
				}
			}
		}
		return false;
	}
}
