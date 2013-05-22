package bpi.most.vdp;

import bpi.most.service.api.RegistrationService;
import org.springframework.remoting.rmi.RmiServiceExporter;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

/**
 * Created with IntelliJ IDEA.
 * User: harald
 * Date: 22.05.13
 * Time: 15:51
 * To change this template use File | Settings | File Templates.
 */

public class VdpProviderRegistration {

    @Inject
    RegistrationService registrationService;

    @Inject
    RmiServiceExporter dpService;

    private String myEndpoint;

    @PostConstruct
    public void register(){
        System.out.println("registering services: " + myEndpoint);

       /*
       1. find out rmi url of our datapoint service
       2. get all implementations via the service loader
       3. register them and their type
        */
    }

    @PreDestroy
    public void unregister(){
        System.out.println("unregistering services");
    }

    public String getMyEndpoint() {
        return myEndpoint;
    }

    public void setMyEndpoint(String myEndpoint) {
        this.myEndpoint = myEndpoint;
    }
}
