package bpi.most.vdp;

import bpi.most.domain.datapoint.Datapoint;
import bpi.most.domain.datapoint.DatapointDataVO;
import bpi.most.dto.VdpProviderDTO;
import bpi.most.service.api.RegistrationService;
import bpi.most.service.impl.datapoint.virtual.VirtualDatapoint;
import bpi.most.service.impl.datapoint.virtual.VirtualDatapointDataFinder;
import bpi.most.service.impl.datapoint.virtual.VirtualDatapointFactory;
import bpi.most.vdp.instance.RandomDataDatapoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.remoting.rmi.RmiServiceExporter;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ServiceLoader;

/**
 * Created with IntelliJ IDEA.
 * User: harald
 *
 * takes care of registering virtual datapoint implementations on startup and unregistering on stopping.
 * it uses Java Service Loader to find implementations.
 *
 */

public class VdpProviderRegistration {

    private static final Logger LOG = LoggerFactory.getLogger(VdpProviderRegistration.class);

    @Inject
    ApplicationContext ctx;

    /**
     * rmi client to vdp-registry
     */
    @Inject
    @Qualifier("registrationServiceRemote")
    RegistrationService registrationService;

    @PersistenceContext(unitName = "most")
    private EntityManager em;

    /**
     * TODO: find out rmi url of our datapoint service
     */
    private String myEndpoint;
    @Inject
    RmiServiceExporter dpService;

    /**
     * registers all found virtual datapoint implementations
     */
    @PostConstruct
    public void register(){
        System.out.println("registering virtual datapoints for endpoint: " + myEndpoint);

        try {
            URI endpoint = new URI(myEndpoint);

            ServiceLoader<VirtualDatapointFactory> virtualDpLoader = ServiceLoader
                    .load(VirtualDatapointFactory.class);
            // loop through all DpVirtualFactory implementations
            for (VirtualDatapointFactory vdpFact : virtualDpLoader) {
                LOG.debug("registering type: " + vdpFact.getVirtualType());

                registrationService.register(new VdpProviderDTO(vdpFact.getVirtualType(), endpoint));
            }

            //try to get data from virtual datapoint "randomDataVdp"
            try{

                System.out.println("got the context " + (ctx != null));

                Datapoint dp = new Datapoint();
                dp.setVirtual("randomDataVdp");
                VirtualDatapointDataFinder vdpDataFinder = new VirtualDatapointDataFinder(em, ctx);
                DatapointDataVO data = vdpDataFinder.getData(dp);
                System.out.println(data.getValue());
            }catch(Exception e){
                LOG.error(e.getMessage(), e);
            }
        } catch (URISyntaxException e) {
            LOG.error(e.getMessage(), e);
        }
    }

    /**
     * unregisters all virtual datapoints for this endpoint
     */
    @PreDestroy
    public void unregister(){
        //unregister all services for our datapoint service rmi url
        try {
            LOG.debug("unregistering all virtual datapoints");
            registrationService.unregister(new URI(myEndpoint));
        } catch (URISyntaxException e) {
            LOG.error(e.getMessage(), e);
        }
    }

    public String getMyEndpoint() {
        return myEndpoint;
    }

    public void setMyEndpoint(String myEndpoint) {
        this.myEndpoint = myEndpoint;
    }
}
