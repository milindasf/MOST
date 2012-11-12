package bpi.most.client.modules.charts;

/**
 * Combines the name of a data point with the representation of its series in
 * the chart. Depending on which chart is used, the class of the series is
 * different. The periodicFlag indicates if the data of the series is real time
 * data or periodic generated and is false by default.
 * 
 * @author mike
 */
public class Curve {
	public String name;
	public Object series;
	public boolean periodicFlag = false;

	/**
	 * Create a new {@link Curve} object with the given name and series. The
	 * periodicFlag is set to false as default.
	 * 
	 * @param name
	 *            The name of the series.
	 * @param curve
	 *            The series object of the chart implementation that is used.
	 */
	public Curve(String name, Object curve) {
		super();
		this.name = name;
		this.series = curve;
		this.periodicFlag = false;
	}

	/**
	 * Create a new {@link Curve} object with the given name and series. The
	 * periodicFlag is set to false as default.
	 * 
	 * @param name
	 *            The name of the series.
	 * @param curve
	 *            The series object of the chart implementation that is used.
	 * @param flag
	 *            The periodicFlag should be set to true if the data of the
	 *            series is created periodic. False otherwise.
	 */
	public Curve(String name, Object curve, boolean flag) {
		super();
		this.name = name;
		this.series = curve;
		this.periodicFlag = flag;
	}

	/**
	 * Get the name of the series.
	 * 
	 * @return Returns the name of the series.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Get the series object of the chart implementation used.
	 * 
	 * @return Returns the series object of the chart implementation used.
	 */
	public Object getSeries() {
		return series;
	}

	/**
	 * Get the periodicFlag of the series.
	 * 
	 * @return Returns true if the data of the series is created periodic and
	 *         false otherwise.
	 */
	public boolean getPeriodicFlag() {
		return periodicFlag;
	}

	/**
	 * Set the periodicFlag.
	 * 
	 * @param periodicFlag
	 *            New state of the periodicFlag.
	 */
	public void setPeriodicFlag(boolean periodicFlag) {
		this.periodicFlag = periodicFlag;
	}
}
