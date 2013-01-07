package bpi.most.client.modules.charts;

import bpi.most.dto.DpDataDTO;
import bpi.most.dto.DpDatasetDTO;

import java.util.ArrayList;
import java.util.Date;

/**
 * Interface for all functions a chart window must implement, the
 * {@link ChartWrapper} has to implement these as well, to pass through the data
 * to the used chart. (The used chart may never been used directly.)
 * 
 * The chart implementation must have an {@link ArrayList} with {@link Curve}-objects.
 * 
 * @author mike
 */

public interface ChartInterface {

	/**
	 * Add an new series to the chart without data.
	 * 
	 * @param name
	 *            The name of the empty series you want to add.
	 */
	void addCurve(String name);

	/**
	 * Add a new series to the chart with the data provided by the data set.
	 * 
	 * @param dpdataset
	 *            The {@link DpDatasetDTO} with name of the series and all data
	 *            points you want do add to the new series.
	 */
	void addCurve(DpDatasetDTO dpdataset);

	/**
	 * Add a new series to the chart with the data provided by the data set.
	 * 
	 * @param dpdataset
	 *            The {@link DpDatasetDTO} with name of the series and all data
	 *            points you want do add to the new series.
	 * @param periodicFlag
	 *            If the data in the data set is generated periodic this should
	 *            be set to true, otherwise false.
	 */
	void addCurve(DpDatasetDTO dpdataset, boolean periodicFlag);

	/**
	 * Get an array list with all the {@link Curve} elements that are currently
	 * in the chart. e.g.: for finding out if there is any periodic generated
	 * series in the chart
	 * 
	 * @return Returns an array list with all the {@link Curve} elements that
	 *         are currently in the chart.
	 */
	ArrayList<Curve> getCurveList();

	/**
	 * Method for adding a single data point value to the series with the given name.
	 * (Warning: There is no check if the data is chronological consecutive.
	 * Time leaps are possible!)
	 * 
	 * @param name
	 *            The name of the series you want to add the data to.
	 * @param date
	 *            The date of the data you want to add to the series.
	 * @param value
	 *            The value of the data you want to add to the series.
	 */
	void addValue(String name, Date date, Double value);

	/**
	 * Method to remove the series with the given name from the chart. If no
	 * series with the name exist, nothing will happen.
	 * 
	 * @param name
	 *            The name of the series you want to remove from the chart.
	 */
	void removeSeries(String name);

	/**
	 * Method to remove the series with the given name from the chart and
	 * redraws the chart. If you want to remove more than one series from the
	 * chart you better use {@link #removeSeries(String)} and use
	 * {@link #redraw()} once you're finished. If no series with the name exist,
	 * nothing will happen.
	 * 
	 * @param name
	 *            The name of the series you want to remove from the chart.
	 * @param redraw
	 *            If you want to redraw the chart after removing the series,
	 *            this must be true.
	 */
	void removeSeries(String name, boolean redraw);

	/**
	 * Removes all series from the chart.
	 */
	void removeAllSeries();

	/**
	 * Removes all series from the chart and choose if you want to redraw it
	 * afterwards.
	 * 
	 * @param redraw
	 *            True if you want to redraw the chart afterwards, false if you
	 *            don't.
	 */
	void removeAllSeries(boolean redraw);

	/**
	 * Redraw the chart.
	 */
	void redraw();

	/**
	 * Get the date of the first value from the series with the given name.
	 * 
	 * @param name
	 *            The name of the series you want the start date.
	 * @return Return the date of the first value from the series with the given
	 *         name.
	 */
	Date getStartDate(String name);

	/**
	 * Get the date of the last value from the series with the given name.
	 * 
	 * @param name
	 *            The name of the series you want the end date.
	 * @return Return the date of the last value from the series with the given
	 *         name.
	 */
	Date getEndDate(String name);

	/**
	 * Append the data from the data set to the series it belongs to. If the
	 * series it belongs to doesn't exit nothing will be added.
	 * 
	 * @param dpdataset
	 *            The {@link DpDatasetDTO} you want to append.
	 */
	void appendDataset(DpDatasetDTO dpdataset);

	/**
	 * Prepend the data from the data set to the series it belongs to. If the
	 * series it belongs to doesn't exit nothing will be added.
	 * 
	 * @param dpdataset
	 *            The {@link DpDatasetDTO} you want to prepend.
	 */
	void prependDataset(DpDatasetDTO dpdataset);

	/**
	 * Append a single {@link DpDataDTO} to the series with the given name if it
	 * exists.
	 * 
	 * @param name
	 *            Name of the series you want to append the data.
	 * @param dpdata
	 *            The {@link DpDataDTO} you want to append.
	 */
	void appendData(String name, DpDataDTO dpdata);

	/**
	 * Prepend a single {@link DpDataDTO} to the series with the given name if
	 * it exists.
	 * 
	 * @param name
	 *            Name of the series you want to append the data.
	 * @param dpdata
	 *            The {@link DpDataDTO} you want to prepend.
	 */
	void prependData(String name, DpDataDTO dpdata);

	/**
	 * Delete the data set from the series with the given name between the two
	 * dates.
	 * 
	 * @param name
	 *            Name of the series you want to delete the data from.
	 * @param from
	 *            The start date from which you want to delete the data.
	 * @param to
	 *            The end date up to which you want to delete the data.
	 */
	void delDataset(String name, Date from, Date to);

	/**
	 * A method to delete a data point from the array list in the
	 * {@link ChartWrapper}. It is needed because when you remove a series in
	 * the chart implementation you have to remove the data point in the
	 * {@link ChartWrapper} as well.
	 * 
	 * @param name
	 *            The name of the data point you want to delete from the list.
	 */
	void delDp(String name);

	/**
	 * Method to set a complete new data set to a series.
	 * 
	 * @param dpdataset
	 *            The data set you want to replace the old data with.
	 * @param periodicFlag
	 *            Set to true if the new data is periodic and set to false if
	 *            the new data is real time data.
	 */
	void setDataset(DpDatasetDTO dpdataset, boolean periodicFlag);

	/**
	 * Method to get the Number of points of a specific series in the chart,
	 * returns zero when the series is not in the chart.
	 * 
	 * @param name
	 *            Name of the series you want to know how many points it have.
	 * @return Returns the number of points the series have. Zero when the
	 *         series is not in the chart.
	 */
	int getPointCount(String name);

	/**
	 * Method to set the periodic flag of a series. The periodic flag indicate
	 * if the data of the series is real time or periodic generated.
	 * 
	 * @param name
	 *            Name of the series you want to set the flag.
	 * @param flag
	 */
	void setPeriodicFlag(String name, boolean flag);

	/**
	 * Method to get the state of the periodic flag of a series. The periodic
	 * flag indicate if the data of the series is real time or periodic
	 * generated.
	 * 
	 * @param name
	 *            Name of the series you want to get the flag.
	 * @return Returns if the data of the series is real time or periodic
	 *         generated. True = periodic generated data, false = real time data
	 */
	boolean getPeriodicFlag(String name);

	/**
	 * Method to check if series with given name already exist in the chart.
	 * 
	 * @param name
	 *            Name of the series you want to know if it's in the chart.
	 * @return Returns if the series with the given name is already in chart.
	 *         True = is in chart, false = is not in chart
	 */
	boolean isInChart(String name);

	/**
	 * Method to figure out if there is periodic data used in any series.
	 * 
	 * @return Return true if any series contains periodic Values and false if
	 *         all series contains only real time data.
	 */
	boolean isPeriodic();

	/**
	 * Method to figure out if a specific series is generated with periodic
	 * data.
	 * 
	 * @param name
	 *            The name of the series.
	 * @return Return true if the series is generated with periodic data, false
	 *         otherwise.
	 */
	boolean isPeriodic(String name);

	/**
	 * If the chart has a zoom feature you can get the start date and time of
	 * the zoom frame.
	 * 
	 * @return The start date and time of the zoom frame.
	 */
	Date getZoomStart();

	/**
	 * If the chart has a zoom feature you can get the end date and time of the
	 * zoom frame.
	 * 
	 * @return The end date and time of the zoom frame.
	 */
	Date getZoomEnd();

	/**
	 * Shows a loading icon on the chart until it's removed with
	 * {@link #hideLoading()}. (Maybe not every chart implementation offers this
	 * feature.)
	 * 
	 * @param text
	 *            The text to be shown while loading.
	 */
	void showLoading(String text);

	/**
	 * Hides the loading icon that is shown with {@link #showLoading(String)}.
	 * (Maybe not every chart implementation offers this feature.)
	 */
	void hideLoading();

	/**
	 * Remove values at the start of a series.
	 * 
	 * @param name
	 *            The name of the series.
	 * @param number
	 *            The number of values that should be removed.
	 * @return Returns the number of removed values.
	 */
	int removeValuesAtStart(String name, int number);

	/**
	 * Remove values at the end of a series.
	 * 
	 * @param name
	 *            The name of the series.
	 * @param number
	 *            The number of values that should be removed.
	 * @return Returns the number of removed values.
	 */
	int removeValuesAtEnd(String name, int number);

	/**
	 * Get a {@link DpDatasetDTO} from a series.
	 * 
	 * @param name
	 *            The name of the series.
	 * @return The DatapointDatasetVO of the series.
	 */
	DpDatasetDTO getDataset(String name);
}
