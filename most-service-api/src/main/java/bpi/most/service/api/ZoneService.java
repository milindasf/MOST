package bpi.most.service.api;

import bpi.most.domain.zone.Zone;
import bpi.most.dto.DpDTO;
import bpi.most.dto.UserDTO;
import bpi.most.dto.ZoneDTO;

import java.util.List;

/**
 * Interface Specification of Zone Service.
 *
 * @author Lukas Weichselbaum
 */
public interface ZoneService {
    ZoneDTO getZone(UserDTO user, ZoneDTO zoneDto);

    List<ZoneDTO> getZone(String searchPattern);

    List<ZoneDTO> getHeadZones();

    List<ZoneDTO> getHeadZones(UserDTO userDTO);

    List<ZoneDTO> getSubzones(UserDTO user, ZoneDTO zoneEntity, int sublevels);

    List<DpDTO> getDatapoints(UserDTO user, ZoneDTO zoneEntity, int sublevels);

    Zone getZone(int zoneId);

    Zone getZone(ZoneDTO zone);

    void resetCache();

    String getBimModel(UserDTO user);

}
