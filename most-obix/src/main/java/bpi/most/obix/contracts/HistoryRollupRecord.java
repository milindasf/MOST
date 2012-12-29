package bpi.most.obix.contracts;

import bpi.most.obix.objects.Abstime;
import bpi.most.obix.objects.IObj;
import bpi.most.obix.objects.Int;
import bpi.most.obix.objects.Real;

/**
 * HistoryRollupRecord
 *
 * @author obix.tools.Obixc
 * @version $Revision$ $Date$
 * @creation 24 May 06
 */
public interface HistoryRollupRecord
        extends IObj {

    public Abstime start();

    public Abstime end();

    public Int count();

    public Real min();

    public Real max();

    public Real avg();

    public Real sum();

}
