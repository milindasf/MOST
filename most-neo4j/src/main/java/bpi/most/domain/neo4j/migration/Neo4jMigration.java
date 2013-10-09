package bpi.most.domain.neo4j.migration;

import bpi.most.domain.datapoint.DatapointFinder;
import bpi.most.domain.datapoint.DpDataFinderHibernate;
import bpi.most.domain.neo4j.datapoint.DpDataFinderNeo4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * Migrates data values from mysql table DATA to Neo4j cluster. Either all data is migrated or only for a given datapoint
 *
 */
@Service
public class Neo4jMigration {

    /**
     * access to neo4j
     */
	private static final Logger LOG = LoggerFactory.getLogger(Neo4jMigration.class);

    @Inject
    private DpDataFinderNeo4j dpDfNeo;

    /**
     * access to mysql database
     */
    @PersistenceContext(unitName = "most")
    private EntityManager em;
    private DpDataFinderHibernate dpDfHibernate;
    private DatapointFinder dpFinderHibernate;


    @PostConstruct
    private void init(){
        dpFinderHibernate = new DatapointFinder(em);
        dpDfHibernate = new DpDataFinderHibernate(em);
    }

    public boolean initSuccessful(){
        return dpDfNeo != null && dpDfHibernate != null && dpFinderHibernate != null;
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
