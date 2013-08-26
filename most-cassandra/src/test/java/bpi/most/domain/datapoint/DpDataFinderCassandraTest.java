package bpi.most.domain.datapoint;

import bpi.most.migration.DataToCassandraMigrator;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.Date;



/**
 * Created with IntelliJ IDEA.
 * User: harald
 * Date: 31.07.13
 * Time: 16:46
 * To change this template use File | Settings | File Templates.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:META-INF/most-cassandra.spring.xml"})
public class DpDataFinderCassandraTest {

    @Inject
    DpDataFinderCassandra dpFinder;
    @Inject
    DataToCassandraMigrator migrator;
    /*@Test
    public void testExistence(){

        Assert.assertNotNull(dpFinder);
        Assert.assertTrue(migrator.initSuccessful());
    }  */
    @Test
    public void testAddColumnfamily()
    {
        dpFinder.addColumnFamily("testcf2");
    }
    @Test
    @Transactional
    public void testgetData() throws Exception {

       Double dc=dpFinder.getData("cdi3").getValue();
       Double dm=migrator.getDpDfHibernate().getData("cdi3").getValue();
       Date tc=dpFinder.getData("tem1").getTimestamp();
       Date tm=migrator.getDpDfHibernate().getData("tem1").getTimestamp();
       Assert.assertEquals(dc,dm);
       Assert.assertEquals(tc,tm);

    }
    @Test
    public void testdelData()
    {
        Assert.assertEquals(1,dpFinder.delData("con9"));
    }
    @Test
    public void testgetDataRange()
    {
        Assert.assertEquals(dpFinder.getData("elepow6"),migrator.getDpDfHibernate().getData("elepow6"));
    }
    /*@Test
    public void testdelDataRange()
    {
        Date st=new Date(111,5,29);
        Date et=new Date(111,5,31);
        Assert.assertNotSame(0,dpFinder.delData("elepow7",st,et));

    } */


    //TODO test calls to IDatapointDataFinder (DpDataFinderCassandra gets injected here)
}
