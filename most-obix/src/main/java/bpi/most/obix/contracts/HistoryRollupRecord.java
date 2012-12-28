package bpi.most.obix.contracts;

import bpi.most.obix.Abstime;
import bpi.most.obix.IObj;
import bpi.most.obix.Int;
import bpi.most.obix.Real;

/**
 * HistoryRollupRecord
 *
 * @author    obix.tools.Obixc
 * @creation  24 May 06
 * @version   $Revision$ $Date$
 */
public interface HistoryRollupRecord
  extends IObj
{

  public Abstime start();

  public Abstime end();

  public Int count();

  public Real min();

  public Real max();

  public Real avg();

  public Real sum();

}
