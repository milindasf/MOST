package bpi.most.client.rpc;

import bpi.most.dto.DpDTO;
import bpi.most.dto.ZoneDTO;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import java.util.List;

@RemoteServiceRelativePath("services/gwtrpc/zone")
public interface ZoneService extends RemoteService {
	
	List<ZoneDTO> getHeadZones();
	
	List<ZoneDTO> getSubzones(ZoneDTO zoneEntity, int sublevels);
	
	ZoneDTO getZone(ZoneDTO zoneDto);
	
	List<DpDTO> getDatapoints(ZoneDTO zoneEntity, int sublevels);
	
	//TODO use right return type
	//options
	//A)get URL
	//B)get all JSON objects 
	//C)get BIM login info
	//D)all communication with "gwt" --> server side  connects to BIMserver 
	public String getBimModel();

}
