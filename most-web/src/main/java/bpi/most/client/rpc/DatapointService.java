package bpi.most.client.rpc;

import java.util.ArrayList;
import java.util.Date;

import bpi.most.shared.DpDatasetDTO;
import bpi.most.shared.DpDTO;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("services/gwtrpc/dp")
public interface DatapointService extends RemoteService {

	/**
	 * A method used to get data between a start and end time.
	 * 
	 * @param datapointName
	 *            The name of the data point you want the data from.
	 * @param starttime
	 *            Start time of the period you want the data.
	 * @param endtime
	 *            End time of the period you want the data.
	 * @return Returns a DatapointDatasetVO with the values between start and end time.
	 */
	DpDatasetDTO getData(String datapointName, Date starttime,
			Date endtime);

	/**
	 * A method used to get all data points as a list of {@link DpDTO}.
	 * 
	 * @return Returns a list of all data points as {@link DpDTO}.
	 */
	ArrayList<DpDTO> getDatapoints();

	/**
	 * A method used to get all data points that contains the search string as a
	 * list of {@link DpDTO}.
	 * 
	 * @param searchstring
	 *            The string to be searched for.
	 * @return Returns a list of all data points that contains the search string
	 *         as {@link DpDTO}.
	 */
	ArrayList<DpDTO> getDatapoints(String searchstring);

	/**
	 * A method used to get all data points that contains the search string and
	 * is in the given zone as a list of {@link DpDTO}.
	 * 
	 * @param searchstring
	 *            The string to be searched for.
	 * @param zone
	 *            The zone in which you want to search.
	 * @return Returns a list of all data points that contains the search string
	 *         and is in the given zone as {@link DpDTO}.
	 * @deprecated Maybe replaced in the future with a {@link ZoneDTO} instead
	 *             of a string.
	 */
	ArrayList<DpDTO> getDatapoints(String searchstring, String zone);

	/**
	 * A method used to get data between a start and end time with a specific
	 * period. This can be useful if the normal amount of data you get with the
	 * selected start and end time is too much.
	 * 
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
	DpDatasetDTO getDataPeriodic(String datapointName, Date starttime,
			Date endtime, Float period);

	/**
	 * A method used to get the number of values in a specific time frame.
	 * 
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
	int getNumberOfValues(String datapointName, Date starttime,
			Date endtime);

}
