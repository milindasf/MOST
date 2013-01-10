package bpi.most.obix.contracts;

import bpi.most.obix.objects.Int;

/**
 * Created with IntelliJ IDEA.
 * User: Alexej Strelzow
 * Date: 10.01.13
 * Time: 13:31
 */
public interface HistoryRecordExt extends HistoryRecord {

    String MODE_CONTRACT = "<int name='mode' val='0' null='true'/>";

    Int mode();

}
