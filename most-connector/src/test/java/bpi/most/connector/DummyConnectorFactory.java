package bpi.most.connector;

import bpi.most.domain.connector.ConnectorVO;
import bpi.most.dto.UserDTO;
import bpi.most.service.api.DatapointService;

/**
 * Created with IntelliJ IDEA.
 * User: harald
 * Date: 25.06.13
 * Time: 09:52
 * To change this template use File | Settings | File Templates.
 */
public class DummyConnectorFactory extends ConnectorFactory{

    @Override
    public Connector getConnector(DatapointService dpService, ConnectorVO connectorMetadata, UserDTO user) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

}
