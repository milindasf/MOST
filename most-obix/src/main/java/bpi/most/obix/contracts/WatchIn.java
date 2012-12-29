package bpi.most.obix.contracts;

import bpi.most.obix.objects.IObj;
import bpi.most.obix.objects.List;

/**
 * WatchIn
 *
 * @author obix.tools.Obixc
 * @version $Revision$ $Date$
 * @creation 24 May 06
 */
public interface WatchIn
        extends IObj {

    public static final String hrefsContract = "<list name='hrefs' of='obix:WatchInItem'/>";

    public List hrefs();

}
