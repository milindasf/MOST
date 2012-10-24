package bpi.most.server.services.rest.api;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import bpi.most.shared.DpDTO;
import bpi.most.shared.DpDataDTO;

//@Path("/dp/")
@Produces("application/xml")
@Consumes("application/xml")
public interface DpResource {

	/**
	 * returns the datapoint with the given name
	 * @param dpName
	 * @return
	 */
	@GET
	@Path("/{name}/")
	public DpDTO getDp(@PathParam("name") String dpName);
	
	@POST
	@Path("/")
	public void addDp(DpDTO dp);
	
	/**
	 * updates the datapoint
	 * @param dpName
	 * @param dp
	 */
	@PUT
	@Path("/{name}/")
	public void updateDp(@PathParam("name") String dpName, DpDTO dp);
	
	/**
	 * deletes the datapoint
	 * @param dpName
	 */
	@DELETE
	@Path("/{name}/")
	public void deleteDp(@PathParam("name") String dpName);
	
	/**
	 * adds a new measurement to the datapoint identified by the given name
	 * @param dpName
	 * @param data
	 */
	@POST
	@Path("/{name}/data")
	public void addDpData(@PathParam("name") String dpName, DpDataDTO data);
	
	/**
	 * returns all measurements for a datapoint in a certain timerange. if the from and to parameters are not given, only
	 * the last measurement is returned.
	 * @param dpName
	 * @param from
	 * @param to
	 * @return
	 */
	@GET
	@Path("/{name}/data")
	public  List<DpDataDTO> getDpData(@PathParam("name") String dpName, @QueryParam("from") String from, @QueryParam("to") String to);
	
	@GET
	@Path("/{name}/periodicdata")
	public List<DpDataDTO> getDpPeriodicData(@PathParam("name") String dpName, @QueryParam("from") String from, @QueryParam("to") String to, @QueryParam("period") int period, @QueryParam("mode") int mode, @QueryParam("type") int type);

	
}
