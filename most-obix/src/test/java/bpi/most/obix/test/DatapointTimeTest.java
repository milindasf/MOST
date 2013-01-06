package bpi.most.obix.test;

import bpi.most.obix.objects.Dp;
import bpi.most.obix.objects.DpData;
import junit.framework.Assert;
import junit.framework.TestCase;
import org.junit.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DatapointTimeTest extends TestCase {

//	@Test
//	public void testUCTTransformation() {
//		String fromDateTime = "2013-01-06T19:00:00";
//		String toDateTime = "2013-01-06T19:15:00";
//
//        Calendar calendar = simulateDateUtils(fromDateTime);
//
//		System.out.println(calendar.getTime().toGMTString());
//
//        // DateUtils.returnNowOnNull(fromDateTime);  should return the same
//	}

    private Calendar simulateDateUtils(String utcString) {
        TimeZone utc = TimeZone.getTimeZone("UTC");
        Calendar calendar = Calendar.getInstance(utc);

        DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        df1.setTimeZone(utc);

        DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
        df2.setTimeZone(utc);
        Date finalTime = null;

        try
        {
            finalTime = df1.parse(utcString);
        } catch (ParseException e1)
        {
            try
            {
                finalTime = df2.parse(utcString);
            } catch (ParseException e2)
            {
                e2.printStackTrace();
            }
        }

        calendar.setTime(finalTime);

        return calendar;
    }
	
	@Test
	public void testFilterByDateIntervall() {
		Dp dp = new Dp("test", "type", "description");

        // 6 Jan 2013 08:00:00 GMT
        long millisDpData1 = 1357459200000L;
		DpData dpData1 = new DpData(dp, millisDpData1, -12, 1);

        // 6 Jan 2013 12:00:00 GMT
        long millisDpData2 = 1357473600000L;
		DpData dpData2 = new DpData(dp, millisDpData2, +12, 1);

        // 6 Jan 2013 16:00:00 GMT
        long millisDpData3 = 1357488000000L;
		DpData dpData3 = new DpData(dp, millisDpData3, +12, 1);

        // 7 Jan 2013 08:00:00 GMT
        long millisDpData4 = 1357545600000L;
		DpData dpData4 = new DpData(dp, millisDpData4, +12, 1);

        // 8 Jan 2013 08:00:00 GMT
        long millisDpData5 = 1357632000000L;
		DpData dpData5 = new DpData(dp, millisDpData5, +12, 1);
		
		dp.add(dpData1);
        dp.add(dpData2);
        dp.add(dpData3);
        dp.add(dpData4);
        dp.add(dpData5);
		
		String fromDate = "2013-01-06";
		String toDate = "2013-01-07";
        long fromMillis = simulateDateUtils(fromDate).getTimeInMillis();
        long toMillis = simulateDateUtils(toDate).getTimeInMillis();

        List<DpData> resultList = new ArrayList<DpData>();

        List<Long> expectedResult = new ArrayList<Long>();
        expectedResult.add(millisDpData1);
        expectedResult.add(millisDpData2);
        expectedResult.add(millisDpData3);

		for (DpData data : dp.getDpData()) {
		    long dataMillis = data.getTimestamp().getMillis();
            if (dataMillis >= fromMillis && dataMillis <= toMillis) {
                resultList.add(data);
            }
		}

        Assert.assertTrue(resultList.size() == 3);
        for (DpData data : resultList) {
            expectedResult.remove(data.getTimestamp().getMillis());
        }

        Assert.assertTrue(expectedResult.size() == 0);
	}

    @Test
    public void testFilterByDateTimeIntervall() {
        Dp dp = new Dp("test", "type", "description");

        // 6 Jan 2013 08:00:00 GMT
        long millisDpData1 = 1357459200000L;
        DpData dpData1 = new DpData(dp, millisDpData1, -12, 1);

        // 6 Jan 2013 12:00:00 GMT
        long millisDpData2 = 1357473600000L;
        DpData dpData2 = new DpData(dp, millisDpData2, +12, 1);

        // 6 Jan 2013 16:00:00 GMT
        long millisDpData3 = 1357488000000L;
        DpData dpData3 = new DpData(dp, millisDpData3, +12, 1);

        // 7 Jan 2013 08:00:00 GMT
        long millisDpData4 = 1357545600000L;
        DpData dpData4 = new DpData(dp, millisDpData4, +12, 1);

        // 8 Jan 2013 08:00:00 GMT
        long millisDpData5 = 1357632000000L;
        DpData dpData5 = new DpData(dp, millisDpData5, +12, 1);

        dp.add(dpData1);
        dp.add(dpData2);
        dp.add(dpData3);
        dp.add(dpData4);
        dp.add(dpData5);

        String fromDateTime = "2013-01-06T08:00:00";
        String toDateTime = "2013-01-07T08:15:00";
        long fromMillis = simulateDateUtils(fromDateTime).getTimeInMillis();
        long toMillis = simulateDateUtils(toDateTime).getTimeInMillis();

        List<DpData> resultList = new ArrayList<DpData>();

        List<Long> expectedResult = new ArrayList<Long>();
        expectedResult.add(millisDpData1);
        expectedResult.add(millisDpData2);
        expectedResult.add(millisDpData3);
        expectedResult.add(millisDpData4);

        for (DpData data : dp.getDpData()) {
            long dataMillis = data.getTimestamp().getMillis();
            if (dataMillis >= fromMillis && dataMillis <= toMillis) {
                resultList.add(data);
            }
        }

        Assert.assertTrue(resultList.size() == 4);
        for (DpData data : resultList) {
            expectedResult.remove(data.getTimestamp().getMillis());
        }

        Assert.assertTrue(expectedResult.size() == 0);

    }
	
	
}
