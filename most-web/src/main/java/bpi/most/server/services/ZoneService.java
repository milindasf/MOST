package bpi.most.server.services;

import java.util.ArrayList;
import java.util.List;

import bpi.most.server.model.Datapoint;
import bpi.most.server.model.Zone;
import bpi.most.server.model.ZoneController;
import bpi.most.dto.DpDTO;
import bpi.most.dto.ZoneDTO;


/**
 * Common Service interface. Handles permissions, etc. Everything returned here is serializable!
 * Different implementations (GWT-RPC, OPC UA, SOAP, etc.) should be based on this implementations
 * @author robert.zach@tuwien.ac.at
 */
public final class ZoneService {
	
	ZoneController zoneCtrl = ZoneController.getInstance();
	private static ZoneService ref = null;
	//Singleton
	private ZoneService() {
		super();
	}
	public static ZoneService getInstance(){
		if (ref == null) {
			ref = new ZoneService();
		}
		return ref;
	}
	
	
	/**
	 * see {@link bpi.most.server.model.ZoneController#getHeadZones(User user)}
	 * @param user
	 * @return A List of all highest Zones were the user still has any permissions (Zone with no parents were the user has any permissions). 
	 */
	public List<ZoneDTO> getHeadZones(User user) {
		List<ZoneDTO> result = new ArrayList<ZoneDTO>();
		
		List<Zone> headZones = zoneCtrl.getHeadZones(user); 
		//convert to DTOs
		for (Zone iterateZone : headZones) {
			result.add(iterateZone.getZoneDTO());
		}
		
		return result;
	}
	
	/**
	 * see {@link bpi.most.server.model.Zone#getSubzones(int sublevels)}
	 * adds permission check
	 * @return A List of ZoneDTOs of requested subzones
	 */
	public List<ZoneDTO> getSubzones(User user, ZoneDTO zoneEntity, int sublevels) {
		List<ZoneDTO> result = new ArrayList<ZoneDTO>();
		Zone requestedZone = zoneCtrl.getZone(zoneEntity);
		//TODO move permission definition
		user.hasPermission(requestedZone, DpDTO.Permissions.READ);
		
		List<Zone> subzones = requestedZone.getSubzones(sublevels); 
		//convert to DTOs
		for (Zone iterateZone : subzones) {
			result.add(iterateZone.getZoneDTO());
		}
		return result;
	}
	
	/**
	 * returns a single {@link ZoneDTO} which is identified by the given
	 * {@link ZoneDTO#getName()}. this is used so that clients can fetch a
	 * fully filled DpDTO object by only having the datapoints name.
	 * @param user
	 * @param zoneEntity
	 * @return
	 */
	public ZoneDTO getZone(User user, ZoneDTO zoneDto) {
		ZoneDTO result = null;
		Zone requestedZone = zoneCtrl.getZone(zoneDto.getZoneId());
		//TODO move permission definition
		if (requestedZone != null){
			user.hasPermission(requestedZone, DpDTO.Permissions.READ);
			result = requestedZone.getZoneDTO();
		}
		
		return result;
	}
	
	/**
	 * see {@link bpi.most.server.model.Zone#getDatapoints(int sublevel)}
	 * TODO implement
	 */
	public List<DpDTO> getDatapoints(User user, ZoneDTO zoneEntity, int sublevels) {
		List<DpDTO> result = new ArrayList<DpDTO>();
		Zone requestedZone = zoneCtrl.getZone(zoneEntity);
		//TODO move permission definition
		user.hasPermission(requestedZone, DpDTO.Permissions.READ);
		
		List<Datapoint> datapoints = requestedZone.getDatapoints(sublevels); 
		//convert to DTOs
		for (Datapoint iterateDp : datapoints) {
			result.add(iterateDp.getDpDTO());
		}
		return result;
	}


}
