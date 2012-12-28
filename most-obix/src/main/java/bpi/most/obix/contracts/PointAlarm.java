package bpi.most.obix.contracts;

import bpi.most.obix.IObj;
import bpi.most.obix.Obj;

/**
 * PointAlarm
 *
 * @author    obix.tools.Obixc
 * @creation  24 May 06
 * @version   $Revision$ $Date$
 */
public interface PointAlarm
  extends IObj, Alarm
{

  public Obj alarmValue();

}
