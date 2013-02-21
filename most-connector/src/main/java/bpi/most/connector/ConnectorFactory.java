package bpi.most.connector;


import bpi.most.domain.connector.ConnectorVO;
import bpi.most.dto.ConnectorDTO;
import bpi.most.dto.UserDTO;
import bpi.most.service.api.DatapointService;

/**
 * POJO used to create Connectors
 * Extend this if you want to implement a new Connector (driver)
 */
public abstract class ConnectorFactory {

	/**
	 * Use unique vendor and model IDs for new connector implementations
	 * The default OPC/JDBC connector use vendor and model null or ""
	 * 
	 * @param connectorMetadata information about requested connector 
	 * @return returns a Connector object or null if the requested type (driver) is not support
	 */
	public abstract Connector getConnector(DatapointService dpService, ConnectorVO connectorMetadata, UserDTO user);

}
