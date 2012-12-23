package bpi.most.service.api;

import bpi.most.domain.connector.ConnectorVO;
import bpi.most.domain.user.User;

import java.util.List;


/**
 * Interface Specification of Connector Service.
 * provides information about defined Connectors (Datapoint - Connector - Device/Sensor/Actor)
 *
 * @author Lukas Weichselbaum
 */
public interface ConnectorService {

    List<ConnectorVO> getConnection(User user);
}
