package bpi.most.obix.history;

import bpi.most.obix.objects.Obj;

public class HistoryHelper {
	public static final int HISTORY_COUNT_DEFAULT = 10;
	
	/**
	 * Adds the history to all value data points (bool, real, int) of an oBIX object
	 * This method required the passed oBIX object to be completely initialized (href,...)
	 * @param obj
	 */
	public static void addHistoryToDatapoints(Obj obj){
		addHistoryToDatapoints(obj, HISTORY_COUNT_DEFAULT);
		
	}
	
	/**
	 * Adds the history to all value data points (bool, real, int) of an oBIX object
	 * This method required the passed oBIX object to be completely initialized (href,...)
	 * @param obj
	 * @param countMax 
	 */
	public static void addHistoryToDatapoints(Obj obj, int countMax){
		if(obj.isInt() || obj.isStr() || obj.isBool() || obj.isReal()){
			HistoryImpl impl = new HistoryImpl(obj, countMax);
			impl.initialize();
		}
		
		for(Obj child : obj.list()){
			addHistoryToDatapoints(child, countMax);
		}
	}
}
