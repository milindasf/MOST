package bpi.most.obix.contracts;

import bpi.most.obix.IObj;
import bpi.most.obix.Op;

/**
 * WritablePoint
 *
 * @author    obix.tools.Obixc
 * @creation  24 May 06
 * @version   $Revision$ $Date$
 */
public interface WritablePoint
  extends IObj, Point
{

  public static final String writePointContract = "<op name='writePoint' in='obix:WritePointIn' out='obix:Point'/>";
  public Op writePoint();

}
