package bpi.most.client.modules.charts;

import bpi.most.client.model.Datapoint;
import bpi.most.client.model.DatapointHandler;
import bpi.most.client.modules.ModuleController;
import bpi.most.client.modules.charts.Highchart.HighchartStandart;
import bpi.most.client.utils.Observable;
import bpi.most.client.utils.Observer;
import bpi.most.client.utils.dnd.DNDController;
import bpi.most.client.utils.dnd.MostDragEndEvent;
import bpi.most.client.utils.ui.DateTimePickerBox;
import bpi.most.client.utils.ui.DpWidget;
import bpi.most.dto.DpDataDTO;
import bpi.most.dto.DpDatasetDTO;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;

import java.util.ArrayList;
import java.util.Date;

/**
 * This class handles the interaction with the chart independently from which
 * chart implementation you're using.
 * 
 * @author mike
 * 
 */
public class ChartWrapper extends Composite implements ChartInterface, Observer {

	/**
	 * Specifies the maximum of points the series can contain.
	 */
	private static final int MAXPOINTS = 100;

	/**
	 * Flag if live data is used (true; rpc-sql-calls) or demo data is used
	 * (false; generates random data).
	 */
	private static final boolean LIVEDATA = true;
	
	private static final int MILLISECONDS_TWO_DAYS = 172800000;

	/**
	 * The date that is used as start date in this chart wrapper.
	 */
	private Date startDate;

	/**
	 * The date that is used as end date in this chart wrapper.
	 */
	private Date endDate;

	/**
	 * List of current attached data points
	 */
	private ArrayList<Datapoint> dplist = new ArrayList<Datapoint>();

	/**
	 * Timer for blinking effect of the estimation button
	 */
	private Timer timerEstBut;

	/**
	 * The chartwrapper UI binder
	 */
	private static ChartWrapperUiBinder uiBinder = GWT
			.create(ChartWrapperUiBinder.class);
	
	interface ChartWrapperUiBinder extends UiBinder<Widget, ChartWrapper> {
	}

	/**
	 * The chosenChart contains the used chart implementation widget.
	 */
	@UiField(provided = true)
	Widget chosenChart;
	@UiField(provided = true)
	DateTimePickerBox startTime;
	@UiField(provided = true)
	DateTimePickerBox endTime;
	@UiField
	Button estimatedButton;
	@UiField
	CheckBox chkBox;
	@UiField
	ListBox listBox;
	@UiField
	FlowPanel flowPanel;

	/**
	 * Attach live listeners to current
	 * 
	 */
	private void attachLiveListener() {
		for (final Datapoint dataPoint : dplist) {
			dataPoint.addObserver(this);
		}
	}

	private void detachLiveListener() {
		for (final Datapoint dataPoint : dplist) {
			dataPoint.deleteObserver(this);
		}
	}

	/**
	 * Creates an new instance of {@link ChartWrapper} and set the start and end
	 * date to the defaults of one hour before the current time and the current
	 * time. The chart implementation used is set to the default.(
	 * {@link HighchartStandart})
	 */
	public ChartWrapper() {
		initChartWrapper();
	}

	/**
	 * Creates an new instance of {@link ChartWrapper} and set the start date to
	 * the given date. The end date is set either to 2 days from the start date
	 * or the current time if that time lies in the future. The chart
	 * implementation used is set to the default.({@link HighchartStandart})
	 * 
	 * @param startTime
	 *            The start date the chart should start with.
	 */
	public ChartWrapper(Date startTime) {
		initChartWrapper();
		setStartDate(startTime);
		Date enddate;
		if ((startTime.getTime() + MILLISECONDS_TWO_DAYS) > (new Date().getTime())) {
			enddate = new Date();
		} else {
			enddate = new Date(startTime.getTime() + MILLISECONDS_TWO_DAYS);
		}
		setEndDate(enddate);
	}

	/**
	 * Creates an new instance of {@link ChartWrapper} and set the start and end
	 * date to the given dates. The chart implementation used is set to the
	 * default.({@link HighchartStandart})
	 * 
	 * @param startTime
	 *            The start date the chart should start with.
	 * @param endTime
	 *            The end date the chart should end with.
	 */
	public ChartWrapper(Date startTime, Date endTime) {
		initChartWrapper();
		setStartDate(startTime);
		setEndDate(endTime);
	}

	/**
	 * Same as the {@link #ChartWrapper()} but the chart implementation used is
	 * handed over.
	 * 
	 * @param chart
	 *            The chart implementation handed over to use.
	 */
	public ChartWrapper(Widget chart) {
		initChartWrapper();
		setChoosenChart(chart);
	}

	/**
	 * Same as the {@link #ChartWrapper(Date)} but the chart implementation used
	 * is handed over.
	 * 
	 * @param chart
	 *            The chart implementation handed over to use.
	 * @param startTime
	 *            The start date the chart should start with.
	 */
	public ChartWrapper(Widget chart, Date startTime) {
		initChartWrapper();
		setChoosenChart(chart);
		setStartDate(startTime);
		Date enddate;
		if ((startTime.getTime() + 172800000) > (new Date().getTime())) {
			enddate = new Date();
		} else {
			enddate = new Date(startTime.getTime() + 172800000);
		}
		setEndDate(enddate);
	}

	/**
	 * Same as the {@link #ChartWrapper(Date, Date)} but the chart
	 * implementation used is handed over.
	 * 
	 * @param chart
	 *            The chart implementation handed over to use.
	 * @param startTime
	 *            The start date the chart should start with.
	 * @param endTime
	 *            The end date the chart should end with.
	 */
	public ChartWrapper(Widget chart, Date startTime, Date endTime) {
		initChartWrapper();
		setChoosenChart(chart);
		setStartDate(startTime);
		setEndDate(endTime);
	}

	/**
	 * Initialize the ChartWrapper.
	 */
	private void initChartWrapper() {
		setChoosenChart(new HighchartStandart());
		startDate = new Date((new Date().getTime() - 3600000));
		startTime = new DateTimePickerBox(startDate) {

			@Override
			public void onDateChange(Date olddate, Date date) {
				startDate = date;
				eventTimeStart(olddate, date);
			}

		};
		endDate = new Date();
		endTime = new DateTimePickerBox(endDate) {

			@Override
			public void onDateChange(Date olddate, Date date) {
				endDate = date;
				eventTimeEnd(olddate, date);
			}

		};
		initWidget(uiBinder.createAndBindUi(this));
		DOM.setStyleAttribute(getElement(), "overflow", "hidden");
		flowPanel.addDomHandler(new DragOverHandler() {

			@Override
			public void onDragOver(DragOverEvent event) {
				// needed for DnD drop functionality
			}
		}, DragOverEvent.getType());
		flowPanel.addDomHandler(new DropHandler() {

			@Override
			public void onDrop(DropEvent event) {
				event.preventDefault();
				DNDController.setDragoverNull();
				if (DNDController.getDragwindow() == null && (DNDController.getDragitem() instanceof DpWidget)){
					// handle the drop of anything else, maybe check
					// DNDController.getDragwidget and maybe pass-through to the
					// widget in the SimplePanel
					dropEventHandler();
				}
				DNDController.setDragoverNull();
				try {
					DNDController.EVENT_BUS.fireEvent(new MostDragEndEvent());
				} catch (Exception e) {

				}
			}
		}, DropEvent.getType());

		timerEstBut = new Timer() {

			@Override
			public void run() {
				if (estimatedButton.getElement().getStyle().getColor()
						.contains("red")) {
					estimatedButton.getElement().getStyle().setColor("black");
				} else {
					estimatedButton.getElement().getStyle().setColor("red");
				}
			}
		};
		timerEstBut.scheduleRepeating(1500);
		checkEstimatedButton();
	}

	/**
	 * Set the start date-time to the given date.
	 * 
	 * @param date
	 *            The date that should be used as start date-time.
	 */
	private void setStartDate(Date date) {
		startDate = date;
		startTime.setDate(startDate, false);
		this.onUnload();
	}

	/**
	 * Set the end date-time to the given date.
	 * 
	 * @param date
	 *            The date that should be used as end date-time.
	 */
	private void setEndDate(Date date) {
		endDate = date;
		endTime.setDate(endDate, false);
	}

	/**
	 * Set the {@link #chosenChart} widget.
	 * 
	 * @param chart
	 *            The widget that should be used as the new chosenChart. Should
	 *            implement the {@link ChartInterface}.
	 */
	private void setChoosenChart(Widget chart) {
		chosenChart = chart;
	}

	// The change handler for the listBox that set the time frame for the live
	// feature.
	@UiHandler("listBox")
	void changeHandler(ChangeEvent e) {
		if (chkBox.getValue()) {
			setLiveTimeFrame(Long.valueOf(listBox.getValue(listBox
					.getSelectedIndex())));
		}
	}

	// The change handler for the chkBox that toggles the live function.
	@UiHandler("chkBox")
	void valueChangeHandler(ValueChangeEvent<Boolean> e) {
		if (e.getValue()) {
			// is true
			startLiveFeature(Long.valueOf(listBox.getValue(listBox
					.getSelectedIndex())));
		} else {
			// is false
			endLiveFeature();
		}
	}

	/**
	 * Method to start the live chart feature.
	 * 
	 * @param value
	 *            The value of the live data time frame in milliseconds.
	 */
	private void startLiveFeature(Long value) {
		if (chkBox.getValue()) {
			startTime.setEnabled(false);
			endTime.setEnabled(false);
			estimatedButton.setVisible(false);
			setLiveTimeFrame(value);
			// attach live listener
			attachLiveListener();
		}
	}

	/**
	 * Method to stop the live chart feature and redraw the chart with the old
	 * values in the selected time frame.
	 */
	private void endLiveFeature() {
		if (!chkBox.getValue()) {
			// TODO remove dp's from observer
			startTime.setEnabled(true);
			endTime.setEnabled(true);
			removeAllSeries();
			for (int i = 0; i < dplist.size(); i++) {
				final Datapoint dptemp = dplist.get(i);
				showLoading("get data");
				((Datapoint) dptemp).getNumberOfValues(startDate, endDate,
						new DatapointHandler((Object) getThis(),
								(Datapoint) dptemp) {
							public void onSuccess(int result) {
								// Window.alert("" + result); // for fast
								// testing
								if (result > MAXPOINTS) {
									for (int i = 0; i < 720; i++) {
										if (300 * i > (((endDate.getTime() / 1000) - (startDate
												.getTime() / 1000)) / MAXPOINTS)) {
											((Datapoint) dptemp)
													.getDataPeriodic(
															startDate,
															endDate,
															new Float(i * 300.0),
															new DatapointHandler(
																	(Object) this,
																	(Datapoint) dptemp) {
																public void onSuccess(
																		DpDatasetDTO dataSet) {
																	addCurve(
																			dataSet,
																			true);
																	hideLoading();
																}
															});
											break;
										}
									}
								} else {
									((Datapoint) dptemp).getData(startDate,
											endDate, new DatapointHandler(
													(Object) getThis(),
													(Datapoint) dptemp) {
												public void onSuccess(
														DpDatasetDTO dataSet) {
													addCurve(dataSet, false);
													hideLoading();
												}
											});
								}
							}
						});
			}
		}
		checkEstimatedButton();
		// detach live listeners
		detachLiveListener();
	}

	/**
	 * Set the time frame of the live chart. Either at the start or when another
	 * time frame is selected.
	 * 
	 * @param value
	 *            The value of the time frame in milliseconds.
	 */
	private void setLiveTimeFrame(final Long value) {
		// TODO find global solution for time zone problem
		@SuppressWarnings("deprecation")
		final Date liveFrameStarttime = new Date(new Date().getTime() - value
				+ (new Date().getTimezoneOffset()*60000));
		@SuppressWarnings("deprecation")
		final Date liveFrameEndtime = new Date(System.currentTimeMillis() + (new Date().getTimezoneOffset()*60000));

		if (chkBox.getValue()) {
			if (LIVEDATA) {
				removeAllSeries();
				for (int i = 0; i < dplist.size(); i++) {
					showLoading("get live data");
					final Datapoint dptemp = dplist.get(i);
					((Datapoint) dptemp).getNumberOfValues(liveFrameStarttime, liveFrameEndtime,
							new DatapointHandler((Object) getThis(),
									(Datapoint) dptemp) {

								@Override
								public void onSuccess(int result) {
									if (result > MAXPOINTS) {
										((Datapoint) dptemp).getDataPeriodic(
												liveFrameStarttime, liveFrameEndtime, new Float(
														(value / 1000) / 100),
												new DatapointHandler(
														(Object) getThis(),
														(Datapoint) dptemp) {
													public void onSuccess(
															DpDatasetDTO result) {
														addCurve(result, true);
														hideLoading();
													}
												});
									} else {
										((Datapoint) dptemp).getData(liveFrameStarttime,
												liveFrameEndtime, new DatapointHandler(
														(Object) getThis(),
														(Datapoint) dptemp) {
													public void onSuccess(
															DpDatasetDTO dataSet) {
														addCurve(dataSet, false);
														hideLoading();
													}
												});
									}
								}

							});

				}
			} else {
				// generate random live data
				removeAllSeries();
				int values;
				Long deltamin;
				Long deltamax;
				if ((int) ((value / 1000) / 900) > MAXPOINTS) {
					values = MAXPOINTS;
					Long period = (Long) (value / 100);
					deltamin = period - (period / 2);
					deltamax = period + (period / 2);
				} else {
					values = (int) (value / 900000);
					deltamin = 600000L;
					deltamax = 1200000L;
				}
				Long startdatevalue = ((new Date().getTime()) - value);
				for (int i = 0; i < dplist.size(); i++) {
					showLoading("get generated live data");
					Date tempdate = new Date(startdatevalue);
					Long tempdatevalue = startdatevalue;
					DpDatasetDTO tempdataset = new DpDatasetDTO(dplist.get(i)
							.getDatapointName());
					for (int j = 0; j < values; j++) {
						if (tempdate.before(new Date())) {
							DpDataDTO tempdata = new DpDataDTO(tempdate,
									Random.nextDouble() * 500);
							tempdataset.add(tempdata);
						} else {
							break;
						}
						tempdatevalue = tempdatevalue + deltamin
								+ Random.nextInt((int) (deltamax - deltamin));
						tempdate = new Date(tempdatevalue);
					}
					addCurve(tempdataset, false);
					hideLoading();
				}
			}
		}
	}

	/**
	 * Method that checks if every data point is still in the time frame. If not
	 * it removes them.
	 * 
	 * @param value
	 *            The value of the time frame in milliseconds.
	 */
	private void checkLiveTimeFrame(Long value) {
		// TODO find global solution for time zone problem
		// TODO add time zone support to ChartWrapper
		if (chkBox.getValue()) {
			boolean flag = false;
			@SuppressWarnings("deprecation")
			final Date newLiveframeStartDate = new Date((new Date().getTime())
					- value - (new Date().getTimezoneOffset()*60000));
			for (int i = 0; i < dplist.size(); i++) {
				if (getStartDate(dplist.get(i).getDatapointName()).before(
						newLiveframeStartDate)) {
					flag = true;
					break;

				}
			}
			if (flag) {
				// 86400000 is just a value big enough to be sure it's far
				// enough in the past to delete all old data points
				delDataset(
						new Date(newLiveframeStartDate.getTime() - 86400000),
						newLiveframeStartDate);
			}
		}
	}

	// The click handler for the estimated values button.
	@UiHandler("estimatedButton")
	void handleClick(ClickEvent e) {
		final Date zoomStartDate = getZoomStart();
		final Date zoomEndDate = getZoomEnd();
		for (int i = 0; i < dplist.size(); i++) {
			showLoading("get new data");
			final Datapoint dptemp = dplist.get(i);
			dptemp.getNumberOfValues(
					zoomStartDate,
					zoomEndDate,
					new DatapointHandler((Object) getThis(), (Datapoint) dptemp) {
						public void onSuccess(int result) {
							// Window.alert("" + result); // for fast
							// testing
							if (result > MAXPOINTS) {
								for (int i = 0; i < 720; i++) {
									if (300 * i > (((zoomEndDate.getTime() / 1000) - (getZoomStart()
											.getTime() / 1000)) / MAXPOINTS)) {
										dptemp.getDataPeriodic(zoomStartDate,
												zoomEndDate, new Float(
														i * 300.0),
												new DatapointHandler(
														(Object) this,
														(Datapoint) dptemp) {
													public void onSuccess(
															DpDatasetDTO dataSet) {
														setDataset(dataSet,
																true);
														hideLoading();
													}
												});
										break;
									}
								}
							} else {
								dptemp.getData(zoomStartDate, zoomEndDate,
										new DatapointHandler(
												(Object) getThis(),
												(Datapoint) dptemp) {
											public void onSuccess(
													DpDatasetDTO dataSet) {
												setDataset(dataSet, false);
												hideLoading();
											}
										});
							}
						}
					});

		}
		startTime.date = zoomStartDate;
		startTime.olddate = zoomStartDate;
		startDate = zoomStartDate;
		endTime.date = zoomEndDate;
		endTime.olddate = zoomEndDate;
		endDate = zoomEndDate;
		startTime.upDateBox();
		endTime.upDateBox();
		if (chkBox.getValue()) {
			chkBox.setValue(false, false);
			startTime.setEnabled(true);
			endTime.setEnabled(true);
			checkEstimatedButton();
			// detach live listeners
			detachLiveListener();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * bpi.most.client.modules.charts.ChartInterface#addCurve(java.lang.String)
	 */
	@Override
	public void addCurve(String name) {
		((ChartInterface) chosenChart).addCurve(name);
		checkEstimatedButton();
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
		((ChartInterface) chosenChart).addCurve(dpdataset);
		checkEstimatedButton();

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
		((ChartInterface) chosenChart).addCurve(dpdataset, periodicFlag);
		checkEstimatedButton();
	}

	/**
	 * Method for adding a single data point value to the series with the given
	 * name. (Warning: There is no check if the data is chronological
	 * consecutive. Time leaps are possible!)
	 * 
	 * @param name
	 *            The name of the series you want to add the data to.
	 * @param data
	 *            The {@link DpDataDTO} that contains the data.
	 */
	public void addValue(String name, DpDataDTO data) {
		addValue(name, data.getTimestamp(), data.getValue());
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
		((ChartInterface) chosenChart).addValue(name, date, value);
		checkEstimatedButton();
		checkLiveTimeFrame(Long.valueOf(listBox.getValue(listBox
				.getSelectedIndex())));
	}

	/**
	 * Get the reference to this instance of {@link ChartWrapper}.
	 * 
	 * @return The reference of this {@link ChartWrapper} instance.
	 */
	public ChartWrapper getThis() {
		return this;
	}

	/**
	 * Handles what happens if a {@link SensorLabel} is dropped at this
	 * {@link ChartWrapper}.
	 * 
	 * @param event
	 *            The {@link DropEvent} that is handed over by the
	 *            {@link DropHandler} of this class.
	 */
	@SuppressWarnings("deprecation")
	public void dropEventHandler() {
		if (DNDController.getDragitem() instanceof DpWidget) {
			final Datapoint dptemp = ModuleController.DPCC
					.getDatapoint(((DpWidget) DNDController.getDragitem())
							.getText());
			dplist.add(dptemp);
			if (((ChartInterface) chosenChart).isInChart(dptemp
					.getDatapointName())) {
				Window.alert(dptemp.getDatapointName()
						+ " is already in chart!");
			} else {
				if (!chkBox.getValue()) {
					showLoading("get data");
					((Datapoint) dptemp).getNumberOfValues(startDate, endDate,
							new DatapointHandler((Object) getThis(),
									(Datapoint) dptemp) {
								public void onSuccess(int result) {
									if (result > MAXPOINTS) {
										for (int i = 0; i < 720; i++) {
											if (300 * i > (((endDate.getTime() / 1000) - (startDate
													.getTime() / 1000)) / MAXPOINTS)) {
												((Datapoint) dptemp)
														.getDataPeriodic(
																startDate,
																endDate,
																new Float(
																		i * 300.0),
																new DatapointHandler(
																		(Object) this,
																		(Datapoint) dptemp) {
																	public void onSuccess(
																			DpDatasetDTO dataSet) {
																		addCurve(
																				dataSet,
																				true);
																		hideLoading();
																	}
																});
												break;
											}
										}
									} else {
										((Datapoint) dptemp).getData(startDate,
												endDate, new DatapointHandler(
														(Object) getThis(),
														(Datapoint) dptemp) {
													public void onSuccess(
															DpDatasetDTO dataSet) {
														addCurve(dataSet, false);
														hideLoading();
													}
												});
									}
								}
							});
				} else {
					// TODO add dp to observer
					// TODO find global solution for time zone problem
					Long value = Long.valueOf(listBox.getValue(listBox
							.getSelectedIndex()));
					if (LIVEDATA) {
						showLoading("get live data");
						((Datapoint) dptemp).getData(
								new Date(System.currentTimeMillis() - value + (new Date().getTimezoneOffset()*60000)),
								new Date(System.currentTimeMillis() + (new Date().getTimezoneOffset()*60000)),
								new DatapointHandler((Object) getThis(),
										(Datapoint) dptemp) {
									public void onSuccess(DpDatasetDTO dataSet) {
										addCurve(dataSet, false);
										hideLoading();
									}
								});

					} else {
						showLoading("get generated live data");
						int values = (int) (value / 900000);
						int deltamin = 600000;
						int deltamax = 1200000;
						Long startdatevalue = ((new Date().getTime()) - value);
						Date tempdate = new Date(startdatevalue);
						Long tempdatevalue = startdatevalue;
						DpDatasetDTO tempdataset = new DpDatasetDTO(
								dptemp.getDatapointName());
						for (int j = 0; j < values; j++) {
							if (tempdate.before(new Date())) {
								DpDataDTO tempdata = new DpDataDTO(tempdate,
										Random.nextDouble() * 500);
								tempdataset.add(tempdata);
							} else {
								break;
							}
							tempdatevalue = tempdatevalue + deltamin
									+ Random.nextInt(deltamax - deltamin);
							tempdate = new Date(tempdatevalue);
						}
						addCurve(tempdataset, false);
						hideLoading();
					}
				}
			}
		}
		DNDController.setDragitemNull();
	}

	/**
	 * Method that is called when the start date-time changed.
	 * 
	 * @param olddate
	 *            The old value of the start date-time.
	 * @param date
	 *            The new value of the start date-time.
	 */
	private void eventTimeStart(final Date olddate, final Date date) {
		if (olddate.getTime() != date.getTime()) {
            if (olddate.getTime() < date.getTime()) {
                delDataset(olddate, date);
            } else if (olddate.getTime() > date.getTime()) {
                for (int i = 0; i < dplist.size(); i++) {
                    showLoading("get new data");
                    if (((ChartInterface) chosenChart).getPeriodicFlag(dplist
                            .get(i).getDatapointName())) {
                        final Datapoint dptemp = dplist.get(i);
                        dptemp.getNumberOfValues(startDate, endDate,
                                new DatapointHandler((Object) getThis(), dptemp) {
                                    public void onSuccess(int result) {
                                        if (result > MAXPOINTS) {
                                            for (int i = 0; i < 720; i++) {
                                                if (300 * i > (((endDate.getTime() / 1000) - (startDate
                                                        .getTime() / 1000)) / MAXPOINTS)) {
                                                    dptemp.getDataPeriodic(
                                                            startDate, endDate,
                                                            new Float(i * 300.0),
                                                            new DatapointHandler(
                                                                    (Object) this,
                                                                    dptemp) {
                                                                public void onSuccess(
                                                                        DpDatasetDTO dataSet) {
                                                                    setDataset(
                                                                            dataSet,
                                                                            true);
                                                                    hideLoading();
                                                                }
                                                            });
                                                    break;
                                                }
                                            }
                                        } else {
                                            dptemp.getData(startDate, endDate,
                                                    new DatapointHandler(
                                                            (Object) getThis(),
                                                            dptemp) {
                                                        public void onSuccess(
                                                                DpDatasetDTO dataSet) {
                                                            setDataset(dataSet,
                                                                    false);
                                                            hideLoading();
                                                        }
                                                    });
                                        }
                                    }
                                });
                    } else {
                        final Datapoint dptemp = dplist.get(i);
                        dptemp.getNumberOfValues(date, olddate,
                                new DatapointHandler((Object) getThis(), dptemp) {
                                    public void onSuccess(int result) {
                                        if (((((ChartInterface) chosenChart)
                                                .getPointCount(dptemp
                                                        .getDatapointName())) + result) > MAXPOINTS) {
                                            for (int i = 0; i < 720; i++) {
                                                if (300 * i > (((endDate.getTime() / 1000) - (startDate
                                                        .getTime() / 1000)) / MAXPOINTS)) {
                                                    dptemp.getDataPeriodic(
                                                            startDate, endDate,
                                                            new Float(i * 300.0),
                                                            new DatapointHandler(
                                                                    (Object) this,
                                                                    dptemp) {
                                                                public void onSuccess(
                                                                        DpDatasetDTO dataSet) {
                                                                    setDataset(
                                                                            dataSet,
                                                                            true);
                                                                    hideLoading();
                                                                }
                                                            });
                                                    break;
                                                }
                                            }
                                        } else {
                                            dptemp.getData(date, olddate,
                                                    new DatapointHandler(
                                                            (Object) getThis(),
                                                            dptemp) {
                                                        public void onSuccess(
                                                                DpDatasetDTO dataSet) {
                                                            prependDataset(dataSet);
                                                            hideLoading();
                                                        }
                                                    });
                                        }
                                    }
                                });
                    }
                }
            }
        }
	}

	/**
	 * Method that is called when the end date-time changed.
	 * 
	 * @param olddate
	 *            The old value of the start date-time.
	 * @param date
	 *            The new value of the start date-time.
	 */
	private void eventTimeEnd(final Date olddate, final Date date) {
		if (olddate.getTime() != date.getTime()) {
            if (olddate.getTime() < date.getTime()) {
                for (int i = 0; i < dplist.size(); i++) {
                    showLoading("get new data");
                    if (((ChartInterface) chosenChart).getPeriodicFlag(dplist
                            .get(i).getDatapointName())) {
                        final Datapoint dptemp = dplist.get(i);
                        dptemp.getNumberOfValues(startDate, endDate,
                                new DatapointHandler((Object) getThis(), dptemp) {
                                    public void onSuccess(int result) {
                                        if (result > MAXPOINTS) {
                                            for (int i = 0; i < 720; i++) {
                                                if (300 * i > (((endDate.getTime() / 1000) - (startDate
                                                        .getTime() / 1000)) / MAXPOINTS)) {
                                                    dptemp.getDataPeriodic(
                                                            startDate, endDate,
                                                            new Float(i * 300.0),
                                                            new DatapointHandler(
                                                                    (Object) this,
                                                                    dptemp) {
                                                                public void onSuccess(
                                                                        DpDatasetDTO dataSet) {
                                                                    setDataset(
                                                                            dataSet,
                                                                            true);
                                                                    hideLoading();
                                                                }
                                                            });
                                                    break;
                                                }
                                            }
                                        } else {
                                            dptemp.getData(startDate, endDate,
                                                    new DatapointHandler(
                                                            (Object) getThis(),
                                                            dptemp) {
                                                        public void onSuccess(
                                                                DpDatasetDTO dataSet) {
                                                            setDataset(dataSet,
                                                                    false);
                                                            hideLoading();
                                                        }
                                                    });
                                        }
                                    }
                                });
                    } else {
                        final Datapoint dptemp = dplist.get(i);
                        dptemp.getNumberOfValues(olddate, date,
                                new DatapointHandler((Object) getThis(), dptemp) {
                                    public void onSuccess(int result) {
                                        if (((((ChartInterface) chosenChart)
                                                .getPointCount(dptemp
                                                        .getDatapointName())) + result) > MAXPOINTS) {
                                            for (int i = 0; i < 720; i++) {
                                                if (300 * i > (((endDate.getTime() / 1000) - (startDate
                                                        .getTime() / 1000)) / MAXPOINTS)) {
                                                    dptemp.getDataPeriodic(
                                                            startDate, endDate,
                                                            new Float(i * 300.0),
                                                            new DatapointHandler(
                                                                    (Object) this,
                                                                    dptemp) {
                                                                public void onSuccess(
                                                                        DpDatasetDTO dataSet) {
                                                                    setDataset(
                                                                            dataSet,
                                                                            true);
                                                                    hideLoading();
                                                                }
                                                            });
                                                    break;
                                                }
                                            }
                                        } else {
                                            dptemp.getData(olddate, date,
                                                    new DatapointHandler(
                                                            (Object) getThis(),
                                                            dptemp) {
                                                        public void onSuccess(
                                                                DpDatasetDTO dataSet) {
                                                            appendDataset(dataSet);
                                                            hideLoading();
                                                        }
                                                    });
                                        }
                                    }
                                });
                    }
                }
            } else if (olddate.getTime() > date.getTime()) {
                delDataset(date, olddate);
            }
        }
	}

	/**
	 * 
	 * Method that adds the data of the {@link DpDatasetDTO} to the chart at the
	 * right position.
	 * 
	 * @deprecated Old all-in-one add data function. Because of the complexity
	 *             of the add data task this function would grown very large, so
	 *             this task is split to several methods.
	 * 
	 * @param dpdataset
	 *            The {@link DpDatasetDTO} with the data you want to add.
	 * @return Returns an int value that tells if and where the data was added.<br>
	 *         -1 = failure<br>
	 *         1 = new data point, data was added to the chart<br>
	 *         2 = data added at the beginning of the series of the
	 *         corresponding data point<br>
	 *         3 = data added at the end of the series of the corresponding data
	 *         point<br>
	 */
	@Deprecated
	public int addDataset(DpDatasetDTO dpdataset) {
		if (dpdataset != null) {
			if (((ChartInterface) chosenChart).getCurveList().size() > 0) {
				for (int i = 0; i < ((ChartInterface) chosenChart)
						.getCurveList().size(); i++) {
					if (((ChartInterface) chosenChart).getCurveList().get(i)
							.getName().compareTo(dpdataset.getDatapointName()) == 0) {
						// data point is already in chart
						if (dpdataset.get(0).getTimestamp().getTime() <= ((ChartInterface) chosenChart)
								.getStartDate(dpdataset.getDatapointName())
								.getTime()) {
							DpDatasetDTO dpdatasetcut = new DpDatasetDTO(
									dpdataset.getDatapointName());
							for (int j = 0; j < dpdataset.size(); j++) {
								if (dpdataset.get(j).getTimestamp().getTime() < ((ChartInterface) chosenChart)
										.getStartDate(
												dpdataset.getDatapointName())
										.getTime()) {
									dpdatasetcut.add(dpdataset.get(j));
								}
							}
							prependDataset(dpdatasetcut);
							return 2; // return 2: in chart and data added at
										// the beginning
						} else if (dpdataset.get(dpdataset.size() - 1)
								.getTimestamp().getTime() >= ((ChartInterface) chosenChart)
								.getEndDate(dpdataset.getDatapointName())
								.getTime()) {
							DpDatasetDTO dpdatasetcut = new DpDatasetDTO(
									dpdataset.getDatapointName());
							for (int j = 0; j < dpdataset.size(); j++) {
								if (dpdataset.get(j).getTimestamp().getTime() > ((ChartInterface) chosenChart)
										.getEndDate(
												dpdataset.getDatapointName())
										.getTime()) {
									dpdatasetcut.add(dpdataset.get(j));
								}
							}
							appendDataset(dpdatasetcut);
							return 3; // return 3: in chart and data added at
										// the end
						}
					}

				}
				// data point is not in chart
				((ChartInterface) chosenChart).addCurve(dpdataset);
				return 1; // return 1: not in chart, added to it
			} else {
				((ChartInterface) chosenChart).addCurve(dpdataset);
				return 1; // return 1: not in chart, added to it
			}
		} else {
			Window.alert("dataset empty");
		}

		return -1; // return -1 on failure
	}

	/**
	 * Delete the data set from all series in the chart between the two dates
	 * and checks if the data is still needed to be generated periodic.
	 * 
	 * @param from
	 *            The start date from which you want to delete the data.
	 * @param to
	 *            The end date up to which you want to delete the data.
	 */
	public void delDataset(Date from, Date to) {
		for (int i = 0; i < dplist.size(); i++) {
			if (((ChartInterface) chosenChart).getPeriodicFlag(dplist.get(i)
					.getDatapointName())) {
				showLoading("get new data");
				final Datapoint dptemp = dplist.get(i);
				dptemp.getNumberOfValues(startDate, endDate,
						new DatapointHandler((Object) getThis(), dptemp) {
							public void onSuccess(int result) {
								if (result > MAXPOINTS) {
									for (int i = 0; i < 720; i++) {
										if (300 * i > (((endDate.getTime() / 1000) - (startDate
												.getTime() / 1000)) / MAXPOINTS)) {
											dptemp.getDataPeriodic(startDate,
													endDate, new Float(
															i * 300.0),
													new DatapointHandler(
															(Object) this,
															dptemp) {
														public void onSuccess(
																DpDatasetDTO dataSet) {
															setDataset(dataSet,
																	true);
															hideLoading();
														}
													});
											break;
										}
									}
								} else {
									dptemp.getData(
											startDate,
											endDate,
											new DatapointHandler(
													(Object) getThis(), dptemp) {
												public void onSuccess(
														DpDatasetDTO dataSet) {
													setDataset(dataSet, false);
													hideLoading();
												}
											});
								}
							}
						});
			} else {
				delDataset(dplist.get(i).getDatapointName(), from, to);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * bpi.most.client.modules.charts.ChartInterface#delDataset(java.lang.String
	 * , java.util.Date, java.util.Date)
	 */
	@Override
	public void delDataset(String name, Date from, Date to) {
		((ChartInterface) chosenChart).delDataset(name, from, to);
		checkEstimatedButton();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * bpi.most.client.modules.charts.ChartInterface#removeSeries(java.lang.
	 * String)
	 */
	@Override
	public void removeSeries(String name) {
		((ChartInterface) chosenChart).removeSeries(name);
		checkEstimatedButton();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * bpi.most.client.modules.charts.ChartInterface#removeSeries(java.lang.
	 * String, boolean)
	 */
	@Override
	public void removeSeries(String name, boolean redraw) {
		((ChartInterface) chosenChart).removeSeries(name, redraw);
		checkEstimatedButton();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bpi.most.client.modules.charts.ChartInterface#removeAllSeries()
	 */
	@Override
	public void removeAllSeries() {
		((ChartInterface) chosenChart).removeAllSeries();
		checkEstimatedButton();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * bpi.most.client.modules.charts.ChartInterface#removeAllSeries(boolean)
	 */
	@Override
	public void removeAllSeries(boolean redraw) {
		((ChartInterface) chosenChart).removeAllSeries(redraw);
		checkEstimatedButton();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * bpi.most.client.modules.charts.ChartInterface#getStartDate(java.lang.
	 * String)
	 */
	@Override
	public Date getStartDate(String name) {
		return ((ChartInterface) chosenChart).getStartDate(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * bpi.most.client.modules.charts.ChartInterface#getEndDate(java.lang.String
	 * )
	 */
	@Override
	public Date getEndDate(String name) {
		return ((ChartInterface) chosenChart).getEndDate(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bpi.most.client.modules.charts.ChartInterface#getCurveList()
	 */
	@Override
	public ArrayList<Curve> getCurveList() {
		return ((ChartInterface) chosenChart).getCurveList();
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
		((ChartInterface) chosenChart).appendDataset(dpdataset);
		checkEstimatedButton();
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
		((ChartInterface) chosenChart).prependDataset(dpdataset);
		checkEstimatedButton();
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
		((ChartInterface) chosenChart).appendData(name, dpdata);
		checkEstimatedButton();
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
		((ChartInterface) chosenChart).prependData(name, dpdata);
		checkEstimatedButton();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * bpi.most.client.modules.charts.ChartInterface#delDp(java.lang.String)
	 */
	@Override
	public void delDp(String name) {
		for (int i = 0; i < dplist.size(); i++) {
			if (dplist.get(i).getDatapointName().equalsIgnoreCase(name)) {
				dplist.remove(i);
			}
		}
		checkEstimatedButton();
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
		return ((ChartInterface) chosenChart).getPointCount(name);
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
		((ChartInterface) chosenChart).setPeriodicFlag(name, flag);
		checkEstimatedButton();
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
		checkEstimatedButton();
		return ((ChartInterface) chosenChart).getPeriodicFlag(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * bpi.most.client.modules.charts.ChartInterface#isInChart(java.lang.String)
	 */
	@Override
	public boolean isInChart(String name) {
		return ((ChartInterface) chosenChart).isInChart(name);
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
		((ChartInterface) chosenChart).setDataset(dpdataset, periodicFlag);
		checkEstimatedButton();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bpi.most.client.modules.charts.ChartInterface#isPeriodic()
	 */
	@Override
	public boolean isPeriodic() {
		if (((ChartInterface) chosenChart).isPeriodic()) {
			return true;
		}
		return false;
	}

	/**
	 * Method that checks if there is periodic generated data in the chart and
	 * shows or hides the "estimated values" button.
	 * 
	 * @return Returns if the button is visible(=true) or not(=false).
	 */
	public boolean checkEstimatedButton() {
		if (isPeriodic()) {
			estimatedButton.setVisible(true);
			return true;
		}
		estimatedButton.setVisible(false);
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bpi.most.client.modules.charts.ChartInterface#getZoomStart()
	 */
	@Override
	public Date getZoomStart() {
		return ((ChartInterface) chosenChart).getZoomStart();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bpi.most.client.modules.charts.ChartInterface#getZoomEnd()
	 */
	@Override
	public Date getZoomEnd() {
		return ((ChartInterface) chosenChart).getZoomEnd();
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
		((ChartInterface) chosenChart).showLoading(text);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bpi.most.client.modules.charts.ChartInterface#hideLoading()
	 */
	@Override
	public void hideLoading() {
		((ChartInterface) chosenChart).hideLoading();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bpi.most.client.modules.charts.ChartInterface#redraw()
	 */
	@Override
	public void redraw() {
		((ChartInterface) chosenChart).redraw();
	}

	/**
	 * Detach all live listeners
	 * 
	 * @TODO always called >>>> BEFORE <<<< a widget is attached and after the
	 *       widget is unloaded, should be called only after the widget is
	 *       unloaded
	 */
	@Override
	protected void onUnload() {
		// TODO call detachLiveListener() only when DragWidget is closed!
		// onUnload is called when the user switches between modules or moves
		// the chart, etc.
		// detachLiveListener();
		super.onUnload();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * bpi.most.client.modules.charts.ChartInterface#isPeriodic(java.lang.String
	 * )
	 */
	@Override
	public boolean isPeriodic(String name) {
		return ((ChartInterface) chosenChart).isPeriodic(name);
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
		return ((ChartInterface) chosenChart).removeValuesAtStart(name, number);
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
		return ((ChartInterface) chosenChart).removeValuesAtEnd(name, number);

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
		return ((ChartInterface) chosenChart).getDataset(name);
	}

	/**
	 * Get a {@link Datapoint} from the {@link #dplist} with the given name.
	 * 
	 * @param name
	 *            The name of the {@link Datapoint} you want from the list.
	 * @return Returns the {@link Datapoint} with the given name if it is in the
	 *         list, null otherwise.
	 */
	public Datapoint getDPFromList(String name) {
		for (int i = 0; i < dplist.size(); i++) {
			if (dplist.get(i).getDatapointName().equalsIgnoreCase(name)) {
				return dplist.get(i);
			}
		}
		return null;
	}

	/**
	 * Method to push a new {@link DpDataDTO} to the live chart. NOTE: Only
	 * works with live chart. (Use {@link #addValue(String, Date, Double)} if
	 * you want to add data to a normal chart instead.)
	 * 
	 * @param name
	 *            The name of the series the data belongs to.
	 * @param data
	 *            The {@link DpDataDTO} with the data.
	 */
	public void push(String name, DpDataDTO data) {
		if (chkBox.getValue()) {
			if (isPeriodic(name)) {
				Datapoint dptemp = getDPFromList(name);
				if (dptemp != null) {
					int periodmillis = (int) (Long.valueOf(listBox
							.getValue(listBox.getSelectedIndex())) / 100);
					if ((data.getTimestamp().getTime() - getEndDate(name)
							.getTime()) > periodmillis) {
						// last value out of one period after the last value
						dptemp.getDataPeriodic(new Date(getEndDate(name)
								.getTime() - 4 * periodmillis), new Date(
								getEndDate(name).getTime() + periodmillis),
								new Float(periodmillis / 1000),
								new DatapointHandler((Object) this,
										(Datapoint) dptemp) {
									@Override
									public void onSuccess(DpDatasetDTO result) {
										removeValuesAtStart(
												result.getDatapointName(), 1);
										addValue(result.getDatapointName(),
												result.get(result.size() - 1));
										hideLoading();
									}
								});
					} else {
						// last value within one period after the last value
						dptemp.getDataPeriodic(new Date(getEndDate(name)
								.getTime() - 5 * periodmillis),
								getEndDate(name),
								new Float(periodmillis / 1000),
								new DatapointHandler((Object) this,
										(Datapoint) dptemp) {
									@Override
									public void onSuccess(DpDatasetDTO result) {
										removeValuesAtEnd(
												result.getDatapointName(), 1);
										addValue(result.getDatapointName(),
												result.get(result.size() - 1));
										hideLoading();
									}
								});
					}
				} else {
					Window.alert("An error occured: push data to live chart.");
				}

			} else {
				addValue(name, data);
			}
		}
	}

	/**
	 * Called when a new measurement arrives
	 */
	public void update(Observable o, Object arg) {
		Datapoint dp = (Datapoint) o;
		DpDataDTO measurement = (DpDataDTO) arg;
		push(dp.getDatapointName(), measurement);
	}

	@Override
	protected void onLoad() {
		super.onLoad();
	}
}
