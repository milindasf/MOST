package bpi.most.migration;

import bpi.most.domain.datapoint.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.util.LocaleServiceProviderPool;

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
    @Transactional
    public void migrateData(){
        //TODO implement
        DatapointDataVO data = dpDfHibernate.getData("tem1");
        LOG.debug("last value from tem1:" + data.getValue());
    }

    /**
     * migrates data of given datapoint
     * @param dpName
     */
    public void migrateData(String dpName){
        //TODO implement
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
