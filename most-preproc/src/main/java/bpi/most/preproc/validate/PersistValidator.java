package bpi.most.preproc.validate;

import bpi.most.domain.datapoint.DatapointDataVO;
import bpi.most.domain.datapoint.DatapointVO;
import bpi.most.dto.DpDTO;
import bpi.most.dto.DpDataDTO;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * validates if a new data measurement from a datapoint should be persisted.
 *
 */
public class PersistValidator {
    private BigDecimal dpDeadband;
    private BigDecimal maxiValue;
    private BigDecimal minValue;
    private BigDecimal sampleInterval;
    private BigDecimal sampleIntervalMin;
    private long secondDifference;
    private Double newVal;
    private Double lastVal;
    private DatapointVO datapoint;
    public boolean validate(DatapointVO dp, DatapointDataVO lastValue, DatapointDataVO newValue){

        newVal=newValue.getValue();
        lastVal=lastValue.getValue();
        datapoint=dp;
        secondDifference=getTimeDifference(lastValue.getTimestamp(),newValue.getTimestamp());

        //If value is outside of range then it should not be added and the procedure ends here
        if(!validateRange())
        return false;

        //If it is the first value for the datapoint then it must be added without any constraint
        if(lastValue==null)
        return true;

        //If sampleInterval is not null and value is outside of sample interval then it should be added
        // In addData procedure add with select value 2;
        if(validateSampleInterval())
        return true;
        else
        {
            //If sampleInterval is null then check Constraint for SampleIntervalMin
            //if sampleIntervalMin is not null and value<sampleIntervalMin then it should not add the value return false
            if(validateSampleIntervalMin())
            return false;
            //If sample intervalMin is null then check deadband Constraint
            else if(validateDeadband())
            return true;
            else
            return false;
        }
    }

    /**
     * time difference to last value has to be bigger than the sample interval
     * @return
     */
    public boolean validateSampleInterval(){
        sampleInterval=datapoint.getSample_interval();
        if(sampleInterval!=null || secondDifference>sampleInterval.longValue())
        return true;
        return false;
    }

    /**
     * TODO check what sampleIntervallMin is for
     * @return
     */
    public boolean validateSampleIntervalMin(){
        sampleIntervalMin=datapoint.getSample_interval_min();
        if(sampleIntervalMin!=null && secondDifference<sampleIntervalMin.longValue())
        return true;
        else
        return false;
    }

    /**
     * if there is a deadband configured; the new value has to be outside the deadband: newValue < (lastvalue - deadband/2) OR (lastvalue + deadband/2) > newValue
     * @return
     */
    public boolean validateDeadband(){
        dpDeadband=datapoint.getDeadband();
        if(dpDeadband==null || (newVal<(lastVal - dpDeadband.longValue()/2) && newVal>(lastVal+ dpDeadband.longValue()/2)))
        return true;
        return false;

    }

    /**
     * value has to be between datapoints min and max value
     * @return
     */
    public boolean validateRange(){
        minValue=datapoint.getMin();
        maxiValue=datapoint.getMax();
        //If minvalue if null then ignore otherwise if new value is less than minvalue then return false
        if(minValue!=null && newVal<minValue.doubleValue())
        return false;
        //If maxvalue if null then ignore otherwise if new value is greater than maxvalue then return false
        else if(maxiValue!=null && newVal>maxiValue.doubleValue())
        return false;
        else
        return true;
    }
    public long getTimeDifference(Date lastValue,Date newValue)
    {
       return (newValue.getTime()-lastValue.getTime())/1000;
    }
}
