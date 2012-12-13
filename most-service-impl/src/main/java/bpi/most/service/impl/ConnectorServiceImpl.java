package bpi.most.service.impl;

import bpi.most.domain.Connector;
import bpi.most.domain.Dp;
import bpi.most.domain.User;
import bpi.most.domain.Zone;
import bpi.most.service.api.ConnectorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Implementation of {@link bpi.most.service.api.ConnectorService}.
 *
 * @author Lukas Weichselbaum
 */
@Service
public class ConnectorServiceImpl implements ConnectorService {

    private static final Logger log = LoggerFactory.getLogger(ConnectorServiceImpl.class);

    @PersistenceContext(unitName = "most")
    private EntityManager em;

    @Override
    public List<Connector> getConnection(User user) {
        return null;  //TODO: implement
    }

    @Override
    public List<Connector> getConnection(User user, Dp dp, String deviceName, String connectionType, Zone zone, String connectorID) {
        return null;   //TODO: implement
    }
}
