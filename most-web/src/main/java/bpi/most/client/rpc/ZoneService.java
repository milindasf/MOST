package bpi.most.client.rpc;

import java.util.List;

import bpi.most.shared.DpDTO;
import bpi.most.shared.ZoneDTO;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("services/gwtrpc/zone")
public interface ZoneService extends RemoteService {
	
	List<ZoneDTO> getHeadZones();
	
	List<ZoneDTO> getSubzones(ZoneDTO zoneEntity, int sublevels);
	
	ZoneDTO getZone(ZoneDTO zoneDto);
	
	List<DpDTO> getDatapoints(ZoneDTO zoneEntity, int sublevels);

}
