package bpi.most.obix.contracts;

import bpi.most.obix.Abstime;
import bpi.most.obix.IObj;
import bpi.most.obix.Op;
import bpi.most.obix.Str;

/**
 * AckAlarm
 *
 * @author obix.tools.Obixc
 * @version $Revision$ $Date$
 * @creation 24 May 06
 */
public interface AckAlarm
        extends IObj, Alarm {

    public static final String ackTimestampContract = "<abstime name='ackTimestamp' val='1969-12-31T19:00:00.000-05:00' null='true'/>";

    public Abstime ackTimestamp();

    public static final String ackUserContract = "<str name='ackUser' val='' null='true'/>";

    public Str ackUser();

    public static final String ackContract = "<op name='ack' in='obix:AlarmAckIn' out='obix:AlarmAckOut'/>";

    public Op ack();

}
