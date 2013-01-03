package bpi.most.service.api;

import bpi.most.domain.user.User;
import bpi.most.domain.zone.Zone;
import bpi.most.dto.DpDTO;
import bpi.most.dto.UserDTO;
import bpi.most.dto.ZoneDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Interface Specification of Zone Service.
 *
 * @author Lukas Weichselbaum
 */
public interface ZoneService {
    public ZoneDTO getZone(UserDTO user, ZoneDTO zoneDto);

    public List<ZoneDTO> getZone(String searchPattern);

    public List<ZoneDTO> getHeadZones();

    public List<ZoneDTO> getHeadZones(UserDTO userDTO);

    public List<ZoneDTO> getSubzones(UserDTO user, ZoneDTO zoneEntity, int sublevels);

    public List<DpDTO> getDatapoints(UserDTO user, ZoneDTO zoneEntity, int sublevels);

    Zone getZone(int zoneId);

    Zone getZone(ZoneDTO zone);

    void resetCache();
}
