package bpi.most.preproc.compare;

import bpi.most.domain.datapoint.DatapointDataVO;
import bpi.most.dto.DpDataDTO;

/**
 *
 * fulfilled if the given value is bigger
 *
 */
public class BiggerCondition implements IDataCondition{

    /**
     * value for testing
     */
    private double testValue;

    @Override
    public boolean satisfied(DatapointDataVO value) {
        return value.getValue() > testValue;
    }
}
