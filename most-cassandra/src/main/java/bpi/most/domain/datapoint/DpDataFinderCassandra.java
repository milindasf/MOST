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
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.query.QueryResult;
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
public class DpDataFinderCassandra{

    /**
     * connects to cassandra
     * @throws Exception
     */
    private KeyspaceDefinition ksdef=null;
    private static String keyspaceName= "most1";
    public static Keyspace keyspace=null;
    @PostConstruct
    public void initIt() throws Exception {
        Cluster myCluster = HFactory.getOrCreateCluster("test-cluster", "localhost:9160");
        ksdef=myCluster.describeKeyspace(keyspaceName);
        keyspace=HFactory.createKeyspace("most1", myCluster);

    }

    /**
     * gracefully releases connections to Cassandra
     * @throws Exception
     */
    @PreDestroy
    public void cleanUp() throws Exception {

    }

    //@Override
    public DatapointDataVO getData(String dpName) {
        DateSerializer dt=new DateSerializer();
        LongSerializer ls=new LongSerializer();
        DoubleSerializer ds=new DoubleSerializer();
        CqlQuery<Date,Long,Double> qry=new CqlQuery<Date, Long, Double>(keyspace,dt,ls,ds);
        qry.setQuery("Select * from "+dpName);
        QueryResult<CqlRows<Date, Long, Double>> result = qry.execute();
        OrderedRows<Date, Long, Double> rows = result.get();
        Iterator<Row<Date, Long, Double>> rowsIterator = rows.iterator();
        while (rowsIterator.hasNext()) {
            Row<Date, Long, Double> row = rowsIterator.next();
            if (row.getColumnSlice().getColumns().isEmpty()) {
                continue;
            }
            else
            {
                ColumnSlice<Long,Double> csl=row.getColumnSlice();
                List<HColumn<Long,Double>> rsltlist = csl.getColumns();
                boolean skiprow=true;
                Iterator<HColumn<Long,Double>> lit=rsltlist.iterator();
                while (lit.hasNext())
                {
                    HColumn<Long,Double> hc=lit.next();
                    if(skiprow)
                    {
                        skiprow=false;
                        continue;
                    }
                    else
                    {
                        System.out.println(new Date(hc.getName().longValue())+"\t\t->"+hc.getValue());
                    }

                }
            }
            //System.out.println(row.getColumnSlice().getColumns().get(1).getValue()+" "+new Date(row.getColumnSlice().getColumns().get(1).getName().longValue()));
        }
        return null;  //To change body of implemented methods use File | Settings | File Templates.

    }
    /*
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
    }  */
}
