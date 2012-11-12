package bpi.most.server.utils;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import javax.xml.bind.DatatypeConverter;

/**
 * util class which holds methods for date calculations
 * and formatting/parsing of dates
 * 
 * @author harald
 *
 */
public class DateUtils {

	public static final String ISO8601_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";
	public static final String DATE_FORMAT = "yyyy-MM-dd";
	public static final String TIME_FORMAT = "HH:mm:ss";
	public static final String DATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
	
	/**
	 * takes a iso 8601 date like "2012-10-01T13:00:00Z"
	 * and returns a Calendar object representing the given time
	 * @param iso8601date
	 * @return
	 */
	public static Calendar parseISO8601Date(String iso8601date){
		Calendar cal = DatatypeConverter.parseDateTime(iso8601date);
		return cal;
	}
	
	/**
	 * tries to parse the given date by several formats.
	 * @param someDate
	 * @return
	 * @throws ParseException 
	 */
	public static Date parseDate(String someDate) throws ParseException{
		Date result = null;
		
		if (someDate != null){
			result = org.apache.commons.lang.time.DateUtils.parseDate(someDate, new String[]{DATETIME_FORMAT, DATE_FORMAT, TIME_FORMAT});
		}
		
		return result;
	}
	
	public static Date returnNowOnNull(String someDate){
		Date result = null;
		
		try{
			if (someDate != null){
				result = parseDate(someDate);
			}
		}catch(Exception e){
		//	e.printStackTrace();
		}
		
		if (result == null){
			result = new Date();
		}
		
		return result;
	}
}
