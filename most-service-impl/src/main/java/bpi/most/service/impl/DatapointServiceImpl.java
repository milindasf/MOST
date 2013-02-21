package bpi.most.service.impl;

import bpi.most.domain.datapoint.DatapointDataFinder;
import bpi.most.domain.datapoint.DatapointDataVO;
import bpi.most.domain.datapoint.DatapointFinder;
import bpi.most.domain.datapoint.DatapointVO;
import bpi.most.dto.DpDTO;
import bpi.most.dto.DpDataDTO;
import bpi.most.dto.DpDatasetDTO;
import bpi.most.dto.UserDTO;
import bpi.most.service.api.DatapointService;
import bpi.most.service.impl.datapoint.virtual.VirtualDatapointDataFinder;

import org.hibernate.Session;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Implementation of {@link bpi.most.service.api.DatapointService}.
 *
 * @author Lukas Weichselbaum
 * @author Jakob Korherr
 */
@Service
public class DatapointServiceImpl implements DatapointService {

    @PersistenceContext(unitName = "most")
    private EntityManager em;

    private DatapointFinder datapointFinder;
    private DatapointDataFinder datapointDataFinder;
    private VirtualDatapointDataFinder virtualDatapointDataFinder;

    @PostConstruct
    protected void init() {
        datapointFinder = new DatapointFinder(em);
        datapointDataFinder = new DatapointDataFinder(em);
        virtualDatapointDataFinder = new VirtualDatapointDataFinder(em);
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
    public DpDTO getDatapoint(UserDTO userDTO, DpDTO dpDto) {
        // Fetch all data from DB
        DatapointVO dp = datapointFinder.getDatapoint(dpDto.getName());
        if(dp != null && userDTO.hasPermission(dp, DpDTO.Permissions.READ)){
            return dp.getDTO();
        }
        return null;
    }

    /**
     * latest measurement
     *
     * @return DatapointDatasetVO of requested timeframe, null if no permissions TODO:
     *         throw exceptions if no permissions, etc.
     */
    @Override
    @Transactional
    public DpDataDTO getData(UserDTO userDTO, DpDTO dpDTO) {
        DpDataDTO result = null;
        DatapointVO dp = datapointFinder.getDatapoint(dpDTO.getName());
        if(dp != null && userDTO.hasPermission(dp, DpDTO.Permissions.READ)){
        	DatapointDataVO data = null;
	        if(!dp.isVirtual()) {
		        data = datapointDataFinder.getData(dp.getName());
        	} else {
            	data = virtualDatapointDataFinder.getData(datapointFinder.getDatapointEntity(dp.getName()));
        	}
	        if(data != null){
	        	result = data.getDTO();
	        }
        }
        return result;
    }

    @Override
    @Transactional
    public DpDatasetDTO getData(UserDTO userDTO, DpDTO dpDTO, Date starttime, Date endtime) {
    	DpDatasetDTO result = null;
        DatapointVO dp = datapointFinder.getDatapoint(dpDTO.getName());
        if(dp != null && userDTO.hasPermission(dp, DpDTO.Permissions.READ)){
        	List<DatapointDataVO> data = null;
	        if(!dp.isVirtual()) {
		        data = datapointDataFinder.getData(dp.getName(), starttime, endtime);	
        	} else {
            	data = virtualDatapointDataFinder.getData(datapointFinder.getDatapointEntity(dp.getName()), starttime, endtime);
        	}
	        if(data != null && data.size() > 0){
	        	result = new DpDatasetDTO(dp.getName());
	        	for(DatapointDataVO vo : data){
	        		result.add(vo.getDTO());
	        	}
	        }
        }
        return result;
    }

    @Override
    @Transactional
    public DpDatasetDTO getDataPeriodic(UserDTO userDTO, DpDTO dpDTO, Date starttime, Date endtime, Float period) {
        return getDataPeriodic(userDTO, dpDTO, starttime, endtime, period, 1);
    }

    @Override
    @Transactional
    public DpDatasetDTO getDataPeriodic(UserDTO userDTO, DpDTO dpDTO, Date starttime, Date endtime, Float period, int mode) {
        DpDatasetDTO result = null;
        DatapointVO dp = datapointFinder.getDatapoint(dpDTO.getName());
        if(dp != null && userDTO.hasPermission(dp, DpDTO.Permissions.READ)){
            // set mode of getDataPeriodic() to 1, because other modes are
            // currently not well supported (or even not implemented)
        	List<DatapointDataVO> data = null;
	        if(!dp.isVirtual()) {
		        data = datapointDataFinder.getDataPeriodic(dp.getName(), starttime, endtime, period, mode);
        	} else {
            	data = virtualDatapointDataFinder.getDataPeriodic(datapointFinder.getDatapointEntity(dp.getName()), starttime, endtime, period, mode);
        	}
            if(data != null && data.size() > 0){
                result = new DpDatasetDTO(dp.getName());
                for(DatapointDataVO vo : data){
                    result.add(vo.getDTO());
                }
            }
        }
        return result;
    }


    @Override
    @Transactional
    public int getNumberOfValues(UserDTO userDTO, DpDTO dpDTO, Date starttime, Date endtime) {
    	int result = 0;
        DatapointVO dp = datapointFinder.getDatapoint(dpDTO.getName());
        if(dp != null && userDTO.hasPermission(dp, DpDTO.Permissions.READ)){
        	Integer number = null;
	        if(!dp.isVirtual()) {
	        	number = datapointDataFinder.getNumberOfValues(dp.getName(), starttime, endtime);
        	} else {
        		number = virtualDatapointDataFinder.getNumberOfValues(datapointFinder.getDatapointEntity(dp.getName()), starttime, endtime);
        	}
	        if(number != null){
	        	result = number;
	        }
        }
        return result;
    }

    @Override
    public void addObserver(String dpName, Object connector) {
        // TODO
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
    
    /**
	 * add new measurement
	 * 
	 * @return 1 = inserted; < 0 constraints violated or procedure error TODO:
	 *         throw exceptions if no permissions, etc.
	 */
    @Override
    @Transactional
    public int addData(UserDTO userDTO, DpDTO dpDTO, DpDataDTO measurement) {
		int result = 0;
        DatapointVO dp = datapointFinder.getDatapoint(dpDTO.getName());
		// check permissions
		if (dp != null && userDTO.hasPermission(dp, DpDTO.Permissions.WRITE)) {
			result = datapointDataFinder.addData(dp.getName(), measurement);
		}
		return result;
	}
	
	/**
	 * Deletes all stored data of datapoint. Use with caution!!
	 */
    @Override
    @Transactional
    public int delData(UserDTO userDTO, DpDTO dpDTO) {
		int result = 0;
        DatapointVO dp = datapointFinder.getDatapoint(dpDTO.getName());
		// check permissions
		if (dp != null && userDTO.hasPermission(dp, DpDTO.Permissions.WRITE)) {
			result = datapointDataFinder.delData(dp.getName());
		}
		return result;
	}

	/**
	 * Deletes data of a given timeslot
	 */
    @Override
    @Transactional
    public int delData(UserDTO userDTO, DpDTO dpDTO, Date starttime, Date endtime) {
		int result = 0;
        DatapointVO dp = datapointFinder.getDatapoint(dpDTO.getName());
		// check permissions
		if (dp != null && userDTO.hasPermission(dp, DpDTO.Permissions.WRITE)) {
			result = datapointDataFinder.delData(dp.getName(), starttime, endtime);
		}
		return result;
	}

}
