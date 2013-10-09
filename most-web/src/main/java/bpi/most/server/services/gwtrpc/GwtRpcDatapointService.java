package bpi.most.server.services.gwtrpc;

import bpi.most.dto.DpDTO;
import bpi.most.dto.DpDatasetDTO;
import bpi.most.dto.UserDTO;
import bpi.most.server.services.User;
import bpi.most.service.api.DatapointService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

/**
 * Implementation of GWT-RPC interface
 * 
 * @author robert.zach@tuwien.ac.at
 */

public class GwtRpcDatapointService extends SpringGwtServlet implements
		bpi.most.client.rpc.DatapointService {
	
	private static final Logger LOG = LoggerFactory.getLogger(GwtRpcDatapointService.class);

    @Inject
    DatapointService dpService;

	private static final long serialVersionUID = 1L;

	// Debug
	public GwtRpcDatapointService() {
		LOG.info("GwtRpcDatapointService");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bpi.most.client.rpc.DatapointService#getData(java.lang.String,
	 * java.util.Date, java.util.Date)
	 */
    @Override
	public DpDatasetDTO getData(String datapointName, Date starttime,
			Date endtime) {
		User user;
		// get user of session
		HttpSession session = this.getThreadLocalRequest().getSession(true);
		user = (User) session.getAttribute("mostUser");

        DpDatasetDTO result = null;
		try{
            result = dpService.getData(new UserDTO(user.getUserName()), new DpDTO(datapointName), starttime,
				endtime);
        }catch(Exception e){
            LOG.error(e.getMessage(), e);
        }
        return result;
	}

	// TODO: add user checking like in the other methods

	/*
	 * (non-Javadoc)
	 * 
	 * @see bpi.most.client.rpc.DatapointService#getDatapoints()
	 */
    @Override
	public List<DpDTO> getDatapoints() {
		// no user checking here yet
		return dpService.getDatapoints(null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bpi.most.client.rpc.DatapointService#getDatapoints(java.lang.String)
	 */
    @Override
	public List<DpDTO> getDatapoints(String searchstring) {
		// no user checking here yet
		//return dpService.getDatapoints(null, searchstring);
        return dpService.getDatapoints(searchstring);
	}

	
	/* (non-Javadoc)
	 * @see bpi.most.client.rpc.DatapointService#getDatapoints(java.lang.String, java.lang.String)
	 */
    @Override
	@SuppressWarnings("deprecation")
	public List<DpDTO> getDatapoints(String searchstring, String zone) {
		// no user checking here yet
		return dpService.getDatapoints(searchstring, zone);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * bpi.most.client.rpc.DatapointService#getDataPeriodic(java.lang.String,
	 * java.util.Date, java.util.Date, java.lang.Float)
	 */
    @Override
	public DpDatasetDTO getDataPeriodic(String datapointName, Date starttime,
			Date endtime, Float period) {
		User user;
		// get user of session
		HttpSession session = this.getThreadLocalRequest().getSession(true);
		user = (User) session.getAttribute("mostUser");

		return dpService.getDataPeriodic(new UserDTO(user.getUserName()), new DpDTO(datapointName),
				starttime, endtime, period);
	}

    @Override
    public DpDatasetDTO getDataPeriodic(String datapointName, Date starttime, Date endtime, Float period, int mode) {
        User user;
        // get user of session
        HttpSession session = this.getThreadLocalRequest().getSession(true);
        user = (User) session.getAttribute("mostUser");

        return dpService.getDataPeriodic(new UserDTO(user.getUserName()), new DpDTO(datapointName),
                starttime, endtime, period, mode);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * bpi.most.client.rpc.DatapointService#getNumberOfValues(java.lang.String,
     * java.util.Date, java.util.Date)
     */
    @Override
	public int getNumberOfValues(String datapointName, Date starttime,
			Date endtime) {
		User user;
		// get user of session
		HttpSession session = this.getThreadLocalRequest().getSession(true);
		user = (User) session.getAttribute("mostUser");

        int result = 0;
        try{
		    result = dpService.getNumberOfValues(new UserDTO(user.getUserName()), new DpDTO(datapointName),
				starttime, endtime);
        }catch (Exception e){
            LOG.error(e.getMessage(), e);
        }
        return result;
	}

}
