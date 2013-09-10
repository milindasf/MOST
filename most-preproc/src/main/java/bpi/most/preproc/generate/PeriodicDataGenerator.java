package bpi.most.preproc.generate;
import bpi.most.domain.datapoint.DatapointDatasetVO;
import bpi.most.dto.DpDatasetDTO;
import bpi.most.preproc.PeriodicMode;

import java.util.Date;

/**
 *
 * creates periodic data values for given non-periodic dp values.
 *
 */
public class PeriodicDataGenerator {

    /**
     *
     * @param data data where periodic values are created from
     * @param starttime starttime of the requested periodic data (tme of first periodic data value)
     * @param endtime endtime of the requested periodic data (time of last periodic data value)
     * @param period time in seconds between two periodic values (e.g 3600 indicates periodic data with 1 hour steps)
     * @param mode
     * @return
     */
    public DatapointDatasetVO getValuesPeriodic(DatapointDatasetVO data, Date starttime, Date endtime, int period, PeriodicMode mode){
        DatapointDatasetVO periodicData = new DatapointDatasetVO();
        if(getTimeDifference(starttime,endtime)<period)
        {
            System.out.println("endtime must be later than starttime + one periode");
            return null;
        }
        else if (data.size()==0)
        {
            System.out.println("Values doesn't exist for given datapoint");
            return null;
        }
        else if(mode.getValue()==0 || mode==null)
        {
            mode.setValue(1);
        }


        //TODO create periodic data
        switch (mode) {
            case WEIGHTED_AVG_LINEAR_INTERPOLATION:
                periodicData = generateValuesWeightedAvgLinearInterpolated(data, starttime, endtime, period);
                break;
            case WEIGHTED_AVG_SAMPLE_AND_HOLD:
                periodicData = generateValuesWeightedAvgSampleAndHold(data, starttime, endtime, period);
                break;
            case MAJORITY_SAMPLE_AND_HOLD:
                periodicData = generateValuesMajoritySampleAndHold(data, starttime, endtime, period);
                break;
            case DOMINATING_ZERO_DEFAULT_ONE:
                periodicData = generateValuesDominatingZeroDefaultOne(data, starttime, endtime, period);
                break;
            case DOMINATING_ONE_DEFAULT_ZERO:
                periodicData=generateValuesDominatingOneDefaultZero(data, starttime, endtime, period);
                break;
        }

        return periodicData;
    }

    /**
     * A linear interpolation and temporally weighted arithmetic average is used for calculating periodic values.
     * @return
     */
    private DatapointDatasetVO generateValuesWeightedAvgLinearInterpolated(DatapointDatasetVO data, Date starttime, Date endtime, int period){
        DatapointDatasetVO periodicData = new DatapointDatasetVO();



        return periodicData;
    }
    private DatapointDatasetVO generateValuesWeightedAvgSampleAndHold(DatapointDatasetVO data, Date starttime, Date endtime, int period){
        DatapointDatasetVO periodicData = new DatapointDatasetVO();



        return periodicData;
    }
    private DatapointDatasetVO generateValuesMajoritySampleAndHold(DatapointDatasetVO data, Date starttime, Date endtime, int period){
        DatapointDatasetVO periodicData = new DatapointDatasetVO();



        return periodicData;
    }
    private DatapointDatasetVO generateValuesDominatingZeroDefaultOne(DatapointDatasetVO data, Date starttime, Date endtime, int period){
        DatapointDatasetVO periodicData = new DatapointDatasetVO();



        return periodicData;
    }
    private DatapointDatasetVO generateValuesDominatingOneDefaultZero(DatapointDatasetVO data, Date starttime, Date endtime, int period){
        DatapointDatasetVO periodicData = new DatapointDatasetVO();



        return periodicData;
    }
    public int getTimeDifference(Date lastValue,Date newValue)
    {
        return (int) ((newValue.getTime()-lastValue.getTime())/1000);
    }


}
