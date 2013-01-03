package bpi.most.obix.test;

import bpi.most.obix.io.ObixDecoder;
import bpi.most.obix.io.ObixEncoder;
import bpi.most.obix.objects.Dp;
import bpi.most.obix.objects.DpData;
import bpi.most.obix.objects.Zone;
import junit.framework.Assert;
import junit.framework.TestCase;
import org.junit.Test;

public class ZoneDecodingTest extends TestCase {

	@Test
	public void testSimpleZoneDecoding() {
		Zone zone = new Zone(1, "WC");
		
		String string = ObixEncoder.toString(zone);
		Zone decodedZone = (Zone) ObixDecoder.fromString(string);
		
		Assert.assertEquals(zone.getZone(), decodedZone.getZone());
		Assert.assertEquals(zone.getZoneName(), decodedZone.getZoneName());
	}
	
	@Test
	public void testComplexZoneEncoding() {
		Zone zone = new Zone(1, "WC");
		
		Dp dp = new Dp("test", "type", "description");
		DpData dpData = new DpData(dp, System.currentTimeMillis(), -12, 1);
		dp.addDpData(dpData);
		
		zone.addDp(dp);
		zone.setShowData(true);
		
		String string = ObixEncoder.toString(zone);
		Zone decodedZone = (Zone) ObixDecoder.fromString(string);
		
		Assert.assertEquals(zone.getZone(), decodedZone.getZone());
		Assert.assertEquals(zone.getZoneName(), decodedZone.getZoneName());
		
//		Dp decodedDp = decodedZone.getDps()[0];
//		Assert.assertEquals(dp.getDatapointName(), decodedDp.getDatapointName());
//		Assert.assertEquals(dp.getType(), decodedDp.getType());
//		Assert.assertEquals(dp.getDescription(), decodedDp.getDescription());
		
	}
	
}
