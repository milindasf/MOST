package bpi.most.obix.contracts;

import bpi.most.obix.objects.Abstime;
import bpi.most.obix.objects.IObj;
import bpi.most.obix.objects.Obj;

/**
 * HistoryRecord
 *
 * @author obix.tools.Obixc
 * @version $Revision$ $Date$
 * @creation 24 May 06
 */
public interface HistoryRecord
        extends IObj {

    public static final String timestampContract = "<abstime name='timestamp' val='1969-12-31T19:00:00.000-05:00' null='true'/>";

    public Abstime timestamp();

    public static final String valueContract = "<obj name='value' null='true'/>";

    public Obj value();

}
