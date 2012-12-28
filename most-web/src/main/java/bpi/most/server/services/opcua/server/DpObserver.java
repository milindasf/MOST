package bpi.most.server.services.opcua.server;

import java.util.Observable;
import java.util.Observer;

import bpi.most.dto.DpDataDTO;

public class DpObserver implements Observer {

	@Override
	public void update(Observable datapoint, Object newValue) {
		if (newValue instanceof DpDataDTO){
			DpDataDTO newDpData = (DpDataDTO) newValue;
			//TODO call notification-object which triggers calling clients back
		}
	}

}
