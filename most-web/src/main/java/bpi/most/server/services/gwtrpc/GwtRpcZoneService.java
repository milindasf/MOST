package bpi.most.server.services.gwtrpc;

import java.util.List;

import javax.servlet.http.HttpSession;

import bpi.most.client.rpc.ZoneService;
import bpi.most.server.services.User;
import bpi.most.dto.DpDTO;
import bpi.most.dto.ZoneDTO;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class GwtRpcZoneService extends RemoteServiceServlet implements
		ZoneService {

	bpi.most.server.services.ZoneService zoneService = bpi.most.server.services.ZoneService
			.getInstance();

	private static final long serialVersionUID = 1L;

	public GwtRpcZoneService() {

	}

	@Override
	public List<ZoneDTO> getHeadZones() {
		User user;
		// get user of session
		HttpSession session = this.getThreadLocalRequest().getSession(true);
		user = (User) session.getAttribute("mostUser");
		return zoneService.getHeadZones(user);
	}

	@Override
	public List<ZoneDTO> getSubzones(ZoneDTO zoneEntity, int sublevels) {
		User user;
		// get user of session
		HttpSession session = this.getThreadLocalRequest().getSession(true);
		user = (User) session.getAttribute("mostUser");
		return zoneService.getSubzones(user, zoneEntity, sublevels);
	}

	@Override
	public ZoneDTO getZone(ZoneDTO zoneDto) {
		User user;
		// get user of session
		HttpSession session = this.getThreadLocalRequest().getSession(true);
		user = (User) session.getAttribute("mostUser");
		return zoneService.getZone(user, zoneDto);
	}

	@Override
	public List<DpDTO> getDatapoints(ZoneDTO zoneEntity, int sublevels) {
		User user;
		// get user of session
		HttpSession session = this.getThreadLocalRequest().getSession(true);
		user = (User) session.getAttribute("mostUser");
		return zoneService.getDatapoints(user, zoneEntity, sublevels);
	}

}
