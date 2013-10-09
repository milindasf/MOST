package bpi.most.vdp;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

/**
 * User: harald
 *
 * Starts the virtual datapoint provider standalone, without a webserver.
 * keypress stopps the provider.
 */
public class VdpStandalone {

    public static void main(String[] args) {

        ConfigurableApplicationContext context =
                new ClassPathXmlApplicationContext("META-INF/most-vdp.spring.xml");

        try {
            System.out.println("Enter any key to stop virtual datapoint providers.");
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        context.close();

    }

}
