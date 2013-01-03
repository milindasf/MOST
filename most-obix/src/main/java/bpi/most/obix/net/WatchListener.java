/*
 * This code licensed to public domain
 */
package bpi.most.obix.net;

import bpi.most.obix.objects.Obj;

/**
 * WatchListener provides callbacks when watch objects are updated.
 *
 * @author Brian Frank
 * @version $Revision$ $Date$
 * @creation 12 Apr 06
 */
public interface WatchListener {

	void changed(Obj obj);

} 
