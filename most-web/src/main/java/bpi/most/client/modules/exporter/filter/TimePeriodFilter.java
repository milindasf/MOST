package bpi.most.client.modules.exporter.filter;

import java.util.Date;

import bpi.most.client.model.Datapoint;
import bpi.most.client.model.DatapointHandler;
import bpi.most.client.modules.ModuleController;
import bpi.most.dto.DpDTO;
import bpi.most.dto.DpDatasetDTO;

public class TimePeriodFilter extends Filter implements FilterInterface {

	public Date startdate = new Date(1304589600000L);
	public Date enddate = new Date(1304611200000L);
	public int period = 0;
	public int mode = 1;
	public boolean anaToBin = false;

	public TimePeriodFilter() {
		super();
	}

	public TimePeriodFilter(String name) {
		super(name);
		createForm();
	}

	public TimePeriodFilter(String name, ExportFilterWidget parent) {
		super(name, parent);
		createForm();
	}

	@Override
	public DpDatasetDTO execute(DpDatasetDTO dpdataset) {
		DpDatasetDTO temp = getFetchedData().get(0);
		return temp;

	}

	private void createForm() {
		setForm(new TimePeriodFormWidget(this));
	}

	@Override
	public void fetchData(TimePeriodFilter timeFilter, DpDTO dp) {
		// TODO Auto-generated method stub
		getFetchedData().clear();
		Datapoint datapoint = ModuleController.DPCC.getDatapoint(dp.getName());
		if (period <= 0) {
			// not periodic
			// TODO implement anaToBin, or remove it if mode option does the job
			datapoint.getData(startdate, enddate, new DatapointHandler(this,
					datapoint) {

				@Override
				public void onSuccess(DpDatasetDTO result) {
					getFetchedData().add(result);
					setFinishedFetch(true);
					getParent().fetchReady();
				}

			});
		} else {
			// periodic
			// TODO implement anaToBin, or remove it if mode option does the job
			datapoint.getDataPeriodic(startdate, enddate, (float) period, mode,
					new DatapointHandler(this, datapoint) {

						@Override
						public void onSuccess(DpDatasetDTO result) {
							getFetchedData().add(result);
							setFinishedFetch(true);
							getParent().fetchReady();
						}

					});
		}
	}

}
