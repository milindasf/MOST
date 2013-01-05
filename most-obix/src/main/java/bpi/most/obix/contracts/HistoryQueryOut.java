package bpi.most.obix.contracts;

import bpi.most.obix.objects.Abstime;
import bpi.most.obix.objects.IObj;
import bpi.most.obix.objects.Int;
import bpi.most.obix.objects.List;

/**
 * HistoryQueryOut
 *
 * @author obix.tools.Obixc
 * @version $Revision$ $Date$
 * @creation 24 May 06
 */
public interface HistoryQueryOut extends IObj {

	String countContract = "<int name='count' val='0' min='0'/>";

	Int count();

	String startContract = "<abstime name='start' val='1969-12-31T19:00:00.000-05:00' null='true'/>";

	Abstime start();

	String endContract = "<abstime name='end' val='1969-12-31T19:00:00.000-05:00' null='true'/>";

	Abstime end();

	String dataContract = "<list name='data' of='obix:HistoryRecord'/>";

	List data();

}
