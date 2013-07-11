package bpi.most.client.rmi;

import bpi.most.dto.DpDTO;
import bpi.most.dto.DpDataDTO;
import bpi.most.dto.UserDTO;
import bpi.most.service.api.DatapointService;
import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
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
 * this tests are actually integration tests (most-rmi-server has to run as well as a virtual datapoint provider for dpRandom1.
 * this may not be the case when doing a maven build. therefore this testcases are ignored (notice the @Ignore annotation)
 * to get only invoked by hand if the environment is set up correctly --> may use puppet and vagrant for automatic test calls.
 *
 *
 *
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:META-INF/most-rmi-service.spring.xml"})
@Ignore
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
        //fetch datapoint and last value
        DpDTO dpReq = new DpDTO("con1");
        DpDTO dpResp = dpService.getDatapoint(user, dpReq);

        DpDataDTO data = dpService.getData(user, dpResp);
        Assert.assertNotNull(data);
        Assert.assertNotNull(data.getValue());
        System.out.println(data.getValue());
    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void testVirtualDatapointRandom1() throws Exception{
        //fetch datapoint to get correct virtual datapoint provider URL
        DpDTO dpReq = new DpDTO("dpRandom1");
        DpDTO dpResp = dpService.getDatapoint(user, dpReq);

        DpDataDTO data = dpService.getData(user, dpResp);
        Assert.assertNotNull(data);
        Assert.assertNotNull(data.getValue());
        System.out.println(data.getValue());
    }
}
