package bpi.most.server.services;

import java.util.List;

import bpi.most.server.model.ConnectorController;
import bpi.most.shared.ConnectorDTO;
import bpi.most.shared.DpDTO;
import bpi.most.shared.ZoneDTO;


/**
 * Common Service interface. Handles permissions, etc. Everything returned here is serializable!
 * This service provide methods for Connectors to request dp and connection information
 * @author robert.zach@tuwien.ac.at
 */
public class ConnectorService {
	ConnectorController connCtrl = ConnectorController.getInstance();
	private static ConnectorService ref = null;

	// Singleton
	private ConnectorService() {
		super();
	}
	public static ConnectorService getInstance() {
		if (ref == null) {
			ref = new ConnectorService();
		}
		return ref;
	}
	
	
	/**
	 * @return all connections were the user has any permissions
	 */
	public List<ConnectorDTO> getConnection(User user) {
		//A get all head zone
		//B get all connections within these headzones
		//C remove double entries
		return connCtrl.getConnection(user);
	}
	
	/**
	 * 
	 * @param dp
	 * @param deviceName
	 * @param connectionType
	 * @param zone
	 * @param connectorID - unique String ID defined within a connector
	 * @return
	 */
	public List<ConnectorDTO> getConnection(User user, DpDTO dp, String deviceName, String connectionType, ZoneDTO zone, String connectorID) {
		return null;	
	}
	
}
