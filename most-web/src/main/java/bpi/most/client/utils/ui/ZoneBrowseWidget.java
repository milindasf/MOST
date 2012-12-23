package bpi.most.client.utils.ui;

import java.util.List;

import bpi.most.client.model.ZoneHandler;
import bpi.most.client.modules.ModuleController;
import bpi.most.shared.DpDTO;
import bpi.most.shared.ZoneDTO;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellBrowser;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.gwt.view.client.TreeViewModel;

public class ZoneBrowseWidget extends Composite {

	private static BrowseEntitiesWidgetUiBinder uiBinder = GWT
			.create(BrowseEntitiesWidgetUiBinder.class);

	interface BrowseEntitiesWidgetUiBinder extends
			UiBinder<Widget, ZoneBrowseWidget> {
	}

	@UiField(provided = true)
	CellBrowser cellBrowser;

	@UiField
	FlowPanel contentArea;
	
	static SingleSelectionModel<ZoneDTO> mySelectionModel = new SingleSelectionModel<ZoneDTO>();

	public ZoneBrowseWidget() {
		TreeViewModel model = new CustomTreeModel();
		cellBrowser = new CellBrowser(model, null);
		initWidget(uiBinder.createAndBindUi(this));
		DOM.setStyleAttribute(contentArea.getElement(), "overflow", "auto");
		mySelectionModel
		.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {

			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				ModuleController.ZONE_CTRL.getDatapoints(
						mySelectionModel.getSelectedObject(),
						1,
						new ZoneHandler(mySelectionModel
								.getSelectedObject()) {

							@Override
							public void onSuccess(List<?> result) {
								contentArea.clear();
								for(int i = 0;i<result.size();i++){
									DpWidget sl;
									sl = new DpWidget(((DpDTO)result.get(i)).getName(), ((DpDTO)result.get(i)).getType());
									contentArea.add(sl);
								}
							}
						});
			}
		});
	}

	private static class HeadDataProvider extends AsyncDataProvider<ZoneDTO> {

		ZoneDTO zone = null;

		public HeadDataProvider() {
			zone = null;
		}

		@Override
		protected void onRangeChanged(HasData<ZoneDTO> display) {
			updateRowCount(0, true);
			ModuleController.ZONE_CTRL.getHeadZones(new ZoneHandler(zone) {

				@SuppressWarnings("unchecked")
				@Override
				public void onSuccess(List<?> result) {
					updateRowData(0, (List<ZoneDTO>) result);
					updateRowCount(result.size(), true);
				}
			});
		}
	}

	private static class SubzoneDataProvider extends AsyncDataProvider<ZoneDTO> {

		ZoneDTO zone = null;

		public SubzoneDataProvider(ZoneDTO zone) {
			this.zone = zone;
		}

		@Override
		protected void onRangeChanged(HasData<ZoneDTO> display) {
			updateRowCount(0, true);
			ModuleController.ZONE_CTRL.getSubzones(zone, 1,
					new ZoneHandler(zone) {

						@SuppressWarnings("unchecked")
						@Override
						public void onSuccess(List<?> result) {
							updateRowData(0, (List<ZoneDTO>) result);
							updateRowCount(result.size(), true);
						}
					});
		}
	}

	private static final class CustomTreeModel implements TreeViewModel {

		private CustomTreeModel() {
			
		}

		// Get the NodeInfo that provides the children of the specified value.
		public <T> NodeInfo<?> getNodeInfo(T value) {

			Cell<ZoneDTO> myCell = new AbstractCell<ZoneDTO>() {

				@Override
				public void render(
						com.google.gwt.cell.client.Cell.Context context,
						ZoneDTO value, SafeHtmlBuilder sb) {
					sb.appendEscaped(value.getName());
				}
			};

			if (value == null) {
				return new DefaultNodeInfo<ZoneDTO>(new HeadDataProvider(),
						myCell, mySelectionModel, null);
			} else if (value instanceof ZoneDTO) {
				return new DefaultNodeInfo<ZoneDTO>(new SubzoneDataProvider(
						((ZoneDTO) value)), myCell, mySelectionModel, null);
			} else {
				Window.alert("Error with CellBrowser!");
			}
			return null;

		}

		// Check if the specified value represents a leaf node. Leaf nodes
		// cannot be opened.
		public boolean isLeaf(Object value) {
			//TODO find a way to figure out if zone has subzone or not
			return false;
		}
	}
	
	public FlowPanel getContentArea(){
		FlowPanel temp = contentArea;
		contentArea.removeFromParent();
		return temp;
	}

}
