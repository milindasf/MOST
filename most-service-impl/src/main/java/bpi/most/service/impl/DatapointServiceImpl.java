package bpi.most.service.impl;

import bpi.most.domain.datapoint.DatapointDataVO;
import bpi.most.domain.datapoint.DatapointFinder;
import bpi.most.domain.datapoint.DatapointVO;
import bpi.most.dto.DpDTO;
import bpi.most.dto.DpDataDTO;
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
import java.util.ArrayList;
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
    public List<DpDTO> getDatapoints() {
        return getDatapoints(null);
    }

    @Override
    @Transactional
    public List<DpDTO> getDatapoints(String searchstring) {
        return transformToDpDTOList(datapointFinder.getDatapoints(searchstring));
    }

    @Override
    @Transactional
    public List<DpDTO> getDatapoints(String searchstring, String zone) {
        return transformToDpDTOList(datapointFinder.getDatapoints(searchstring, zone));
    }

    @Override
    @Transactional
    public DpDTO getDatapoint(UserDTO user, DpDTO dpDto) {
        // Fetch all data from DB
        DatapointVO dp = datapointFinder.getDatapoint(dpDto.getName());
        if(dp == null){
            return null;
        }

        // TODO implement permission system
        return dp.getDTO();
    }

    /**
     * latest measurement see {@link bpi.most.server.model.Datapoint#getData()}
     *
     * @return DatapointDatasetVO of requested timeframe, null if no permissions TODO:
     *         throw exceptions if no permissions, etc.
     */
    public DpDataDTO getData(UserDTO user, DpDTO dpDTO) {
        DpDataDTO result = null;
        DatapointVO dp = datapointFinder.getDatapoint(dpDTO.getName());

        // TODO ASE: return the correct DpDataDTO! We will need DpVirtual + DpPhysical...
        /*
        // check permissions
        if (user.hasPermission(dp, DpDTO.Permissions.READ)) {
            result = dp.getData();
        }
        */
        return result;
    }

    private List<DpDTO> transformToDpDTOList(List<DatapointVO> dpList){
        List<DpDTO> dpDTOList = null;

        if(dpList != null){
            dpDTOList = new ArrayList<DpDTO>();
            for (DatapointVO datapointVO : dpList) {
                dpDTOList.add(datapointVO.getDTO());
            }
        }

        return dpDTOList;
    }
}
