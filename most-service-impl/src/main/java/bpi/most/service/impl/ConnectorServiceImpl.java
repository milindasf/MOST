package bpi.most.service.impl;

import bpi.most.domain.connector.ConnectorFinder;
import bpi.most.domain.connector.ConnectorVO;
import bpi.most.domain.user.User;
import bpi.most.dto.UserDTO;
import bpi.most.service.api.ConnectorService;
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

    @PersistenceContext(unitName = "most")
    private EntityManager em;

    private ConnectorFinder connectorFinder;

    @PostConstruct
    protected void init() {
        connectorFinder = new ConnectorFinder(em);
    }

    @Override
    @Transactional
    public List<ConnectorVO> getConnection(UserDTO user) {
        //TODO check permission
        return connectorFinder.findConnections();
    }

}
