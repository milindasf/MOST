package bpi.most.client.rpc;

import java.util.List;

import bpi.most.shared.DpDTO;
import bpi.most.shared.ZoneDTO;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ZoneServiceAsync {

	void getHeadZones(AsyncCallback<List<ZoneDTO>> callback);

	void getSubzones(ZoneDTO zoneEntity, int sublevels,
			AsyncCallback<List<ZoneDTO>> callback);

	void getZone(ZoneDTO zoneDto, AsyncCallback<ZoneDTO> callback);

	void getDatapoints(ZoneDTO zoneEntity, int sublevels,
			AsyncCallback<List<DpDTO>> callback);

}
