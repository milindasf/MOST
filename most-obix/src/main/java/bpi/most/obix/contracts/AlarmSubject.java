package bpi.most.obix.contracts;

import bpi.most.obix.objects.Feed;
import bpi.most.obix.objects.IObj;
import bpi.most.obix.objects.Int;
import bpi.most.obix.objects.Op;

/**
 * AlarmSubject
 *
 * @author obix.tools.Obixc
 * @version $Revision$ $Date$
 * @creation 24 May 06
 */
public interface AlarmSubject
        extends IObj {

    public static final String countContract = "<int name='count' val='0' min='0'/>";

    public Int count();

    public static final String queryContract = "<op name='query' in='obix:AlarmFilter' out='obix:AlarmQueryOut'/>";

    public Op query();

    public static final String feedContract = "<feed name='feed' in='obix:AlarmFilter' of='obix:Alarm'/>";

    public Feed feed();

}
