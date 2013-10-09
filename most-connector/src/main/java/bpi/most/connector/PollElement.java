package bpi.most.connector;

import java.util.Date;

public class PollElement {
	public Connector connector = null;
	public int pollInterval;
	public int nextPoll;
	public Date lastPollTimestamp = null;
	
	public PollElement(Connector connector, int pollInterval, Date lastPollTimestamp) {
		super();
		this.connector = connector;
		this.pollInterval = pollInterval;
		this.nextPoll =  pollInterval;
		this.lastPollTimestamp = lastPollTimestamp;
	}
}
