package bpi.most.preproc.compare;

import bpi.most.domain.datapoint.DatapointDataVO;
import bpi.most.domain.datapoint.DatapointDatasetVO;
import bpi.most.dto.DpDataDTO;
import bpi.most.dto.DpDatasetDTO;
import bpi.most.preproc.PeriodicMode;
import bpi.most.preproc.generate.PeriodicDataGenerator;

import java.util.Date;

/**
 *
 *
 *
 */
public class PeriodicDataComparison {

    private PeriodicDataGenerator dg;

    public PeriodicDataComparison() {
        this.dg = new PeriodicDataGenerator();
    }

    /**
     * returns values of data1 based on satisfied conditions on data2. Periodic values are created for both datasets (periodicData1, periodicData2)
     * with the same period so that there exists a value tuple [val1, val2] for each period.
     * Then this methods returns all values val1 from the tuples where val2 satisfies the given condition.
     *
     * @param starttime
     * @param endtime
     * @param data1
     * @param mode1
     * @param data2
     * @param mode2
     * @return
     */
    public DatapointDatasetVO getConditionedPeriodicValues(Date starttime, Date endtime, int period, DatapointDatasetVO data1, PeriodicMode mode1, DatapointDatasetVO data2, PeriodicMode mode2, IDataCondition condition) {
        DatapointDatasetVO result = new DatapointDatasetVO();

        DatapointDatasetVO pd1 = dg.getValuesPeriodic(data1, starttime, endtime, period, mode1);
        DatapointDatasetVO pd2 = dg.getValuesPeriodic(data2, starttime, endtime, period, mode2);

        int idx = 0;
        for(DatapointDataVO val2: pd2){
            if (condition.satisfied(val2)){
                result.add(pd1.get(idx));
            }
            idx++;
        }

        return result;
    }


}

