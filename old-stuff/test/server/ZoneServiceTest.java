package server;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import bpi.most.server.model.ZoneController;
import bpi.most.server.services.User;
import bpi.most.server.services.ZoneService;
import bpi.most.shared.DpDTO;
import bpi.most.shared.ZoneDTO;

public class ZoneServiceTest {
	ZoneService zoneService = ZoneService.getInstance();
	ZoneController zoneCtrl = ZoneController.getInstance();
	User user = new User("mostsoc");
	//zone with subzones
	ZoneDTO zoneEntity = zoneCtrl.getZone(10).getZoneDTO();

	@Test
	public void testGetHeadZones() {
		boolean testResult = true;
		List<ZoneDTO> zones = zoneService.getHeadZones(user);

		System.out.println("getHeadzones");
		for (ZoneDTO iterateZone : zones) {
			if (! (iterateZone instanceof ZoneDTO) ) {
				testResult = false;
			}
			System.out.println(iterateZone.getZoneId());
		}
		assertTrue(testResult == true);
	}

	@Test
	public void testGetSubzones() {
		boolean testResult = true;
		//TODO: test multiple subzones
		List<ZoneDTO> zones = zoneService.getSubzones(user, zoneEntity, 1);

		System.out.println("getSubzones");
		for (ZoneDTO iterateZone : zones) {
			if (! (iterateZone instanceof ZoneDTO) ) {
				testResult = false;
			}
			System.out.println(iterateZone.getZoneId());
		}
		assertTrue(testResult == true);
	}

	@Test
	public void testGetDatapoints() {
		boolean testResult = true;
		//TODO: test multiple subzones
		List<DpDTO> datapoints = zoneService.getDatapoints(user, zoneEntity, 0);
		
		for (DpDTO iterateDp : datapoints) {
			if (! (iterateDp instanceof DpDTO) ) {
				testResult = false;
			}
			System.out.println("getDatapoints");
			System.out.println(iterateDp.getName());
		}
		assertTrue(testResult == true);
	}

}
