package server;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.TimeZone;

import org.junit.Test;

import bpi.most.server.utils.DateUtils;

public class DateUtilsTest {

	
	
	@Test
	public void testParseISO8601Data() {
		final String fullIso8601Date = "2012-10-01T13:33:01-01:00"; 
		Calendar parsed = DateUtils.parseISO8601Date(fullIso8601Date);
		Calendar expected = Calendar.getInstance();
		expected.set(2012, 9, 1, 13, 33, 1); //month is zero based!
		expected.setTimeZone(TimeZone.getTimeZone("GMT-1:00"));
		assertCalsEqual(expected, parsed);
		System.out.println(parsed.toString());
	}

	private void assertCalsEqual(Calendar expected, Calendar actual){
		assertEquals(expected.get(Calendar.YEAR), actual.get(Calendar.YEAR));
		assertEquals(expected.get(Calendar.MONTH), actual.get(Calendar.MONTH));
		assertEquals(expected.get(Calendar.DATE), actual.get(Calendar.DATE));
		assertEquals(expected.get(Calendar.HOUR), actual.get(Calendar.HOUR));
		assertEquals(expected.get(Calendar.MINUTE), actual.get(Calendar.MINUTE));
		assertEquals(expected.get(Calendar.SECOND), actual.get(Calendar.SECOND));
		assertEquals(expected.get(Calendar.ZONE_OFFSET), actual.get(Calendar.ZONE_OFFSET));
	}
}
