package bpi.most.obix.contracts;

import bpi.most.obix.Abstime;
import bpi.most.obix.IObj;
import bpi.most.obix.Ref;

/**
 * Alarm
 *
 * @author obix.tools.Obixc
 * @version $Revision$ $Date$
 * @creation 24 May 06
 */
public interface Alarm
        extends IObj {

    public Ref source();

    public Abstime timestamp();

}
