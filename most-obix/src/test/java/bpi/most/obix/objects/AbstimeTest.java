/*
 * This code licensed to public domain
 */
package bpi.most.obix.objects;

import java.util.SimpleTimeZone;
import java.util.TimeZone;

import junit.framework.TestCase;

import org.junit.Test;

/**
 * AbstimeTest ensures Abstime follows XML Schema lexical rules
 *
 * @author Brian Frank
 * @version $Revision$ $Date$
 * @creation 27 Apr 05
 */
public class AbstimeTest
        extends TestCase {

    public static TimeZone[] timeZones =
            {
                    TimeZone.getDefault(),
                    TimeZone.getTimeZone("GMT"),
                    TimeZone.getTimeZone("America/New_York"),
                    TimeZone.getTimeZone("America/Phoenix"),
                    TimeZone.getTimeZone("America/Los_Angeles"),
                    TimeZone.getTimeZone("US/Mountain"),
                    TimeZone.getTimeZone("Europe/Brussels"),
                    TimeZone.getTimeZone("Europe/Vienna"),
                    TimeZone.getTimeZone("Asia/Tokyo"),
                    TimeZone.getTimeZone("Asia/Hong_Kong"),
                    new SimpleTimeZone(-4 * 60 * 60 * 1000, "Offset1"),
                    new SimpleTimeZone(-5 * 60 * 60 * 1000, "Offset2")
            };

    public static final int january = 1;
    public static final int february = 2;
    public static final int march = 3;
    public static final int april = 4;
    public static final int may = 5;
    public static final int june = 6;
    public static final int july = 7;
    public static final int august = 8;
    public static final int september = 9;
    public static final int october = 10;
    public static final int november = 11;
    public static final int december = 12;

    public static final int sunday = 0;
    public static final int monday = 1;
    public static final int tuesday = 2;
    public static final int wednesday = 3;
    public static final int thursday = 4;
    public static final int friday = 5;
    public static final int saturday = 6;

    @Test
    public void testBasics() {
        TimeZone est = TimeZone.getTimeZone("America/New_York");
        Abstime t;

        // Eastern Daylight (-4 GMT)
        t = new Abstime(2005, 9, 21, 10, 59, 7, 654, est);
        assertTrue(t.getTimeZone() == est);
        assertTrue(t.getYear() == 2005);
        assertTrue(t.getMonth() == 9);
        assertTrue(t.getDay() == 21);
        assertTrue(t.getHour() == 10);
        assertTrue(t.getMinute() == 59);
        assertTrue(t.getSecond() == 7);
        assertTrue(t.getMillisecond() == 654);
        assertTrue(t.inDaylightTime());
        assertTrue(t.getTimeZoneOffset() == -4 * 60 * 60 * 1000);

        // Eastern Standard (-5 GMT)
        t = new Abstime(2005, 12, 25, 13, 2, 0, 0, est);
        assertTrue(t.getTimeZone() == est);
        assertTrue(t.getYear() == 2005);
        assertTrue(t.getMonth() == 12);
        assertTrue(t.getDay() == 25);
        assertTrue(t.getHour() == 13);
        assertTrue(t.getMinute() == 2);
        assertTrue(t.getSecond() == 0);
        assertTrue(t.getMillisecond() == 0);
        assertTrue(!t.inDaylightTime());
        assertTrue(t.getTimeZoneOffset() == -5 * 60 * 60 * 1000);

        // Eastern Daylight from millis: 2005-09-21 10:59:01.310EDT
        t = new Abstime(1127314741310L, est);
        assertTrue(t.getYear() == 2005);
        assertTrue(t.getMonth() == 9);
        assertTrue(t.getDay() == 21);
        assertTrue(t.getHour() == 10);
        assertTrue(t.getMinute() == 59);
        assertTrue(t.getSecond() == 1);
        assertTrue(t.getMillisecond() == 310);
        assertTrue(t.inDaylightTime());
        assertTrue(t.getTimeZoneOffset() == -4 * 60 * 60 * 1000);

        // convert to utc (-4)
        t = t.toUtcTime();
        assertTrue(t.getYear() == 2005);
        assertTrue(t.getMonth() == 9);
        assertTrue(t.getDay() == 21);
        assertTrue(t.getHour() == 14);
        assertTrue(t.getMinute() == 59);
        assertTrue(t.getSecond() == 1);
        assertTrue(t.getMillisecond() == 310);
        assertTrue(!t.inDaylightTime());
        assertTrue(t.getTimeZoneOffset() == 0);
        assertTrue(t.encodeVal().equals("2005-09-21T14:59:01.310Z"));

        // back to local time
        t = t.toLocalTime();
        assertTrue(t.getYear() == 2005);
        assertTrue(t.getMonth() == 9);
        assertTrue(t.getDay() == 21);
        assertTrue(t.getHour() == 16);
        assertTrue(t.getMinute() == 59);
        assertTrue(t.getSecond() == 1);
        assertTrue(t.getMillisecond() == 310);
        assertTrue(t.inDaylightTime());
        assertTrue(t.getTimeZoneOffset() == 2 * 60 * 60 * 1000);
        assertTrue(t.encodeVal().equals("2005-09-21T16:59:01.310+02:00"));
    }

    @Test
    public void testCodec()
            throws Exception {
 
        for (int i = 0; i < timeZones.length; ++i) {
            for (int j = 0; j < timeZones.length; ++j) {
                TimeZone iz = timeZones[i];
                TimeZone jz = timeZones[j];
                Abstime[] times =
                        {
                                new Abstime(System.currentTimeMillis()),
                                new Abstime(System.currentTimeMillis(), iz),
                                new Abstime(System.currentTimeMillis(), jz),
                                new Abstime(2001, june, 7, 0, 0, 0, 0, iz),
                                new Abstime(2001, june, 7, 0, 0, 0, 0, jz),
                                new Abstime(2001, november, 30, 0, 0, 0, 0, iz),
                                new Abstime(2001, november, 30, 0, 0, 0, 0, jz),
                                new Abstime(1004553579152L, iz),
                                new Abstime(1004553579152L, jz),
                        };

                for (int k = 0; k < times.length; ++k)
                    for (int m = 0; m < times.length; ++m) {
                        Abstime t = times[k];
                        Abstime x = new Abstime();
                        x.decodeVal(t.encodeVal());
                        assertTrue(t.equals(x));
                    }
            }
        }

        // test arbitrary seconds precision
        Abstime t = new Abstime();
        t.decodeVal("2005-09-21T13:14:02.1234567Z");
        assertTrue(t.equals(new Abstime(2005, 9, 21, 13, 14, 2, 123, TimeZone.getTimeZone("UTC"))));
        t.decodeVal("2005-09-21T13:14:02Z");
        assertTrue(t.equals(new Abstime(2005, 9, 21, 13, 14, 2, 000, TimeZone.getTimeZone("UTC"))));
        t.decodeVal("2005-09-21T13:14:02.7Z");
        assertTrue(t.equals(new Abstime(2005, 9, 21, 13, 14, 2, 700, TimeZone.getTimeZone("UTC"))));
        t.decodeVal("2005-09-21T13:14:02.74Z");
        assertTrue(t.equals(new Abstime(2005, 9, 21, 13, 14, 2, 740, TimeZone.getTimeZone("UTC"))));
        t.decodeVal("2005-09-21T13:14:02.005Z");
        assertTrue(t.equals(new Abstime(2005, 9, 21, 13, 14, 2, 05, TimeZone.getTimeZone("UTC"))));
    }

    @Test
    public void testNextPrevDay() {
        //fall daylight savings
        Abstime start = new Abstime(2001, october, 28, 0, 0, 0, 0);
        Abstime next = start.nextDay();
        assertTrue(next.equals(new Abstime(2001, october, 29, 0, 0, 0, 0)));
        assertTrue(next.prevDay().equals(start));
        //non-boundry test
        start = next;
        next = start.nextDay();
        assertTrue(next.equals(new Abstime(2001, october, 30, 0, 0, 0, 0)));
        assertTrue(next.prevDay().equals(start));
        //end of year
        start = new Abstime(2001, december, 31, 0, 0, 0, 0);
        next = start.nextDay();
        assertTrue(next.equals(new Abstime(2002, january, 1, 0, 0, 0, 0)));
        assertTrue(next.prevDay().equals(start));
    }

    @Test
    public void testNextPrevMonth() {
        // non-boundary
    	assertTrue(new Abstime(2003, july, 17).nextMonth().equals(
                new Abstime(2003, august, 17)));
    	assertTrue(new Abstime(2003, july, 17).prevMonth().equals(
                new Abstime(2003, june, 17)));
        // jan/dec
    	assertTrue(new Abstime(2003, december, 17).nextMonth().equals(
                new Abstime(2004, january, 17)));
    	assertTrue(new Abstime(2003, january, 17).prevMonth().equals(
                new Abstime(2002, december, 17)));
        // month caps next
    	assertTrue(new Abstime(2003, january, 31).nextMonth().equals(
                new Abstime(2003, february, 28)));
    	assertTrue(new Abstime(2003, january, 30).nextMonth().equals(
                new Abstime(2003, february, 28)));
    	assertTrue(new Abstime(2003, january, 29).nextMonth().equals(
                new Abstime(2003, february, 28)));
    	assertTrue(new Abstime(2003, january, 28).nextMonth().equals(
                new Abstime(2003, february, 28)));
    	assertTrue(new Abstime(2003, january, 27).nextMonth().equals(
                new Abstime(2003, february, 27)));
        // month caps prev (in leap year)
    	assertTrue(new Abstime(2004, march, 31).prevMonth().equals(
                new Abstime(2004, february, 29)));
    	assertTrue(new Abstime(2004, march, 30).prevMonth().equals(
                new Abstime(2004, february, 29)));
    	assertTrue(new Abstime(2004, march, 29).prevMonth().equals(
                new Abstime(2004, february, 29)));
    	assertTrue(new Abstime(2004, march, 28).prevMonth().equals(
                new Abstime(2004, february, 28)));
        // cap carry thru next
    	assertTrue(new Abstime(2003, february, 28).nextMonth().equals(
                new Abstime(2003, march, 31)));
    	assertTrue(new Abstime(2004, february, 29).nextMonth().equals(
                new Abstime(2004, march, 31)));
    	assertTrue(new Abstime(2004, march, 31).nextMonth().equals(
                new Abstime(2004, april, 30)));
        // cap carry thru prev
    	assertTrue(new Abstime(2004, april, 30).prevMonth().equals(
                new Abstime(2004, march, 31)));
    	assertTrue(new Abstime(2004, march, 31).prevMonth().equals(
                new Abstime(2004, february, 29)));
    	assertTrue(new Abstime(2004, february, 28).prevMonth().equals(
                new Abstime(2004, january, 28)));
    	assertTrue(new Abstime(2004, february, 29).prevMonth().equals(
                new Abstime(2004, january, 31)));
    	assertTrue(new Abstime(2003, march, 31).prevMonth().equals(
                new Abstime(2003, february, 28)));
    	assertTrue(new Abstime(2003, february, 28).prevMonth().equals(
                new Abstime(2003, january, 31)));
    }

    @Test
    public void testNextPrevYear() {
        // next
    	assertTrue(new Abstime(2003, april, 30).nextYear().equals(
                new Abstime(2004, april, 30)));
    	assertTrue(new Abstime(2003, february, 28).nextYear().equals(
                new Abstime(2004, february, 28)));
    	assertTrue(new Abstime(2004, february, 29).nextYear().equals(
                new Abstime(2005, february, 28)));

        // prev
    	assertTrue(new Abstime(2003, april, 30).prevYear().equals(
                new Abstime(2002, april, 30)));
    	assertTrue(new Abstime(2003, february, 28).prevYear().equals(
                new Abstime(2002, february, 28)));
    	assertTrue(new Abstime(2004, february, 29).prevYear().equals(
                new Abstime(2003, february, 28)));
    }

    @Test
    public void testNextPrevWeekdays() {
    	assertTrue(new Abstime(2003, july, 17).nextWeekday(thursday).equals(
                new Abstime(2003, july, 24)));
    	assertTrue(new Abstime(2003, july, 17).nextWeekday(friday).equals(
                new Abstime(2003, july, 18)));
    	assertTrue(new Abstime(2003, july, 17).nextWeekday(wednesday).equals(
                new Abstime(2003, july, 23)));
    	assertTrue(new Abstime(2003, december, 30).nextWeekday(friday).equals(
                new Abstime(2004, january, 2)));

    	assertTrue(new Abstime(2003, november, 3).prevWeekday(thursday).equals(
                new Abstime(2003, october, 30)));
    	assertTrue(new Abstime(2003, november, 3).prevWeekday(monday).equals(
                new Abstime(2003, october, 27)));
    }

    @Test
    public void testLeapYears() {
    	assertTrue(Abstime.isLeapYear(1996));
    	assertTrue(!Abstime.isLeapYear(1997));
    	assertTrue(!Abstime.isLeapYear(1998));
    	assertTrue(!Abstime.isLeapYear(1999));
        assertTrue(Abstime.isLeapYear(2000));
        assertTrue(!Abstime.isLeapYear(2001));
        assertTrue(!Abstime.isLeapYear(2002));
        assertTrue(!Abstime.isLeapYear(2003));
        assertTrue(Abstime.isLeapYear(2004));
        assertTrue(!Abstime.isLeapYear(2005));
        assertTrue(!Abstime.isLeapYear(2006));
        assertTrue(!Abstime.isLeapYear(2007));
        assertTrue(Abstime.isLeapYear(2008));

        assertTrue(Abstime.getDaysInYear(2000) == 366);
        assertTrue(Abstime.getDaysInYear(2001) == 365);
    }

    @Test
    public void testDaysInMonth() {
    	assertTrue(Abstime.getDaysInMonth(2000, january) == 31);
    	assertTrue(Abstime.getDaysInMonth(2000, february) == 29);
    	assertTrue(Abstime.getDaysInMonth(2001, february) == 28);
    	assertTrue(Abstime.getDaysInMonth(2000, march) == 31);
    	assertTrue(Abstime.getDaysInMonth(2000, april) == 30);
        assertTrue(Abstime.getDaysInMonth(2000, may) == 31);
        assertTrue(Abstime.getDaysInMonth(2000, june) == 30);
        assertTrue(Abstime.getDaysInMonth(2000, july) == 31);
        assertTrue(Abstime.getDaysInMonth(2000, august) == 31);
        assertTrue(Abstime.getDaysInMonth(2000, september) == 30);
        assertTrue(Abstime.getDaysInMonth(2000, october) == 31);
        assertTrue(Abstime.getDaysInMonth(2000, november) == 30);
        assertTrue(Abstime.getDaysInMonth(2000, december) == 31);
    }

    @Test
    public void testTimeOfDayMillis() {
    	assertTrue(0 == new Abstime(2000, january, 30, 0, 0, 0, 0).getTimeOfDayMillis());
    	assertTrue(1 == new Abstime(2000, january, 30, 0, 0, 0, 1).getTimeOfDayMillis());
    	assertTrue(1000 == new Abstime(2000, january, 30, 0, 0, 1, 0).getTimeOfDayMillis());
    	assertTrue(7000 == new Abstime(2000, january, 30, 0, 0, 7, 0).getTimeOfDayMillis());
    	assertTrue(60000 == new Abstime(2000, january, 30, 0, 1, 0, 0).getTimeOfDayMillis());
        assertTrue(120000 == new Abstime(2000, january, 30, 0, 2, 0, 0).getTimeOfDayMillis());
        assertTrue(3600000 == new Abstime(2000, january, 30, 1, 0, 0, 0).getTimeOfDayMillis());
        assertTrue(86399999 == new Abstime(2000, january, 30, 23, 59, 59, 999).getTimeOfDayMillis());
        assertTrue(0 == new Abstime(2000, january, 30, 24, 0, 0, 0).getTimeOfDayMillis());
    }

    public static void dump(TimeZone tz) {
        System.out.println(tz.getID() + " = " + tz.getRawOffset() / (60 * 60 * 1000));
    }

}
