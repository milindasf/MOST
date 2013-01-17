package bpi.most.obix.server.rest;

import javax.ws.rs.*;

/**
 * Created with IntelliJ IDEA.
 * User: Alexej Strelzow
 * Date: 06.01.13
 * Time: 18:22
 */
//@Path("obix/dp/")
@Produces("application/xml")
@Consumes("application/xml")
public interface ObixDpResource {

    /**
     * GET /obix/dp/{name}
     *
     * @param name The name of the oBix object, which is the last part of: /obix/dp/{datapointName}
     *            of the data point to retrieve.
     * @return One oBix object with the {@link bpi.most.obix.objects.Uri} <code>uri</code>,
     *         or <code>null</code>, if the <code>uri</code> is a wrong one.
     *         Dp contains only data, which belongs to Dp.
     */
    @GET
    @Path("/{name}/")
    String getDp(@PathParam("name") String name); // = datapointName = {name} /obix/dp/{name}

    /**
     * GET /obix/dp/{name}/data
     *
     * @param name The name of the oBix object, which is the last part of: /obix/dp/{datapointName}
     *            of the data point to retrieve.
     * @return One oBix object with the {@link bpi.most.obix.objects.Uri} <code>uri</code>,
     *         or <code>null</code>, if the <code>uri</code> is a wrong one.
     *         The latest measurement for the data point.
     */
    @GET
    @Path("/{name}/data")
    String getDpData(@PathParam("name") String name); // = datapointName = {name}

    /**
     * POST /obix/dp/{name}
     * <p/>
     * Adds the encoded Dp and its values to the server.
     *
     * @param encodedDp An encoded instance of Dp
     */
    @POST
    @Path("/")
    void addDp(String encodedDp);

    /**
     * adds a new measurement to the data point identified by the given name
     * @param dpName The name of the oBix object, which gets data added
     * @param encodedDpData The encoded DpData, which needs to be added
     */
    @POST
    @Path("/{name}/data")
    void addDpData(@PathParam("name") String dpName, String encodedDpData);

    /**
     * updates the data point
     * @param dpName The name of the oBix object, which needs to be updated
     * @param encodedDp An encoded instance of Dp
     */
    @PUT
    @Path("/{name}/")
    void updateDp(@PathParam("name") String dpName, String encodedDp);

    /**
     * GET /obix/dp
     *
     * @return All data points
     */
    @GET
//    @Path("/")
    String getAllDps();
    // return list with all data points

    /**
     * GET /obix/dp/{name}/data?from={UCT datetime}&to=
     * {UCT datetime}
     *
     * @param from
     * @param to
     * @return
     */
    @GET
    @Path("/data")
    String getDpData(@PathParam("name") String name, @QueryParam("from") String from, @QueryParam("to") String to);


    @GET
    @Path("/{name}/periodicdata")
    String getDpPeriodicData(@PathParam("name") String dpName, @QueryParam("from") String from, @QueryParam("to") String to, @QueryParam("period") int period, @QueryParam("mode") int mode, @QueryParam("type") int type);
}
