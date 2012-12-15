package bpi.most.server.services.gwtrpc;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Enumeration;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bpi.most.client.rpc.PersonModuleService;
import bpi.most.server.utils.DbPool;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class GwtRpcPersonModuleService extends RemoteServiceServlet implements
    PersonModuleService {
  
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
      if (ap.size() < 4) {
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
    try {
      Connection con = DbPool.getInstance().getConnection();
      Statement stmt = con.createStatement();
      String query = "INSERT INTO personModuleData(temperature, air, air_movement, clothing) VALUES "
          + "('"
          + ap.get("Temperatur")
          + "','"
          + ap.get("Luft")
          + "','"
          + ap.get("Luftbewegung") + "','" + ap.get("Kleidung") + "');";
      stmt.executeUpdate(query);
      stmt.close();
      con.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return false;
  }
}
