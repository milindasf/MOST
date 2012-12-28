package bpi.most.obix.contracts;

import bpi.most.obix.IObj;
import bpi.most.obix.Op;

/**
 * WatchService
 *
 * @author    obix.tools.Obixc
 * @creation  24 May 06
 * @version   $Revision$ $Date$
 */
public interface WatchService
  extends IObj
{

  public static final String makeContract = "<op name='make' in='obix:Nil' out='obix:Watch'/>";
  public Op make();

}
