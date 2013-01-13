/**
 * 
 */
package bpi.most.obix.objects;

import bpi.most.obix.io.ObixEncoder;
import bpi.most.obix.objects.Dp;
import bpi.most.obix.objects.DpData;
import junit.framework.Assert;
import junit.framework.TestCase;
import org.junit.Test;

/**
 * @author Alexej Strelzow
 *
 */
public class TestDatapointCloning extends TestCase {

	@Test
	public void testSimpleCloning() {
		Dp dp = new Dp("test", "type", "description");
		ObixEncoder.dump(dp);
		
		Dp clone = dp.clone();
		ObixEncoder.dump(clone);
		
		Assert.assertEquals(dp.getDatapointName(), clone.getDatapointName());
		Assert.assertEquals(dp.getType(), clone.getType());
		Assert.assertEquals(dp.getDescription(), clone.getDescription());
	}
	
	@Test
	public void testComplexCloning() {
		System.out.println();
		Dp dp = new Dp("test", "type", "description");
		DpData dpData1 = new DpData(dp, System.currentTimeMillis(), -12, 1);
		DpData dpData2 = new DpData(dp, System.currentTimeMillis()+1000, +12, 1);
		
		dp.addDpData(dpData1);
		dp.addDpData(dpData2);
		dp.setShowData(true);
		ObixEncoder.dump(dp);
		
		Dp clone = dp.clone();
		clone.setShowData(true);
		ObixEncoder.dump(clone);
		
		Assert.assertEquals(dp.getDatapointName(), clone.getDatapointName());
		Assert.assertEquals(dp.getType(), clone.getType());
		Assert.assertEquals(dp.getDescription(), clone.getDescription());
		
		DpData[] cloneData = clone.getDpData();
		
		DpData cloneData1 = cloneData[1];
        Assert.assertEquals(dpData1.getTimestamp(), cloneData1.getTimestamp());
		Assert.assertEquals(dpData1.getValue(), cloneData1.getValue());
		Assert.assertEquals(dpData1.getQuality(), cloneData1.getQuality());
		
		DpData cloneData2 = cloneData[0];
		Assert.assertEquals(dpData2.getTimestamp(), cloneData2.getTimestamp());
		Assert.assertEquals(dpData2.getValue(), cloneData2.getValue());
		Assert.assertEquals(dpData2.getQuality(), cloneData2.getQuality());
		
	}
	
}
