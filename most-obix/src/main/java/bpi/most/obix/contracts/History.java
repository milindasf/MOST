package bpi.most.obix.contracts;

import bpi.most.obix.objects.*;

/**
 * History
 *
 * @author obix.tools.Obixc
 * @version $Revision$ $Date$
 * @creation 24 May 06
 */
public interface History extends IObj {

	String countContract = "<int name='count' val='0' min='0'/>";

	Int count();

	String startContract = "<abstime name='start' val='1969-12-31T19:00:00.000-05:00' null='true'/>";

	Abstime start();

	String endContract = "<abstime name='end' val='1969-12-31T19:00:00.000-05:00' null='true'/>";

	Abstime end();

	String queryContract = "<op name='query' in='obix:HistoryFilter' out='obix:HistoryQueryOut'/>";

	Op query();

	String feedContract = "<feed name='feed' in='obix:HistoryFilter' of='obix:HistoryRecord'/>";

	Feed feed();

	String rollupContract = "<op name='rollup' in='obix:HistoryRollupIn' out='obix:HistoryRollupOut'/>";

	Op rollup();

}
