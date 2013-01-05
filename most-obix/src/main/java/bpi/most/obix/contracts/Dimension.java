package bpi.most.obix.contracts;

import bpi.most.obix.objects.IObj;
import bpi.most.obix.objects.Int;

/**
 * Dimension
 *
 * @author obix.tools.Obixc
 * @version $Revision$ $Date$
 * @creation 24 May 06
 */
public interface Dimension extends IObj {

	Int kg();

	Int m();

	Int sec();

	Int k();

	Int a();

	Int mol();

	Int cd();

}
