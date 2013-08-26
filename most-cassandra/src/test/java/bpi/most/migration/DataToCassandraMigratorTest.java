package bpi.most.migration;

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
        Assert.assertEquals(migrator.getDpDfHibernate().getData("con1"),migrator.getDpDfCass().getData("con1"));

        //TODO: do some assertions if data was migrated.
    }
    @Test
    public void testMigration(){
        migrator.migrateData();
    }
}
