package bpi.most.service.impl;

import bpi.most.dto.DpDTO;
import bpi.most.dto.DpDataDTO;
import bpi.most.dto.DpDatasetDTO;
import bpi.most.dto.UserDTO;
import bpi.most.service.api.DatapointService;

import java.util.Date;
import java.util.List;

/**
 *
 * communicates with the DatapontService at the RMI Server
 *
 *
 * User: harald
 */
public class DatapointServiceRmiClient implements DatapointService{

    //TODO do not implement, but extend database DatapointServiceImpl --> update maven dependencies
    //create branch for virtual datapoint registry

    @Override
    public boolean isValidDp(String dpName) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<DpDTO> getDatapoints() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<DpDTO> getDatapoints(String searchstring) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<DpDTO> getDatapoints(String searchstring, String zone) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public DpDTO getDatapoint(UserDTO user, DpDTO dpDto) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public DpDataDTO getData(UserDTO userDTO, DpDTO dpDTO) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public DpDatasetDTO getData(UserDTO userDTO, DpDTO dpDTO, Date starttime, Date endtime) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public DpDatasetDTO getDataPeriodic(UserDTO userDTO, DpDTO dpDTO, Date starttime, Date endtime, Float period) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int getNumberOfValues(UserDTO userDTO, DpDTO dpDTO, Date starttime, Date endtime) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int addData(UserDTO userDTO, DpDTO dpDTO, DpDataDTO measurement) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int delData(UserDTO userDTO, DpDTO dpDTO) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int delData(UserDTO userDTO, DpDTO dpDTO, Date starttime, Date endtime) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public DpDatasetDTO getDataPeriodic(UserDTO user, DpDTO dpDTO, Date starttime, Date endtime, Float period, int mode) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void addObserver(String dpName, Object connector) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
