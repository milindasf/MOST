package bpi.most.obix.contracts;

import bpi.most.obix.objects.IObj;

/**
 * AckAlarmOut
 *
 * @author obix.tools.Obixc
 * @version $Revision$ $Date$
 * @creation 24 May 06
 */
public interface AckAlarmOut extends IObj {

	String ALARM_CONTRACT = "<obj name='alarm' is='obix:AckAlarm obix:Alarm'/>";

	AckAlarm alarm();

}
