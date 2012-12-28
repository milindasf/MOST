package bpi.most.client.modules.charts.Highchart;

import java.util.ArrayList;
import java.util.Date;
import org.moxieapps.gwt.highcharts.client.Axis;
import org.moxieapps.gwt.highcharts.client.Chart;
import org.moxieapps.gwt.highcharts.client.Legend;
import org.moxieapps.gwt.highcharts.client.Point;
import org.moxieapps.gwt.highcharts.client.Series;
import org.moxieapps.gwt.highcharts.client.Legend.Align;
import org.moxieapps.gwt.highcharts.client.events.SeriesLegendItemClickEvent;
import org.moxieapps.gwt.highcharts.client.events.SeriesLegendItemClickEventHandler;
import org.moxieapps.gwt.highcharts.client.plotOptions.LinePlotOptions;
import org.moxieapps.gwt.highcharts.client.plotOptions.SeriesPlotOptions;

import bpi.most.client.modules.charts.ChartInterface;
import bpi.most.client.modules.charts.ChartWrapper;
import bpi.most.client.modules.charts.Curve;
import bpi.most.dto.DpDataDTO;
import bpi.most.dto.DpDatasetDTO;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

/**
 * This is the chart implementation of the GWT Highcharts project
 * (http://www.moxiegroup.com/moxieapps/gwt-highcharts/) that based on the
 * JavaScript chart libraries Highcharts and Highstock.
 * 
 * @author mike
 * 
 */
public class HighchartStandart extends Composite implements ChartInterface {

	/**
	 * The curveList is a {@link ArrayList} of {@link Curve}-objects and
	 * contains one {@link Curve}-object for every series in the chart.
	 */
	ArrayList<Curve> curveList = new ArrayList<Curve>();

	private static HighchartStandartUiBinder uiBinder = GWT
			.create(HighchartStandartUiBinder.class);

	private static final int Z_INDEX = 500;
	private static final int LOADING_TIMER_SCHEDULE = 10000;
	private static final int PARENT_WIDGETS = 10;
	
	interface HighchartStandartUiBinder extends
			UiBinder<Widget, HighchartStandart> {
	}

	@UiField
	AbsolutePanel chartParent;

	Chart chart;

	int isLoadingCounter = 0;

	Timer isLoadingTimer = new Timer() {

		@Override
		public void run() {
			Window.alert("data loading timeout");
			chart.hideLoading();
		}
	};

	/**
	 * Creates a new instance of Highcharts with all necessary options.
	 */
	public HighchartStandart() {
		initWidget(uiBinder.createAndBindUi(this));
		chart = new Chart()
				.setType(Series.Type.SPLINE)
				.setLinePlotOptions(new LinePlotOptions().setZIndex(Z_INDEX))
				.setHeight100()
				.setWidth100()
				.setZoomType(Chart.ZoomType.X)
				.setChartTitleText("")
				.setLegend(
						new Legend().setAlign(Align.RIGHT)
								.setLayout(Legend.Layout.VERTICAL)
								.setVerticalAlign(Legend.VerticalAlign.MIDDLE));
		chart.setPersistent(true);

		chart.getYAxis().setAxisTitleText("Value");
		chart.getXAxis().setType(Axis.Type.DATE_TIME);
		// add a new SeriesLegendItemClickEventHandler to the chart to delete
		// the series from the chart instead of hiding it
		chart.setSeriesPlotOptions(new SeriesPlotOptions()
				.setSeriesLegendItemClickEventHandler(new SeriesLegendItemClickEventHandler() {

					@Override
					public boolean onClick(
							SeriesLegendItemClickEvent seriesLegendItemClickEvent) {
						for (int i = 0; i < curveList.size(); i++) {
							if (curveList
									.get(i)
									.getName()
									.equalsIgnoreCase(
											seriesLegendItemClickEvent
													.getSeriesName())) {
								delDp(seriesLegendItemClickEvent
										.getSeriesName());
								((Series) curveList.get(i).getSeries())
										.remove();
								curveList.remove(i);
							}
						}
						return true;
					}
				}));

		setGlobalOptions();
		chartParent.add(chart);
		chart.redraw();
	}

	/**
	 * Check if the series with the given name is in the {@link #curveList}.
	 * 
	 * @param name
	 *            The name of the series that should be checked.
	 * @return Returns true if the series with the given name is in the chart,
	 *         false otherwise.
	 */
	private boolean checkCurveList(String name) {
		for (int i = 0; i < curveList.size(); i++) {
			if (curveList.get(i).name.equalsIgnoreCase(name)) {
				return true;
			}
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bpi.most.client.modules.charts.ChartInterface#getCurveList()
	 */
	public ArrayList<Curve> getCurveList() {
		return curveList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * bpi.most.client.modules.charts.ChartInterface#addCurve(java.lang.String)
	 */
	@Override
	public void addCurve(String name) {
		if (!checkCurveList(name)) {
			Series series = chart.createSeries().setName(name);
			chart.addSeries(series);
			curveList.add(new Curve(name, series));
			chart.redraw();
		} else {
			Window.alert("Already in Window..");
		}
	}

	/**
	 * Get the reference to this {@link HighchartStandart}-object.
	 * 
	 * @return Returns the reference to this object.
	 */
	private HighchartStandart getThis() {
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * bpi.most.client.modules.charts.ChartInterface#addCurve(bpi.most.shared
	 * .DpDatasetDTO)
	 */
	@Override
	public void addCurve(DpDatasetDTO dpdataset) {
		if (dpdataset != null) {
			if (!checkCurveList(dpdataset.getDatapointName())) {
				Series series = chart.createSeries().setName(
						dpdataset.getDatapointName());
				curveList.add(new Curve(dpdataset.getDatapointName(), series));
				chart.addSeries(series, true, false);
				// add Values to the series
				for (DpDataDTO i : dpdataset) {
					series.addPoint(i.getTimestamp().getTime(), i.getValue(),
							false, false, false);
				}
				chart.redraw();

			} else {
				Window.alert("Already in Window.");
			}
		} else {
			Window.alert("No Values for the choosen data point between the start and end time!");
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * bpi.most.client.modules.charts.ChartInterface#addCurve(bpi.most.shared
	 * .DpDatasetDTO, boolean)
	 */
	@Override
	public void addCurve(DpDatasetDTO dpdataset, boolean periodicFlag) {
		if (dpdataset != null) {
			if (!checkCurveList(dpdataset.getDatapointName())) {
				Series series = chart.createSeries().setName(
						dpdataset.getDatapointName());
				curveList.add(new Curve(dpdataset.getDatapointName(), series,
						periodicFlag));
				chart.addSeries(series, true, false);
				// add Values to the series
				for (DpDataDTO i : dpdataset) {
					series.addPoint(i.getTimestamp().getTime(), i.getValue(),
							false, false, false);
				}

				chart.redraw();

			} else {
				Window.alert("Already in Window.");
			}
		} else {
			Window.alert("No Values for the choosen data point between the start and end time!");
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * bpi.most.client.modules.charts.ChartInterface#addSingleDatapoint(java
	 * .lang.String, java.util.Date, java.lang.Double)
	 */
	@Override
	public void addValue(String name, Date date, Double value) {
		for (int i = 0; i < curveList.size(); i++) {
			if (curveList.get(i).getName().equals(name)) {
				((Series) curveList.get(i).getSeries()).addPoint(
						date.getTime(), value, true, false, true);
			}
		}
		chart.redraw();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * bpi.most.client.modules.charts.ChartInterface#removeSeries(java.lang.
	 * String)
	 */
	public void removeSeries(String name) {
		for (Curve i : curveList) {
			if (i.getName().contentEquals(name)) {
				chart.removeSeries((Series) i.getSeries());
				curveList.remove(i);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * bpi.most.client.modules.charts.ChartInterface#removeSeries(java.lang.
	 * String, boolean)
	 */
	public void removeSeries(String name, boolean redraw) {
		for (Curve i : curveList) {
			if (i.getName().contentEquals(name)) {
				chart.removeSeries((Series) i.getSeries(), redraw);
				curveList.remove(i);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bpi.most.client.modules.charts.ChartInterface#removeAllSeries()
	 */
	public void removeAllSeries() {
		chart.removeAllSeries();
		curveList.clear();
		chartParent.remove(chart);
		chartParent.add(chart);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * bpi.most.client.modules.charts.ChartInterface#removeAllSeries(boolean)
	 */
	public void removeAllSeries(boolean redraw) {
		chart.removeAllSeries(redraw);
		curveList.clear();
		chartParent.remove(chart);
		chartParent.add(chart);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * bpi.most.client.modules.charts.ChartInterface#getStartDate(java.lang.
	 * String)
	 */
	public Date getStartDate(String name) {
		Series series = null;
		for (Curve i : curveList) {
			if (i.getName().contentEquals(name)) {
				series = (Series) i.getSeries();
			}
		}
		if (series != null) {
			return new Date(series.getPoints()[0].getX().longValue());
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * bpi.most.client.modules.charts.ChartInterface#getEndDate(java.lang.String
	 * )
	 */
	public Date getEndDate(String name) {
		Series series = null;
		for (Curve i : curveList) {
			if (i.getName().contentEquals(name)) {
				series = (Series) i.getSeries();
			}
		}
		if (series != null) {
			return new Date(series.getPoints()[series.getPoints().length - 1]
					.getX().longValue());
		}
		return null;
	}

	/**
	 * Get the series representation of this chart type.
	 * 
	 * @param name
	 *            The name of the series.
	 * @return Returns the series with the given name if it's in the chart.
	 */
	public Series getSeries(String name) {
		for (int i = 0; i < curveList.size(); i++) {
			if (curveList.get(i).name.equalsIgnoreCase(name)) {
				return (Series) curveList.get(i).getSeries();
			}
		}
		return null;
	}

	/**
	 * Get the {@link Curve} element of a series from the list.
	 * 
	 * @param name
	 *            The name of the series.
	 * @return Returns the {@link Curve} element of the series with the given
	 *         name.
	 */
	public Curve getCurve(String name) {
		for (int i = 0; i < curveList.size(); i++) {
			if (curveList.get(i).name.equalsIgnoreCase(name)) {
				return curveList.get(i);
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * bpi.most.client.modules.charts.ChartInterface#appendDataset(bpi.most.
	 * shared.DpDatasetDTO)
	 */
	@Override
	public void appendDataset(DpDatasetDTO dpdataset) {
		Series series;
		series = getSeries(dpdataset.getDatapointName());
		if (series != null) {
			for (int i = 0; i < dpdataset.size(); i++) {
				series.addPoint(dpdataset.get(i).getTimestamp().getTime(),
						dpdataset.get(i).getValue(), false, false, false);
			}
			chart.redraw();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * bpi.most.client.modules.charts.ChartInterface#prependDataset(bpi.most
	 * .shared.DpDatasetDTO)
	 */
	@Override
	public void prependDataset(DpDatasetDTO dpdataset) {
		Series series;
		series = getSeries(dpdataset.getDatapointName());
		if (series != null) {
			int pointslen = series.getPoints().length;
			int datasetlen = dpdataset.size();
			Point[] pointsnew = new Point[pointslen + datasetlen];
			for (int i = 0; i < datasetlen; i++) {
				pointsnew[i] = new Point(dpdataset.get(i).getTimestamp()
						.getTime(), dpdataset.get(i).getValue());
			}
			for (int i = 0; i < pointslen; i++) {
				pointsnew[datasetlen + i] = new Point(
						series.getPoints()[i].getX(),
						series.getPoints()[i].getY());
			}
			series.setPoints(pointsnew);

			chart.redraw();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * bpi.most.client.modules.charts.ChartInterface#appendData(java.lang.String
	 * , bpi.most.shared.DpDataDTO)
	 */
	@Override
	public void appendData(String name, DpDataDTO dpdata) {
		getSeries(name).addPoint(dpdata.getTimestamp().getTime(),
				dpdata.getValue(), true, false, true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * bpi.most.client.modules.charts.ChartInterface#prependData(java.lang.String
	 * , bpi.most.shared.DpDataDTO)
	 */
	@Override
	public void prependData(String name, DpDataDTO dpdata) {
		DpDatasetDTO dpdataset = new DpDatasetDTO(name);
		dpdataset.add(dpdata);
		prependDataset(dpdataset);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * bpi.most.client.modules.charts.ChartInterface#delDataset(java.lang.String
	 * , java.util.Date, java.util.Date)
	 */
	public void delDataset(String name, Date from, Date to) {
		showLoading("get new data");
		Series series;
		series = getSeries(name);
		int size = series.getPoints().length;
		for (int i = 0; i < size; i++) {
			long timestamp = (((Double) series.getPoints()[i].getX())
					.longValue());
			if (timestamp > from.getTime() && timestamp < to.getTime()) {
				Point point = series.getPoints()[i];
				series.removePoint(point);
				size--;
				i--;
			}
		}
		chart.redraw();
		hideLoading();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * bpi.most.client.modules.charts.ChartInterface#setDataset(bpi.most.shared
	 * .DpDatasetDTO, boolean)
	 */
	@Override
	public void setDataset(DpDatasetDTO dpdataset, boolean periodicFlag) {
		Series series;
		series = getSeries(dpdataset.getDatapointName());
		int datasetlen = dpdataset.size();
		Point[] pointsnew = new Point[datasetlen];
		for (int i = 0; i < datasetlen; i++) {
			pointsnew[i] = new Point(dpdataset.get(i).getTimestamp().getTime(),
					dpdataset.get(i).getValue());
		}
		series.setPoints(pointsnew);
		getCurve(dpdataset.getDatapointName()).setPeriodicFlag(periodicFlag);

		chart.redraw();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * bpi.most.client.modules.charts.ChartInterface#delDp(java.lang.String)
	 */
	@Override
	public void delDp(String name) {
		// ((ChartWrapper) this.getParent().getParent()).delDp(name); should be
		// enough, but the new method is more flexible when the Highchart is
		// wrapped with more widgets (but less than ten)
		Widget parent = getThis().getParent();
		for (int i = 0; i < PARENT_WIDGETS; i++) {
			if (parent instanceof ChartWrapper) {
				((ChartWrapper) parent).delDp(name);
				break;
			}
			parent = parent.getParent();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * bpi.most.client.modules.charts.ChartInterface#getPointCount(java.lang
	 * .String)
	 */
	@Override
	public int getPointCount(String name) {
		int count = 0;
		Series series = getSeries(name);
		if (series != null) {
			count = series.getPoints().length;
		}
		return count;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * bpi.most.client.modules.charts.ChartInterface#setPeriodicFlag(java.lang
	 * .String, boolean)
	 */
	@Override
	public void setPeriodicFlag(String name, boolean flag) {
		getCurve(name).setPeriodicFlag(flag);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * bpi.most.client.modules.charts.ChartInterface#getPeriodicFlag(java.lang
	 * .String)
	 */
	@Override
	public boolean getPeriodicFlag(String name) {
		return getCurve(name).getPeriodicFlag();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * bpi.most.client.modules.charts.ChartInterface#isInChart(java.lang.String)
	 */
	@Override
	public boolean isInChart(String name) {
		for (int i = 0; i < curveList.size(); i++) {
			if (curveList.get(i).name.equalsIgnoreCase(name)) {
				return true;
			}
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bpi.most.client.modules.charts.ChartInterface#isPeriodic()
	 */
	@Override
	public boolean isPeriodic() {
		for (int i = 0; i < curveList.size(); i++) {
			if (curveList.get(i).getPeriodicFlag()) {
				return true;
			}
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bpi.most.client.modules.charts.ChartInterface#getZoomStart()
	 */
	@Override
	public Date getZoomStart() {
		Date start;
		if (chart.getXAxis().getExtremes().getMin().longValue() < chart
				.getXAxis().getExtremes().getDataMin().longValue()) {
			start = new Date(chart.getXAxis().getExtremes().getDataMin()
					.longValue());
		} else {
			start = new Date(chart.getXAxis().getExtremes().getMin()
					.longValue());
		}
		return start;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bpi.most.client.modules.charts.ChartInterface#getZoomEnd()
	 */
	@Override
	public Date getZoomEnd() {
		Date end;
		if (chart.getXAxis().getExtremes().getMax().longValue() > chart
				.getXAxis().getExtremes().getDataMax().longValue()) {
			end = new Date(chart.getXAxis().getExtremes().getDataMax()
					.longValue());
		} else {
			end = new Date(chart.getXAxis().getExtremes().getMax().longValue());
		}
		return end;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * bpi.most.client.modules.charts.ChartInterface#showLoading(java.lang.String
	 * )
	 */
	@Override
	public void showLoading(String text) {
		if (isLoadingCounter >= 0) {
			isLoadingTimer.cancel();
			isLoadingTimer.schedule(LOADING_TIMER_SCHEDULE);
		}
		isLoadingCounter++;
		chart.showLoading(text);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bpi.most.client.modules.charts.ChartInterface#hideLoading()
	 */
	@Override
	public void hideLoading() {
		isLoadingCounter--;
		if (isLoadingCounter <= 0) {
			isLoadingTimer.cancel();
			chart.hideLoading();
			// to prevent a counter smaller as 0
			isLoadingCounter = 0;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bpi.most.client.modules.charts.ChartInterface#redraw()
	 */
	@Override
	public void redraw() {
		chart.redraw();
	}

	/**
	 * JSNI: Used for setting global options in the java script code of the
	 * chart for which there is no wrapper yet.
	 */
	public static native void setGlobalOptions() /*-{
		$wnd.Highcharts.setOptions({
			global : {
				useUTC : false
			}
		});
	}-*/;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * bpi.most.client.modules.charts.ChartInterface#isPeriodic(java.lang.String
	 * )
	 */
	@Override
	public boolean isPeriodic(String name) {
		return getCurve(name).periodicFlag;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * bpi.most.client.modules.charts.ChartInterface#removeValuesAtStart(java
	 * .lang.String, int)
	 */
	@Override
	public int removeValuesAtStart(String name, int number) {
		Series series = getSeries(name);
		int serieslength = series.getPoints().length;
		if (number >= serieslength) {
			Point[] pointsnew = new Point[0];
			series.setPoints(pointsnew);
			return serieslength;
		} else {
			Point[] pointsnew = new Point[serieslength - number];
			for (int i = number, j = 0; i < serieslength; i++, j++) {
				pointsnew[j] = new Point(series.getPoints()[i].getX(),
						series.getPoints()[i].getY());
			}
			series.setPoints(pointsnew);
			return number;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * bpi.most.client.modules.charts.ChartInterface#removeValuesAtEnd(java.
	 * lang.String, int)
	 */
	@Override
	public int removeValuesAtEnd(String name, int number) {
		Series series = getSeries(name);
		int serieslength = series.getPoints().length;
		if (number >= serieslength) {
			Point[] pointsnew = new Point[0];
			series.setPoints(pointsnew);
			return serieslength;
		} else {
			Point[] pointsnew = new Point[serieslength - number];
			for (int i = number, j = 0; i < serieslength; i++, j++) {
				pointsnew[j] = new Point(series.getPoints()[j].getX(),
						series.getPoints()[j].getY());
			}
			series.setPoints(pointsnew);
			return number;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * bpi.most.client.modules.charts.ChartInterface#getDataset(java.lang.String
	 * )
	 */
	@Override
	public DpDatasetDTO getDataset(String name) {
		Series series = getSeries(name);
		DpDatasetDTO tempdataset = null;
		if (series != null) {
			tempdataset = new DpDatasetDTO(name);
			for (int i = 0; i < series.getPoints().length; i++) {
				tempdataset.add(new DpDataDTO(new Date((Long) series
						.getPoints()[i].getY()), (Double) series.getPoints()[i]
						.getX()));
			}
		}
		return tempdataset;
	}
}
