package bpi.most.client.modules.exporter.filter;

import bpi.most.dto.DpDTO;
import bpi.most.dto.DpDatasetDTO;

public interface FilterInterface {

	public void fetchData(TimePeriodFilter timeFilter, DpDTO dp);
	
	public DpDatasetDTO execute(DpDatasetDTO dpdataset);
	
}
