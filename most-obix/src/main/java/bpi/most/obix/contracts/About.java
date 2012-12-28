package bpi.most.obix.contracts;

import bpi.most.obix.Abstime;
import bpi.most.obix.IObj;
import bpi.most.obix.Str;
import bpi.most.obix.Uri;

/**
 * About
 *
 * @author    obix.tools.Obixc
 * @creation  24 May 06
 * @version   $Revision$ $Date$
 */
public interface About
  extends IObj
{

  public Str obixVersion();

  public Str serverName();

  public Abstime serverTime();

  public Abstime serverBootTime();

  public Str vendorName();

  public Uri vendorUrl();

  public Str productName();

  public Str productVersion();

  public Uri productUrl();

}
