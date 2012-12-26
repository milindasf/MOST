package bpi.most.client.rpc;

import java.util.ArrayList;
import java.util.Date;

import bpi.most.shared.DpDatasetDTO;
import bpi.most.shared.DpDTO;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DatapointServiceAsync {

	/**
	 * A method used to get data between a start and end time.
	 * 
	 * @param datapointName
	 *            The name of the data point you want the data from.
	 * @param starttime
	 *            Start time of the period you want the data.
	 * @param endtime
	 *            End time of the period you want the data.
	 * @param callback
	 *            Needed by GWT-RPC for asynchronous callback to return the
	 *            data. Used as follows:
	 * 
	 *            <pre>
	 * <code>
	 * new AsyncCallback{@literal<DpDataset>}() {
	 * 		public void onSuccess(DpDatasetDTO result) {
	 * 					
	 * 		}
	 * 		public void onFailure(Throwable caught) {
	 * 					
	 * 		}
	 * }
	 * </code>
	 * </pre>
	 */
	void getData(String datapointName, Date starttime, Date endtime,
			AsyncCallback<DpDatasetDTO> callback);

	/**
	 * A method used to get all data points as a list of {@link DpDTO}.
	 * 
	 * @param callback
	 *            Needed by GWT-RPC for asynchronous callback to return the
	 *            data. Used as follows:
	 * 
	 *            <pre>
	 * <code>
	 * new AsyncCallback{@literal<ArrayList<DpDTO>>}() {
	 * 		public void onSuccess(ArrayList{@literal<DpDTO>} result) {
	 * 					
	 * 		}
	 * 		public void onFailure(Throwable caught) {
	 * 					
	 * 		}
	 * }
	 * </code>
	 * </pre>
	 */
	void getDatapoints(AsyncCallback<ArrayList<DpDTO>> callback);

	/**
	 * A method used to get all data points that contains the search string as a
	 * list of {@link DpDTO}.
	 * 
	 * @param searchstring
	 *            The string to be searched for.
	 * @param callback
	 *            Needed by GWT-RPC for asynchronous callback to return the
	 *            data. Used as follows:
	 * 
	 *            <pre>
	 * <code>
	 * new AsyncCallback{@literal<ArrayList<DpDTO>>}() {
	 * 		public void onSuccess(ArrayList{@literal<DpDTO>} result) {
	 * 					
	 * 		}
	 * 		public void onFailure(Throwable caught) {
	 * 					
	 * 		}
	 * }
	 * </code>
	 * </pre>
	 */
	void getDatapoints(String searchstring,
			AsyncCallback<ArrayList<DpDTO>> callback);

	/**
	 * A method used to get all data points that contains the search string and
	 * is in the given zone as a list of {@link DpDTO}.
	 * 
	 * @param searchstring
	 *            The string to be searched for.
	 * @param zone
	 *            The zone in which you want to search.
	 * @param callback
	 *            Needed by GWT-RPC for asynchronous callback to return the
	 *            data. Used as follows:
	 * 
	 *            <pre>
	 * <code>
	 * new AsyncCallback{@literal<ArrayList<DpDTO>>}() {
	 * 		public void onSuccess(ArrayList{@literal<DpDTO>} result) {
	 * 					
	 * 		}
	 * 		public void onFailure(Throwable caught) {
	 * 					
	 * 		}
	 * }
	 * </code>
	 * </pre>
	 * @deprecated Maybe replaced in the future with a {@link ZoneDTO} instead
	 *             of a string.
	 */
	void getDatapoints(String searchstring, String zone,
			AsyncCallback<ArrayList<DpDTO>> callback);

	/**
	 * 
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
	 * @param callback
	 *            Needed by GWT-RPC for asynchronous callback to return the
	 *            data. Used as follows:
	 * 
	 *            <pre>
	 * <code>
	 * new AsyncCallback<DatapointDatasetVO>() {
	 * 		public void onSuccess(DpDatasetDTO result) {
	 * 					
	 * 		}
	 * 		public void onFailure(Throwable caught) {
	 * 					
	 * 		}
	 * }
	 * </code>
	 * </pre>
	 */
	void getDataPeriodic(String datapointName, Date starttime, Date endtime,
			Float period, AsyncCallback<DpDatasetDTO> callback);

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
	 * @param callback
	 *            Needed by GWT-RPC for asynchronous callback to return the
	 *            data. Used as follows:
	 * 
	 *            <pre>
	 * <code>
	 * new AsyncCallback<Integer>() {
	 * 		public void onSuccess(Integer result) {
	 * 					
	 * 		}
	 * 		public void onFailure(Throwable caught) {
	 * 					
	 * 		}
	 * }
	 * </code>
	 * </pre>
	 */
	void getNumberOfValues(String datapointName, Date starttime, Date endtime,
			AsyncCallback<Integer> callback);

}
