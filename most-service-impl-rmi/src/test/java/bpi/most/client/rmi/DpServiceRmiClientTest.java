package bpi.most.client.rmi;

import bpi.most.dto.DpDTO;
import bpi.most.dto.DpDataDTO;
import bpi.most.dto.UserDTO;
import bpi.most.service.api.DatapointService;
import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 *
 * User: harald
 *
 * test classes only work if RMI server (most-rmi-server) is running.
 *
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:META-INF/most-rmi-service.spring.xml"})
public class DpServiceRmiClientTest{

    @Resource(name = "datapointServiceImpl")
    DatapointService dpService;

    private UserDTO user;

    @Before
    public void setUp() throws Exception {
        user = new UserDTO("mostsoc");
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testServiceExists() throws Exception {
        Assert.assertNotNull(dpService);
    }


    @Test
    public void testGetDpData() throws Exception {
        DpDataDTO data = dpService.getData(user, new DpDTO("exampleVdp"));
        Assert.assertNotNull(data);
        Assert.assertNotNull(data.getValue());
        System.out.println(data.getValue());
    }
}
