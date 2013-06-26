package bpi.most.connector;

import bpi.most.domain.connector.ConnectorVO;
import bpi.most.dto.DpDataDTO;
import bpi.most.dto.DpDatasetDTO;
import bpi.most.dto.UserDTO;
import bpi.most.service.api.DatapointService;
import junit.framework.Assert;

import javax.inject.Inject;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: harald
 * Date: 25.06.13
 * Time: 09:53
 * To change this template use File | Settings | File Templates.
 */
public class DummyConnector extends Connector{

    @Inject
    DatapointService dpService;

    /**
     * connect the connector to a datapoint
     */
    public DummyConnector(DatapointService dpService, ConnectorVO connectorDTO, UserDTO user) {
        super(dpService, connectorDTO, user);
    }

    @Override
    protected boolean writeData(DpDataDTO newValue) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public DpDataDTO getSourceData() {

        Assert.assertNotNull(dpService);
        //TODO change this to the actual test implementation
        Assert.assertTrue(dpService instanceof  DatapointService);

        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public DpDatasetDTO getSourceData(Date starttime) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
