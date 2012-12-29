package bpi.most.obix.contracts;

import bpi.most.obix.IObj;
import bpi.most.obix.Int;

/**
 * Dimension
 *
 * @author obix.tools.Obixc
 * @version $Revision$ $Date$
 * @creation 24 May 06
 */
public interface Dimension
        extends IObj {

    public Int kg();

    public Int m();

    public Int sec();

    public Int K();

    public Int A();

    public Int mol();

    public Int cd();

}
