package bpi.most.obix.contracts;

import bpi.most.obix.objects.IObj;
import bpi.most.obix.objects.Op;

/**
 * WritablePoint
 *
 * @author obix.tools.Obixc
 * @version $Revision$ $Date$
 * @creation 24 May 06
 */
public interface WritablePoint extends IObj, Point {

	String WRITE_POINT_CONTRACT = "<op name='writePoint' in='obix:WritePointIn' out='obix:Point'/>";

	Op writePoint();

}
