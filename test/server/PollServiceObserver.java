package server;

import java.util.Observable;
import java.util.Observer;

import bpi.most.shared.DpDataDTO;

public class PollServiceObserver implements Observer{
	public boolean wasNotified = false;

	@Override
	public void update(Observable obs, Object arg) {
		wasNotified = true;
		DpDataDTO measurement = (DpDataDTO) arg;
		System.out.println("New value:" + measurement.getValue() + " timestamp: " + measurement.getTimestamp());
	}

}
