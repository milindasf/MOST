package bpi.most.connector;

import bpi.most.domain.connector.ConnectorVO;
import bpi.most.dto.UserDTO;
import bpi.most.service.api.ConnectorService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: harald
 * Date: 10.07.13
 * Time: 11:44
 * To change this template use File | Settings | File Templates.
 */
public class DummyConService implements ConnectorService{
    @Override
    public List<ConnectorVO> getConnection(UserDTO user) {
        List<ConnectorVO> cons = new ArrayList<ConnectorVO>();
        cons.add(new ConnectorVO(1, DummyConnectorFactory.DUMMY_DP_NAME, null, null, null, false, null, null));
        return cons;
    }
}
