package bpi.most.obix.contracts;

import bpi.most.obix.objects.Abstime;
import bpi.most.obix.objects.IObj;
import bpi.most.obix.objects.Int;
import bpi.most.obix.objects.List;

/**
 * HistoryRollupOut
 *
 * @author obix.tools.Obixc
 * @version $Revision$ $Date$
 * @creation 24 May 06
 */
public interface HistoryRollupOut
        extends IObj {

    public static final String countContract = "<int name='count' val='0' min='0'/>";

    public Int count();

    public static final String startContract = "<abstime name='start' val='1969-12-31T19:00:00.000-05:00' null='true'/>";

    public Abstime start();

    public static final String endContract = "<abstime name='end' val='1969-12-31T19:00:00.000-05:00' null='true'/>";

    public Abstime end();

    public static final String dataContract = "<list name='data' of='obix:HistoryRollupRecord'/>";

    public List data();

}
