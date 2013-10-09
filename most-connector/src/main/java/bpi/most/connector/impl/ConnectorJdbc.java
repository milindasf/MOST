package bpi.most.connector.impl;

import bpi.most.connector.Connector;
import bpi.most.connector.ConnectorFactory;
import bpi.most.domain.connector.ConnectorVO;
import bpi.most.dto.UserDTO;
import bpi.most.service.api.DatapointService;

/**
 * Provides a JDBC Connector using any JDBC compatible database.
 * The following variables are required:
 * A) based on DriverManager interface
 * - sqlDriverManager - e.g. "org.sqlite.JDBC" for sqlite
 * - sqlDriverManagerUrl - e.g. "jdbc:sqlite:/path/to/sqllite/file" for sqlite 
 * or
 * B) based on DataSource interface
 * TODO implement sqlDataSource - Name of the DataSource registered to the respective machine running the connector
 * 
 * Additional variables see @see bpi.most.connector.impl.ConnectorJdbcImpl
 * 
 * TODO implement overwritting of data source definition with local properties
 * @author robert.zach@tuwien.ac.at
 */
public class ConnectorJdbc extends ConnectorFactory{
	protected final String SQL_DRIVER_MANAGER = "sqlDriverManager";
	protected final String SQL_DRIVER_MANAGER_URL = "sqlDriverManagerUrl";
	protected final String SQL_DATA_SOURCE = "sqlDataSource";
	
	@Override
	public Connector getConnector(ConnectorVO connectorMetadata, UserDTO user) {
		//Check if type is "jdbc" and vendor and model are null or "".
		//check for null first to avoid null pointer exceptions
		if (connectorMetadata.getConnectionType() != null &&
			connectorMetadata.getConnectionType().equals("jdbc") &&
			( connectorMetadata.getVendor() == null || connectorMetadata.getVendor().equals("") ) &&
			( connectorMetadata.getModel() == null || connectorMetadata.getModel().equals("") ) ) {
			
			//check if data source uses DriverManager
			if (connectorMetadata.getVariable(SQL_DRIVER_MANAGER) != null) {
				//create ConnectionFactory with provided values
				DbConnectionFactory connFactory = new DbConnectionFactoryDriverManager(connectorMetadata.getVariable(SQL_DRIVER_MANAGER), connectorMetadata.getVariable(SQL_DRIVER_MANAGER_URL));
				return new ConnectorJdbcImpl(connectorMetadata, connFactory, user);
			}
			
			//TODO implement DataSource ConnectionFactory
			//check if DataSource Name is provided by 
			// check if DataSource can be accessed 
			//implement ConnectionFactory
			//return new ConnectorJdbcImpl(connectorMetadata, new DataSourceConnectionFactory());
		}
		return null;
	}
	
			
	}
	

