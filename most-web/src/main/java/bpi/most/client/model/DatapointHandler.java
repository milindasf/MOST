package bpi.most.client.model;

import bpi.most.dto.DpDataDTO;
import bpi.most.dto.DpDatasetDTO;
import com.google.gwt.user.client.Window;

/*
 * provides async handlers for some Datapoint methods 
 * */
public abstract class DatapointHandler {

	//can be used to reference to any objects which are needed on callback
	public Object tmpRef = null;
	//corresponding datapoint
	public Datapoint datapoint = null;
	
	//constructors for any case
    public DatapointHandler(Object tmpRef) {
    	this.tmpRef = tmpRef;
    }
    public DatapointHandler(Datapoint datapoint) {
    	this.datapoint = datapoint;
    }    	
    public DatapointHandler(Object tmpRef, Datapoint datapoint) {
    	this.tmpRef = tmpRef;
    	this.datapoint = datapoint;
    }
    
	//asynchronous handlers
    public void onSuccess(DpDataDTO result){
    }
    public void onSuccess(DpDatasetDTO result){
    }
    public void onSuccess(String result){
    }
    public void onSuccess(Double result){
    }
    public void onSuccess(int result){
    }
    //handle failures as well in the handler
    public void onFailure(Throwable caught, String function){
    	if(caught.getMessage().equalsIgnoreCase(caught.getLocalizedMessage())){
    		Window.alert("" + function + "-RPC-Error: " + caught.getMessage());
    	} else {
        	Window.alert("" + function + "-RPC-Error: " + caught.getMessage() + " ; " + caught.getLocalizedMessage());
    	}
    }
    
    //getter and setter
	public Object getTmpRef() {
		return tmpRef;
	}
	public void setTmpRef(Object tmpRef) {
		this.tmpRef = tmpRef;
	}
	public Datapoint getDatapoint() {
		return datapoint;
	}
	public void setDatapoint(Datapoint datapoint) {
		this.datapoint = datapoint;
	}
}
