package bpi.most.connector;

import bpi.most.domain.connector.ConnectorVO;
import bpi.most.dto.UserDTO;
import bpi.most.service.api.DatapointService;
import junit.framework.Assert;

/**
 * Created with IntelliJ IDEA.
 * User: harald
 * Date: 25.06.13
 * Time: 09:52
 * To change this template use File | Settings | File Templates.
 */
public class DummyConnectorFactory extends ConnectorFactory{

    public static final String DUMMY_DP_NAME = "dummyDp";

    /**
     * creates a dummyconnector which can be tested in the testcase later on
     * @param connectorMetadata information about requested connector
     * @param user
     * @return
     */
    @Override
    public Connector getConnector(ConnectorVO connectorMetadata, UserDTO user) {
        DummyConnector con = null;

        if (DUMMY_DP_NAME.equals(connectorMetadata.getDpName())){
            con = new DummyConnector(connectorMetadata, user);
        }else{
            Assert.fail("got wrong datapoint name for testcase");
        }

        return con;
    }

}
