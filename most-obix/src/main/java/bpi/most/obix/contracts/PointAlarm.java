package bpi.most.obix.contracts;

import bpi.most.obix.IObj;
import bpi.most.obix.Obj;

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
