package bpi.most.obix.contracts;

import bpi.most.obix.IObj;
import bpi.most.obix.Real;
import bpi.most.obix.Str;

/**
 * Unit
 *
 * @author obix.tools.Obixc
 * @version $Revision$ $Date$
 * @creation 24 May 06
 */
public interface Unit
        extends IObj {

    public Str symbol();

    public static final String dimensionContract = "<obj name='dimension' is='obix:Dimension'/>";

    public Dimension dimension();

    public static final String scaleContract = "<real name='scale' val='1.0'/>";

    public Real scale();

    public Real offset();

}
