package bpi.most.client.rpc;

import java.util.List;

import bpi.most.shared.DpDTO;
import bpi.most.shared.ZoneDTO;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("services/gwtrpc/zone")
public interface ZoneService extends RemoteService {
	
	public List<ZoneDTO> getHeadZones();
	
	public List<ZoneDTO> getSubzones(ZoneDTO zoneEntity, int sublevels);
	
	public ZoneDTO getZone(ZoneDTO zoneDto);
	
	public List<DpDTO> getDatapoints(ZoneDTO zoneEntity, int sublevels);

}
