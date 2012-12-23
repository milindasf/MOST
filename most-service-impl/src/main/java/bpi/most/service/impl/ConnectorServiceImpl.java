package bpi.most.service.impl;

import bpi.most.domain.connector.ConnectorFinder;
import bpi.most.domain.connector.ConnectorVO;
import bpi.most.domain.user.User;
import bpi.most.service.api.ConnectorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Implementation of {ConnectorService}.
 *
 * @author robert.zach@tuwien.ac.at
 * @author Lukas Weichselbaum
 */
@Service
public class ConnectorServiceImpl implements ConnectorService {

    private static final Logger LOG = LoggerFactory.getLogger(ConnectorServiceImpl.class);

    @PersistenceContext(unitName = "most")
    private EntityManager em;

    private ConnectorFinder connectorFinder;

    @PostConstruct
    protected void init() {
        connectorFinder = new ConnectorFinder(em);
    }

    @Override
    @Transactional
    public List<ConnectorVO> getConnection(User user) {
        return connectorFinder.findConnections();
    }

}
