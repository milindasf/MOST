package bpi.most.domain.datapoint;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

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
    private DpDataFinderCassandra dpFinder;
   /*@Test
    public void testExistence(){
        Assert.assertNotNull(dpFinder);

    }   */
   @Test
    public void testgetData() throws Exception {
      DatapointDataVO ds=dpFinder.getData("elepow7");
      Assert.assertEquals("It is working fine",ds.getTimestamp(),dpFinder.getData("elepow7").getTimestamp());

    }     /*
    @Test
    public void testdelData()
    {
        Assert.assertEquals(0,dpFinder.delData("con9"));
    }*/
    @Test
    public void testgetDatainRange()
    {
        Date st=new Date(111,5,29);
        Date et=new Date(111,5,31);
        dpFinder.getData("elepow7",st,et);
    }

    //TODO test calls to IDatapointDataFinder (DpDataFinderCassandra gets injected here)
}
