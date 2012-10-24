package server;

import static org.junit.Assert.*;

import java.util.List;
import org.junit.Test;
import bpi.most.server.model.ConnectorController;
import bpi.most.shared.ConnectorDTO;

public class ConnectorControllerTest {

	@Test
	public void testGetConnector() {
		ConnectorController connCtrl = ConnectorController.getInstance();
		List<ConnectorDTO> connList = null;
		connList = connCtrl.getConnection();
		for (ConnectorDTO iterateConn : connList) {
			System.out.println("Conn:" + iterateConn.dpName + iterateConn.connectionType + iterateConn.min + iterateConn.deadband + iterateConn.sampleInterval);
		}
		assertTrue(connList.size() >= 1);
	}

	@Test
	public void testGetConnectorUser() {
		fail("Not yet implemented");
	}

}
