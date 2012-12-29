package bpi.most.obix.contracts;

import bpi.most.obix.objects.Abstime;
import bpi.most.obix.objects.Real;

/**
 * Equivalent to DpDataDTO.
 *
 * @author Alexej Strelzow
 */
public interface DatapointData extends Point {

    Abstime getTimestamp();

    Real getValue();    // double

    Real getQuality();    // double
}
