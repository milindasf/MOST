package bpi.most.obix.contracts;

import bpi.most.obix.objects.Abstime;
import bpi.most.obix.objects.IObj;
import bpi.most.obix.objects.Op;
import bpi.most.obix.objects.Str;

/**
 * AckAlarm
 *
 * @author obix.tools.Obixc
 * @version $Revision$ $Date$
 * @creation 24 May 06
 */
public interface AckAlarm extends IObj, Alarm {

	String ACK_TIMESTAMP_CONTRACT = "<abstime name='ackTimestamp' val='1969-12-31T19:00:00.000-05:00' null='true'/>";

	Abstime ackTimestamp();

	String ACK_USER_CONTRACT = "<str name='ackUser' val='' null='true'/>";

	Str ackUser();

	String ACK_CONTRACT = "<op name='ack' in='obix:AlarmAckIn' out='obix:AlarmAckOut'/>";

	Op ack();

}
