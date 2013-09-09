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
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
    private static final long latestvalue=0;
    private String query;
    private String cfname;
    private Cluster cluster=null;
    private Metadata metadata;
    private Session session;


    /**
     * access to mysql database
     */
    @PersistenceContext(unitName = "most")
    private EntityManager em;
    private DatapointFinder dpFinderHibernate;

    @PostConstruct
    public void initIt() throws Exception {
        try
        {
            cluster = com.datastax.driver.core.Cluster.builder().addContactPoint(CASSANDRA_ADDRESS).build();
            session = cluster.connect(keyspace);
            session.execute("use "+keyspace);

            dpFinderHibernate = new DatapointFinder(em);
        }
        catch(Exception e){
            LOG.error(e.getMessage(), e);
        }
    }

    public boolean initSuccess(){
        return dpFinderHibernate != null && cluster != null && session != null;
    }

    /*
     * Adds new columnfamily to the cassandra keyspace
     * @param dpName : datapoint name to add as a columnfamily
     * Author : Nikunj
     */
    public void addColumnFamily(String dpName)
    {
        //Get valid columnfamily name for given datapoint
        cfname=getColumnFamilyName(dpName);

        //Check for validation if null then return
        if(cfname.equals("") || cfname.trim().equals(null))
        return;

        else
        {
            if(checkExist(cfname)==false)
            {
                session.execute("CREATE TABLE "+cfname+"(day timestamp,ts timestamp,value double, PRIMARY KEY(day,ts))");
            }
        }

    }
    /*
    * Checks if columnfamily alredy exist in the keyspace
    * @param dpName : columnfamily name to check
    * Author : Nikunj
    */
    boolean checkExist(String dpName)
    {
        //Get valid columnfamily name for given datapoint
        cfname=getColumnFamilyName(dpName);

        try
        {
            //Get metadata for given cluster. It returns details about existing columnfamilies
            metadata=cluster.getMetadata();
            KeyspaceMetadata kspm=metadata.getKeyspace(keyspace);

            //Loop through all table metadat exis in the keysapce
            for(TableMetadata tableMetadatas : kspm.getTables())
            {
                //if columnfamily exist for given datapoint then return true
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
    /*
     *  Returns valid columnFamily name from the given datapoint name
     *  Author : Nikunj
     */

    public String getColumnFamilyName(String dpName)
    {
        return dpName.replaceAll("[^a-zA-Z0-9]+","").toLowerCase();
    }
    /**
     * gracefully releases connections to Cassandra
     * @throws Exception
     */
    @PreDestroy
    public void cleanUp() throws Exception {
           //Closes down the cluster
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

        cfname=getColumnFamilyName(dpName);
        if(!checkExist(cfname))
        {
            LOG.error("Column Family doesn't exist for datapoint : "+dpName);
            return null;
        }
        query="select * from "+cfname+" where day="+latestvalue+" order by ts DESC limit 1";
        ResultSet r=session.execute(query);
        List<Row> lstrslt=r.all();
        if(lstrslt.size()==0)
        return null;
        Row rs=lstrslt.get(0);
        LOG.debug("Final results : "+rs);
        DatapointDataVO ds=new DatapointDataVO();
        ds.setTimestamp(rs.getDate("ts"));
        ds.setValue(rs.getDouble("value"));
        return ds;
    }
    /**
     *
     * Prepares select statement for the range queries
     * used repeatedly in many functions so generalized method is created
     * Author: Nikunj
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
            sb.append(String.format("'%s',",temp.getTime().getTime()));
            System.out.println(temp.getTime());
        }
        sb.deleteCharAt(sb.length()-1);

        //Preparing query for the execution
        query="select * from "+dpName+" where day IN ("+sb+") and ts >= "+starttime.getTime()+" and ts <= "+endtime.getTime()+" order by ts desc";
        System.out.println(query);
        return query;
    }
    @Override
    public DatapointDatasetVO getData(String dpName, Date starttime, Date endtime){
        if(!starttime.before(endtime) || !checkExist(dpName))
        return null;
        DatapointDatasetVO returnData=new DatapointDatasetVO();
        cfname=getColumnFamilyName(dpName);
        query=getSelectQuery(dpName,starttime,endtime);
        System.out.println(query);
        ResultSet results = session.execute(query);
        int i=0;
        for (com.datastax.driver.core.Row row : results) {
            DatapointDataVO returnRow = new DatapointDataVO();
            returnRow.setTimestamp(row.getDate("ts"));
            returnRow.setValue(row.getDouble("value"));
            System.out.println(String.format("at %s; value: %s", row.getDate("ts"), row.getDouble("value")));
            returnData.add(returnRow);
            i++              ;
        }
        System.out.println("values:" + i);
        return returnData;

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
     * addData adds data to the cassndra columnfamily
     * @param dpName : columnfamily name
     * @param measurement: measurements to insert
     * Author : Nikunj Thakkar
     */
    @Override
    public int addData(String dpName,DpDataDTO measurement) {
        cfname=getColumnFamilyName(dpName);
        if(!checkExist(cfname))
        {
            LOG.debug("Column family does not exist for this datapoint");

            //If columnfamily doesn't exist then create new columnfamily for the datapoint
            addColumnFamily(cfname);

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
            long truncatedDate = calendar.getTimeInMillis();
            Long ts=measurement.getTimestamp().getTime();
            Double value=measurement.getValue();

            session.execute("insert into "+dpName+"(day,ts,value) values("+truncatedDate+","+ts+","+value+")");
            session.execute("DELETE FROM "+dpName+" where day="+latestvalue);
            session.execute("insert into "+dpName+"(day,ts,value) values("+latestvalue+","+ts+","+value+")");

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

}
