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

        //If it is the first value for the datapoint then it must be added without any constraint
        if(lastValue==null)
        {
            System.out.println("first Value added without any constraint");
            return true;
        }

        newVal=newValue.getValue();
        lastVal=lastValue.getValue();
        datapoint=dp;
        secondDifference=getTimeDifference(lastValue.getTimestamp(),newValue.getTimestamp());
        System.out.println("New Value :"+newVal+" Last value :"+lastVal+" Datapoint :"+datapoint+" Second Diff. :"+secondDifference);

        //If value is outside of range then it should not be added and the procedure ends here
        if(!validateRange())
        {
            System.out.println("Invalid Range");
            return false;
        }



        //If sampleInterval is not null and value is outside of sample interval then it should be added
        // In addData procedure add with select value 2;
        if(validateSampleInterval())
        {
            System.out.println("Vaildated Sample Interval");
            return true;
        }
        else
        {
            System.out.println("Sample Interval is Null");
            //If sampleInterval is null then check Constraint for SampleIntervalMin
            //if sampleIntervalMin is not null and value<sampleIntervalMin then it should not add the value return false
            if(!validateSampleIntervalMin())
            {
                System.out.println("Invalid SampleIntervalMin");
                return false;
            }
            //If sample intervalMin is null then check deadband Constraint
            else if(validateDeadband())
            {
                System.out.println("valid Deadband");
                return true;
            }
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
        System.out.println("Sample Interval Value : "+sampleInterval);
        if(sampleInterval==null || secondDifference<sampleInterval.longValue())
        {
            System.out.println("Sample Interval is null or value less then sample interval");
            return false;
        }
        else
        return true;
    }

    /**
     * TODO check what sampleIntervallMin is for
     * @return
     */
    public boolean validateSampleIntervalMin(){
        sampleIntervalMin=datapoint.getSample_interval_min();
        System.out.println("Sample Interval Min Value : "+sampleIntervalMin);
        if(sampleIntervalMin!=null && secondDifference<sampleIntervalMin.longValue())
        return false;
        else
        return true;
    }

    /**
     * if there is a deadband configured; the new value has to be outside the deadband: newValue < (lastvalue - deadband/2) OR (lastvalue + deadband/2) > newValue
     * @return
     */
    public boolean validateDeadband(){
        System.out.println("Validating Deadband");
        dpDeadband=datapoint.getDeadband();
        System.out.println("Deadband : "+dpDeadband);
        System.out.println("Condition 1 : "+(lastVal-dpDeadband.longValue()/2));
        System.out.println("Condition 2: "+(lastVal+ dpDeadband.longValue()/2));
        if(dpDeadband==null || (newVal<(lastVal - dpDeadband.longValue()/2) || newVal>(lastVal+ dpDeadband.longValue()/2)))
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
        long milliseconds1 = lastValue.getTime();
        long milliseconds2 = newValue.getTime();
        long diff = milliseconds2 - milliseconds1;
        return (diff/1000);
    }
}
