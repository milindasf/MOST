package bpi.most.service.impl;

import bpi.most.domain.datapoint.Dp;
import bpi.most.domain.datapoint.DpData;
import bpi.most.domain.datapoint.DpDataset;
import bpi.most.domain.user.User;
import bpi.most.service.api.DatapointService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.Observer;

/**
 * Implementation of {@link bpi.most.service.api.DatapointService}.
 *
 * @author Lukas Weichselbaum
 */
@Service
public class DatapointServiceImpl implements DatapointService{

    private static final Logger log = LoggerFactory.getLogger(DatapointServiceImpl.class);

    @PersistenceContext(unitName = "most")
    private EntityManager em;

    @Override
    public int addData(User user, Dp dp, DpData measurement) {
        return 0;  //TODO: implement
    }

    @Override
    public DpData getData(User user, Dp dp) {
        return null;  //TODO: implement
    }

    @Override
    public DpDataset getData(User user, Dp dp, Date starttime, Date endtime) {
        return null;  //TODO: implement
    }

    @Override
    public DpDataset getDataPeriodic(User user, Dp dp, Date starttime, Date endtime, Float period) {
        return null;  //TODO: implement
    }

    @Override
    public int getNumberOfValues(User user, Dp dp, Date starttime, Date endtime) {
        return 0;  //TODO: implement
    }

    @Override
    public boolean addDpObserver(User user, Dp dpEntity, Observer observer) {
        return false;  //TODO: implement
    }

    @Override
    public boolean deleteDpObserver(User user, Dp dpEntity, Observer observer) {
        return false;  //TODO: implement
    }

    @Override
    public boolean addWarningObserver(User user, Observer observer) {
        return false;  //TODO: implement
    }

    @Override
    public boolean deletesWarningObserver(User user, Observer observer) {
        return false;  //TODO: implement
    }

    @Override
    public ArrayList<Dp> getDatapoints(User user) {
        return null;  //TODO: implement
    }

    @Override
    public ArrayList<Dp> getDatapoints(User user, String searchstring) {
        return null;  //TODO: implement
    }

    @Override
    public ArrayList<Dp> getDatapoints(User user, String searchstring, String zone) {
        return null;  //TODO: implement
    }

    @Override
    public Dp getDatapoint(User user, Dp dp) {
        return null;  //TODO: implement
    }
}
