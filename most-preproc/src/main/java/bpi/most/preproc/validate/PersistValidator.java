package bpi.most.preproc.validate;

import bpi.most.domain.datapoint.DatapointDataVO;
import bpi.most.domain.datapoint.DatapointVO;
import bpi.most.dto.DpDTO;
import bpi.most.dto.DpDataDTO;

/**
 *
 * validates if a new data measurement from a datapoint should be persisted.
 *
 */
public class PersistValidator {


    public boolean validate(DatapointVO dp, DatapointDataVO lastValue, DatapointDataVO newValue){
        return false;
    }

    /**
     * time difference to last value has to be bigger than the sample interval
     * @return
     */
    public boolean validateSampleInterval(){
        return false;
    }

    /**
     * TODO check what sampleIntervallMin is for
     * @return
     */
    public boolean validateSampleIntervalMin(){
        return false;
    }

    /**
     * if there is a deadband configured; the new value has to be outside the deadband: newValue < (lastvalue - deadband/2) OR (lastvalue + deadband/2) > newValue
     * @return
     */
    public boolean validateDeadband(){
        return false;
    }

    /**
     * value has to be between datapoints min and max value
     * @return
     */
    public boolean validateRange(){
        return false;
    }
}
