package bpi.most.domain.neo4j.datapoint;

import bpi.most.domain.datapoint.DatapointDataVO;
import bpi.most.domain.datapoint.DatapointDatasetVO;
import bpi.most.domain.datapoint.IDatapointDataFinder;
import bpi.most.dto.DpDataDTO;

import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.cypher.javacompat.ExecutionResult;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.kernel.EmbeddedGraphDatabase;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import java.io.File;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created with IntelliJ IDEA. User: harald Date: 13.08.13 Time: 13:16 To change
 * this template use File | Settings | File Templates.
 */
@Service
public class DpDataFinderNeo4j implements IDatapointDataFinder {

	private Neo4jDatabaseProcedure procedure;
	
	/**
	 * connects to neo4j server
	 * 
	 * @throws Exception
	 */
	@PostConstruct
	public void initIt() throws Exception {
		/**
		 * initialize local neo4j VS connecting to neo4j standalone server.
		 */
		procedure=new Neo4jDatabaseProcedure();
		procedure.SetDatabasePath("/home/milinda/neo4j-community-1.8.2","MOST.db");
		procedure.CreateDatabase();
		
	}

	/**
	 * gracefully releases connections to Neo4j
	 * 
	 * @throws Exception
	 */
	@PreDestroy
	public void cleanUp() throws Exception {

		procedure.ShutDownDatabase();
		
	}

	@Override
	public DatapointDataVO getData(String dpName) {
		

		Node [] data=procedure.getValues(dpName, null, null);
		DatapointDataVO output=null;
		
		if(data[0]!=null){
		
			Date timestamp=(Date)Timestamp.valueOf(data[0].getProperty("timestamp").toString());
			Double value=Double.valueOf(data[0].getProperty("value").toString());
			Float quality=Float.valueOf(data[0].getProperty("quality").toString());
			
			output=new DatapointDataVO(timestamp, value,quality);
			
		}
		
		
		 
	
		return output;
	}

	@Override
	public DatapointDatasetVO getData(String dpName, Date starttime,
			Date endtime) {
		Timestamp ts_starttime=(Timestamp)starttime;
		Timestamp ts_endtime=(Timestamp)endtime;
		Node [] data=procedure.getValues(dpName,ts_starttime.toString(),ts_endtime.toString());
		
		DatapointDatasetVO output=new DatapointDatasetVO(dpName);
		DatapointDataVO temp;
		for(int i=0;i<data.length;i++){
			
			if(data[i]!=null){
				Date timestamp=(Date)Timestamp.valueOf(data[0].getProperty("timestamp").toString());
				Double value=Double.valueOf(data[0].getProperty("value").toString());
				Float quality=Float.valueOf(data[0].getProperty("quality").toString());
				temp=new DatapointDataVO(timestamp, value,quality);
				output.add(temp);
			}
			
		}
		
		
		
		return output; 
	}

	@Override
	public DatapointDatasetVO getDataPeriodic(String dpName, Date starttime,
			Date endtime, Float period, int mode) {
		return null; // To change body of implemented methods use File |
						// Settings | File Templates.
	}

	@Override
	public Integer getNumberOfValues(String dpName, Date starttime, Date endtime) {
		
		Timestamp ts_starttime=(Timestamp)starttime;
		Timestamp ts_endtime=(Timestamp)endtime;
		int temp=procedure.getNumberofValues(dpName, ts_starttime.toString(), ts_endtime.toString());
		Integer output=new Integer(temp);
       	return output; 
	}

	@Override
	public int addData(String dpName, DpDataDTO measurement) {
		
		String timestamp=((Timestamp)(measurement.getTimestamp())).toString();
		Double value=measurement.getValue();
		procedure.addData(dpName, timestamp, value);
		return 0; 
	}

	@Override
	public int delData(String dpName) {

		procedure.emptyDatapoint(dpName);
		return 0; 
	}

	@Override
	public int delData(String dpName, Date starttime, Date endtime) {
		
		Timestamp start=(Timestamp) starttime;
		Timestamp end=(Timestamp)endtime;
		procedure.emptyDatapoint(dpName, start.toString(), end.toString());
		
		return 0; 
	}

	
	
	
}
