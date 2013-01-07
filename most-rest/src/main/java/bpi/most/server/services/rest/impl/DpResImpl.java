package bpi.most.server.services.rest.impl;

import bpi.most.dto.DpDTO;
import bpi.most.dto.DpDataDTO;
import bpi.most.server.services.rest.api.DpResource;
import bpi.most.server.services.rest.utils.DateUtils;
import bpi.most.service.api.DatapointService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DpResImpl extends BaseResImpl implements DpResource {

	private static final Logger LOG = LoggerFactory.getLogger(DpResImpl.class);

    @Inject
    private DatapointService dpService;
	
	public DpResImpl(){

	}
	
	@Override
	public DpDTO getDp(String dpName) {
		return dpService.getDatapoint(getUser(), new DpDTO(dpName));
	}
	
	public void addDp(DpDTO dp) {
		LOG.warn("should creatin new datapoint \n" + dp.toString() + "\nbut is not implemented yet.");
		throw new WebApplicationException(NOT_IMPLEMENTED);
	}

	@Override
	public void updateDp(String dpName, DpDTO dp) {
		LOG.warn("updating datapoint " + dpName + "\n" + dp.toString() + "\nbut is is not implemented yet.");
		throw new WebApplicationException(NOT_IMPLEMENTED);
	}

	@Override
	public void deleteDp(String dpName) {
		LOG.warn("should delete datapoint " + dpName + "\nbutis not implemented yet.");
		throw new WebApplicationException(NOT_IMPLEMENTED);
	}

	@Override
	public void addDpData(String dpName, DpDataDTO data) {
		LOG.info("adding new data to datapoint: " + dpName);
		LOG.info(data.toString());
        dpService.addData(getUser(), new DpDTO(dpName), data);
	}

	@Override
	public List<DpDataDTO> getDpData(String dpName, String sFrom, String sTo) {
		LOG.info("returning data");
		List<DpDataDTO> result = null;
		DpDTO dp = new DpDTO(dpName);
		if (sFrom == null && sTo == null) {
			//no restriction in time -> fetch newest value
            DpDataDTO lastData = dpService.getData(getUser(), dp);
			result = new ArrayList<DpDataDTO>();
            result.add(lastData);
		}else{
			//some restriction in time, either "from", or "to", or both
            Date from = DateUtils.returnNowOnNull(sFrom);
            Date to = DateUtils.returnNowOnNull(sTo);
            result = dpService.getData(getUser(), dp, from, to);
		}
		return result;
	}

	@Override
	public List<DpDataDTO> getDpPeriodicData(String dpName, String sFrom, String sTo, int period,
			int mode, int type) {
		LOG.info("returning periodic data");
		List<DpDataDTO> result = null;
		DpDTO dp = new DpDTO(dpName);
        Date from = DateUtils.returnNowOnNull(sFrom);
        Date to = DateUtils.returnNowOnNull(sTo);
        result = dpService.getDataPeriodic(getUser(), dp, from, to, (float)period);
		return result;
	}
}
