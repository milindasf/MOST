package bpi.most.obix.objects;

import bpi.most.obix.io.ObixEncoder;
import bpi.most.obix.objects.Dp;
import bpi.most.obix.objects.DpData;
import bpi.most.obix.objects.Zone;
import junit.framework.TestCase;
import org.junit.Test;

public class ZoneEncodingTest extends TestCase {

	@Test
	public void testSimpleZoneEncoding() {
		Zone zone = new Zone(1, "WC");
		
		ObixEncoder.dump(zone);
	}
	
	@Test
	public void testComplexZoneEncoding() {
		Zone zone = new Zone(1, "WC");
		
		Dp dp = new Dp("test", "type", "description");
		DpData dpData = new DpData(dp, System.currentTimeMillis(), -12, 1);
		dp.addDpData(dpData);
		
		zone.addDp(dp);
		
		System.out.println();
		ObixEncoder.dump(zone);
		
		zone.setShowData(true);
		
		System.out.println();
		ObixEncoder.dump(zone);
	}
	
}
