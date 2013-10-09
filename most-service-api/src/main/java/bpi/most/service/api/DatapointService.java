package bpi.most.service.api;

import bpi.most.domain.datapoint.DatapointVO;
import bpi.most.dto.DpDTO;
import bpi.most.dto.DpDataDTO;
import bpi.most.dto.DpDatasetDTO;
import bpi.most.dto.UserDTO;

import java.util.Date;
import java.util.List;


/**
 * Interface Specification of Datapoint Service.
 *
 * @author Lukas Weichselbaum
 * @author Jakob Korherr
 */
public interface DatapointService {
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
    List<DpDTO> getDatapoints();

    /**
     * Get a list of {@link DatapointVO} of all data points in the database that
     * contains the search string.
     *
     *
     * @param searchstring
     *            The string to be searched for.
     * @return Returns a list of {@link DatapointVO} of all data points in the
     *         database that contains the search string.
     */
    List<DpDTO> getDatapoints(String searchstring);

    /**
     * Get a list of {@link DatapointVO} of all data points in the database that
     * contains the search string and are in the given zone.
     *
     *
     * @param searchstring
     *            The string to be searched for.
     * @param zone
     *            The zone in which you want to search.
     * @return Returns a list of {@link DatapointVO} of all data points in the
     *         database that contains the search string and are in the given
     *         zone.
     */
    List<DpDTO> getDatapoints(String searchstring, String zone);

    /**
     * fetching one DpDTO identified by the given {@link DatapointVO#getName()}. this
     * is used so that clients can fetch a fully filled DpDTO object by only
     * having the data points name.
     *
     * @param user
     * @param dpDto
     * @return
     */
    DpDTO getDatapoint(UserDTO user, DpDTO dpDto);

    /**
     * latest measurement see {@link bpi.most.server.model.Datapoint#getData()}
     *
     * @return DatapointDatasetVO of requested timeframe, null if no permissions TODO:
     *         throw exceptions if no permissions, etc.
     */
    DpDataDTO getData(UserDTO userDTO, DpDTO dpDTO);

    DpDatasetDTO getData(UserDTO userDTO, DpDTO dpDTO, Date starttime, Date endtime);

    DpDatasetDTO getDataPeriodic(UserDTO userDTO, DpDTO dpDTO, Date starttime, Date endtime, Float period);

    int getNumberOfValues(UserDTO userDTO, DpDTO dpDTO, Date starttime, Date endtime);
    
    /**
	 * add new measurement see {@link bpi.most.server.model.Datapoint#addData()}
	 * 
	 * @return 1 = inserted; < 0 constraints violated or procedure error TODO:
	 *         throw exceptions if no permissions, etc.
	 */
	int addData(UserDTO userDTO, DpDTO dpDTO, DpDataDTO measurement);
	
	/**
	 * Deletes all stored data of datapoint. Use with caution!!
	 */
    int delData(UserDTO userDTO, DpDTO dpDTO);

	/**
	 * Deletes data of a given timeslot
	 */
    int delData(UserDTO userDTO, DpDTO dpDTO, Date starttime, Date endtime);

    DpDatasetDTO getDataPeriodic(UserDTO user, DpDTO dpDTO, Date starttime, Date endtime, Float period, int mode);

    void addObserver(String dpName, Object connector);  // TODO
    
}
