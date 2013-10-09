package bpi.most.domain.datapoint;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: harald
 * Date: 19.09.13
 * Time: 09:44
 * To change this template use File | Settings | File Templates.
 */
public class DummyDpFinder implements IDatapointFinder{
    @Override
    public DatapointVO getDatapoint(String dpName) {
        DatapointVO dp = new DatapointVO(dpName, "testdatapoint", "just for testing", null, "degree", null, new BigDecimal(100), null, new BigDecimal(2), new BigDecimal(3), new BigDecimal(2), null, null, null);
        return dp;
    }

    @Override
    public Datapoint getDatapointEntity(String dpName) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<DatapointVO> getDatapoints(String searchstring) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<DatapointVO> getDatapoints(String searchstring, String zone) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<DatapointVO> getDpFromSubZones(int zoneId, int sublevels) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
