package bpi.most.obix.contracts;

import bpi.most.obix.IObj;
import bpi.most.obix.Reltime;

/**
 * HistoryRollupIn
 *
 * @author    obix.tools.Obixc
 * @creation  24 May 06
 * @version   $Revision$ $Date$
 */
public interface HistoryRollupIn
  extends IObj, HistoryFilter
{

  public Reltime interval();

}
