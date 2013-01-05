package bpi.most.obix.contracts;

import bpi.most.obix.objects.IObj;
import bpi.most.obix.objects.Op;
import bpi.most.obix.objects.Reltime;

/**
 * Watch
 *
 * @author obix.tools.Obixc
 * @version $Revision$ $Date$
 * @creation 24 May 06
 */
public interface Watch extends IObj {

	String leaseContract = "<reltime name='lease' val='PT0S' writable='true' min='PT0S'/>";

	Reltime lease();

	String addContract = "<op name='add' in='obix:WatchIn' out='obix:WatchOut'/>";

	Op add();

	String removeContract = "<op name='remove' in='obix:WatchIn' out='obix:obj'/>";

	Op remove();

	String pollChangesContract = "<op name='pollChanges' in='obix:obj' out='obix:WatchOut'/>";

	Op pollChanges();

	String pollRefreshContract = "<op name='pollRefresh' in='obix:obj' out='obix:WatchOut'/>";

	Op pollRefresh();

	Op delete();

}
