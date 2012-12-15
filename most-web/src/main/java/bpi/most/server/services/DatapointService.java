package bpi.most.server.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.Observer;

import bpi.most.server.model.Datapoint;
import bpi.most.server.model.DpController;
import bpi.most.server.model.WarningController;
import bpi.most.shared.DpDTO;
import bpi.most.shared.DpDataDTO;
import bpi.most.shared.DpDatasetDTO;
import bpi.most.shared.ZoneDTO;

/**
 * Common Service interface. Handles permissions, etc. Everything returned here
 * is serializable! Different implementations (GWT-RPC, OPC UA, SOAP, etc.)
 * should be based on this implementations
 * 
 * @author robert.zach@tuwien.ac.at
 *TODO check on all RPCs if dp is valid
 */
public class DatapointService {
	DpController dpCtrl = DpController.getInstance();
	private static DatapointService ref = null;

	// Singleton
	private DatapointService() {
		super();
	}

	public static DatapointService getInstance() {
		if (ref == null) {
			ref = new DatapointService();
		}
		return ref;
	}

	// TODO: Write and/or clean java doc.

	/**
	 * add new measurement see {@link bpi.most.server.model.Datapoint#addData()}
	 * 
	 * @return 1 = inserted; < 0 constraints violated or procedure error TODO:
	 *         throw exceptions if no permissions, etc.
	 */
	public int addData(User user, DpDTO dpDTO, DpDataDTO measurement) {
		int result = 0;
		Datapoint dp = dpCtrl.getDatapoint(dpDTO);
		// check permissions
		if (user.hasPermission(dp, DpDTO.Permissions.WRITE)) {
			result = dp.addData(measurement);
		}
		return result;
	}

	/**
	 * latest measurement see {@link bpi.most.server.model.Datapoint#getData()}
	 * 
	 * @return DatapointDatasetVO of requested timeframe, null if no permissions TODO:
	 *         throw exceptions if no permissions, etc.
	 */
	public DpDataDTO getData(User user, DpDTO dpDTO) {
		DpDataDTO result = null;
		Datapoint dp = dpCtrl.getDatapoint(dpDTO);
		// check permissions
		if (user.hasPermission(dp, DpDTO.Permissions.READ)) {
			result = dp.getData();
		}
		return result;
	}

	/**
	 * @return DatapointDatasetVO of requested timeframe, null if no permissions TODO:
	 *         throw exceptions if no permissions, etc.
	 */
	public DpDatasetDTO getData(User user, DpDTO dpDTO, Date starttime,
			Date endtime) {
		DpDatasetDTO result = null;
		Datapoint dp = dpCtrl.getDatapoint(dpDTO);
		// check permissions
		if (user.hasPermission(dp, DpDTO.Permissions.READ)) {
			result = dp.getData(starttime, endtime);
		}
		return result;
	}

	/**
	 * A method used to get data between a start and end time with a specific
	 * period. This can be useful if the normal amount of data you get with the
	 * selected start and end time is too much or if periodic values are
	 * required for further processing.
	 * 
	 * @param user
	 *            The user who want to make the query. Needed for permission
	 *            test.
	 * @param datapointName
	 *            The name of the data point you want the data from.
	 * @param starttime
	 *            Start time of the period you want the data.
	 * @param endtime
	 *            End time of the period you want the data.
	 * @param period
	 *            The period in seconds, with which you want the data.
	 * @return Returns a DatapointDatasetVO with the values between start and end time
	 *         with the selected period.
	 */
	public DpDatasetDTO getDataPeriodic(User user, DpDTO dpDTO, Date starttime,
			Date endtime, Float period) {
		DpDatasetDTO result = null;
		Datapoint dp = dpCtrl.getDatapoint(dpDTO);
		// check permissions
		if (user.hasPermission(dp, DpDTO.Permissions.READ)) {
			// set mode of getDataPeriodic() to 1, because other modes are
			// currently not well supported (or even not implemented)
			result = dp.getDataPeriodic(starttime, endtime, period, 1);
			;
		}
		return result;
	}

	/**
	 * A method used to get the number of real measurements in a specific time
	 * frame.
	 * 
	 * @param user
	 *            The user who want to make the query. Needed for permission
	 *            test.
	 * @param datapointName
	 *            The name of the data point you want the data from.
	 * @param starttime
	 *            Start time of the time frame you want to know how many values
	 *            are in.
	 * @param endtime
	 *            End time of the time frame you want to know how many values
	 *            are in.
	 * @return Number of values in the specific time frame between start and end
	 *         time.
	 */
	public int getNumberOfValues(User user, DpDTO dpDTO, Date starttime,
			Date endtime) {
		int result = 0;
		Datapoint dp = dpCtrl.getDatapoint(dpDTO);
		// check permissions
		if (user.hasPermission(dp, DpDTO.Permissions.READ)) {
			// set mode of getDataPeriodic() to 1, because other modes are
			// currently not well supported (or even not implemented)
			result = dp.getNumberOfValues(starttime, endtime);
		}
		return result;
	}

	/**
	 * Adds a observer for new measurements to a data point.
	 * 
	 * @param user
	 * @param dpEntity
	 * @param observer
	 * @return true if added, false if no permissions
	 */
	public boolean addDpObserver(User user, DpDTO dpEntity, Observer observer) {
		boolean result = false;
		Datapoint dp = dpCtrl.getDatapoint(dpEntity);
		// check permissions
		if (user.hasPermission(dp, DpDTO.Permissions.READ)) {
			dp.addObserver(observer);
			return true;
		}
		return result;
	}

	/**
	 * Delete a observer from a data point.
	 * 
	 * @param user
	 * @param dpEntity
	 * @param observer
	 * @return true if added, false if no permissions
	 */
	public boolean deleteDpObserver(User user, DpDTO dpEntity, Observer observer) {
		boolean result = false;
		Datapoint dp = dpCtrl.getDatapoint(dpEntity);
		// check permissions
		if (user.hasPermission(dp, DpDTO.Permissions.READ)) {
			dp.deleteObserver(observer);
			return true;
		}
		return result;
	}

	/**
	 * Adds a observer for global warning notification.
	 * 
	 * @param user
	 *            The user who want to make the query. Needed for permission
	 *            test.
	 * @param observer
	 *            The observer object.
	 * @return Returns true.
	 */
	public boolean addWarningObserver(User user, Observer observer) {
		// TODO: Add user permission check.
		WarningController warningCtl = WarningController.getInstance();
		warningCtl.addObserver(observer);
		return true;
	}

	/**
	 * Deletes a observer from global warning notification.
	 * 
	 * @param user
	 *            The user who want to make the query. Needed for permission
	 *            test.
	 * @param observer
	 *            The observer object.
	 * @return Returns true.
	 */
	public boolean deletesWarningObserver(User user, Observer observer) {
		// TODO: Add user permission check.
		WarningController warningCtl = WarningController.getInstance();
		warningCtl.deleteObserver(observer);
		return true;
	}

	/**
	 * Get a list with all data points as {@link DpDTO} for which the user has
	 * permission.
	 * 
	 * @param user
	 *            The user who want to make the query. Needed for permission
	 *            test.
	 * @return Returns a list with all {@link DpDTO} for which the user has
	 *         permission.
	 */
	public ArrayList<DpDTO> getDatapoints(User user) {
		// TODO: Add user permission check.
		return dpCtrl.getDatapoints();
	}

	/**
	 * Get a list with all data points that contains the search string as
	 * {@link DpDTO} that for which the user has permission.
	 * 
	 * @param user
	 *            The user who want to make the query. Needed for permission
	 *            test.
	 * @param searchstring
	 *            The string to be searched for.
	 * @return Returns a list with all data points that contains the search
	 *         string as {@link DpDTO} that for which the user has permission.
	 */
	public ArrayList<DpDTO> getDatapoints(User user, String searchstring) {
		// TODO: Add user permission check.
		return dpCtrl.getDatapoints(searchstring);
	}

	/**
	 * Get a list with all data points that contains the search string and is in
	 * the given zone as {@link DpDTO} that for which the user has permission.
	 * 
	 * @deprecated Maybe replaced in the future with a {@link ZoneDTO} instead
	 *             of a string.
	 * @param user
	 *            The user who want to make the query. Needed for permission
	 *            test.
	 * @param searchstring
	 *            The string to be searched for.
	 * @param zone
	 *            The zone in which you want to search.
	 * @return Returns a list with all data points that contains the search
	 *         string and is in the given zone as {@link DpDTO} that for which
	 *         the user has permission.
	 */
	public ArrayList<DpDTO> getDatapoints(User user, String searchstring,
			String zone) {
		// TODO: Add user permission check.
		return dpCtrl.getDatapoints(searchstring, zone);
	}

	/**
	 * fetching one DpDTO identified by the given {@link DpDTO#getName()}. this
	 * is used so that clients can fetch a fully filled DpDTO object by only
	 * having the data points name.
	 * 
	 * @param user
	 * @param dpDto
	 * @return
	 */
	public DpDTO getDatapoint(User user, DpDTO dpDto) {
		DpDTO result = null;
		Datapoint requestedDp = dpCtrl.getDatapoint(dpDto.getName());
		// TODO move permission definition
		user.hasPermission(requestedDp, DpDTO.Permissions.READ);

		result = requestedDp.getDpDTO();

		return result;
	}
}
