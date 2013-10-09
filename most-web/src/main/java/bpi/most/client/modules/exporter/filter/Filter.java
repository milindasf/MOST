package bpi.most.client.modules.exporter.filter;

import java.util.ArrayList;

import bpi.most.dto.DpDTO;
import bpi.most.dto.DpDatasetDTO;

public abstract class Filter implements FilterInterface {
	
	private ExportFilterWidget parent;

	public String name;
	private FormWidget form;
	public boolean finishedFetch = false;
	private ArrayList<DpDatasetDTO> fetchedData = new ArrayList<DpDatasetDTO>();

	public Filter() {

	}

	public Filter(String name) {
		this.name = name;
	}
	
	public Filter(String name, ExportFilterWidget parent) {
		this.name = name;
		this.parent = parent;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public FormWidget getForm() {
		return form;
	}
	
	public void setForm(FormWidget form) {
		this.form = form;
	}

	public boolean isFinishedFetch() {
		return finishedFetch;
	}

	public void setFinishedFetch(boolean finishedFetch) {
		this.finishedFetch = finishedFetch;
	}

	public ArrayList<DpDatasetDTO> getFetchedData() {
		return fetchedData;
	}

	public void setFetchedData(ArrayList<DpDatasetDTO> fetchedData) {
		this.fetchedData = fetchedData;
	}

	/**
	 * Clear all fetched data before fetch new one.
	 */
	public void clearFetchedData() {
		fetchedData.clear();
	}

	public ExportFilterWidget getParent() {
		return parent;
	}

	public void setParent(ExportFilterWidget parent) {
		this.parent = parent;
	}

	@Override
	public DpDatasetDTO execute(DpDatasetDTO dpdataset) {
		return null;
	}

	@Override
	public void fetchData(TimePeriodFilter timeFilter, DpDTO dp) {
	}

}
