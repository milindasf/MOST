package bpi.most.domain.datapoint;

import bpi.most.migration.DataToCassandraMigrator;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import sun.util.LocaleServiceProviderPool;

import javax.inject.Inject;
import java.util.Calendar;
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
    @Transactional
    public void testgetDataRange()
    {
        Assert.assertEquals(dpFinder.getData("con1"),migrator.getDpDfHibernate().getData("con1"));
    }

    /**
     * this is no real testcase. I (hare) used it to call getData and look at the returned values.
     */
    @Test
    @Transactional
    public void testgetDataRange1()
    {
        Calendar cal = Calendar.getInstance();
        cal.set(2011, 04, 29, 12, 00, 00);
        Date start = cal.getTime();
        cal.set(2011, 05, 21, 11, 22, 00);
        Date end = cal.getTime();
        DatapointDatasetVO dataset = dpFinder.getData("con1", start, end);
        System.out.println("data from " + start + " to " + end);
        for (DatapointDataVO data: dataset){
            System.out.println(data.getTimestamp() + ": " + data.getValue());
        }
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
