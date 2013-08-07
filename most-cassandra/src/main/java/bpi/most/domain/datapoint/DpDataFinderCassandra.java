package bpi.most.domain.datapoint;

import bpi.most.dto.DpDataDTO;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.factory.HFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Date;

/**
 *
 */
@Service
public class DpDataFinderCassandra implements IDatapointDataFinder {

    /**
     * connects to cassandra
     * @throws Exception
     */
    @PostConstruct
    public void initIt() throws Exception {
        Cluster myCluster = HFactory.getOrCreateCluster("test-cluster", "localhost:9160");
    }

    /**
     * gracefully releases connections to Cassandra
     * @throws Exception
     */
    @PreDestroy
    public void cleanUp() throws Exception {

    }

    @Override
    public DatapointDataVO getData(String dpName) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public DatapointDatasetVO getData(String dpName, Date starttime, Date endtime) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public DatapointDatasetVO getDataPeriodic(String dpName, Date starttime, Date endtime, Float period, int mode) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Integer getNumberOfValues(String dpName, Date starttime, Date endtime) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int addData(String dpName, DpDataDTO measurement) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int delData(String dpName) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int delData(String dpName, Date starttime, Date endtime) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
