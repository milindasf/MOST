package bpi.most.server.services.gwtrpc;

import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import bpi.most.service.api.ZoneService;
import bpi.most.dto.UserDTO;
import bpi.most.server.services.User;
import bpi.most.dto.DpDTO;
import bpi.most.dto.ZoneDTO;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class GwtRpcZoneService extends SpringGwtServlet implements
        bpi.most.client.rpc.ZoneService {

    @Inject
	ZoneService zoneService;

	private static final long serialVersionUID = 1L;

	public GwtRpcZoneService() {

	}

	@Override
	public List<ZoneDTO> getHeadZones() {
		User user;
		// get user of session
		HttpSession session = this.getThreadLocalRequest().getSession(true);
		user = (User) session.getAttribute("mostUser");
		List<ZoneDTO> list = zoneService.getHeadZones(new UserDTO(user.getUserName()));

        return list;

	}

	@Override
	public List<ZoneDTO> getSubzones(ZoneDTO zoneEntity, int sublevels) {
		User user;
		// get user of session
		HttpSession session = this.getThreadLocalRequest().getSession(true);
		user = (User) session.getAttribute("mostUser");
		return zoneService.getSubzones(new UserDTO(user.getUserName()), zoneEntity, sublevels);
	}

	@Override
	public ZoneDTO getZone(ZoneDTO zoneDto) {
		User user;
		// get user of session
		HttpSession session = this.getThreadLocalRequest().getSession(true);
		user = (User) session.getAttribute("mostUser");
		return zoneService.getZone(new UserDTO(user.getUserName()), zoneDto);
	}

	@Override
	public List<DpDTO> getDatapoints(ZoneDTO zoneEntity, int sublevels) {
		User user;
		// get user of session
		HttpSession session = this.getThreadLocalRequest().getSession(true);
		user = (User) session.getAttribute("mostUser");
		return zoneService.getDatapoints(new UserDTO(user.getUserName()), zoneEntity, sublevels);
	}

}
