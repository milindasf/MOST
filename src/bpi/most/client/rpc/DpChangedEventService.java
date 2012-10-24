package bpi.most.client.rpc;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("services/gwtrpc/dp-event-service")
public interface DpChangedEventService extends RemoteService {
	public void startListening(String dpName);
	public void stopListening(String dpName);
}
