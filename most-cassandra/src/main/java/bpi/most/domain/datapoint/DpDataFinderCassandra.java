package bpi.most.domain.datapoint;

import bpi.most.dto.DpDataDTO;
import me.prettyprint.cassandra.model.BasicColumnDefinition;
import me.prettyprint.cassandra.model.CqlQuery;
import me.prettyprint.cassandra.model.CqlRows;
import me.prettyprint.cassandra.serializers.DateSerializer;
import me.prettyprint.cassandra.serializers.DoubleSerializer;
import me.prettyprint.cassandra.serializers.LongSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.beans.ColumnSlice;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.beans.OrderedRows;
import me.prettyprint.hector.api.beans.Row;
import me.prettyprint.hector.api.ddl.ColumnFamilyDefinition;
import me.prettyprint.hector.api.ddl.ColumnIndexType;
import me.prettyprint.hector.api.ddl.ComparatorType;
import me.prettyprint.hector.api.ddl.KeyspaceDefinition;
import me.prettyprint.hector.api.exceptions.HectorException;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;
import me.prettyprint.hector.api.query.QueryResult;
import org.hibernate.type.DoubleType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.xml.crypto.Data;
import java.sql.Timestamp;
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
    private static Keyspace keyspace=null;
    private ColumnFamilyDefinition cfdef=null;
    private Cluster myCluster=null;
    private static StringSerializer stringSerializer = StringSerializer.get();
    @PostConstruct
    public void initIt() throws Exception {
        try{
            myCluster = HFactory.getOrCreateCluster("test-cluster", "localhost:9160");
            ksdef=myCluster.describeKeyspace(keyspaceName);
            keyspace=HFactory.createKeyspace(keyspaceName, myCluster);
        }catch(HectorException e){
            LOG.error(e.getMessage(), e);
        }
    }

    /*
     * Adds new columnfamily to the cassandra keyspace
     * @param cfname : columnfamily name to add
     * Author : Nikunj Thakkar
     */
    public void addColumnFamily(String cfname)
    {
        if(cfname.equals("") || cfname.trim().equals(null))
        {

            return;
        }
        else
        {
            cfname= cfname.toLowerCase();
            if(checkExist(cfname)==false)
            {
                cfdef=HFactory.createColumnFamilyDefinition(keyspaceName, cfname);
                myCluster.addColumnFamily(cfdef, true);
            }

        }

    }

    /*
    * Checks if columnfamily alredy exist in the keyspace
    * @param cfname : columnfamily name to add
    * Author : Nikunj Thakkar
    */
    boolean checkExist(String cfname)
    {
        try
        {
            List<ColumnFamilyDefinition> lcf=ksdef.getCfDefs();
            Iterator<ColumnFamilyDefinition> it=lcf.iterator();
            while(it.hasNext())
            {
                ColumnFamilyDefinition cf=it.next();
                if(cf.getName().equals(cfname))
                return true;

            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
        return false;
    }


    /**
     * gracefully releases connections to Cassandra
     * @throws Exception
     */
    @PreDestroy
    public void cleanUp() throws Exception {

    }

    /**
     * This funtion returns the latest value for the given datapoint
     * @param dpName
     * @return DatapointDataVo
     * Author : Nikunj Thakkar
     */
    @Override
    public DatapointDataVO getData(String dpName) {

        // Define serializers to insert the data into columnfamily
        DateSerializer dt=new DateSerializer();
        LongSerializer ls=new LongSerializer();
        DoubleSerializer ds=new DoubleSerializer();

        //Create Cqlquery to insert the data into columnfamily
        CqlQuery<Date,Long,Double> qry=new CqlQuery<Date, Long, Double>(keyspace,dt,ls,ds);
        qry.setQuery("Select * from "+dpName);

        //Executing the query
        QueryResult<CqlRows<Date, Long, Double>> result = qry.execute();

        //Extracting the resultant rows from the result
        OrderedRows<Date, Long, Double> rows = result.get();

        //Peeking last row from the rows
        Row<Date, Long, Double> row=rows.peekLast();

        //row contains number of columns
        //Columnslice is used to extract the columns from rows
        ColumnSlice<Long,Double> csl=row.getColumnSlice();

        //Extracting columns from columnslice
        List<HColumn<Long,Double>> rsltlist = csl.getColumns();

        //Getting the value of last column which is latest value in that column
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

    /*
     * addData adds data to the cassndra columnfamily
     * @param dpName : columnfamily name
     * @param measurement: measurements to insert
     * Author : Nikunj Thakkar
     */
    @Override
    public int addData(String dpName, DpDataDTO measurement) {
        try
        {
            Date d=measurement.getTimestamp();
            Long ts=measurement.getTimestamp().getTime();
            Double value=measurement.getValue();
            //Create serialize object
            DateSerializer ds=new DateSerializer();
            //Create Mutator object to insert data to columnfamily
            Mutator<Date> mu=HFactory.createMutator(keyspace, ds);

            //Here d act as a row key and columns are added to single row
            mu.insert(d, dpName, HFactory.createColumn(ts, value));
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int delData(String cfname) {
        if(cfname.equals("") || cfname.trim().equals(null))
        {
            System.out.println("Null Value");
            return 1;
        }
        else
        {
            cfname= cfname.toLowerCase();
            if(checkExist(cfname))
            myCluster.dropColumnFamily(keyspaceName,cfname);
            return 0;

        }


    }

    @Override
    public int delData(String dpName, Date starttime, Date endtime) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
