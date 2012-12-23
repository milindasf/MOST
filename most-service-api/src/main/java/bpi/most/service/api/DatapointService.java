package bpi.most.service.api;

import bpi.most.domain.datapoint.DatapointVO;

import java.util.List;


/**
 * Interface Specification of Datapoint Service.
 *
 * @author Lukas Weichselbaum
 * @author Jakob Korherr
 */
public interface DatapointService {

    /**
     * A) Searches for data point in the object cache. <br> B) Instantiates respective
     * physical or virtual data point.
     *
     * @param dpName
     *            The name of the data point.
     *
     * @return Returns an instance of a "physical" (DpServer) or "virtual"
     *          (DpVirtual) data point. Null if not valid.
     */
    // TODO specify class Datapoint.
    // public Datapoint getDatapoint(String dpName);

    /**
     * Checks if a data point with the given name is in the database.
     *
     * @param dpName
     *            The name of the data point you are looking for.
     * @return Returns true if a data point with the given name exists, false
     *         otherwise.
     */
    boolean isValidDp(String dpName);

    /**
     * Get a list of {@link DatapointVO} of all data points in the database.
     *
     * @return Returns a list of {@link DatapointVO} of all data points in the
     *         database.
     */
    List<DatapointVO> getDatapoints();

    /**
     * Get a list of {@link DatapointVO} of all data points in the database that
     * contains the search string.
     *
     * @param searchstring
     *            The string to be searched for.
     * @return Returns a list of {@link DatapointVO} of all data points in the
     *         database that contains the search string.
     */
    List<DatapointVO> getDatapoints(String searchstring);

    /**
     * Get a list of {@link DatapointVO} of all data points in the database that
     * contains the search string and are in the given zone.
     *
     * @param searchstring
     *            The string to be searched for.
     * @param zone
     *            The zone in which you want to search.
     * @return Returns a list of {@link DatapointVO} of all data points in the
     *         database that contains the search string and are in the given
     *         zone.
     */
    List<DatapointVO> getDatapoints(String searchstring, String zone);

}
