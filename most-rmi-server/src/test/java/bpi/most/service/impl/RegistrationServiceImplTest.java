package bpi.most.service.impl;

import bpi.most.service.api.ConnectorService;
import bpi.most.service.api.RegistrationService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;

/**
 *
 * User: harald
 *
 *
 */

@ContextConfiguration(locations = {"file:most-rmi-server/src/main/webapp/WEB-INF/application-server.spring.xml"})
public class RegistrationServiceImplTest extends AbstractTransactionalJUnit4SpringContextTests {

    //TODO write test methods

    @Inject
    private RegistrationService regService;

    @Test
    public void testExistence(){
        Assert.assertNotNull(regService);
    }

}
