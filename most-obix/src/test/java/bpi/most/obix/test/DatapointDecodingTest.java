package bpi.most.obix.test;

import bpi.most.obix.io.ObixDecoder;
import bpi.most.obix.io.ObixEncoder;
import bpi.most.obix.objects.Dp;
import bpi.most.obix.objects.DpData;
import junit.framework.Assert;
import junit.framework.TestCase;
import org.junit.Test;

/**
 * Decoding an encoded obix object, which is a string
 * that can be parsed with XML-parser.
 *
 * @author Alexej Strelzow
 */
public class DatapointDecodingTest extends TestCase {

	@Test
	public void testSimpleDpDecoding() {
		Dp dp = new Dp("test", "type", "description");
		ObixEncoder.dump(dp);
		System.out.println();
		
		String string = ObixEncoder.toString(dp);
		System.out.println(string);
		
		Dp decodedDp = (Dp) ObixDecoder.fromString(string);
		
		Assert.assertEquals(dp.getDatapointName(), decodedDp.getDatapointName());
		Assert.assertEquals(dp.getType(), decodedDp.getType());
		Assert.assertEquals(dp.getDescription(), decodedDp.getDescription());
		
		System.out.println();
		ObixEncoder.dump(decodedDp);
	}

    @Test
    public void testComplexDpDecoding() {
        Dp dp = new Dp("test", "type", "description");
        DpData dpData = new DpData(dp, System.currentTimeMillis(), -12, 1);
        dp.addDpData(dpData);
        dp.setShowData(true);

        String string = ObixEncoder.toString(dp);
        System.out.println(string);

        Dp decodedDp = (Dp) ObixDecoder.fromString(string);

        Assert.assertEquals(dp.getDatapointName(), decodedDp.getDatapointName());
        Assert.assertEquals(dp.getType(), decodedDp.getType());
        Assert.assertEquals(dp.getDescription(), decodedDp.getDescription());

        DpData decodedDpData = decodedDp.getDpData()[0];

        Assert.assertEquals(dpData.getTimestamp(), decodedDpData.getTimestamp());
        Assert.assertEquals(dpData.getValue(), decodedDpData.getValue());
        Assert.assertEquals(dpData.getQuality(), decodedDpData.getQuality());
    }
	
	@Test
	public void testSimpleDpDataDecoding() {
		System.out.println();
		Dp dp = new Dp("test", "type", "description");
		DpData dpData = new DpData(dp, System.currentTimeMillis(), -12, 1);
		
		ObixEncoder.dump(dpData);
		System.out.println();
		String string = ObixEncoder.toString(dpData);
		
		DpData decodedDpData = (DpData) ObixDecoder.fromString(string);
		
		Assert.assertEquals(dpData.getTimestamp(), decodedDpData.getTimestamp());
		Assert.assertEquals(dpData.getValue(), decodedDpData.getValue());
		Assert.assertEquals(dpData.getQuality(), decodedDpData.getQuality());
		
		System.out.println();
		ObixEncoder.dump(decodedDpData);
	}
	
}
