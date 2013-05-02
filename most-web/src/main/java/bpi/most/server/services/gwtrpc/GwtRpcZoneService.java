package bpi.most.server.services.gwtrpc;

import bpi.most.dto.DpDTO;
import bpi.most.dto.UserDTO;
import bpi.most.dto.ZoneDTO;
import bpi.most.server.services.User;
import bpi.most.service.api.ZoneService;

import javax.inject.Inject;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpSession;
import java.util.List;

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

    @Override
    public String getBimModel() {
        User user;
        // get user of session
        HttpSession session = this.getThreadLocalRequest().getSession(true);
        user = (User) session.getAttribute("mostUser");
        //TODO implement
        //TODO: how to get ServletContext?
        //InputStream input = ServletConfig.getServletContext().getResourceAsStream("/testfile.json");
//		InputStream json = null;
//		try {
//				json = new FileInputStream("testFile.json");
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return json;
        Context initCtx;
        //default demo model
        String modelPath = "models/BPI_archicad.json";
        try {
            initCtx = new InitialContext();
            Context bimCtx = (Context) initCtx.lookup("java:comp/env/bim");
            modelPath = (String) bimCtx.lookup("modelPath");
            System.out.println("BimModel");
            System.out.println("modelPath: " + modelPath);
        } catch (NamingException e) {
            System.out.println("getBimModel no context supported!");
        }

        return modelPath;
        //return zoneService.getBimModel(new UserDTO(user.getUserName()));
    }

}
