/*
 * This code licensed to public domain
 */
package bpi.most.obix.net;

import bpi.most.obix.Obj;

/**
 * WatchListener provides callbacks when watch objects are updated.
 *
 * @author    Brian Frank
 * @creation  12 Apr 06
 * @version   $Revision$ $Date$
 */
public interface WatchListener
{

  public void changed(Obj obj); 
  
} 
