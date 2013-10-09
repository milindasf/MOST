package bpi.most.client.rmi;

import bpi.most.dto.DpDTO;
import bpi.most.dto.DpDataDTO;
import bpi.most.dto.DpDatasetDTO;
import bpi.most.dto.UserDTO;
import bpi.most.service.api.DatapointService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;

import javax.annotation.Resource;
import javax.inject.Inject;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * communicates with the DatapontService at the RMI Server.
 *
 * for virtual datapoints:
 * <ol>
 *    <li>The DpDTO.providerAddress contains the address where to find a service, providing an implementation of the virtual datapoint type.</li>
 *    <li>Hence, the DatapointService of the providerAddress is used over RMI to read the actual data.</li>
 * </ol>
 *
 * User: harald
 */
public class DatapointServiceRmiClient implements DatapointService{

    private static final Logger LOG = LoggerFactory.getLogger(DatapointServiceRmiClient.class);

    /**
     * connects to rmi server to delegate calls to him.
     *
     * virtual datapoints are treated special: if data for a virtual datapoint should be read,
     * the rmi server at the datapoints providerAddress-property is used.
     *
     */
    @Resource(name = "datapointServiceRmiClient")
    DatapointService rmiClient;


    /*
     * TODO use some cache for providerAddress per datapoint cause client may only use
     * the datapoint-name in its DpDTO to query its value. therefore we have to cache the
     * provider address for the client
     *
     * which expiration time? should we use any at all?
     *
     * which cache impl?
     * https://code.google.com/p/guava-libraries/wiki/CachesExplained
     * http://ehcache.org/
     * VS just using a hashmap (does not have to support concurrency, cause very client uses its own one)
     *
     */

    /**
     * quick and dirty solution for testing
     */
    Map<String, DpDTO> dpCache = new HashMap<String, DpDTO>();

    /**
     * caches the given dp object
     * @param dp
     */
    private void cacheDp(DpDTO dp){
        if (dp != null){
            LOG.debug("added dp to cache: " + dp.getName() + "; vdp: " + dp.getProviderAddress());
            dpCache.put(dp.getName(), dp);
        }
    }

    private void cacheDpList(List<DpDTO> dps){
        if (dps != null){
            for(DpDTO dp: dps){
                cacheDp(dp);
            }
        }
    }

    private DpDTO getDp(String name){
        DpDTO dp = dpCache.get(name);
        if (dp == null){
            //build a simple dpDTO with only name set; we do not have more informatin here
            dp = new DpDTO(name);
            LOG.debug("using simple dpDTO with only dp name set: " + name);
        }else{
            LOG.debug("using server dpDto for dp " + dp.getName() + "with provider address: " + dp.getProviderAddress());
        }
        return dp;
    }

    @Override
    public DpDataDTO getData(UserDTO userDTO, DpDTO clientDpDTO) {
        DatapointService dpService = null;
        DpDTO dp = getDp(clientDpDTO.getName());

        //TODO: apply this to all getData methods!!

        if (dp.isVirtual()){
            //use special server for virtual datapoints
            if (dp.getProviderAddress() == null){
                LOG.error("no virtual datapoint providers registered for the requested type");
                //TODO throw exception that not suitable provider was found
                return null;
            }else{
                LOG.debug("getData for virtual datapoint, using provider at " + dp.getProviderAddress());
                dpService = getDpService(dp.getProviderAddress());
            }
        }else{
            //use default server
            dpService = rmiClient;
        }

        return dpService.getData(userDTO, dp);
    }

    @Override
    public DpDatasetDTO getData(UserDTO userDTO, DpDTO clientDpDTO, Date starttime, Date endtime) {
        DatapointService dpService = null;
        DpDTO dp = getDp(clientDpDTO.getName());

        if (dp.isVirtual()){
            //use special server for virtual datapoints
            if (dp.getProviderAddress() == null){
                LOG.error("no virtual datapoint providers registered for the requested type");
                //TODO throw exception that not suitable provider was found
                return null;
            }else{
                LOG.debug("getData for virtual datapoint, using provider at " + dp.getProviderAddress());
                dpService = getDpService(dp.getProviderAddress());
            }
        }else{
            //use default server
            dpService = rmiClient;
        }

        return dpService.getData(userDTO, dp, starttime, endtime);
    }

    @Override
    public DpDatasetDTO getDataPeriodic(UserDTO userDTO, DpDTO clientDpDTO, Date starttime, Date endtime, Float period) {
        DatapointService dpService = null;
        DpDTO dp = getDp(clientDpDTO.getName());

        if (dp.isVirtual()){
            //use special server for virtual datapoints
            if (dp.getProviderAddress() == null){
                LOG.error("no virtual datapoint providers registered for the requested type");
                //TODO throw exception that not suitable provider was found
                return null;
            }else{
                LOG.debug("getData for virtual datapoint, using provider at " + dp.getProviderAddress());
                dpService = getDpService(dp.getProviderAddress());
            }
        }else{
            //use default server
            dpService = rmiClient;
        }

        return dpService.getDataPeriodic(userDTO, dp, starttime, endtime, period);
    }

    @Override
    public DpDatasetDTO getDataPeriodic(UserDTO user, DpDTO clientDpDTO, Date starttime, Date endtime, Float period, int mode) {
        DatapointService dpService = null;
        DpDTO dp = getDp(clientDpDTO.getName());

        if (dp.isVirtual()){
            //use special server for virtual datapoints
            if (dp.getProviderAddress() == null){
                LOG.error("no virtual datapoint providers registered for the requested type");
                //TODO throw exception that not suitable provider was found
                return null;
            }else{
                LOG.debug("getData for virtual datapoint, using provider at " + dp.getProviderAddress());
                dpService = getDpService(dp.getProviderAddress());
            }
        }else{
            //use default server
            dpService = rmiClient;
        }

        return dpService.getDataPeriodic(user, dp, starttime, endtime, period, mode);
    }

    @Override
    public int getNumberOfValues(UserDTO userDTO, DpDTO clientDpDTO, Date starttime, Date endtime) {
        DatapointService dpService = null;
        DpDTO dp = getDp(clientDpDTO.getName());

        if (dp.isVirtual()){
            //use special server for virtual datapoints
            if (dp.getProviderAddress() == null){
                LOG.error("no virtual datapoint providers registered for the requested type");
                //TODO throw exception that not suitable provider was found
                return -1;
            }else{
                LOG.debug("getData for virtual datapoint, using provider at " + dp.getProviderAddress());
                dpService = getDpService(dp.getProviderAddress());
            }
        }else{
            //use default server
            dpService = rmiClient;
        }

        return dpService.getNumberOfValues(userDTO, dp, starttime, endtime);
    }

    /**
     * returns a DatapointService implementation which is a RMI client bound to the
     * given serverAddress
     * @param serverAddress
     * @return
     */
    private DatapointService getDpService(String serverAddress){
        DatapointService dpService = null;

        RmiProxyFactoryBean proxy = new RmiProxyFactoryBean();
        proxy.setCacheStub(true);
        proxy.setRefreshStubOnConnectFailure(true);
        proxy.setServiceInterface(DatapointService.class);
        proxy.setServiceUrl(serverAddress);
        proxy.afterPropertiesSet();
        dpService = (DatapointService)proxy.getObject();

        return dpService;
    }

    /**
     * closes the rmi proxy
     * @param dpService
     */
    private void close(DatapointService dpService){
        //is this even possible??
    }


    @Override
    public boolean isValidDp(String dpName) {
        return rmiClient.isValidDp(dpName);
    }

    @Override
    public List<DpDTO> getDatapoints() {
        List<DpDTO> dpList = rmiClient.getDatapoints();
        cacheDpList(dpList);
        return dpList;
    }

    @Override
    public List<DpDTO> getDatapoints(String searchstring) {
        List<DpDTO> dpList = rmiClient.getDatapoints(searchstring);
        cacheDpList(dpList);
        return dpList;
    }

    @Override
    public List<DpDTO> getDatapoints(String searchstring, String zone) {
        List<DpDTO> dpList = rmiClient.getDatapoints(searchstring, zone);
        cacheDpList(dpList);
        return dpList;
    }

    @Override
    public DpDTO getDatapoint(UserDTO user, DpDTO dpDto) {
        DpDTO dp = rmiClient.getDatapoint(user, dpDto);
        cacheDp(dp);
        return dp;
    }

    @Override
    public int addData(UserDTO userDTO, DpDTO dpDTO, DpDataDTO measurement) {
        return rmiClient.addData(userDTO, dpDTO, measurement);
    }

    @Override
    public int delData(UserDTO userDTO, DpDTO dpDTO) {
        return rmiClient.delData(userDTO, dpDTO);
    }

    @Override
    public int delData(UserDTO userDTO, DpDTO dpDTO, Date starttime, Date endtime) {
        return rmiClient.delData(userDTO, dpDTO, starttime, endtime);
    }

    @Override
    public void addObserver(String dpName, Object connector) {
        //nothing to do here
    }
}
