package bpi.most.obix.contracts;

import bpi.most.obix.Abstime;
import bpi.most.obix.IObj;
import bpi.most.obix.Int;

/**
 * HistoryFilter
 *
 * @author obix.tools.Obixc
 * @version $Revision$ $Date$
 * @creation 24 May 06
 */
public interface HistoryFilter
        extends IObj {

    public static final String limitContract = "<int name='limit' val='0' null='true'/>";

    public Int limit();

    public static final String startContract = "<abstime name='start' val='1969-12-31T19:00:00.000-05:00' null='true'/>";

    public Abstime start();

    public static final String endContract = "<abstime name='end' val='1969-12-31T19:00:00.000-05:00' null='true'/>";

    public Abstime end();

}
