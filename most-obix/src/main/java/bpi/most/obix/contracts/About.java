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
public interface About
        extends IObj {

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
