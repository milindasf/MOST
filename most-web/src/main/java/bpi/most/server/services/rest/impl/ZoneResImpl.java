package bpi.most.server.services.rest.impl;

import java.util.List;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bpi.most.server.services.ZoneService;
import bpi.most.server.services.rest.api.ZoneResource;
import bpi.most.shared.DpDTO;
import bpi.most.shared.ZoneDTO;

public class ZoneResImpl extends BaseResImpl implements ZoneResource {

	private static final Logger LOG = LoggerFactory.getLogger(ZoneResImpl.class);
	
	ZoneService service;
	
	public ZoneResImpl(){
		service = ZoneService.getInstance();
		LOG.info("-->> created zone resource impl");
	}
	
	@Override
	public ZoneDTO getZone(int id) {
		ZoneDTO zone = service.getZone(getUser(), new ZoneDTO(id));
		
		if (zone == null){
			throw new WebApplicationException(Status.NOT_FOUND);
		}
		
		return zone;
	}

	@Override
	public List<ZoneDTO> getSubZones(int id, int level) {
		List<ZoneDTO> subZones = service.getSubzones(getUser(), new ZoneDTO(id), level);
		return subZones;
	}

	@Override
	public void updateZone(int id, ZoneDTO zone) {
		LOG.warn("should update zone " + id + "\n" + zone.toString() + "\n but is not implemented yet");
		throw new WebApplicationException(NOT_IMPLEMENTED);
	}

	@Override
	public void deleteZone(int id) {
		LOG.warn("should delete zone " + id + "\n but is not implemented yet");
	}

	@Override
	public List<DpDTO> getDatapoints(int id, int level) {
		return service.getDatapoints(getUser(), new ZoneDTO(id), level);
	}

	@Override
	public List<ZoneDTO> getHeadZones() {
		return service.getHeadZones(getUser());
	}

	@Override
	@POST
	@Path("/{id}/subzones/")
	public void addZone(@PathParam("id") int id, ZoneDTO zone) {
		// TODO Auto-generated method stub
		LOG.warn("should add new subzone for zone " + id + "\n" + zone.toString() + "\n but is not implemented yet");
	}

	@Override
	@POST
	public void addHeadZone(ZoneDTO zone) {
		LOG.warn("should add new headzone\n" + zone.toString() + "\n but is not implemented yet");
	}
}
