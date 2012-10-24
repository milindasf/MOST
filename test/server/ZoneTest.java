package server;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import bpi.most.server.model.Datapoint;
import bpi.most.server.model.Zone;
import bpi.most.server.model.ZoneController;
import bpi.most.shared.ZoneDTO;

/**
 * Tests bpi.most.server.Zone
 * @author robert.zach@tuwien.ac.at
 */
public class ZoneTest {
	ZoneController zoneCtrl = ZoneController.getInstance();
	//zone with subzones
	Zone zone = zoneCtrl.getZone(10);

	@Test
	public void testGetEntity() {
		System.out.println(zone.getZoneDTO().getZoneId());
		assertTrue(zone.getZoneDTO() instanceof ZoneDTO);
	}

	@Test
	public void testGetSubzones() {
		boolean testResult = true;
		//TODO: test multiple subzones
		List<Zone> subzones = zone.getSubzones(1);
		for (Zone iterateZone : subzones) {
			if (! (iterateZone instanceof Zone) ) {
				testResult = false;
			}
			System.out.println(iterateZone.getZoneId());
		}
		assertTrue(testResult == true);
	}

	@Test
	public void testAddSubzone() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetDatapointsInt() {
		boolean testResult = true;
		//TODO: test multiple subzones
		List<Datapoint> dps = zone.getDatapoints(4);
		for (Datapoint iterateDp : dps) {
			if (! (iterateDp instanceof Datapoint) ) {
				testResult = false;
			}
			System.out.println(iterateDp.getDatapointName());
		}
		assertTrue(testResult == true);
	}

	@Test
	public void testGetDatapointsIntString() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetZoneId() {
		assertTrue(zone.getZoneId() != 0);
	}

	@Test
	public void testGetName() {
		assertTrue(zone.getName() != null);
	}

	@Test
	public void testGetDescription() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetCountry() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetState() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetCounty() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetCity() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetBuilding() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetFloor() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetRoom() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetArea() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetVolume() {
		fail("Not yet implemented");
	}

}
