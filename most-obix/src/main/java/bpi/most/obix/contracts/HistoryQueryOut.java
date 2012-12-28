package bpi.most.obix.contracts;

import bpi.most.obix.Abstime;
import bpi.most.obix.IObj;
import bpi.most.obix.Int;
import bpi.most.obix.List;

/**
 * HistoryQueryOut
 *
 * @author    obix.tools.Obixc
 * @creation  24 May 06
 * @version   $Revision$ $Date$
 */
public interface HistoryQueryOut
  extends IObj
{

  public static final String countContract = "<int name='count' val='0' min='0'/>";
  public Int count();

  public static final String startContract = "<abstime name='start' val='1969-12-31T19:00:00.000-05:00' null='true'/>";
  public Abstime start();

  public static final String endContract = "<abstime name='end' val='1969-12-31T19:00:00.000-05:00' null='true'/>";
  public Abstime end();

  public static final String dataContract = "<list name='data' of='obix:HistoryRecord'/>";
  public List data();

}
