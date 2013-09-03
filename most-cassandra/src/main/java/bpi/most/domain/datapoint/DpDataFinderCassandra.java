package bpi.most.domain.datapoint;
import bpi.most.dto.DpDataDTO;
import com.datastax.driver.core.*;
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

    private static final String keyspace="most";
    private String query;
    /**
     * connects to cassandra
     * @throws Exception
     */
    public Cluster cluster=null;
    public Metadata metadata;
    public Session session;
    @PostConstruct
    public void initIt() throws Exception {
        try
        {
            cluster = com.datastax.driver.core.Cluster.builder().addContactPoint(CASSANDRA_ADDRESS).build();
            session = cluster.connect("most");
            /*metadata = cluster.getMetadata();
            System.out.printf("Connected to cluster: %s\n",metadata.getClusterName());
            for ( Host host : metadata.getAllHosts() )
            {
                System.out.printf("Datacenter: %s; Host: %s; Rack: %s\n",host.getDatacenter(), host.getAddress(), host.getRack());
            }*/
        }
        catch(Exception e){
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
        return;
        else
        {
            cfname= cfname.toLowerCase();
            if(checkExist(cfname)==false)
            {
                session=cluster.connect();
                session.execute("use "+keyspace);
                session.execute("CREATE TABLE "+cfname+"(day timestamp,ts timestamp,value double, PRIMARY KEY(day,ts))");
            }
        }

    }
    /*
    * Checks if columnfamily alredy exist in the keyspace
    * @param cfname : columnfamily name to check
    * Author : Nikunj Thakkar
    */
    boolean checkExist(String cfname)
    {
        try
        {
            metadata=cluster.getMetadata();
            KeyspaceMetadata kspm=metadata.getKeyspace("most");
            for(TableMetadata tableMetadatas : kspm.getTables())
            {
                if(tableMetadatas.getName().equals(cfname))
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
           cluster.shutdown();
    }
     /**
     * This funtion returns the latest value for the given datapoint
     * @param dpName
     * @return DatapointDataVo
     * Author : Nikunj Thakkar
     */
    @Override
    public DatapointDataVO getData(String dpName) {
        if(!checkExist(dpName))
        {
            LOG.error("Column Family doesn't exist for datapoint : "+dpName);
            return null;
        }

        session = cluster.connect();  //Connect to the cassandra cluster
        session.execute("use most");  //Specifying keysapce name

        //Prepare select query to execute
        query="Select day FROM "+dpName;
        ResultSet results = session.execute(query);
        List<Date> dateList=new ArrayList<Date>(100);
        int i=0;
        for (com.datastax.driver.core.Row row : results) {
            dateList.add(row.getDate("day"));
            i++;
        }
        LOG.debug("values:" + i);
        Collections.sort(dateList);
        Date lastDate=dateList.get(dateList.size()-1);
        LOG.debug("Last Date: "+lastDate);
        query="select * from "+dpName+" where day="+lastDate.getTime()+" order by ts DESC limit 1";
        ResultSet r=session.execute(query);
        Row rs=r.all().get(0);
        LOG.debug("Final results : "+rs);
        DatapointDataVO ds=new DatapointDataVO();
        ds.setTimestamp(rs.getDate("ts"));
        ds.setValue(rs.getDouble("value"));
        return ds;
    }








        /*
        LOG.debug("Running GetData Method Cassandra");
        // Define serializers to insert the data into columnfamily
        StringSerializer st=new StringSerializer();
        LongSerializer ls=new LongSerializer();
        DoubleSerializer ds=new DoubleSerializer();

        SliceQuery<String, Long, Double> sliceQuery = HFactory.createSliceQuery(keyspace, st,ls,ds);
        sliceQuery.setColumnFamily(dpName).setKey("latestva");
        sliceQuery.setRange(null, null, false, 4);

        QueryResult<ColumnSlice<Long, Double>> result = sliceQuery.execute();
        System.out.println("\nInserted data is as follows:\n" + result.get());
        System.out.println();
        /*
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

        return null;  //To change body of implemented methods use File | Settings | File Templates.

    }


    /**
     *
     * Prepares select statement for the range queries
     * used repeatedly in many functions so generalized method is created
     * Author: Nikunj Thakkar
     *
     *
     */
    public String getSelectQuery(String dpName, Date starttime, Date endtime){


        //Getting row key from the starttime
        Calendar stime=Calendar.getInstance();
        stime.setTime(starttime);
        stime.set(Calendar.DAY_OF_MONTH,1);
        stime.set(Calendar.HOUR_OF_DAY,0);
        stime.clear(Calendar.MINUTE);
        stime.clear(Calendar.SECOND);
        stime.clear(Calendar.MILLISECOND);

        //Getting row key from the endtime
        Calendar etime=Calendar.getInstance();
        etime.setTime(endtime);
        etime.set(Calendar.DAY_OF_MONTH,1);
        etime.set(Calendar.HOUR_OF_DAY,0);
        etime.set(Calendar.MINUTE,0);
        etime.set(Calendar.SECOND,0);
        etime.set(Calendar.MILLISECOND,0);

        //Counting month difference between starttime and endtime to generate rowkey sequence
        int diffYear = etime.get(Calendar.YEAR) - stime.get(Calendar.YEAR);
        int diffMonth = diffYear * 12 + etime.get(Calendar.MONTH) - stime.get(Calendar.MONTH);

        //Generating rowkey sequence to be used to fetch the data from cassandra
        Calendar temp=stime;
        temp.add(Calendar.MONTH,-1);

        StringBuffer sb = new StringBuffer();
        for(int i=0;i<=diffMonth;i++)
        {
            temp.add(Calendar.MONTH,1);
            sb.append(String.format("'%s',",temp.getTimeInMillis()));
            System.out.println(temp.getTime());
        }
        sb.deleteCharAt(sb.length()-1);

        //Preparing query for the execution
        query="select * from "+dpName+" where day IN ("+sb+") and ts >= "+starttime.getTime()+" and ts <= "+endtime.getTime()+" order by ts desc";
        return query;
    }
    @Override
    public DatapointDatasetVO getData(String dpName, Date starttime, Date endtime){
        if(!starttime.before(endtime) || !checkExist(dpName))
        return null;

        //Initializing the session object used to interact with the cassandra
        session = cluster.connect();  //Connect to the cassandra cluster
        session.execute("use most");  //Specifying keysapce name

        query=getSelectQuery(dpName,starttime,endtime);
        ResultSet results = session.execute(query);
        int i=0;
        for (com.datastax.driver.core.Row row : results) {
            System.out.println(String.format("at %s; value: %s", row.getDate("ts"),row.getDouble("value")));
            i++              ;
        }
        System.out.println("values:" + i);
        return null;

    }
    /*

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
    */
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
        if(!starttime.before(endtime) || !checkExist(dpName))
        return null;

        session = cluster.connect();  //Connect to the cassandra cluster
        session.execute("use most");  //Specifying keysapce name

        //Get select query to execute for given period
        query=getSelectQuery(dpName,starttime,endtime);

        //Since we need only count of the columns for the specified duration we are getting only count value
        //using select count(*).... query

        String q=query.replace("*","count(*)");
        ResultSet results = session.execute(q);

        //Returned result is BigInt type casting it to int and return the result

        Integer rtrn=new Integer((int) results.one().getLong(0));
        System.out.println("Return : " + rtrn);

        return rtrn;
    }
    /*
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
   */
    /*
     * addData adds data to the cassndra columnfamily
     * @param dpName : columnfamily name
     * @param measurement: measurements to insert
     * Author : Nikunj Thakkar
     */
    @Override
    public int addData(String dpName,DpDataDTO measurement) {
        if(!checkExist(dpName))
        {
            LOG.error("Column family does not exist for this datapoint");
            return 0;
        }
        try
        {
            Date d=measurement.getTimestamp();

            //Setting rowkey for the given measurement
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(d);
            calendar.set(Calendar.DATE,1);
            calendar.set(Calendar.HOUR_OF_DAY,0);
            calendar.clear(Calendar.MINUTE);
            calendar.clear(Calendar.SECOND);
            calendar.clear(Calendar.MILLISECOND);
            Long truncatedDate = calendar.getTimeInMillis();
            Long ts=measurement.getTimestamp().getTime();
            Double value=measurement.getValue();
            //Create serialize object

      //      session=cluster.connect();
            session.execute("use most");
            session.execute("insert into "+dpName+"(day,ts,value) values("+truncatedDate+","+ts+","+value+")");
           /* long rowkey=0;
            session.execute("DELETE FROM "+dpName+" where day="+rowkey);
            session.execute("insert into "+dpName+"(day,ts,value) values("+rowkey+","+ts+","+value+")");*/


            /*
            DateSerializer ds=new DateSerializer();
            StringSerializer ss=new StringSerializer();
            //Create Mutator object to insert data to columnfamily
            Mutator<Date> mu=HFactory.createMutator(keyspace, ds);
            //Here d act as a row key and columns are added to single row
            mu.insert(truncatedDate, dpName, HFactory.createColumn(ts, value));
            Mutator<String> su=HFactory.createMutator(keyspace,ss);
            su.delete("latestva",dpName,null,ss);
            su.insert("latestva",dpName,HFactory.createColumn(ts,value));*/
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
               session=cluster.connect();
               session.execute("use most");
               session.execute("Drop table "+cfname);
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
        if(!starttime.before(endtime) || !checkExist(dpName))
            return 0;

        //Initializing the session object used to interact with the cassandra
        session = cluster.connect();  //Connect to the cassandra cluster
        session.execute("use most");  //Specifying keysapce name

        query=getSelectQuery(dpName,starttime,endtime);
        ResultSet results = session.execute(query);
        int i=0;
        for (com.datastax.driver.core.Row row : results) {
            session.execute("DELETE FROM "+dpName+" where day="+row.getDate("day").getTime()+" AND ts="+row.getDate("ts").getTime());
            System.out.println(String.format("Deleted timestamp: %s; value: %s", row.getDate("ts"),row.getDouble("value")));
            i++              ;
        }
        System.out.println("values:" + i);

        return i;
    }

     /*   int del_count=0;
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
    } */
}
