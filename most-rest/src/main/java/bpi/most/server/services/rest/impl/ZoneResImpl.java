package bpi.most.server.services.rest.impl;

import bpi.most.dto.DpDTO;
import bpi.most.dto.ZoneDTO;
import bpi.most.server.services.rest.api.ZoneResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response.Status;
import java.util.List;

public class ZoneResImpl extends BaseResImpl implements ZoneResource {

	private static final Logger LOG = LoggerFactory.getLogger(ZoneResImpl.class);

    // TODO ASE  ZoneService service;
	
	public ZoneResImpl(){
        // TODO ASE  service = ZoneService.getInstance();
		LOG.info("-->> created zone resource impl");
	}
	
	@Override
	public ZoneDTO getZone(int id) {
		ZoneDTO zone = null; // TODO ASE  service.getZone(getUser(), new ZoneDTO(id));

		if (zone == null){
			throw new WebApplicationException(Status.NOT_FOUND);
		}
		
		return zone;
	}

	@Override
	public List<ZoneDTO> getSubZones(int id, int level) {
		List<ZoneDTO> subZones = null; // TODO ASE  service.getSubzones(getUser(), new ZoneDTO(id), level);
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
		return null; // TODO ASE  service.getDatapoints(getUser(), new ZoneDTO(id), level);
	}

	@Override
	public List<ZoneDTO> getHeadZones() {
		return null;  // TODO ASE  service.getHeadZones(getUser());
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
