package bpi.most.domain.datapoint;

import bpi.most.dto.DpDataDTO;

import java.util.Date;

/**
 * Interface definition to access stored data values in an storage independent way. It is used since we
 * are storing data not only in an relational database but also in some NoSql solution (Cassandra / Neo4j)
 *
 * Implementations for this Interface for example are
 * DpDataFinderHibernate
 * DpDataFinderCassandra
 * ...
 *
 */
public interface IDatapointDataFinder {
    DatapointDataVO getData(String dpName);

    DatapointDatasetVO getData(String dpName, Date starttime, Date endtime);

    DatapointDatasetVO getDataPeriodic(String dpName, Date starttime, Date endtime, Float period, int mode);

    Integer getNumberOfValues(String dpName, Date starttime, Date endtime);

    int addData(String dpName, DpDataDTO measurement);

    int delData(String dpName);

    int delData(String dpName, Date starttime, Date endtime);
}
