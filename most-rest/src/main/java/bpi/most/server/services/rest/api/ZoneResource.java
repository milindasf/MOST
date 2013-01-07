package bpi.most.server.services.rest.api;

import bpi.most.dto.DpDTO;
import bpi.most.dto.ZoneDTO;

import javax.ws.rs.*;
import java.util.List;

/**
 * further information:
 * <ul>
 * <li>Oreilly.RESTful.Web.Services.Cookbook.Mar.2010
 * <ul>
 * <li>Chapter 2 - designing restful services</li>
 * <li>Chapter 8 - queries</li>
 * </ul>
 * </li>
 * <li>Oreilly.RESTful.Java.with.JAX.RS.Nov.2009
 * <ul>
 * <li>chapter 3 - Your First JAX-RS Service</li>
 * </ul>
 * </li>
 * <ul>
 * 
 * @author harald
 * 
 */
//@Path("/zones/")
@Produces("application/xml")
@Consumes("application/xml")
public interface ZoneResource {

	/**
	 * returns the zone identified by the given id
	 * 
	 * @param id
	 * @return
	 */
	@GET
	@Path("/{id}/")
	ZoneDTO getZone(@PathParam("id") int id);

	/**
	 * returns all headzones. so this resource acts as a starting point for all information
	 * @param level
	 * @return
	 */
	@GET
//	@Path("/")
	List<ZoneDTO> getHeadZones();
	
	/**
	 * creates the given zone as new subzone of the identified one
	 * 
	 * @param zone
	 */
	@POST
	@Path("/{id}/subzones/")
	void addZone(@PathParam("id") int id, ZoneDTO zone);
	
	/**
	 * returns all subzones for the given zone id if there exists some.
	 * otherwhise an empty list is returned.
	 * 
	 * @param id
	 * @param level
	 * @return
	 */
	@GET
	@Path("/{id}/subzones/")
	List<ZoneDTO> getSubZones(@PathParam("id") int id, @QueryParam("level") int level);

	/**
	 * creates the given zone as new headzone
	 * 
	 * @param zone
	 */
	@POST
	// @Path("/")
	void addHeadZone(ZoneDTO zone);

	/**
	 * updates the zone identified by the given id with the values of the given
	 * zone
	 * 
	 * @param id
	 * @param zone
	 */
	@PUT
	@Path("/{id}/")
	void updateZone(@PathParam("id") int id, ZoneDTO zone);

	/**
	 * deletes the zone identified by the given id
	 * 
	 * @param id
	 */
	@DELETE
	@Path("/{id}/")
	void deleteZone(@PathParam("id") int id);

	/**
	 * returns all datapoints for the given zone
	 * @param id
	 * @param level
	 * @return
	 */
	@GET
	@Path("/{id}/dps/")
	List<DpDTO> getDatapoints(@PathParam("id") int id, @QueryParam("level") int level);
}
