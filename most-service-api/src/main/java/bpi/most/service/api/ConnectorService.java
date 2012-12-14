package bpi.most.service.api;

import java.util.List;

import bpi.most.domain.connector.Connector;
import bpi.most.domain.user.User;


/**
 * Interface Specification of Connector Service.
 * provides information about defined Connectors (Datapoint - Connector - Device/Sensor/Actor)
 *
 * @author Lukas Weichselbaum
 */
public interface ConnectorService {

    public List<Connector> getConnection(User user);
}
