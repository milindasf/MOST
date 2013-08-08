package bpi.most.preproc.compare;

import bpi.most.domain.datapoint.DatapointDataVO;
import bpi.most.dto.DpDataDTO;

/**
 * Created with IntelliJ IDEA.
 * User: harald
 * Date: 08.08.13
 * Time: 10:47
 * To change this template use File | Settings | File Templates.
 */
public interface IDataCondition {

    public boolean satisfied(DatapointDataVO value);

}
