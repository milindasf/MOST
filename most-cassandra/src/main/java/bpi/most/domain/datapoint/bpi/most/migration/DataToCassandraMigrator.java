package bpi.most.domain.datapoint.bpi.most.migration;

import bpi.most.domain.datapoint.DatapointFinder;
import bpi.most.domain.datapoint.DpDataFinderCassandra;
import bpi.most.domain.datapoint.DpDataFinderHibernate;
import bpi.most.domain.datapoint.IDatapointDataFinder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * Migrates data values of mysql table DATA to Cassandra cluster. Either all data is migrated or only for a given datapoint
 *
 */
@Service
public class DataToCassandraMigrator {

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
    private void init(){
        dpFinder = new DatapointFinder(em);
        dpDfHibernate = new DpDataFinderHibernate(em);
    }

    public boolean initSuccessful(){
        return dpDfCass != null && dpDfHibernate != null && dpFinder != null;
    }

    /**
     * migrates all data of all datapoints
     */
    public void migrateData(){
        //TODO implement
    }

    /**
     * migrates data of given datapoint
     * @param dpName
     */
    public void migrateData(String dpName){
        //TODO implement
    }
}
