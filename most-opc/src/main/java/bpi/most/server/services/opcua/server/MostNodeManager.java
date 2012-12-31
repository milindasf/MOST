package bpi.most.server.services.opcua.server;

import bpi.most.dto.DpDTO;
import bpi.most.dto.DpDataDTO;
import bpi.most.dto.ZoneDTO;
import bpi.most.opc.uaserver.annotation.IAnnotatedNodeSource;
//TODO import bpi.most.server.services.DatapointService;
//TODO import bpi.most.server.services.User;
//TODO import bpi.most.server.services.ZoneService;
import bpi.most.server.services.opcua.server.nodes.DpDataNode;
import bpi.most.server.services.opcua.server.nodes.DpNode;
import bpi.most.server.services.opcua.server.nodes.ZoneNode;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * manages all nodes of the most-namespace which includes
 * zones and datapoints from the central mysql database.
 * 
 * @author harald
 *
 */
public class MostNodeManager implements IAnnotatedNodeSource{

	
	private final String ZONE_NODE = ZoneNode.class.getSimpleName();
	private final String DP_NODE = DpNode.class.getSimpleName();
	
	//TODO ASE private ZoneService zService = ZoneService.getInstance();
    //TODO ASE private DatapointService dpService = DatapointService.getInstance();

    //TODO ASE private User mostUser;
	
	public MostNodeManager(String mostUserName){
        //TODO ASE mostUser = new User(mostUserName);
	}
	
	@Override
	public Object getObjectById(String className, String id){
		Object result = null;
		
		//distinguish which class we want to fetch by id
		if (className != null){
			if (ZONE_NODE.equals(className)){
				result = getZoneById(id);
			}else if (DP_NODE.equals(className)){
				result = getDpById(id);
			}
		}
		
		return result;
	}
	
	private ZoneNode getZoneById(String id){
		int zoneId = Integer.parseInt(id);
        //TODO ASE ZoneDTO zone = zService.getZone(mostUser, new ZoneDTO(zoneId));
        //TODO ASE return mapZoneToZoneNode(zone);
        return null; //TODO remove

    }
	
	private DpNode getDpById(String dpName){
        //TODO ASE DpDTO dp = dpService.getDatapoint(mostUser, new DpDTO(dpName));
        //TODO ASE DpDataDTO dpData = dpService.getData(mostUser, dp);
		
		/*
		 * here we also have to fetch the last datapoint value because
		 * the current value of the datapoint is annotated as variable
		 * on the DpNode object. 
		 */
        //TODO ASE DpNode dpNode = mapDpToDpNode(dp);
        //TODO ASE dpNode.setData(mapDpDataToDpDataNode(dpData));
		
		//TODO remove mock data. used it because i do not understand the database and why do some points do not have any valuesS
        //TODO ASE dpNode.setData(new DpDataNode("temperature", new Date(), 23.33, 1f));

        //TODO ASE return dpNode ;
        return null; //TODO remove

    }
	
	@Override
	public List<?> getTopLevelElements() {
        //TODO ASE List<ZoneDTO> headZones = zService.getHeadZones(mostUser);
        //TODO ASE return mapZoneToZoneNode(headZones);
        return null; //TODO remove
	}
	
	
	@Override
	public List<?> getChildren(String className, String parentId) {
		List<Object> result = new ArrayList<Object>();
		
		//distinguish which class we want to fetch children from
		if (className != null && ZONE_NODE.equals(className)){
			int parentZoneId = Integer.parseInt(parentId);
			ZoneDTO parentZone = new ZoneDTO(parentZoneId);
			//zones have subzones and datapoints as children
			result.addAll(getSubZones(parentZone));
			result.addAll(getDatapoints(parentZone));
		}
		
		return result;
	}
	
	private List<?> getSubZones(ZoneDTO parentZone){
        //TODO ASE List<ZoneDTO> subZones = zService.getSubzones(mostUser, parentZone, 1);
        //TODO ASE return mapZoneToZoneNode(subZones);
        return null; //TODO remove

    }
	
	private List<?> getDatapoints(ZoneDTO parentZone){
        //TODO ASE List<DpDTO> datapoints = zService.getDatapoints(mostUser, parentZone, 1);
        //TODO ASE return mapDpToDpNode(datapoints);
        return null; //TODO remove

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
