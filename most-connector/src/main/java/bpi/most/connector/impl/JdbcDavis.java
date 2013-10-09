package bpi.most.connector.impl;

import bpi.most.connector.Connector;
import bpi.most.domain.connector.ConnectorVO;
import bpi.most.dto.DpDataDTO;
import bpi.most.dto.DpDatasetDTO;
import bpi.most.dto.UserDTO;
import bpi.most.service.api.DatapointService;

/**
 * Jdbc connector for davis weather station using weewx
 * - vendor - "weewx"
 * @see ConnectorJdbc
 */
public class JdbcDavis extends ConnectorJdbc{

	@Override
	public Connector getConnector(ConnectorVO connectorMetadata, UserDTO user) {
		//check for null first to avoid null pointer exceptions
		if (connectorMetadata.getConnectionType() != null &&
			connectorMetadata.getConnectionType().equals("jdbc") &&
			( connectorMetadata.getVendor() == null || connectorMetadata.getVendor().equals("weewx") ) &&
			( connectorMetadata.getModel() == null || connectorMetadata.getModel().equals("") ) ) {
			
			//check if data source uses DriverManager
			if (connectorMetadata.getVariable(SQL_DRIVER_MANAGER) != null) {
				//create ConnectionFactory with provided values
				DbConnectionFactory connFactory = new DbConnectionFactoryDriverManager(connectorMetadata.getVariable(SQL_DRIVER_MANAGER), connectorMetadata.getVariable(SQL_DRIVER_MANAGER_URL));
				return new JdbcDavisImpl(connectorMetadata, connFactory, user);
			}
			
			//TODO implement DataSource ConnectionFactory
			//check if DataSource Name is provided by 
			// check if DataSource can be accessed 
			//implement ConnectionFactory
			//return new ConnectorJdbcImpl(connectorMetadata, new DataSourceConnectionFactory());
		}
		return null;
	}
	
	private class JdbcDavisImpl extends ConnectorJdbcImpl{

		public JdbcDavisImpl(ConnectorVO connectorVO, DbConnectionFactory jdbcConnectionFactory, UserDTO user) {
			super(connectorVO, jdbcConnectionFactory, user);
			// TODO Auto-generated constructor stub
		}
	
		/**
		 * change unit of measurements
		 */
		@Override
		protected DpDatasetDTO getSourceDatasetFromQuery(String query) {
			DpDatasetDTO result = super.getSourceDatasetFromQuery(query);
			
			//change unit depending on dp type
			//checking which column is used by this connector
			if (getSqlDatapointColumn().equals("barometer")) {
				for (DpDataDTO dpDataDTO : result) {
					//unit transformation --> mBar (*33.85)
					dpDataDTO.setValue(dpDataDTO.getValue() * 33.85 );
				}
			}
			if (getSqlDatapointColumn().equals("outTemp")) {
				for (DpDataDTO dpDataDTO : result) {
					//unit transformation --> F --> C ((outTemp-32)*5/9)
					dpDataDTO.setValue((dpDataDTO.getValue() - 32) * 5/9 );
				}
			}
			if (getSqlDatapointColumn().equals("windSpeed")) {
				for (DpDataDTO dpDataDTO : result) {
					//unit transformation
					dpDataDTO.setValue(dpDataDTO.getValue() * 0.44704 );
				}
			}
			
			return result;
		}
		
	}
	
}
