package bpi.most.domain.neo4j.datapoint;

import bpi.most.domain.datapoint.IDatapointDataFinder;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;

/**
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:META-INF/most-neo4j.spring.xml"})
public class DpDataFinderNeo4jTest {

    @Inject
    private IDatapointDataFinder dpDataFinder;

    @Test
    public void testExistence(){
        Assert.assertNotNull(dpDataFinder);
        Assert.assertTrue(dpDataFinder instanceof DpDataFinderNeo4j);
    }

    //TODO test calls to IDatapointDataFinder (DpDataFinderNeo4j gets injected here)

}
