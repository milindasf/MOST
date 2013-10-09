package bpi.most.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DpChangedEventServiceAsync {
	void startListening(String dpName, AsyncCallback<Void> callback);
	void stopListening(String dpName, AsyncCallback<Void> callback);
}