package bpi.most.client.modules.exporter.filter;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

import bpi.most.client.model.Datapoint;
import bpi.most.client.model.DatapointHandler;
import bpi.most.client.model.DpController;
import bpi.most.client.modules.ModuleController;
import bpi.most.dto.DpDTO;
import bpi.most.dto.DpDatasetDTO;

public class ExternalConditionsFilter extends Filter implements FilterInterface {

	public String condition1 = "enter value or drop data point";
	public String condition2 = "enter value or drop data point";
	public String compare = "smallerThan";
	private boolean cond1fetched = false;
	private boolean cond2fetched = false;

	public ExternalConditionsFilter() {
		super();
	}

	public ExternalConditionsFilter(String name) {
		super(name);
		createForm();
	}

	public ExternalConditionsFilter(String name, ExportFilterWidget parent) {
		super(name, parent);
		createForm();
	}

	@Override
	public DpDatasetDTO execute(DpDatasetDTO dpdataset) {
		DpDatasetDTO newDataset = new DpDatasetDTO(dpdataset.getDatapointName());
		DpDatasetDTO compareDataset = new DpDatasetDTO("tempCompareDatapoint");
		if (isDouble(condition2)) {
			Double cond = getDouble(condition2);
			for (int i = 0; i < dpdataset.size(); i++) {
				if (compare(getFetchedData().get(0).get(i).getValue(), cond, compare)) {
					compareDataset.add(dpdataset.get(i));
				}
			}
			Integer saveFetchedIndex = null;
			int fetchDataIndex = 0;
			for (int i = 0; i < dpdataset.size(); i++) {
				for (int j = fetchDataIndex; j < compareDataset.size(); j++) {
					if (dpdataset.get(i).getTimestamp().after(compareDataset.get(j).getTimestamp())
							|| dpdataset.get(i).getTimestamp().equals(compareDataset.get(j).getTimestamp())) {
						saveFetchedIndex = j;
						fetchDataIndex = j;
					} else {
						break;
					}
				}
				if (saveFetchedIndex != null) {
					if (compare(dpdataset.get(i).getValue(), compareDataset.get(saveFetchedIndex).getValue(), compare)) {
						newDataset.add(dpdataset.get(i));
					}
				}
			}
		} else {
			if (getFetchedData().size() > 1 && getFetchedData().get(0) != null && getFetchedData().get(1) != null) {
				Integer saveFetchedIndex = null;
				int fetchDataIndex = 0;
				int fetchDataLenght = getFetchedData().get(1).size();
				DpDatasetDTO fetchedData = getFetchedData().get(1);
				for (int i = 0; i < getFetchedData().get(0).size(); i++) {
					for (int j = fetchDataIndex; j < fetchDataLenght; j++) {
						if (getFetchedData().get(0).get(i).getTimestamp().after(fetchedData.get(j).getTimestamp())
								|| getFetchedData().get(0).get(i).getTimestamp().equals(fetchedData.get(j).getTimestamp())) {
							saveFetchedIndex = j;
							fetchDataIndex = j;
						} else {
							break;
						}
					}
					if (saveFetchedIndex != null) {
						if (compare(getFetchedData().get(0).get(i).getValue(), fetchedData.get(saveFetchedIndex).getValue(), compare)) {
							compareDataset.add(getFetchedData().get(0).get(i));
						}
					}
				}
				saveFetchedIndex = null;
				fetchDataIndex = 0;
				for (int i = 0; i < dpdataset.size(); i++) {
					for (int j = fetchDataIndex; j < compareDataset.size(); j++) {
						if (dpdataset.get(i).getTimestamp().after(compareDataset.get(j).getTimestamp())
								|| dpdataset.get(i).getTimestamp().equals(compareDataset.get(j).getTimestamp())) {
							saveFetchedIndex = j;
							fetchDataIndex = j;
						} else {
							break;
						}
					}
					if (saveFetchedIndex != null) {
						if (compare(dpdataset.get(i).getValue(), compareDataset.get(saveFetchedIndex).getValue(), compare)) {
							newDataset.add(dpdataset.get(i));
						}
					}
				}
			}
		}
		// Window.alert("" + compareDataset); //for testing of results
		return newDataset;
	}

	private void createForm() {
		setForm(new ExternalConditionsFormWidget(this));
	}

	@Override
	public void fetchData(final TimePeriodFilter timeFilter, final DpDTO dp) {
		// IMPORTANT: COND1-Dataset has to be on place 0 and COND2-Dataset has
		// to be on place 1
		getFetchedData().clear();
		getFetchedData().add(null);
		getFetchedData().add(null);
		cond1fetched = false;
		cond2fetched = false;
		final ArrayList<Datapoint> datapoint = new ArrayList<Datapoint>();
		datapoint.add(null);
		datapoint.add(null);
		// start fetch data here
		if (isDouble(condition2)) {
			// set cond2fetched to true
			cond2fetched = true;
			// test if cond1 is a data point
			DpController.DP_SERVICE.getDatapoints(condition1, new AsyncCallback<List<DpDTO>>() {

				@Override
				public void onSuccess(List<DpDTO> result) {
					// TODO Auto-generated method stub
					// fetch data and save in position 0 of the
					// getFetched-List
					if (result.size() == 1) {
						datapoint.remove(0);
						datapoint.add(0, ModuleController.DPCC.getDatapoint(result.get(0).getName()));
						if (timeFilter.period <= 0) {
							// not periodic
							// TODO implement anaToBin, or remove it if
							// mode option does the job
							datapoint.get(0).getData(timeFilter.startdate, timeFilter.enddate,
									new DatapointHandler(this, datapoint.get(0)) {

										@Override
										public void onSuccess(DpDatasetDTO result) {
											// set cond1fetched and call
											// fetchReady()
											getFetchedData().remove(0);
											getFetchedData().add(0, result);
											cond1fetched = true;
											fetchReady();
										}

									});
						} else {
							// periodic
							// TODO implement anaToBin, or remove it if
							// mode option does the job
							datapoint.get(0).getDataPeriodic(timeFilter.startdate, timeFilter.enddate, (float) timeFilter.period,
									timeFilter.mode, new DatapointHandler(this, datapoint.get(0)) {

										public void onSuccess(DpDatasetDTO result) {
											// set cond1fetched and call
											// fetchReady()
											getFetchedData().remove(0);
											getFetchedData().add(0, result);
											cond1fetched = true;
											fetchReady();
										}

									});
						}
					} else {
						Window.alert(condition1
								+ ": Invalid external condition! One External Conditions value is not valid! Please check if all values are either a value or a data point!");
					}
				}

				@Override
				public void onFailure(Throwable caught) {
					Window.alert(condition1 + ": Invalid external condition!");
				}
			});
		} else {
			// test if cond2 is a data point
			DpController.DP_SERVICE.getDatapoints(condition2, new AsyncCallback<List<DpDTO>>() {

				@Override
				public void onFailure(Throwable caught) {
					Window.alert(condition2 + ": Invalid external condition!");
				}

				@Override
				public void onSuccess(List<DpDTO> result) {
					// fetch data and save both to the list on the right place
					// (cond1 -> index 0; cond2 -> index 1)
					if (result.size() == 1) {
						datapoint.remove(1);
						datapoint.add(1, ModuleController.DPCC.getDatapoint(result.get(0).getName()));
						// test if cond1 is a data point
						DpController.DP_SERVICE.getDatapoints(condition1, new AsyncCallback<List<DpDTO>>() {

							@Override
							public void onFailure(Throwable caught) {
								Window.alert(condition1 + ": Invalid external condition!");
							}

							@Override
							public void onSuccess(List<DpDTO> result) {
								if (result.size() == 1) {
									datapoint.remove(0);
									datapoint.add(0, ModuleController.DPCC.getDatapoint(result.get(0).getName()));
									if (timeFilter.period <= 0) {
										// not periodic
										// TODO implement anaToBin, or remove it
										// if mode option does the job
										datapoint.get(0).getData(timeFilter.startdate, timeFilter.enddate,
												new DatapointHandler(this, datapoint.get(0)) {

													@Override
													public void onSuccess(DpDatasetDTO result) {
														// set cond1fetched and
														// call fetchReady()
														getFetchedData().remove(0);
														getFetchedData().add(0, result);
														cond1fetched = true;
														fetchReady();
													}

												});
										datapoint.get(1).getData(timeFilter.startdate, timeFilter.enddate,
												new DatapointHandler(this, datapoint.get(1)) {

													@Override
													public void onSuccess(DpDatasetDTO result) {
														// set cond2fetched and
														// call fetchReady()
														getFetchedData().remove(1);
														getFetchedData().add(1, result);
														cond2fetched = true;
														fetchReady();
													}

												});
									} else {
										// periodic
										// TODO implement anaToBin, or remove it
										// if mode option does the job
										datapoint.get(0).getDataPeriodic(timeFilter.startdate, timeFilter.enddate,
												(float) timeFilter.period, timeFilter.mode, new DatapointHandler(this, datapoint.get(0)) {

													public void onSuccess(DpDatasetDTO result) {
														// set cond1fetched and
														// call fetchReady()
														getFetchedData().remove(0);
														getFetchedData().add(0, result);
														cond1fetched = true;
														fetchReady();
													}

												});
										datapoint.get(1).getData(timeFilter.startdate, timeFilter.enddate,
												new DatapointHandler(this, datapoint.get(1)) {

													@Override
													public void onSuccess(DpDatasetDTO result) {
														// set cond1fetched and
														// call fetchReady()
														getFetchedData().remove(1);
														getFetchedData().add(1, result);
														cond2fetched = true;
														fetchReady();
													}

												});
									}

								} else {
									Window.alert(condition1
											+ ": Invalid external condition! One External Conditions value is not valid! Please check if all values are either a value or a data point!");
								}

							}
						});
					} else {
						Window.alert(condition2
								+ ": Invalid external condition! One External Conditions value is not valid! Please check if all values are either a value or a data point!");
					}
				}
			});

		}
	}

	private void fetchReady() {
		if (cond1fetched == true && cond2fetched == true) {
			setFinishedFetch(true);
			getParent().fetchReady();
		}
	}

	private boolean isDouble(String cond) {
		try {
			Double.valueOf(cond);
		} catch (NullPointerException npe) {
			return false;
		} catch (NumberFormatException nfe) {
			try {
				Double.valueOf(cond.replaceAll(",", "."));
			} catch (NullPointerException npe) {
				return false;
			} catch (NumberFormatException nfenew) {
				return false;
			}
			return true;
		}
		return true;
	}

	private Double getDouble(String cond) {
		Double value = null;
		try {
			value = Double.valueOf(cond);
		} catch (NullPointerException npe) {
			return value;
		} catch (NumberFormatException nfe) {
			try {
				value = Double.valueOf(cond.replaceAll(",", "."));
			} catch (NullPointerException npe) {
				return value;
			} catch (NumberFormatException nfenew) {
				return value;
			}
			return value;
		}
		return value;
	}

	private boolean compare(Double value1, Double value2, String compareCond) {
		boolean returnvalue = false;
		if (compareCond.equalsIgnoreCase("smallerThan") && value1 < value2) {
			returnvalue = true;
		} else if (compareCond.equalsIgnoreCase("greaterThan") && value1 > value2) {
			returnvalue = true;
		} else if (compareCond.equalsIgnoreCase("equalTo") && value1 == value2) {
			returnvalue = true;
		} else if (compareCond.equalsIgnoreCase("notEqualTo") && value1 != value2) {
			returnvalue = true;
		} else if (compareCond.equalsIgnoreCase("smallerOrEqual") && value1 <= value2) {
			returnvalue = true;
		} else if (compareCond.equalsIgnoreCase("greaterOrEqual") && value1 >= value2) {
			returnvalue = true;
		}
		return returnvalue;
	}
}
