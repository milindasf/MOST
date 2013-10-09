package bpi.most.obix.comparator;

import bpi.most.obix.objects.DpData;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Sorts DpData ascending or descending (default) by timestamp.
 *
 * @author Alexej Strelzow
 */
public class DpDataComparator implements Comparator<DpData>, Serializable {

	private static final long serialVersionUID = -3516029306314472125L;
	
	private Boolean sortCriteriaTimeDesc = true;
    private Boolean sortCriteriaValueDesc;
	
	/**
	 * The compare logic.
     *
	 * @param o1 The first DpData object
	 * @param o2 The second DpData object
	 * @return if descending sorting: 0 if equal, -1 if o1(ts) > o2(ts), else 1
	 */
	@Override
	public int compare(DpData o1, DpData o2) {
		long timestamp1 = o1.getTimestamp().get();
		long timestamp2 = o2.getTimestamp().get();
        double value1 = o1.getValue().get();
        double value2 = o2.getValue().get();
		
		if (sortCriteriaTimeDesc != null && sortCriteriaTimeDesc) {
			return  timestamp1 == timestamp2 ? 0 : timestamp1 > timestamp2 ? -1 : 1;
		} else if (sortCriteriaTimeDesc != null && !sortCriteriaTimeDesc) {
			return  timestamp1 == timestamp2 ? 0 : timestamp1 > timestamp2 ? 1 : -1;
		} else if (sortCriteriaValueDesc != null && sortCriteriaValueDesc) {
            return  value1 == value2 ? 0 : value1 > value2 ? -1 : 1;
        } else if (sortCriteriaValueDesc != null && !sortCriteriaValueDesc) {
            return  value1 == value2 ? 0 : value1 > value2 ? 1 : -1;
        }
        return 0;
	}

    /**
     * Sets the sort order to descending
     */
    public void sortAscendingByTime() {
        sortCriteriaTimeDesc = false;
        sortCriteriaValueDesc = null;
    }

    /**
     * Sets the sort order to ascending
     */
    public void sortDescendingByTime() {
        sortCriteriaTimeDesc = true;
        sortCriteriaValueDesc = null;
    }

    /**
     * Sets the sort order to descending
     */
    public void sortAscendingByValue() {
        sortCriteriaValueDesc = false;
        sortCriteriaTimeDesc = null;
    }

    /**
     * Sets the sort order to ascending
     */
    public void sortDescendingByValue() {
        sortCriteriaValueDesc = true;
        sortCriteriaTimeDesc = null;
    }
	
}
