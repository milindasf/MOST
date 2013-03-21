package bpi.most.calibration;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Implementation of Simulation-Model Calibration Service
 * @author christiantauber@gmx.at
 */

public class CalibrationStartup {

    private final static Logger log = Logger.getLogger( CalibrationStartup.class );


    public static void main(String[] args) throws Exception {
        ApplicationContext context = new ClassPathXmlApplicationContext("META-INF/calibration.spring.xml");
        CalibrationBooter calibrationBooter = context.getAutowireCapableBeanFactory().createBean(CalibrationBooter.class);
        calibrationBooter.boot();
    }
}
