package bpi.most.obix.contracts;

import bpi.most.obix.IObj;

/**
 * AckAlarmOut
 *
 * @author obix.tools.Obixc
 * @version $Revision$ $Date$
 * @creation 24 May 06
 */
public interface AckAlarmOut
        extends IObj {

    public static final String alarmContract = "<obj name='alarm' is='obix:AckAlarm obix:Alarm'/>";

    public AckAlarm alarm();

}
