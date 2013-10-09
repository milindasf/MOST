package bpi.most.domain.neo4j.migration;

import java.util.List;

import bpi.most.domain.datapoint.DatapointDataVO;
import bpi.most.domain.datapoint.DatapointDatasetVO;
import bpi.most.domain.datapoint.DatapointFinder;
import bpi.most.domain.datapoint.DatapointVO;
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
    private void init() throws Exception {
        dpFinderHibernate = new DatapointFinder(em);
        dpDfHibernate = new DpDataFinderHibernate(em);
        dpDfNeo.initIt();
    }

    public boolean initSuccessful(){
        return dpDfNeo != null && dpDfHibernate != null && dpFinderHibernate != null;
    }

    /**
     * migrates all data of all datapoints
     * @author milinda
     */
    public void migrateData(){
       
    	List<DatapointVO> datapoint=dpFinderHibernate.getDatapoints(null);
    	DatapointVO temp;
    	for(int i=0;i<datapoint.size();i++){
    		temp=datapoint.get(i);
    		System.out.println("Starting Migration from datapoint "+temp.getName());
    		this.migrateData(temp.getName());
    		System.out.println("MIgration completed of datapoint "+temp.getName());
    	}
    	
    	System.out.println("MySQL table Data sucessfully migrated to Neo4j Graph database....");
    	
    	
    }

    /**
     * migrates data of given datapoint
     * @param dpName
     * @author milinda
     */
    public void migrateData(String dpName){
       
    	DatapointDatasetVO dataSet=dpDfHibernate.getallData(dpName);
        System.out.println("Migrating data from datapoint "+dpName+" to Neo4j graph Database..");
    	DatapointDataVO temp;
        for (int i=0;i<dataSet.size();i++){
    		temp=dataSet.get(i);
    		dpDfNeo.addData(dpName, temp.getDTO());
    	}
    	
        System.out.println("Data Migration Successfull... "+dataSet.size() +"Rows successfully migrated");
    	
    	
    }

    public DatapointVO getDatapointFromMySQL(String dpName){
    	
    	DatapointVO temp=dpFinderHibernate.getDatapoint(dpName);
    	return temp;
    }
    
    
}
