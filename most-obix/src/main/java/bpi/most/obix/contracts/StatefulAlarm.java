package bpi.most.obix.contracts;

import bpi.most.obix.objects.Abstime;
import bpi.most.obix.objects.IObj;

/**
 * StatefulAlarm
 *
 * @author obix.tools.Obixc
 * @version $Revision$ $Date$
 * @creation 24 May 06
 */
public interface StatefulAlarm
        extends IObj, Alarm {

    public static final String normalTimestampContract = "<abstime name='normalTimestamp' val='1969-12-31T19:00:00.000-05:00' null='true'/>";

    public Abstime normalTimestamp();

}
