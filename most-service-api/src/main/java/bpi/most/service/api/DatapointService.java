package bpi.most.service.api;

import java.util.ArrayList;
import java.util.Date;
import java.util.Observer;

import bpi.most.domain.datapoint.Dp;
import bpi.most.domain.datapoint.DpData;
import bpi.most.domain.datapoint.DpDataset;
import bpi.most.domain.user.User;


/**
 * Interface Specification of Datapoint Service.
 *
 * @author Lukas Weichselbaum
 */
public interface DatapointService {
    public int addData(User user, Dp dp, DpData measurement);

    public DpData getData(User user, Dp dp);

    public DpDataset getData(User user, Dp dp, Date starttime, Date endtime);

    public DpDataset getDataPeriodic(User user, Dp dp, Date starttime, Date endtime, Float period);

    public int getNumberOfValues(User user, Dp dp, Date starttime, Date endtime);

    public boolean addDpObserver(User user, Dp dpEntity, Observer observer);

    public boolean deleteDpObserver(User user, Dp dpEntity, Observer observer);

    public boolean addWarningObserver(User user, Observer observer);

    public boolean deletesWarningObserver(User user, Observer observer);

    public ArrayList<Dp> getDatapoints(User user);

    public ArrayList<Dp> getDatapoints(User user, String searchstring);

    public ArrayList<Dp> getDatapoints(User user, String searchstring, String zone);

    public Dp getDatapoint(User user, Dp dp);
}
