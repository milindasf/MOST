package bpi.most.vdp;

import bpi.most.service.api.RegistrationService;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

/**
 * User: harald
 *
 * Starts the virtual datapoint provider standalone, without a webserver.
 */
public class VdpStandalone {

    public static void main(String[] args) {
        ConfigurableApplicationContext context =
                new ClassPathXmlApplicationContext("META-INF/most-vdp.spring.xml");

        RegistrationService registrationService = (RegistrationService) context.getBean("registrationService");
        System.out.println("got regservice: " + (registrationService != null));

        try {
            System.out.println("Enter any key to stop virtual datapoint providers.");
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        context.close();
    }

}
