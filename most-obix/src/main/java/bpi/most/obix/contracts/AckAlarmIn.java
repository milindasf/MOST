package bpi.most.obix.contracts;

import bpi.most.obix.objects.IObj;
import bpi.most.obix.objects.Str;

/**
 * AckAlarmIn
 *
 * @author obix.tools.Obixc
 * @version $Revision$ $Date$
 * @creation 24 May 06
 */
public interface AckAlarmIn extends IObj {

	String ACK_USER_CONTRACT = "<str name='ackUser' val='' null='true'/>";

	Str ackUser();

}
