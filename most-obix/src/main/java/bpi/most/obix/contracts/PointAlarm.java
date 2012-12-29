package bpi.most.obix.contracts;

import bpi.most.obix.objects.IObj;
import bpi.most.obix.objects.Obj;

/**
 * PointAlarm
 *
 * @author obix.tools.Obixc
 * @version $Revision$ $Date$
 * @creation 24 May 06
 */
public interface PointAlarm
        extends IObj, Alarm {

    public Obj alarmValue();

}
