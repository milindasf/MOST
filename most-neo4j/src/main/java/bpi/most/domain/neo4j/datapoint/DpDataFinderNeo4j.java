package bpi.most.domain.neo4j.datapoint;

import bpi.most.domain.datapoint.DatapointDataVO;
import bpi.most.domain.datapoint.DatapointDatasetVO;
import bpi.most.domain.datapoint.IDatapointDataFinder;
import bpi.most.dto.DpDataDTO;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: harald
 * Date: 13.08.13
 * Time: 13:16
 * To change this template use File | Settings | File Templates.
 */
@Service
public class DpDataFinderNeo4j implements IDatapointDataFinder {

    private GraphDatabaseFactory graphDbFactory;

    /**
     * connects to neo4j server
     * @throws Exception
     */
    @PostConstruct
    public void initIt() throws Exception {
        /**
         * initialize local neo4j VS connecting to neo4j standalone server.
         */
        graphDbFactory = new GraphDatabaseFactory();
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
