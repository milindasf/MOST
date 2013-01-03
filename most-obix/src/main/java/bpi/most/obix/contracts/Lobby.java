package bpi.most.obix.contracts;

import bpi.most.obix.objects.IObj;
import bpi.most.obix.objects.Op;
import bpi.most.obix.objects.Ref;

/**
 * Lobby
 *
 * @author obix.tools.Obixc
 * @version $Revision$ $Date$
 * @creation 24 May 06
 */
public interface Lobby extends IObj {

	static final String aboutContract = "<ref name='about' is='obix:About'/>";

	Ref about();

	static final String batchContract = "<op name='batch' in='obix:BatchIn' out='obix:BatchOut'/>";

	Op batch();

	static final String watchServiceContract = "<ref name='watchService' is='obix:WatchService'/>";

	Ref watchService();

}
