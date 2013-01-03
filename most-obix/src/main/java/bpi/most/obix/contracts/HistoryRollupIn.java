package bpi.most.obix.contracts;

import bpi.most.obix.objects.IObj;
import bpi.most.obix.objects.Reltime;

/**
 * HistoryRollupIn
 *
 * @author obix.tools.Obixc
 * @version $Revision$ $Date$
 * @creation 24 May 06
 */
public interface HistoryRollupIn extends IObj, HistoryFilter {

	Reltime interval();

}
