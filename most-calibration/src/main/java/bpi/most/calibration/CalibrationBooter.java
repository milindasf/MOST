package bpi.most.calibration;


import org.apache.log4j.Logger;
import genopt.GenOpt;

import java.net.URL;

/**
 * Implementation of Simulation-Model Calibration Service
 * @author christiantauber@gmx.at
 */

public class CalibrationBooter {

    private final static Logger log = Logger.getLogger( CalibrationBooter.class );

    public void boot() throws Exception {

        GenOpt.main(new String[]{".\\most-calibration\\calibration models\\EnergyPlus\\opt.ini"});
    }

}
