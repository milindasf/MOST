package bpi.most.obix.contracts;

import bpi.most.obix.objects.Abstime;
import bpi.most.obix.objects.IObj;
import bpi.most.obix.objects.Int;
import bpi.most.obix.objects.List;

/**
 * HistoryRollupOut
 *
 * @author obix.tools.Obixc
 * @version $Revision$ $Date$
 * @creation 24 May 06
 */
public interface HistoryRollupOut extends IObj {

	String COUNT_CONTRACT = "<int name='count' val='0' min='0'/>";

	Int count();

	String START_CONTRACT = "<abstime name='start' val='1969-12-31T19:00:00.000-05:00' null='true'/>";

	Abstime start();

	String END_CONTRACT = "<abstime name='end' val='1969-12-31T19:00:00.000-05:00' null='true'/>";

	Abstime end();

	String DATA_CONTRACT = "<list name='data' of='obix:HistoryRollupRecord'/>";

	List data();

}
