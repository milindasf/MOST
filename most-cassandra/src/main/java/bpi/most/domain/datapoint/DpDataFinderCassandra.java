package bpi.most.domain.datapoint;

import bpi.most.dto.DpDataDTO;
import me.prettyprint.cassandra.model.CqlQuery;
import me.prettyprint.cassandra.model.CqlRows;
import me.prettyprint.cassandra.serializers.DateSerializer;
import me.prettyprint.cassandra.serializers.DoubleSerializer;
import me.prettyprint.cassandra.serializers.LongSerializer;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.beans.ColumnSlice;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.beans.OrderedRows;
import me.prettyprint.hector.api.beans.Row;
import me.prettyprint.hector.api.ddl.KeyspaceDefinition;
import me.prettyprint.hector.api.exceptions.HectorException;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.query.QueryResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 */
@Service
public class DpDataFinderCassandra implements IDatapointDataFinder{

    private static final Logger LOG = LoggerFactory.getLogger(DpDataFinderCassandra.class);

    /**
     * connects to cassandra
     * @throws Exception
     */
    private KeyspaceDefinition ksdef=null;
    private static String keyspaceName= "most1";
    public static Keyspace keyspace=null;

    @PostConstruct
    public void initIt() throws Exception {
        try{
            Cluster myCluster = HFactory.getOrCreateCluster("test-cluster", "localhost:9160");
            ksdef=myCluster.describeKeyspace(keyspaceName);
            keyspace=HFactory.createKeyspace(keyspaceName, myCluster);
        }catch(HectorException e){
            LOG.error(e.getMessage(), e);
        }
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

        DateSerializer dt=new DateSerializer();
        LongSerializer ls=new LongSerializer();
        DoubleSerializer ds=new DoubleSerializer();
        CqlQuery<Date,Long,Double> qry=new CqlQuery<Date, Long, Double>(keyspace,dt,ls,ds);
        qry.setQuery("Select * from "+dpName);
        QueryResult<CqlRows<Date, Long, Double>> result = qry.execute();
        OrderedRows<Date, Long, Double> rows = result.get();
        Row<Date, Long, Double> row=rows.peekLast();
        ColumnSlice<Long,Double> csl=row.getColumnSlice();
        List<HColumn<Long,Double>> rsltlist = csl.getColumns();
        HColumn<Long,Double> hc=rsltlist.get(rsltlist.size()-1);
        DatapointDataVO dtpnt=new DatapointDataVO();
        dtpnt.setTimestamp(new Date(hc.getName().longValue()));
        dtpnt.setValue(hc.getValue());
        return dtpnt;

        //return null;  //To change body of implemented methods use File | Settings | File Templates.

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
