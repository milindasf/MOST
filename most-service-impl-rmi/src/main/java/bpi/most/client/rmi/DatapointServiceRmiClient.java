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
import java.util.List;

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


    @Override
    public DpDataDTO getData(UserDTO userDTO, DpDTO dpDTO) {
        DatapointService dpService = null;

        if (dpDTO.isVirtual()){
            //use special server for virtual datapoints
            if (dpDTO.getProviderAddress() == null){
                LOG.error("no virtual datapoint providers registered for the requested type");
                //TODO throw exception that not suitable provider was found
                return null;
            }else{
                LOG.debug("getData for virtual datapoint, using provider at " + dpDTO.getProviderAddress());
                dpService = getDpService(dpDTO.getProviderAddress());
            }
        }else{
            //use default server
            dpService = rmiClient;
        }

        return dpService.getData(userDTO, dpDTO);
    }

    @Override
    public DpDatasetDTO getData(UserDTO userDTO, DpDTO dpDTO, Date starttime, Date endtime) {
        DatapointService dpService = null;

        if (dpDTO.isVirtual()){
            //use special server for virtual datapoints
            if (dpDTO.getProviderAddress() == null){
                LOG.error("no virtual datapoint providers registered for the requested type");
                //TODO throw exception that not suitable provider was found
                return null;
            }else{
                LOG.debug("getData for virtual datapoint, using provider at " + dpDTO.getProviderAddress());
                dpService = getDpService(dpDTO.getProviderAddress());
            }
        }else{
            //use default server
            dpService = rmiClient;
        }

        return dpService.getData(userDTO, dpDTO, starttime, endtime);
    }

    @Override
    public DpDatasetDTO getDataPeriodic(UserDTO userDTO, DpDTO dpDTO, Date starttime, Date endtime, Float period) {
        DatapointService dpService = null;

        if (dpDTO.isVirtual()){
            //use special server for virtual datapoints
            if (dpDTO.getProviderAddress() == null){
                LOG.error("no virtual datapoint providers registered for the requested type");
                //TODO throw exception that not suitable provider was found
                return null;
            }else{
                LOG.debug("getData for virtual datapoint, using provider at " + dpDTO.getProviderAddress());
                dpService = getDpService(dpDTO.getProviderAddress());
            }
        }else{
            //use default server
            dpService = rmiClient;
        }

        return dpService.getDataPeriodic(userDTO, dpDTO, starttime, endtime, period);
    }

    @Override
    public DpDatasetDTO getDataPeriodic(UserDTO user, DpDTO dpDTO, Date starttime, Date endtime, Float period, int mode) {
        DatapointService dpService = null;

        if (dpDTO.isVirtual()){
            //use special server for virtual datapoints
            if (dpDTO.getProviderAddress() == null){
                LOG.error("no virtual datapoint providers registered for the requested type");
                //TODO throw exception that not suitable provider was found
                return null;
            }else{
                LOG.debug("getData for virtual datapoint, using provider at " + dpDTO.getProviderAddress());
                dpService = getDpService(dpDTO.getProviderAddress());
            }
        }else{
            //use default server
            dpService = rmiClient;
        }

        return dpService.getDataPeriodic(user, dpDTO, starttime, endtime, period, mode);
    }

    @Override
    public int getNumberOfValues(UserDTO userDTO, DpDTO dpDTO, Date starttime, Date endtime) {
        DatapointService dpService = null;

        if (dpDTO.isVirtual()){
            //use special server for virtual datapoints
            if (dpDTO.getProviderAddress() == null){
                LOG.error("no virtual datapoint providers registered for the requested type");
                //TODO throw exception that not suitable provider was found
                return -1;
            }else{
                LOG.debug("getData for virtual datapoint, using provider at " + dpDTO.getProviderAddress());
                dpService = getDpService(dpDTO.getProviderAddress());
            }
        }else{
            //use default server
            dpService = rmiClient;
        }

        return dpService.getNumberOfValues(userDTO, dpDTO, starttime, endtime);
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
        return rmiClient.getDatapoints();
    }

    @Override
    public List<DpDTO> getDatapoints(String searchstring) {
        return rmiClient.getDatapoints();
    }

    @Override
    public List<DpDTO> getDatapoints(String searchstring, String zone) {
        return rmiClient.getDatapoints(searchstring, zone);
    }

    @Override
    public DpDTO getDatapoint(UserDTO user, DpDTO dpDto) {
        return rmiClient.getDatapoint(user, dpDto);
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
