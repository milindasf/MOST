package bpi.most.service.impl;

import bpi.most.domain.datapoint.DatapointFinder;
import bpi.most.domain.datapoint.DatapointVO;
import bpi.most.dto.UserDTO;
import bpi.most.service.api.DatapointService;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Implementation of {@link bpi.most.service.api.DatapointService}.
 *
 * @author Lukas Weichselbaum
 * @author Jakob Korherr
 */
@Service
public class DatapointServiceImpl implements DatapointService {

    private static final Logger LOG = LoggerFactory.getLogger(DatapointServiceImpl.class);

    @PersistenceContext(unitName = "most")
    private EntityManager em;

    private DatapointFinder datapointFinder;

    @PostConstruct
    protected void init() {
        datapointFinder = new DatapointFinder(em);
    }

    @Override
    @Transactional
    public boolean isValidDp(final String dpName) {
        return ((Session) em.getDelegate()).createSQLQuery("{CALL getDatapoint(:dpName)}")
                .setParameter("dpName", dpName)
                .setReadOnly(true)
                .scroll().first();
    }

    @Override
    @Transactional
    public List<DatapointVO> getDatapoints() {
        return getDatapoints(null);
    }

    @Override
    @Transactional
    public List<DatapointVO> getDatapoints(String searchstring) {
        return datapointFinder.getDatapoints(searchstring);
    }

    @Override
    @Transactional
    public List<DatapointVO> getDatapoints(String searchstring, String zone) {
        return datapointFinder.getDatapoints(searchstring, zone);
    }

    @Override
    @Transactional
    public DatapointVO getDatapoint(UserDTO user, DatapointVO dpDto) {
        // TODO implement permission system
        return dpDto;
    }

}
