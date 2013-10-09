package bpi.most.obix.server.rest;

import javax.ws.rs.*;

/**
 * Created with IntelliJ IDEA.
 * User: Alexej Strelzow
 * Date: 06.01.13
 * Time: 11:42
 */

//@Path("obix/zones/")
@Produces("application/xml")
@Consumes("application/xml")
public interface ObixZoneResource {

    /**
     * GET /obix/zones/{id}
     *
     * @param id The id of the zone
     * @return The zone, which contains data points
     */
    @GET
    @Path("/{id}/")
    String getZone(@PathParam("id") int id); // = {id}
    // return zone

    /**
     * GET /obix/zones/{id}/data
     *
     * @param id The id of the zone
     * @return The zone, which contains data points
     */
    @GET
    @Path("/{id}/data/")
    String getDpForZone(@PathParam("id") int id, @QueryParam("level") int level); // = {id}
    // return zone with dp inside the zone

    /**
     * GET /obix/zones
     *
     * @return All objects, wrapped in their own zone
     */
    @GET
//    @Path("/")
    String getHeadZones();
    // return list with all zones

}
