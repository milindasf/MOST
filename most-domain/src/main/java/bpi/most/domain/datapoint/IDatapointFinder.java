package bpi.most.domain.datapoint;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: harald
 * Date: 19.09.13
 * Time: 09:26
 * To change this template use File | Settings | File Templates.
 */
public interface IDatapointFinder {
    DatapointVO getDatapoint(String dpName);

    Datapoint getDatapointEntity(String dpName);

    List<DatapointVO> getDatapoints(String searchstring);

    List<DatapointVO> getDatapoints(String searchstring, String zone);

    List<DatapointVO> getDpFromSubZones(int zoneId, int sublevels);
}
