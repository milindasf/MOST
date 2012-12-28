package bpi.most.obix;

import bpi.most.obix.contracts.DatapointData;

/**
 * 
 * @author Alexej Strelzow
 */
public class DpData extends Obj implements DatapointData {

	private static final String TIMESTAMP = "timestamp";
	private static final String VALUE = "value";
	private static final String QUALITY = "quality";
	
	private long timestamp;
	private double value;
	private double quality;
	
	public DpData(long timestamp, double value, double quality) {
		this.timestamp = timestamp;
		this.value = value;
		this.quality = quality;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Abstime getTimestamp() {
		return new Abstime(TIMESTAMP, timestamp);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Real getValue() {
		return new Real(VALUE, value);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Real getQuality() {
		return new Real(QUALITY, quality);
	}

}
