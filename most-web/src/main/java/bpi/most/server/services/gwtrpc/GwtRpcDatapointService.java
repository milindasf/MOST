package bpi.most.server.services.gwtrpc;

import java.util.ArrayList;
import java.util.Date;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bpi.most.server.services.User;
import bpi.most.shared.DpDatasetDTO;
import bpi.most.shared.DpDTO;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * Implementation of GWT-RPC interface
 * 
 * @author robert.zach@tuwien.ac.at
 */
public class GwtRpcDatapointService extends RemoteServiceServlet implements
		bpi.most.client.rpc.DatapointService {
	
	private static final Logger LOG = LoggerFactory.getLogger(GwtRpcDatapointService.class);
	
	bpi.most.server.services.DatapointService dpService = bpi.most.server.services.DatapointService
			.getInstance();

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
	public DpDatasetDTO getData(String datapointName, Date starttime,
			Date endtime) {
		User user;
		// get user of session
		HttpSession session = this.getThreadLocalRequest().getSession(true);
		user = (User) session.getAttribute("mostUser");

		return dpService.getData(user, new DpDTO(datapointName), starttime,
				endtime);

	}

	// TODO: add user checking like in the other methods

	/*
	 * (non-Javadoc)
	 * 
	 * @see bpi.most.client.rpc.DatapointService#getDatapoints()
	 */
	public ArrayList<DpDTO> getDatapoints() {
		// no user checking here yet
		return dpService.getDatapoints(null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bpi.most.client.rpc.DatapointService#getDatapoints(java.lang.String)
	 */
	public ArrayList<DpDTO> getDatapoints(String searchstring) {
		// no user checking here yet
		return dpService.getDatapoints(null, searchstring);
	}

	
	/* (non-Javadoc)
	 * @see bpi.most.client.rpc.DatapointService#getDatapoints(java.lang.String, java.lang.String)
	 */
	@SuppressWarnings("deprecation")
	public ArrayList<DpDTO> getDatapoints(String searchstring, String zone) {
		// no user checking here yet
		return dpService.getDatapoints(null, searchstring, zone);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * bpi.most.client.rpc.DatapointService#getDataPeriodic(java.lang.String,
	 * java.util.Date, java.util.Date, java.lang.Float)
	 */
	public DpDatasetDTO getDataPeriodic(String datapointName, Date starttime,
			Date endtime, Float period) {
		User user;
		// get user of session
		HttpSession session = this.getThreadLocalRequest().getSession(true);
		user = (User) session.getAttribute("mostUser");

		return dpService.getDataPeriodic(user, new DpDTO(datapointName),
				starttime, endtime, period);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * bpi.most.client.rpc.DatapointService#getNumberOfValues(java.lang.String,
	 * java.util.Date, java.util.Date)
	 */
	public int getNumberOfValues(String datapointName, Date starttime,
			Date endtime) {
		User user;
		// get user of session
		HttpSession session = this.getThreadLocalRequest().getSession(true);
		user = (User) session.getAttribute("mostUser");

		return dpService.getNumberOfValues(user, new DpDTO(datapointName),
				starttime, endtime);
	}

}
