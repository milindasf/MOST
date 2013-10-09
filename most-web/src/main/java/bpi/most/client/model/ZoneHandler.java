package bpi.most.client.model;

import bpi.most.dto.ZoneDTO;

import java.util.List;


/*
 * provides asynchronous handlers for some zone methods 
 * */
public abstract class ZoneHandler {
	
	// can be used to reference to any objects which are needed on callback
	public Object tmpRef = null;
	// corresponding zone
	public ZoneDTO zone = null;
	
	public ZoneHandler() {
	}

	public ZoneHandler(Object tmpRef) {
		this.tmpRef = tmpRef;
	}

	public ZoneHandler(ZoneDTO zone) {
		this.zone = zone;
	}

	public ZoneHandler(Object tmpRef, ZoneDTO zone) {
		this.tmpRef = tmpRef;
		this.zone = zone;
	}
	
	//asynchronous handlers
	//TODO: what onSuscess methods are needed
	public void onSuccess(List<?> result){
	}
	public void onSuccess(ZoneDTO result){
	}
	public void onSuccess(String result){
		
	}
	
	
	public Object getTmpRef() {
		return tmpRef;
	}
	public void setTmpRef(Object tmpRef) {
		this.tmpRef = tmpRef;
	}
	public ZoneDTO getDatapoint() {
		return zone;
	}
	public void setDatapoint(ZoneDTO zone) {
		this.zone = zone;
	}

}
