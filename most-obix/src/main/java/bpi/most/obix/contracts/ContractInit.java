package bpi.most.obix.contracts;

import bpi.most.obix.objects.ContractRegistry;

/**
 * ContractInit
 *
 * @author obix.tools.Obixc
 * @version $Revision$ $Date$
 * @creation 24 May 06
 */
public class ContractInit {

    public static void init() {
        ContractRegistry.put("obix:BadUriErr", "bpi.most.obix.contracts.BadUriErr");
        ContractRegistry.put("obix:UnsupportedErr", "bpi.most.obix.contracts.UnsupportedErr");
        ContractRegistry.put("obix:PermissionErr", "bpi.most.obix.contracts.PermissionErr");
        ContractRegistry.put("obix:Lobby", "bpi.most.obix.contracts.Lobby");
        ContractRegistry.put("obix:About", "bpi.most.obix.contracts.About");
        ContractRegistry.put("obix:BatchIn", "bpi.most.obix.contracts.BatchIn");
        ContractRegistry.put("obix:BatchOut", "bpi.most.obix.contracts.BatchOut");
        ContractRegistry.put("obix:Read", "bpi.most.obix.contracts.Read");
        ContractRegistry.put("obix:Write", "bpi.most.obix.contracts.Write");
        ContractRegistry.put("obix:Invoke", "bpi.most.obix.contracts.Invoke");
        ContractRegistry.put("obix:Nil", "bpi.most.obix.contracts.Nil");
        ContractRegistry.put("obix:Range", "bpi.most.obix.contracts.Range");
        ContractRegistry.put("obix:Weekday", "bpi.most.obix.contracts.Weekday");
        ContractRegistry.put("obix:Month", "bpi.most.obix.contracts.Month");
        ContractRegistry.put("obix:Dimension", "bpi.most.obix.contracts.Dimension");
        ContractRegistry.put("obix:Unit", "bpi.most.obix.contracts.Unit");
        ContractRegistry.put("obix:WatchService", "bpi.most.obix.contracts.WatchService");
        ContractRegistry.put("obix:Watch", "bpi.most.obix.contracts.Watch");
        ContractRegistry.put("obix:WatchIn", "bpi.most.obix.contracts.WatchIn");
        ContractRegistry.put("obix:WatchInItem", "bpi.most.obix.contracts.WatchInItem");
        ContractRegistry.put("obix:WatchOut", "bpi.most.obix.contracts.WatchOut");
        ContractRegistry.put("obix:Point", "bpi.most.obix.contracts.Point");
        ContractRegistry.put("obix:WritablePoint", "bpi.most.obix.contracts.WritablePoint");
        ContractRegistry.put("obix:WritePointIn", "bpi.most.obix.contracts.WritePointIn");
        ContractRegistry.put("obix:History", "bpi.most.obix.contracts.History");
        ContractRegistry.put("obix:HistoryRecord", "bpi.most.obix.contracts.HistoryRecord");
        ContractRegistry.put("obix:HistoryFilter", "bpi.most.obix.contracts.HistoryFilter");
        ContractRegistry.put("obix:HistoryQueryOut", "bpi.most.obix.contracts.HistoryQueryOut");
        ContractRegistry.put("obix:HistoryRollupIn", "bpi.most.obix.contracts.HistoryRollupIn");
        ContractRegistry.put("obix:HistoryRollupOut", "bpi.most.obix.contracts.HistoryRollupOut");
        ContractRegistry.put("obix:HistoryRollupRecord", "bpi.most.obix.contracts.HistoryRollupRecord");
        ContractRegistry.put("obix:Alarm", "bpi.most.obix.contracts.Alarm");
        ContractRegistry.put("obix:StatefulAlarm", "bpi.most.obix.contracts.StatefulAlarm");
        ContractRegistry.put("obix:AckAlarm", "bpi.most.obix.contracts.AckAlarm");
        ContractRegistry.put("obix:AckAlarmIn", "bpi.most.obix.contracts.AckAlarmIn");
        ContractRegistry.put("obix:AckAlarmOut", "bpi.most.obix.contracts.AckAlarmOut");
        ContractRegistry.put("obix:PointAlarm", "bpi.most.obix.contracts.PointAlarm");
        ContractRegistry.put("obix:AlarmSubject", "bpi.most.obix.contracts.AlarmSubject");
        ContractRegistry.put("obix:AlarmFilter", "bpi.most.obix.contracts.AlarmFilter");
        ContractRegistry.put("obix:AlarmQueryOut", "bpi.most.obix.contracts.AlarmQueryOut");
    }

}
