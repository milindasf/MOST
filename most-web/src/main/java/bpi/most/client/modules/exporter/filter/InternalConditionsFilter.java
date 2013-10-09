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

public class InternalConditionsFilter extends Filter implements FilterInterface {

	public String condition = "enter value or drop data point";
	public String compare = "smallerThan";

	public InternalConditionsFilter() {
		super();
	}

	public InternalConditionsFilter(String name) {
		super(name);
		createForm();
	}

	public InternalConditionsFilter(String name, ExportFilterWidget parent) {
		super(name, parent);
		createForm();
	}

	@Override
	public DpDatasetDTO execute(DpDatasetDTO dpdataset) {
		DpDatasetDTO newDataset = new DpDatasetDTO(dpdataset.getDatapointName());
		if (isDouble(condition)) {
			// getDouble and start execute
			Double cond = getDouble(condition);
			for (int i = 0; i < dpdataset.size(); i++) {
				if (compare(dpdataset.get(i).getValue(), cond, compare)) {
					newDataset.add(dpdataset.get(i));
				}
			}
		} else {
			Integer saveFetchedIndex = null;
			int fetchDataIndex = 0;
			int fetchDataLenght = getFetchedData().get(0).size();
			DpDatasetDTO fetchedData = getFetchedData().get(0);
			for (int i = 0; i < dpdataset.size(); i++) {
				for (int j = fetchDataIndex; j < fetchDataLenght; j++) {
					if (dpdataset.get(i).getTimestamp().after(fetchedData.get(j).getTimestamp())
							|| dpdataset.get(i).getTimestamp().equals(fetchedData.get(j).getTimestamp())) {
						saveFetchedIndex = j;
						fetchDataIndex = j;
					} else {
						break;
					}
				}
				if (saveFetchedIndex != null) {
					if (compare(dpdataset.get(i).getValue(), fetchedData.get(saveFetchedIndex).getValue(), compare)) {
						newDataset.add(dpdataset.get(i));
					}
				}
			}
			// get fetchedData and execute with it
			// getFetchedData().get(0);

		}
		return newDataset;
	}

	private void createForm() {
		setForm(new InternalConditionsFormWidget(this));
	}

	@Override
	public void fetchData(final TimePeriodFilter timeFilter, DpDTO dp) {
		getFetchedData().clear();
		if (isDouble(condition)) {
			// its a number so you don't need to fetch data
			setFinishedFetch(true);
			getParent().fetchReady();
		} else {
			// check if it's a valid data point and get it's data
			DpController.DP_SERVICE.getDatapoints(condition, new AsyncCallback<List<DpDTO>>() {

				@Override
				public void onSuccess(List<DpDTO> result) {
					if (result.size() == 1) {
						Datapoint datapoint = ModuleController.DPCC.getDatapoint(result.get(0).getName());
						if (timeFilter.period <= 0) {
							// not periodic
							// TODO implement anaToBin, or remove it if
							// mode option does the job
							datapoint.getData(timeFilter.startdate, timeFilter.enddate, new DatapointHandler(this, datapoint) {

								@Override
								public void onSuccess(DpDatasetDTO result) {
									getFetchedData().add(result);
									setFinishedFetch(true);
									getParent().fetchReady();
								}

							});
						} else {
							// periodic
							// TODO implement anaToBin, or remove it if
							// mode option does the job
							datapoint.getDataPeriodic(timeFilter.startdate, timeFilter.enddate, (float) timeFilter.period, timeFilter.mode,
									new DatapointHandler(this, datapoint) {

										@Override
										public void onSuccess(DpDatasetDTO result) {
											getFetchedData().add(result);
											setFinishedFetch(true);
											getParent().fetchReady();
										}

									});
						}
					} else {
						Window.alert(condition
								+ ": Invalid internal condition! One Internal Conditions value is not valid! Please check if all values are either a value or a data point!");
					}
				}

				@Override
				public void onFailure(Throwable caught) {
					Window.alert(condition + ": Invalid internal condition!");
				}
			});
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
