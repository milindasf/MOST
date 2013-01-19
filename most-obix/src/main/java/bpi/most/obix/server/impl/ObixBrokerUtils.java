package bpi.most.obix.server.impl;

import bpi.most.dto.DpDTO;
import bpi.most.dto.DpDataDTO;
import bpi.most.dto.ZoneDTO;
import bpi.most.obix.history.HistoryRecordImpl;
import bpi.most.obix.history.HistoryRollupRecordImpl;
import bpi.most.obix.objects.*;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Alexej Strelzow
 * Date: 17.01.13
 * Time: 22:49
 */
public class ObixBrokerUtils {

    public static Dp transformDpDTO(DpDTO dpDto) {
        return new Dp(dpDto.getName(), dpDto.getType(), dpDto.getDescription());
    }

    public static DpData transformDpDataDTO(Dp dp, DpDataDTO dpDataDto) {
        return new DpData(dp, dpDataDto.getTimestamp().getTime(), dpDataDto.getValue(), dpDataDto.getQuality());
    }

    public static Zone transformZoneDTO(ZoneDTO zoneDTO) {
        Zone zone = new Zone(zoneDTO.getZoneId(), zoneDTO.getName());

        if (zoneDTO.getDescription() != null) {
            zone.setDescription(zoneDTO.getDescription());
        }
        if (zoneDTO.getArea() != null) {
            zone.setArea(zoneDTO.getArea());
        }
        if (zoneDTO.getCountry() != null) {
            zone.setCountry(zoneDTO.getCountry());
        }
        if (zoneDTO.getState() != null) {
            zone.setState(zoneDTO.getState());
        }
        if (zoneDTO.getCounty() != null) {
            zone.setCounty(zoneDTO.getCounty());
        }
        if (zoneDTO.getCity() != null) {
            zone.setCity(zoneDTO.getCity());
        }
        if (zoneDTO.getBuilding() != null) {
            zone.setBuilding(zoneDTO.getBuilding());
        }
        if (zoneDTO.getFloor() != null) {
            zone.setFloor(zoneDTO.getFloor());
        }
        if (zoneDTO.getRoom() != null) {
            zone.setRoom(zoneDTO.getRoom());
        }
        if (zoneDTO.getVolume() != null) {
            zone.setVolume(zoneDTO.getVolume());
        }

        return zone;
    }

    public static DpDTO transformDp(Dp dp) {
        return new DpDTO(dp.getDatapointName().get(), dp.getType().get(), dp.getDescription().get());
    }

    public static DpDataDTO transformDpData(DpData dpData) {
        return new DpDataDTO(new Date(dpData.getTimestamp().get()), dpData.getValue().get(), (float) dpData.getQuality().get());
    }

    public static HistoryRecordImpl transformDpDataDTO(DpDataDTO dpDataDto) {
        Real o = new Real("value", dpDataDto.getValue());
        Abstime time = new Abstime("timestamp", dpDataDto.getTimestamp().getTime());

        return new HistoryRecordImpl(o, time);
    }

    public static HistoryRollupRecordImpl transformDpDataDTO(ArrayList<DpData> data) {
        return new HistoryRollupRecordImpl(data);
    }
}
