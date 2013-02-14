package bpi.most.server.services.opcua.server;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import bpi.most.dto.DpDTO;
import bpi.most.dto.DpDataDTO;
import bpi.most.dto.UserDTO;
import bpi.most.dto.ZoneDTO;
import bpi.most.opcua.server.annotation.IAnnotatedNodeSource;
import bpi.most.opcua.server.core.RequestContext;
import bpi.most.opcua.server.core.Session;
import bpi.most.server.services.opcua.server.nodes.DpDataNode;
import bpi.most.server.services.opcua.server.nodes.DpNode;
import bpi.most.server.services.opcua.server.nodes.ZoneNode;
import bpi.most.service.api.DatapointService;
import bpi.most.service.api.ZoneService;

/**
 * manages all nodes of the most-namespace which includes
 * zones and datapoints from the central mysql database.
 * 
 * @author harald
 *
 */
public class MostNodeManager implements IAnnotatedNodeSource{

	@Inject
	private ZoneService zService;

	@Inject
    private DatapointService dpService;

	public MostNodeManager(){
	}
	
    /**
	 * @param zService
	 * @param dpService
	 */
	public MostNodeManager(ZoneService zService, DatapointService dpService) {
		this.zService = zService;
		this.dpService = dpService;
	}

	/**
	 * reads the most user who authenticated himself from the {@link Session}.
	 * @return
	 */
	private UserDTO getUser(){
		Session s = RequestContext.get().getSession();
		return (UserDTO) s.getCustomObj();
	}
	
	@Override
	public Object getObjectById(Class<?> clazz, String id){
		Object result = null;
		
		//distinguish which class we want to fetch by id
		if (ZoneNode.class.equals(clazz)){
			result = getZoneById(id);
		}else if (DpNode.class.equals(clazz)){
			result = getDpById(id);
		}
		
		return result;
	}
	
	private ZoneNode getZoneById(String id){
		int zoneId = Integer.parseInt(id);
		ZoneDTO zone = zService.getZone(getUser(), new ZoneDTO(zoneId));
		return mapZoneToZoneNode(zone);
	}
	
	private DpNode getDpById(String dpName){
		DpDTO dp = dpService.getDatapoint(getUser(), new DpDTO(dpName));
		DpDataDTO dpData = dpService.getData(getUser(), dp);
		
		/*
		 * here we also have to fetch the last datapoint value because
		 * the current value of the datapoint is annotated as variable
		 * on the DpNode object. 
		 */
		DpNode dpNode = mapDpToDpNode(dp);
		dpNode.setData(mapDpDataToDpDataNode(dpData));
		
		//TODO remove mock data. used it because i do not understand the database and why do some points do not have any valuesS
//		dpNode.setData(new DpDataNode("temperature", new Date(), 23.33, 1f));
		
		return dpNode ;
	}
	
	@Override
	public List<?> getTopLevelElements() {
		List<ZoneDTO> headZones = zService.getHeadZones(getUser());
		return mapZoneToZoneNode(headZones);
	}
	
	
	@Override
	public List<?> getChildren(Class<?> parentClazz, String parentId) {
		List<Object> result = new ArrayList<Object>();
		
		//distinguish which class we want to fetch children from
		if (ZoneNode.class.equals(parentClazz)){
			int parentZoneId = Integer.parseInt(parentId);
			ZoneDTO parentZone = new ZoneDTO(parentZoneId);
			//zones have subzones and datapoints as children
			result.addAll(getSubZones(parentZone));
			result.addAll(getDatapoints(parentZone));
		}else if (DpNode.class.equals(parentClazz)){
			
		}
		
		return result;
	}
	
	private List<?> getSubZones(ZoneDTO parentZone){
		List<ZoneDTO> subZones = zService.getSubzones(getUser(), parentZone, 1);
		return mapZoneToZoneNode(subZones);
	}
	
	private List<?> getDatapoints(ZoneDTO parentZone){
		List<DpDTO> datapoints = zService.getDatapoints(getUser(), parentZone, 1);
		return mapDpToDpNode(datapoints);
	}
	
	private List<DpNode> mapDpToDpNode(List<DpDTO> dpList){
		List<DpNode> dpNodeList = new ArrayList<DpNode>();
		
		if (dpList != null){
			for (DpDTO d: dpList){
				dpNodeList.add(mapDpToDpNode(d));
			}
		}
		
		return dpNodeList;
	}
	
	private DpNode mapDpToDpNode(DpDTO dp){
		DpNode dpNode = null;
		
		if (dp != null){
			dpNode = new DpNode();
			dpNode.setName(dp.getName());
			dpNode.setDescription(dp.getDescription());
			dpNode.setType(dp.getType());
		}
		
		return dpNode;
	}
	
	private DpDataNode mapDpDataToDpDataNode(DpDataDTO dpData){
		DpDataNode dpDataNode = null;
		
		if(dpData != null){
			dpDataNode = new DpDataNode("current value", dpData.getTimestamp(), dpData.getValue(), dpData.getQuality());
		}
		
		return dpDataNode;
	}
	
	private List<ZoneNode> mapZoneToZoneNode(List<ZoneDTO> zoneList){
		List<ZoneNode> zoneNodeList = new ArrayList<ZoneNode>();
		
		if (zoneList != null){
			for (ZoneDTO z: zoneList){
				zoneNodeList.add(mapZoneToZoneNode(z));
			}
		}
		
		return zoneNodeList;
	}
	
	private ZoneNode mapZoneToZoneNode(ZoneDTO zone){
		ZoneNode zoneNode = null;
		
		if (zone != null){
			zoneNode = new ZoneNode();
			zoneNode.setZoneID(zone.getZoneId());
			zoneNode.setName(zone.getName());
			zoneNode.setDescription(zone.getDescription());
			zoneNode.setCountry(zone.getCountry());
			zoneNode.setState(zone.getState());
			zoneNode.setCounty(zone.getCounty());
			zoneNode.setCity(zone.getCity());
			zoneNode.setBuilding(zone.getBuilding());
			zoneNode.setFloor(zone.getFloor());
			zoneNode.setRoom(zone.getRoom());
			zoneNode.setVolume(zone.getVolume());
			zoneNode.setArea(zone.getArea());
		}
		
		return zoneNode;
	}

}
