package bpi.most.server.services.gwtrpc;

import bpi.most.client.rpc.PersonModuleService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Enumeration;
import java.util.HashMap;

@SuppressWarnings("serial")
public class GwtRpcPersonModuleService extends RemoteServiceServlet implements
PersonModuleService {

	private static final int MAP_MAX_SIZE = 4;

	@Override
	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		HashMap<String, String> ap = new HashMap<String, String>();
		for (@SuppressWarnings("rawtypes")
		Enumeration e = request.getParameterNames(); e.hasMoreElements();) {
			// ap.put(key, value)
			String temp = e.nextElement().toString();
			ap.put(temp, request.getParameterValues(temp)[0]);
			// TODO validation
			if (ap.size() < MAP_MAX_SIZE) {
				System.out.println("error person module set data");
			} else {
				savePersonModuleDataSetIntoDb(ap);
			}
		}
	}

	@Override
	public boolean setPersonValues(HashMap<String, String> personForm) {

		System.out.println(personForm.get("Temperatur"));
		// TODO Auto-generated method stub
		return false;
	}

	public boolean setForm() {

		HttpServletRequest req = this.getThreadLocalRequest();
		System.out.println("" + req.getPathInfo());
		return true;
	}

	public boolean savePersonModuleDataSetIntoDb(HashMap<String, String> ap) {

		// TODO write stored procedures
//		try {
//			Connection con = DbPool.getInstance().getConnection();
//			Statement stmt = con.createStatement();
//			final String sql = new StringBuilder("INSERT INTO personModuleData(temperature, air, air_movement, clothing) VALUES ('").
//					append(ap.get("Temperatur")).
//					append("','").
//					append(ap.get("Luft")).
//					append("','").
//					append(ap.get("Luftbewegung")).
//					append("','").
//					append(ap.get("Kleidung")).
//					append("');").toString();
//			stmt.executeUpdate(sql);
//			stmt.close();
//			con.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		return false;
	}
}
