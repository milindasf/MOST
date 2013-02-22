package bpi.most.connector;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

/**
 * call start(), stop() after instantiating!
 * Polls Connectors with the defined interval.
 * The method Connector.processTimeframe(Date lastPollTimestamp) is used to poll new values.
 * 
 * @author robert.zach@tuwien.ac.at
 */
public class PollService {
	private final static Logger log = Logger.getLogger( PollService.class );
	
	private static PollService ref = null;
	Timer defaultPollTimer = new Timer();
	private List<PollElement> pollList = new LinkedList<PollElement>();
	//poll interval in ms, default 60s
	private int pollInterval = 60000;
	
	/**
	 * start poll service
	 */
	public void start() {
		//TODO start only if not running yet
		//create EventPoller
		defaultPollTimer.schedule( new DefaultPoller(), 0, pollInterval );
		return;
	}
	/**
	 * cancel all timers
	 */
	public void stop() {
		defaultPollTimer.cancel();
		return;
	}
	
	
	/**
	 * Register Connector for polling
	 */
	public boolean registerConnector(Connector connector, int pollInterval) {
		//register - connector, pollInterval, lastPollTimestamp
		//null lastPollTimestamp is replaced during first poll after calling Connector.processEvent() 
		pollList.add(new PollElement(connector, pollInterval, null));
		return true;
	}
	
	
	/**
	 * change the DB polling interval for new dp values
	 * 
	 * @arg pollInterval in milliseconds!
	 */
	public void setPollInterval(int pollInterval) {
		defaultPollTimer.cancel();
		this.pollInterval = pollInterval;
		//TODO restart only if it was running before
		defaultPollTimer.schedule( new DefaultPoller(), 0, pollInterval );
		return;
	}
	public int getPollInterval() {
		return pollInterval;
	}
	
	
	//Singleton
	private PollService() {
		super();
	}	
	public static PollService getInstance(){
		if (ref == null) {
			ref = new PollService();
		}
		return ref;
	}
	/**
	 * 
	 * @param pollInterval in ms, e.g. 60000 = 60s
	 * @return
	 */
	public static PollService getInstance(int pollInterval){
		if (ref == null) {
			ref = new PollService();
			ref.pollInterval = pollInterval;
		}
		return ref;
	}
	

	/**
	 * Default poller thread.
	 * Polls all Connectors which are in the current timeframe
	 * Connector.processTimeframe(Date lastPollTimestamp) or Connector.processEvent() is used  
	 */
	public class DefaultPoller extends TimerTask{
		private final Logger log = Logger.getLogger( DefaultPoller.class );
		Date actualTimestamp = null;
		public void run() {
			log.info( "PollService: check for Connectors required to poll" );
			for (PollElement iterateConnector : pollList) {
				log.info( "Polling " + iterateConnector.connector.dpName);
				//first run?
				if (iterateConnector.lastPollTimestamp != null) {
					//not first run --> we know last timestamp
					//Connector need processing? --> check nextPoll
					if (iterateConnector.nextPoll <= 0) {
						//poll starting from lastPollTimestamp
						actualTimestamp = iterateConnector.connector.processTimeframe(iterateConnector.lastPollTimestamp);
						//update lastPollTimestamp
						iterateConnector.lastPollTimestamp = actualTimestamp;
						//reset nextPoll timer
						iterateConnector.nextPoll = iterateConnector.pollInterval;
					}else {
						//reduce nextPoll with defaultPollInterval
						iterateConnector.nextPoll = iterateConnector.nextPoll - PollService.getInstance().getPollInterval()/1000; 
					}
				}else {
					//first time --> don't know last poll timestamp --> processEvent()
					actualTimestamp = iterateConnector.connector.processEvent();
					//set timestmap
					iterateConnector.lastPollTimestamp = actualTimestamp;
				}

			}
		}		 	
	}
	

}
