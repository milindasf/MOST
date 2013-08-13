package bpi.most.domain.neo4j.migration;

import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;

/**
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:META-INF/most-neo4j.spring.xml"})
public class Neo4jMigrationTest {

    @Inject
    Neo4jMigration migrator;

    @Test
    public void testInit(){
        Assert.assertTrue(migrator.initSuccessful());
    }

}