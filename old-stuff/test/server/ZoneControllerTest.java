package server;


import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import bpi.most.server.model.ZoneController;
import bpi.most.server.model.Zone;
import bpi.most.server.services.User;

/**
 * Tests the bpi.most.server.ZoneController
 * Junit docu: http://www.vogella.com/articles/JUnit/article.html#juniteclipse_eclipse
 * @author robert.zach@tuwien.ac.at
 */
public class ZoneControllerTest {
	ZoneController zoneCtrl = ZoneController.getInstance();
	
	
	@Test
	public void testGetZoneInt() {
		assertTrue(zoneCtrl.getZone(1).getZoneId() == 1);
	}

	@Test
	public void testGetZoneString() {
		assertTrue(zoneCtrl.getZone("description2") != null);
	}

	@Test
	public void testGetHeadZones() {
		List<Zone> result;
		result = zoneCtrl.getHeadZones();
		//print zones
		for (Zone iterateZone : result) {
			System.out.println(iterateZone.getName());
		}
		assertTrue(result.size() >= 1);
	}

	@Test
	public void testGetHeadZonesUser() {
		List<Zone> result;
		User user = new User("mostsoc");
		result = zoneCtrl.getHeadZones(user);
		//print zones
		for (Zone iterateZone : result) {
			System.out.println(iterateZone.getName());
		}
		assertTrue(result.size() >= 1);	
	}

	@Test
	public void testAddZone() {
		assertTrue(zoneCtrl.addZone(new Zone(24)));
	}

}
