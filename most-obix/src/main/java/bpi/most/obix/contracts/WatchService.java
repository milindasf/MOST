package bpi.most.obix.contracts;

import bpi.most.obix.objects.IObj;
import bpi.most.obix.objects.Op;

/**
 * WatchService
 *
 * @author obix.tools.Obixc
 * @version $Revision$ $Date$
 * @creation 24 May 06
 */
public interface WatchService extends IObj {

	String makeContract = "<op name='make' in='obix:Nil' out='obix:Watch'/>";

	Op make();

}
