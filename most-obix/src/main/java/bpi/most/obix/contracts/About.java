package bpi.most.obix.contracts;

import bpi.most.obix.objects.Abstime;
import bpi.most.obix.objects.IObj;
import bpi.most.obix.objects.Str;
import bpi.most.obix.objects.Uri;

/**
 * About
 *
 * @author obix.tools.Obixc
 * @version $Revision$ $Date$
 * @creation 24 May 06
 */
public interface About extends IObj {

	Str obixVersion();

	Str serverName();

	Abstime serverTime();

	Abstime serverBootTime();

	Str vendorName();

	Uri vendorUrl();

	Str productName();

	Str productVersion();

	Uri productUrl();

}
