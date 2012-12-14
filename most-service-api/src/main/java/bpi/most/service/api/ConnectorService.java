package bpi.most.service.api;

import java.util.List;

import bpi.most.domain.Connector;
import bpi.most.domain.Dp;
import bpi.most.domain.User;
import bpi.most.domain.Zone;



/**
 * Interface Specification of Connector Service.
 * provides information about defined Connectors (Datapoint - Connector - Device/Sensor/Actor)
 *
 * @author Lukas Weichselbaum
 */
public interface ConnectorService {

    public List<Connector> getConnection(User user);
}
