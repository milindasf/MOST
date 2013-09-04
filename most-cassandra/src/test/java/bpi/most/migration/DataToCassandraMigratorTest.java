package bpi.most.migration;

import bpi.most.domain.datapoint.DatapointDataVO;
import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

/**
 * Created with IntelliJ IDEA.
 * User: harald
 * Date: 06.08.13
 * Time: 18:14
 * To change this template use File | Settings | File Templates.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:META-INF/most-cassandra.spring.xml"})
public class DataToCassandraMigratorTest {

    @Inject
    DataToCassandraMigrator migrator;

    @Test
    public void testInit(){
        Assert.assertTrue(migrator.initSuccessful());
    }
    @Test
    @Transactional
    public void testGetDataFromHibernate(){
         Assert.assertNotNull(migrator.getDpDfHibernate().getData("tem1"));
    }
    @Test
    @Transactional
    public void testMigrationSimpleDatapoint(){

        migrator.migrateData("con1");
        migrator.migrateData("con2");
        migrator.migrateData("con3");
        migrator.migrateData("con4");
        migrator.migrateData("con5");

        //If all the data from con5 datapoint is migrated it will return same latest values
        DatapointDataVO dataHibernate = migrator.getDpDfHibernate().getData("con5");
        DatapointDataVO dataCassandra =migrator.getDpDfCass().getData("con5");
        Assert.assertEquals(dataHibernate.getTimestamp().getTime(),dataCassandra.getTimestamp().getTime());
        Assert.assertEquals(dataHibernate.getValue(),dataCassandra.getValue());


    }/*
    @Test
    public void testMigration(){
        migrator.migrateData();
    }  */
}
