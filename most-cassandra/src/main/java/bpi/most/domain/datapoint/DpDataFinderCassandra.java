package bpi.most.domain.datapoint;

import bpi.most.dto.DpDataDTO;
import com.datastax.driver.core.*;
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
import me.prettyprint.hector.api.query.RangeSlicesQuery;
import org.hibernate.type.DateType;
import org.hibernate.type.DoubleType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.xml.crypto.Data;
import java.nio.ByteBuffer;
import java.sql.Timestamp;
import java.util.*;

/**
 *
 */
@Service
public class DpDataFinderCassandra implements IDatapointDataFinder{

    private static final Logger LOG = LoggerFactory.getLogger(DpDataFinderCassandra.class);

    private static final String CASSANDRA_ADDRESS = "128.130.110.94";
    //private static final String CASSANDRA_ADDRESS = "localhost";

    /**
     * connects to cassandra
     * @throws Exception
     */
    private KeyspaceDefinition ksdef=null;
    private static String keyspaceName= "most";
    private static Keyspace keyspace=null;
    private ColumnFamilyDefinition cfdef=null;
    private Cluster myCluster=null;
    private static StringSerializer stringSerializer = StringSerializer.get();
    @PostConstruct
    public void initIt() throws Exception {
        try{
            myCluster = HFactory.getOrCreateCluster("test-cluster", CASSANDRA_ADDRESS + ":9160");
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
                cfdef.setComparatorType(ComparatorType.DATETYPE);
                cfdef.setKeyValidationClass(ComparatorType.DATETYPE.getClassName());
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

        LOG.debug("Running GetData Method Cassandra");
        // Define serializers to insert the data into columnfamily
        DateSerializer dt=new DateSerializer();
        LongSerializer ls=new LongSerializer();
        DoubleSerializer ds=new DoubleSerializer();

        //Create Cqlquery to insert the data into columnfamily
        CqlQuery<Date,Long,Double> qry=new CqlQuery<Date, Long, Double>(keyspace,dt,ls,ds);
        qry.setQuery("Select * from "+dpName);

        LOG.debug("Executing the query");
        //Executing the query
        QueryResult<CqlRows<Date, Long, Double>> result = qry.execute();

        //Extracting the resultant rows from the result
        OrderedRows<Date, Long, Double> rows = result.get();

        LOG.debug("Peeking last row from the rows");
        //Peeking last row from the rows
        Row<Date, Long, Double> row=rows.peekLast();

        //row contains number of columns
        //Columnslice is used to extract the columns from rows
        ColumnSlice<Long,Double> csl=row.getColumnSlice();

        //Extracting columns from columnslice
        List<HColumn<Long,Double>> rsltlist = csl.getColumns();

        LOG.debug("Getting the value of last column which is latest value in that column");
        //Getting the value of last column which is latest value in that column
        HColumn<Long,Double> hc=rsltlist.get(rsltlist.size()-1);
        DatapointDataVO dtpnt=new DatapointDataVO();
        dtpnt.setTimestamp(new Date(hc.getName().longValue()));
        dtpnt.setValue(hc.getValue());
        LOG.debug("TimeStamp :"+dtpnt.getTimestamp()+" Value :"+dtpnt.getValue());
        return dtpnt;

        //return null;  //To change body of implemented methods use File | Settings | File Templates.

    }

    /**
     *
     * uses datastax Cassandra java driver:
     * http://www.datastax.com/documentation/developer/java-driver/1.0/webhelp/index.html#java-driver/quick_start/qsSimpleClientCreate_t.html
     *
     * does a hardcoded query to test range queries and sort them.
     * did work on my machine!
     *
     */
    public void getDataSortedCon1() {
        com.datastax.driver.core.Cluster cluster = com.datastax.driver.core.Cluster.builder()
                .addContactPoint(CASSANDRA_ADDRESS).build();
        Metadata metadata = cluster.getMetadata();
        System.out.printf("Connected to cluster: %s\n",
                metadata.getClusterName());
        for ( Host host : metadata.getAllHosts() ) {
            System.out.printf("Datacenter: %s; Host: %s; Rack: %s\n",
                    host.getDatacenter(), host.getAddress(), host.getRack());
        }
        Session session = cluster.connect();
        session.execute("use most");
        StringBuffer sb = new StringBuffer();

        /**
         * create the IN CLAUSE for one year (from 1.1.2011 to 28.12.2011)
         * blabla
         */
        for (int m=1; m<=12; m++){
            //28 here to not create an error on february
            for (int d=1; d<=28; d++){
                sb.append(String.format("'2011-%d-%d 00:00:00+0200',", m, d));
            }
        }
        sb.deleteCharAt(sb.length()-1);
        LOG.debug("IN CLAUSE: " + sb.toString());
        ResultSet results = session.execute("select * from con1 where KEY IN (" + sb.toString() + ") AND column1 > '2011-01-1 16:00:00+0200' AND column1 < '2011-12-31 11:21:50+0200' order by column1 desc");
        int i=0;
        for (com.datastax.driver.core.Row row : results) {
            System.out.println(String.format("at %s; value: %s", row.getDate("column1"), row.getBytes("value")));
            i++              ;
        }
        System.out.println("values:" + i);

    }

    public DatapointDatasetVO getDataSorted(String dpName, Date starttime, Date endtime){
        DatapointDatasetVO values = new DatapointDatasetVO();

        LOG.debug("Running GetData Method Cassandra");
        // Define serializers to insert the data into columnfamily
        DateSerializer dt=new DateSerializer();
        LongSerializer ls=new LongSerializer();
        DoubleSerializer ds=new DoubleSerializer();

        //Create Cqlquery to insert the data into columnfamily
        CqlQuery<Date,Long,Double> qry=new CqlQuery<Date, Long, Double>(keyspace,dt,ls,ds);
        qry.setQuery("select * from con1 where KEY IN ('2011-06-20 00:00:00+0200', '2011-06-21 00:00:00+0200') AND column1 > '2011-06-20 16:00:00+0200' AND column1 < '2011-06-21 11:21:50+0200'");

        LOG.debug("Executing the query");
        //Executing the query
        QueryResult<CqlRows<Date, Long, Double>> result = qry.execute();

        //Extracting the resultant rows from the result
        OrderedRows<Date, Long, Double> rows = result.get();

        for (Row<Date, Long, Double> row: rows){
            //row contains number of columns
            //Columnslice is used to extract the columns from rows
            ColumnSlice<Long,Double> csl=row.getColumnSlice();

            //Extracting columns from columnslice
            List<HColumn<Long,Double>> columns = csl.getColumns();
            for (HColumn<Long,Double> col: columns){
                if (col.getName() != null){
                    LOG.debug(String.format("at %s; value: %f", new Date(col.getName()), col.getValue()));
                }
            }
        }
        return values;
    }

    @Override
    public DatapointDatasetVO getData(String dpName, Date starttime, Date endtime)
    {
        //First check if columnfamily exist or not
        if(checkExist(dpName))
        {
            DatapointDatasetVO returnData=new DatapointDatasetVO();
            //Required to specify row_count to fetch for row slice queries
            //Keep it the count higher than expected rows so it will not effect on result
            int row_count = 1000;

            LOG.debug("Operating on " + dpName + " table");

            // Define serializers to read the data from columnfamily
            DateSerializer dt=new DateSerializer();
            LongSerializer ls=new LongSerializer();
            DoubleSerializer ds=new DoubleSerializer();

            //We have stored long values of timestamp in columns so dates are converted to timestamp
            Long st=new Timestamp(starttime.getTime()).getTime();
            Long et=new Timestamp(endtime.getTime()).getTime();
            LOG.debug("Start time: "+st);
            LOG.debug("End time: "+et);

            //RangesliceQuery is used to fetch the rows in range from columnfamily
            RangeSlicesQuery<Date, Long, Double> sl=HFactory.createRangeSlicesQuery(keyspace, dt, ls,ds);
            sl.setColumnFamily(dpName).setRange(et,st,true,row_count)
            .setRowCount(row_count);

            //Used for fetching the row keys
            Date Lastkey=null;

            while (true)
            {
                sl.setKeys(Lastkey,null);

                QueryResult<OrderedRows<Date, Long, Double>> result = sl.execute();
                OrderedRows<Date, Long, Double> rows = result.get();
                Iterator<Row<Date, Long, Double>> rowsIterator = rows.iterator();

                // we'll skip this first one, since it is the same as the last one from previous time we executed
                if (Lastkey != null && rowsIterator != null) rowsIterator.next();

                while (rowsIterator.hasNext())
                {
                    Row<Date, Long, Double> row = rowsIterator.next();
                    Lastkey = row.getKey();
                    ColumnSlice<Long, Double> cs=row.getColumnSlice();

                    //Extracting columns from the columnslice
                    List<HColumn<Long, Double>> hc=cs.getColumns();
                    Iterator<HColumn<Long, Double>> hcit=hc.iterator();
                    while(hcit.hasNext())
                    {
                        HColumn<Long, Double> h=hcit.next();
                        DatapointDataVO data=new DatapointDataVO();
                        data.setTimestamp(new Date(h.getName()));
                        data.setValue(h.getValue());
                        returnData.add(data);
                        LOG.debug("Data :" + new Date(h.getName()) + "\t\t" + h.getValue());
                    }

                }
                return returnData;
            }
        }

        return null;

    }

    @Override
    public DatapointDatasetVO getDataPeriodic(String dpName, Date starttime, Date endtime, Float period, int mode) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /*
     * Returns the number of values for given datapoint in available in the given range
     * Author : Nikunj Thakkar
     */
    @Override
    public Integer getNumberOfValues(String dpName, Date starttime, Date endtime)
    {
        int result_count=0;
        //First check if columnfamily exist or not
       if(checkExist(dpName))
        {

            //Required to specify row_count to fetch for row slice queries
            //Keep it the count higher than expected rows so it will not effect on result
            int row_count = 1000;

            LOG.debug("Operating on " + dpName + " table");

            // Define serializers to read the data from columnfamily
            DateSerializer dt=new DateSerializer();
            LongSerializer ls=new LongSerializer();
            DoubleSerializer ds=new DoubleSerializer();

            //We have stored long values of timestamp in columns so dates are converted to timestamp
            Long st=new Timestamp(starttime.getTime()).getTime();
            Long et=new Timestamp(endtime.getTime()).getTime();
            LOG.debug("Start time: "+st);
            LOG.debug("End time: "+et);

            //RangesliceQuery is used to fetch the rows in range from columnfamily
            RangeSlicesQuery<Date, Long, Double> sl=HFactory.createRangeSlicesQuery(keyspace, dt, ls,ds);
            sl.setColumnFamily(dpName).setRange(et,st,true,row_count)
                    .setRowCount(row_count);

            //Used for fetching the row keys
            Date Lastkey=null;

            while (true)
            {
                sl.setKeys(Lastkey,null);

                QueryResult<OrderedRows<Date, Long, Double>> result = sl.execute();
                OrderedRows<Date, Long, Double> rows = result.get();
                Iterator<Row<Date, Long, Double>> rowsIterator = rows.iterator();

                // we'll skip this first one, since it is the same as the last one from previous time we executed
                if (Lastkey != null && rowsIterator != null) rowsIterator.next();

                while (rowsIterator.hasNext())
                {
                    Row<Date, Long, Double> row = rowsIterator.next();
                    Lastkey = row.getKey();
                    ColumnSlice<Long, Double> cs=row.getColumnSlice();

                    //Extracting columns from the columnslice
                    List<HColumn<Long, Double>> hc=cs.getColumns();
                    Iterator<HColumn<Long, Double>> hcit=hc.iterator();
                    while(hcit.hasNext())
                    {
                        HColumn<Long, Double> h=hcit.next();
                        LOG.debug("Deleted Data :" + new Date(h.getName()) + "\t\t" + h.getValue());
                        result_count++;
                    }
                }
                return new Integer(result_count);
            }
          //To change body of implemented methods use File | Settings | File Templates.
    }
    return null;
  }

    /*
     * addData adds data to the cassndra columnfamily
     * @param dpName : columnfamily name
     * @param measurement: measurements to insert
     * Author : Nikunj Thakkar
     */
    @Override
    public int addData(String dpName,DpDataDTO measurement) {
        try
        {
            Date d=measurement.getTimestamp();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(d);
            calendar.set(Calendar.HOUR_OF_DAY,0);
            calendar.clear(Calendar.MINUTE);
            calendar.clear(Calendar.SECOND);
            calendar.clear(Calendar.MILLISECOND);
            Date truncatedDate = calendar.getTime();
            Long ts=measurement.getTimestamp().getTime();
            Double value=measurement.getValue();
            //Create serialize object
            DateSerializer ds=new DateSerializer();
            //Create Mutator object to insert data to columnfamily
            Mutator<Date> mu=HFactory.createMutator(keyspace, ds);

            //Here d act as a row key and columns are added to single row
            mu.insert(truncatedDate, dpName, HFactory.createColumn(ts, value));
            return 1;
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /*
     * Deleting all data of given datapoint
     * Author : Nikunj Thakkar
     */
    @Override
    public int delData(String cfname) {

        //Check if string value is null or not
        if(cfname.equals("") || cfname.trim().equals(null))
        {
            LOG.debug("Null Value");
            return 0;
        }
        else
        {
            cfname= cfname.toLowerCase();
            if(checkExist(cfname))
            {
            myCluster.dropColumnFamily(keyspaceName,cfname);
            return 1;
            }


        }
        return 0;


    }

    /*
     * Deleting range of data from cilumnfamily
     * Author : Nikunj Thakkar
     */

    @Override
    public int delData(String dpName, Date starttime, Date endtime)
    {
        int del_count=0;
        //First check if columnfamily exist or not
        Iterator<HColumn<Long, Double>> hcit=null;
        if(checkExist(dpName))
        {

            //Required to specify row_count to fetch for row slice queries
            //Keep it the count higher than expected rows so it will not effect on result
            int row_count = 1000;

            LOG.debug("Operating on " + dpName + " table");

            // Define serializers to read the data from columnfamily
            DateSerializer dt=new DateSerializer();
            LongSerializer ls=new LongSerializer();
            DoubleSerializer ds=new DoubleSerializer();

            //We have stored long values of timestamp in columns so dates are converted to timestamp
            Long st=new Timestamp(starttime.getTime()).getTime();
            Long et=new Timestamp(endtime.getTime()).getTime();
            LOG.debug("Start time: "+st);
            LOG.debug("End time: "+et);

            //RangesliceQuery is used to fetch the rows in range from columnfamily
            RangeSlicesQuery<Date, Long, Double> sl=HFactory.createRangeSlicesQuery(keyspace, dt, ls,ds);
            sl.setColumnFamily(dpName).setRange(et,st,true,row_count)
                    .setRowCount(row_count);

            //Used for fetching the row keys
            Date Lastkey=null;

            while (true)
            {
                sl.setKeys(Lastkey,null);

                QueryResult<OrderedRows<Date, Long, Double>> result = sl.execute();
                OrderedRows<Date, Long, Double> rows = result.get();
                Iterator<Row<Date, Long, Double>> rowsIterator = rows.iterator();

                // we'll skip this first one, since it is the same as the last one from previous time we executed
                if (Lastkey != null && rowsIterator != null) rowsIterator.next();

                while (rowsIterator.hasNext())
                {
                    Row<Date, Long, Double> row = rowsIterator.next();
                    Lastkey = row.getKey();
                    ColumnSlice<Long, Double> cs=row.getColumnSlice();

                    //Extracting columns from the columnslice
                    List<HColumn<Long, Double>> hc=cs.getColumns();
                    hcit=hc.iterator();
                    DateSerializer dser=new DateSerializer();
                    LongSerializer lser=new LongSerializer();
                    Mutator<Date> mu=HFactory.createMutator(keyspace, dser);
                    while(hcit.hasNext())
                    {
                        HColumn<Long, Double> h=hcit.next();
                        System.out.println("Deleted Data :" + new Date(h.getName()) + "\t\t" + h.getValue());
                        del_count++;
                        mu.delete(Lastkey, dpName,h.getName() ,lser );
                    }

                }
                return del_count;
            }
        }

        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
