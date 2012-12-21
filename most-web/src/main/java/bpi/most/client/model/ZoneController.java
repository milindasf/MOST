package bpi.most.client.model;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

import bpi.most.client.rpc.ZoneService;
import bpi.most.client.rpc.ZoneServiceAsync;
import bpi.most.shared.DpDTO;
import bpi.most.shared.ZoneDTO;

/**
 * Client-side implementation of the
 * {@link bpi.most.server.services.ZoneService}
 * 
 * @author mike
 * 
 */
public final class ZoneController {

	private static ZoneController ref = null;

	public static final ZoneServiceAsync zoneService = GWT
			.create(ZoneService.class);

	// Singleton
	private ZoneController() {
		super();
	}

	public static ZoneController getInstance() {
		if (ref == null) {
			ref = new ZoneController();
		}
		return ref;
	}

	public List<ZoneDTO> getHeadZones(final ZoneHandler zoneHandler) {
		zoneService.getHeadZones(new AsyncCallback<List<ZoneDTO>>() {

			@Override
			public void onSuccess(List<ZoneDTO> result) {
				zoneHandler.onSuccess(result);
			}

			@Override
			public void onFailure(Throwable caught) {
				// TODO: implement better error handling
				Window.alert("getHeadZones() RPC-Error");
			}
		});
		return null;
	}

	public List<ZoneDTO> getSubzones(ZoneDTO zoneEntity, int sublevels,
			final ZoneHandler zoneHandler) {
		zoneService.getSubzones(zoneEntity, sublevels,
				new AsyncCallback<List<ZoneDTO>>() {

					@Override
					public void onSuccess(List<ZoneDTO> result) {
						zoneHandler.onSuccess(result);
					}

					@Override
					public void onFailure(Throwable caught) {
						// TODO: implement better error handling
						Window.alert("getSubzones(ZoneDTO zoneEntity, int sublevels) RPC-Error");
					}
				});
		return null;
	}

	public ZoneDTO getZone(ZoneDTO zoneDto, final ZoneHandler zoneHandler) {
		zoneService.getZone(zoneDto, new AsyncCallback<ZoneDTO>() {

			@Override
			public void onSuccess(ZoneDTO result) {
				zoneHandler.onSuccess(result);
			}

			@Override
			public void onFailure(Throwable caught) {
				// TODO: implement better error handling
				Window.alert("getZone(ZoneDTO zoneDto) RPC-Error");
			}
		});
		return null;
	}

	public List<DpDTO> getDatapoints(ZoneDTO zoneEntity, int sublevels,
			final ZoneHandler zoneHandler) {
		zoneService.getDatapoints(zoneEntity, sublevels,
				new AsyncCallback<List<DpDTO>>() {

					@Override
					public void onSuccess(List<DpDTO> result) {
						zoneHandler.onSuccess(result);
					}

					@Override
					public void onFailure(Throwable caught) {
						// TODO: implement better error handling
						Window.alert("getDatapoints(ZoneDTO zoneEntity, int sublevels) RPC-Error");
					}
				});
		return null;
	}

}
