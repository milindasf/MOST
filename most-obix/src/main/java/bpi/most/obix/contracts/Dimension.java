package bpi.most.obix.contracts;

import bpi.most.obix.IObj;
import bpi.most.obix.Int;

/**
 * Dimension
 *
 * @author    obix.tools.Obixc
 * @creation  24 May 06
 * @version   $Revision$ $Date$
 */
public interface Dimension
  extends IObj
{

  public Int kg();

  public Int m();

  public Int sec();

  public Int K();

  public Int A();

  public Int mol();

  public Int cd();

}
