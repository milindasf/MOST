package server;

import static org.junit.Assert.*;
import org.junit.Test;

import bpi.most.server.model.Datapoint;
import bpi.most.server.model.DpController;
import bpi.most.server.utils.PollService;

public class PollServiceTest {

	@Test
	public void testProcessNewValues() throws InterruptedException {
		//poll every second
		PollService pollService = PollService.getInstance(1000, 1000);
		DpController dpCtrl = DpController.getInstance();
		Datapoint dp = dpCtrl.getDatapoint("rhu1");
		pollService.start();
		//add observer
		PollServiceObserver obs = new PollServiceObserver();
		dp.addObserver(obs);
		//pollService.processNewValues();
		//wait until someone added a new value directly in the DB
		//TODO implement some kinf of wait(5000) within JUnit
		//pollService.processNewValues();
		pollService.stop();
		
		//check if observer was called
		assertTrue(obs.wasNotified == true);
		
	}


}
