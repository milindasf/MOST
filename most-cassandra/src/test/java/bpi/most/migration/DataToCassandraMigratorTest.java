package bpi.most.migration;

import bpi.most.domain.datapoint.bpi.most.migration.DataToCassandraMigrator;
import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

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

}
