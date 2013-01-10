package bpi.most.obix.contracts;

import bpi.most.obix.objects.Int;

/**
 * Created with IntelliJ IDEA.
 * User: Alexej Strelozw
 * Date: 10.01.13
 * Time: 13:24
 */
public interface HistoryFilterExt extends HistoryFilter {

    String MODE_CONTRACT = "<int name='mode' val='0' null='true'/>";

    Int mode();

}
