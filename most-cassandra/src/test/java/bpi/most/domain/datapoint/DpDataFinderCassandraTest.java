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
    @Test
    public void testExistence(){

        Assert.assertNotNull(dpFinder);
        Assert.assertTrue(migrator.initSuccessful());
    }

    @Test
    @Transactional
    public void testgetData(){
      DatapointDataVO dataHibernate = migrator.getDpDfHibernate().getData("con1");
      DatapointDataVO dataCassandra = dpFinder.getData("con1");
      Assert.assertEquals(dataHibernate.getTimestamp().getTime(), dataCassandra.getTimestamp().getTime());
      Assert.assertEquals(dataHibernate.getValue(), dataCassandra.getValue());
    }


    @Test
    @Transactional
    public void testgetDataRange1()
    {

        Calendar cal = Calendar.getInstance();
        cal.set(2011, 04, 19, 12, 00, 00);
        Date start = cal.getTime();
        cal.set(2011, 05, 19, 11, 22, 00);
        Date end = cal.getTime();
        DatapointDatasetVO dataset =dpFinder.getData("con1",start,end);
        System.out.println("data from " + start + " to " + end);
        for (DatapointDataVO data: dataset){
            System.out.println(data.getTimestamp() + ": " + data.getValue());
        }
    }



    /*
    @Test
    public void testdelData()
    {
        Assert.assertEquals(1,dpFinder.delData("con9"));
    }
    @Test
    @Transactional
    public void testgetDataRange()
    {
        Calendar cal = Calendar.getInstance();
        cal.set(2011, 04, 19, 12, 00, 00);
        Date start = cal.getTime();
        cal.set(2011, 05, 19, 11, 22, 00);
        Date end = cal.getTime();
        Assert.assertEquals(dpFinder.getData("con1",start,end),migrator.getDpDfHibernate().getData("con1",start,end));
    }

    /**
     * this is no real testcase. I (hare) used it to call getData and look at the returned values.
     */ /*
    @Test
    @Transactional
    public void testgetDataRange1()
    {

        Calendar cal = Calendar.getInstance();
        cal.set(2011, 04, 19, 12, 00, 00);
        Date start = cal.getTime();
        cal.set(2011, 05, 19, 11, 22, 00);
        Date end = cal.getTime();
        DatapointDatasetVO dataset =dpFinder.getData("con1",start,end);
        System.out.println("data from " + start + " to " + end);
        for (DatapointDataVO data: dataset){
            System.out.println(data.getTimestamp() + ": " + data.getValue());
        }
    }
    @Test
    public void testdelDataRange()
    {
        Calendar cal = Calendar.getInstance();
        cal.set(2011, 04, 19, 12, 00, 00);
        Date start = cal.getTime();
        cal.set(2011, 05, 19, 11, 22, 00);
        Date end = cal.getTime();
        Assert.assertNotSame(0,dpFinder.delData("con1",start,end));

    } */



    //TODO test calls to IDatapointDataFinder (DpDataFinderCassandra gets injected here)
}
