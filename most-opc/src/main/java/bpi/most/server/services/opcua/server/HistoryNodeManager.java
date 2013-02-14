package bpi.most.server.services.opcua.server;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import bpi.most.dto.DpDTO;
import bpi.most.dto.DpDataDTO;
import bpi.most.dto.DpDatasetDTO;
import bpi.most.dto.UserDTO;
import bpi.most.opcua.server.annotation.IAnnotationHistoryManager;
import bpi.most.opcua.server.core.RequestContext;
import bpi.most.opcua.server.core.Session;
import bpi.most.opcua.server.core.history.HistoryValue;
import bpi.most.server.services.opcua.server.nodes.DpNode;
import bpi.most.service.api.DatapointService;

/**
 * handles history requests for datapoints.
 * @author harald
 *
 */
public class HistoryNodeManager implements IAnnotationHistoryManager {

	@Inject
    private DatapointService dpService;
	
	public HistoryNodeManager(){
	}
	
	/**
	 * @param dpService
	 */
	public HistoryNodeManager(DatapointService dpService) {
		this.dpService = dpService;
	}

	@Override
	public List<HistoryValue> getHistoryValues(Class<?> clazz, String id, String historyQualfier, Date startTime, Date endTime) {

		
		
		List<HistoryValue> values = new ArrayList<HistoryValue>();

		if (DpNode.class.equals(clazz)) {
			if ("data".equals(historyQualfier)) {
				DpDTO dp = new DpDTO(id);
				
				Session s = RequestContext.get().getSession();
				UserDTO user = (UserDTO) s.getCustomObj();
				
				DpDatasetDTO dpValues = dpService.getData(user, dp, startTime, endTime);
				for (DpDataDTO dpValue : dpValues) {
					values.add(new HistoryValue(dpValue.getValue(), dpValue.getTimestamp()));
				}
			}
		}

		return values;
	}

}
