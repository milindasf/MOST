package bpi.most.obix.server.impl;

import bpi.most.dto.DpDTO;
import bpi.most.dto.DpDataDTO;
import bpi.most.dto.ZoneDTO;
import bpi.most.obix.objects.Dp;
import bpi.most.obix.objects.DpData;
import bpi.most.obix.objects.Zone;

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
        zone.setArea(zoneDTO.getArea());
        zone.setBuilding(zoneDTO.getBuilding());
        zone.setCity(zoneDTO.getCity());
        zone.setCountry(zoneDTO.getCountry());
        zone.setCounty(zoneDTO.getCounty());
        zone.setDescription(zoneDTO.getDescription());
        zone.setFloor(zoneDTO.getFloor());
        zone.setRoom(zoneDTO.getRoom());
        zone.setState(zoneDTO.getState());

        return zone;
    }

    public static DpDTO transformDp(Dp dp) {
        return new DpDTO(dp.getDatapointName().get(), dp.getType().get(), dp.getDescription().get());
    }

    public static DpDataDTO transformDpData(DpData dpData) {
        return new DpDataDTO(new Date(dpData.getTimestamp().get()), dpData.getValue().get(), (float) dpData.getQuality().get());
    }
}
