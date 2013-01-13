/**
 * 
 */
package bpi.most.obix.objects;

import bpi.most.obix.io.ObixEncoder;
import bpi.most.obix.objects.Dp;
import bpi.most.obix.objects.DpData;
import bpi.most.obix.objects.Zone;
import junit.framework.Assert;
import junit.framework.TestCase;
import org.junit.Test;

/**
 * @author Alexej Strelzow
 *
 */
public class TestZoneCloning extends TestCase {

	@Test
	public void testSimpleCloning() {
		Zone zone = new Zone(1, "WC");
		Dp dp = new Dp("test", "type", "description");
		zone.addDp(dp);
		
		ObixEncoder.dump(zone);
		
		Zone clone = zone.clone();
		ObixEncoder.dump(clone);
		
		Assert.assertEquals(zone.getZone(), clone.getZone());
		Assert.assertEquals(zone.getZoneName(), clone.getZoneName());
	}
	
	@Test
	public void testComplexCloning() {
		Zone zone = new Zone(1, "WC");
		
		Dp dp = new Dp("test", "type", "description");
		DpData dpData = new DpData(dp, System.currentTimeMillis(), -12, 1);
		dp.addDpData(dpData);
		
		zone.addDp(dp);
		zone.setShowData(true);
		
		Zone clone = zone.clone();
		
		Assert.assertEquals(zone.getZone(), clone.getZone());
		Assert.assertEquals(zone.getZoneName(), clone.getZoneName());
		
		Dp clonedDp = clone.getDps()[0];
		Assert.assertEquals(dp.getDatapointName(), clonedDp.getDatapointName());
		Assert.assertEquals(dp.getType(), clonedDp.getType());
		Assert.assertEquals(dp.getDescription(), clonedDp.getDescription());
		
	}
	
	@Test
	public void testVeryComplexZoneEncoding() {
Zone zone = new Zone(1, "WC");
		
		Dp dp = new Dp("test", "type", "description");
		DpData dpData = new DpData(dp, System.currentTimeMillis(), -12, 1);
		dp.addDpData(dpData);
		
		zone.addDp(dp);
		zone.setShowData(true);
		
		Zone clone = zone.clone();
		
		Assert.assertEquals(zone.getZone(), clone.getZone());
		Assert.assertEquals(zone.getZoneName(), clone.getZoneName());
		
		Dp clonedDp = clone.getDps()[0];
		Assert.assertEquals(dp.getDatapointName(), clonedDp.getDatapointName());
		Assert.assertEquals(dp.getType(), clonedDp.getType());
		Assert.assertEquals(dp.getDescription(), clonedDp.getDescription());
		
		DpData clonedDpData = clonedDp.getDpData()[0];
        
        Assert.assertEquals(dpData.getTimestamp(), clonedDpData.getTimestamp());
		Assert.assertEquals(dpData.getValue(), clonedDpData.getValue());
		Assert.assertEquals(dpData.getQuality(), clonedDpData.getQuality());
	}
	
}
