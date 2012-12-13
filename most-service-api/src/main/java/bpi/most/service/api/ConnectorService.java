package bpi.most.service.api;

import java.util.List;

import bpi.most.domain.Connector;
import bpi.most.domain.Dp;
import bpi.most.domain.User;
import bpi.most.domain.Zone;



/**
 * Interface Specification of Connector Service.
 *
 * @author Lukas Weichselbaum
 */
public interface ConnectorService {

    public List<Connector> getConnection(User user);

    public List<Connector> getConnection(User user, Dp dp, String deviceName, String connectionType, Zone zone, String connectorID);
}
