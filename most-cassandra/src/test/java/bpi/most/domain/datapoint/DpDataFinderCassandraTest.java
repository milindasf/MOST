package bpi.most.domain.datapoint;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;

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
    private IDatapointDataFinder dpDataFinder;

    @Test
    public void testExistence(){
        Assert.assertNotNull(dpDataFinder);
        Assert.assertTrue(dpDataFinder instanceof DpDataFinderCassandra);
    }

    //TODO test calls to IDatapointDataFinder (DpDataFinderCassandra gets injected here)
}
