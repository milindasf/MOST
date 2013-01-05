package bpi.most.obix.comparator;

import bpi.most.obix.objects.DpData;

import java.util.Comparator;


public class DpDataComparator implements Comparator<DpData> {

	private boolean sortCriteriaTimeDesc = true;
	
	/**
	 * 
	 * @param o1
	 * @param o2
	 * @return
	 */
	@Override
	public int compare(DpData o1, DpData o2) {
		long timestamp1 = o1.getTimestamp().get();
		long timestamp2 = o2.getTimestamp().get();
		
		if (sortCriteriaTimeDesc) {
			return  timestamp1 == timestamp2 ? 0 : timestamp1 > timestamp2 ? -1 : 1;
		} else {
			return  timestamp1 == timestamp2 ? 0 : timestamp1 > timestamp2 ? 1 : -1;
		}
	}

    public void sortAscendingByTime() {
        sortCriteriaTimeDesc = false;
    }

    public void sortDescendingByTime() {
        sortCriteriaTimeDesc = true;
    }
	
}
