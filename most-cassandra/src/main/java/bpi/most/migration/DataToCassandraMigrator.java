package bpi.most.migration;

import bpi.most.domain.datapoint.*;
import bpi.most.dto.DpDataDTO;
import org.apache.cassandra.thrift.InvalidRequestException;
import org.apache.cassandra.thrift.SchemaDisagreementException;
import org.apache.cassandra.thrift.TimedOutException;
import org.apache.cassandra.thrift.UnavailableException;
import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.util.LocaleServiceProviderPool;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;

/**
 *
 * Migrates data values of mysql table DATA to Cassandra cluster. Either all data is migrated or only for a given datapoint
 *
 */
@Service
public class DataToCassandraMigrator  {

    private static final Logger LOG = LoggerFactory.getLogger(DataToCassandraMigrator.class);

    /**
     * access to Cassandra cluster
     */
    @Inject
    private DpDataFinderCassandra dpDfCass;

    /**
     * access to mysql database
     */
    @PersistenceContext(unitName = "most")
    private EntityManager em;
    private DpDataFinderHibernate dpDfHibernate;
    private DatapointFinder dpFinder;


    @PostConstruct
    private void init() throws Exception {
        dpFinder = new DatapointFinder(em);
        dpDfHibernate = new DpDataFinderHibernate(em);
        dpDfCass.initIt();
    }

    public boolean initSuccessful(){
        return dpDfCass != null && dpDfHibernate != null && dpFinder != null;
    }

    /**
     * migrates all data of all datapoints
     */
    @Transactional
    public void migrateData(){
        //TODO implement
        List<DatapointVO> dps = dpFinder.getDatapoints(null);
        LOG.debug(String.format("migrating %d datapoints from Hibernate to Cassandra", dps.size()));

        for(DatapointVO dp: dps)
        //for(int j=0;j<5;j++)
        {
            //String dName=dps.get(i).getName();
            LOG.debug(String.format("migrating data of datapoint %s...",dp.getName()));
            migrateData(dp.getName());
            LOG.debug(String.format("Data migration complete for datapoint %s...", dp.getName()));

        }

    }
    /**
     * migrates data of given datapoint
     * @param dpName
     * Author : Nikunj Thakkar
     */
    public void migrateData(String dpName){


        //Add new columnfamily to keyspace to add data into it
        dpDfCass.addColumnFamily(dpName);


        //Get the data from MySql
        DatapointDatasetVO ds=dpDfHibernate.getallData(dpName);
        System.out.println("Migrating "+ds.size()+"Rows from "+dpName+" to Cassandra");

        //Adds data to the cassandra columnfamily
       for(DatapointDataVO dp:ds)
       {
           DpDataDTO insertData =dp.getDTO();
           dpDfCass.addData(dpName,insertData);

       }

    }
    public DatapointFinder getDpFinder() {
        return dpFinder;
    }

    public DpDataFinderHibernate getDpDfHibernate() {
        return dpDfHibernate;
    }

    public DpDataFinderCassandra getDpDfCass() {
        return dpDfCass;
    }
}
