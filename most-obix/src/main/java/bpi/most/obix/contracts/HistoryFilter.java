package bpi.most.obix.contracts;

import bpi.most.obix.objects.Abstime;
import bpi.most.obix.objects.IObj;
import bpi.most.obix.objects.Int;

/**
 * HistoryFilter
 *
 * @author obix.tools.Obixc
 * @version $Revision$ $Date$
 * @creation 24 May 06
 */
public interface HistoryFilter extends IObj {

	String limitContract = "<int name='limit' val='0' null='true'/>";

	Int limit();

	String startContract = "<abstime name='start' val='1969-12-31T19:00:00.000-05:00' null='true'/>";

	Abstime start();

	String endContract = "<abstime name='end' val='1969-12-31T19:00:00.000-05:00' null='true'/>";

	Abstime end();

}
