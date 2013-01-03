package bpi.most.obix.test;

import bpi.most.obix.comparator.DpDataComparator;
import bpi.most.obix.io.ObixDecoder;
import bpi.most.obix.io.ObixEncoder;
import bpi.most.obix.objects.*;
import junit.framework.Assert;
import junit.framework.TestCase;
import org.junit.Test;

import java.util.Collections;

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

    @Test
    public void testGetAllDatapointsDecoding() {
        Dp dp1 = new Dp("test1", "type1", "description1");
        DpData dpData1 = new DpData(dp1, System.currentTimeMillis(), -12, 1);
        dp1.addDpData(dpData1);

        Dp dp2 = new Dp("test2", "type2", "description2");
        DpData dpData2 = new DpData(dp2, System.currentTimeMillis()+10, 1, 1);
        dp2.addDpData(dpData2);

        List dpList = new List("datapoints", new Contract("obix:dp"));
        dp1.setShowData(true);
        dp2.setShowData(true);
        dpList.addAll(new Dp[] { dp1, dp2 });

        String string = ObixEncoder.toString(dpList);
        Obj decodedDpList = ObixDecoder.fromString(string);

        System.out.println();
        ObixEncoder.dump(decodedDpList);

        java.util.List<Dp> kidsByClass = decodedDpList.getKidsByClass(Dp.class);

        Dp dp_1 = kidsByClass.get(0);
        Assert.assertEquals(dp1.getDatapointName(), dp_1.getDatapointName());
        Assert.assertEquals(dp1.getType(), dp_1.getType());
        Assert.assertEquals(dp1.getDescription(), dp_1.getDescription());

        DpData decodedDpData1 = dp_1.getDpData()[0];
        Assert.assertEquals(dpData1.getTimestamp(), decodedDpData1.getTimestamp());
        Assert.assertEquals(dpData1.getValue(), decodedDpData1.getValue());
        Assert.assertEquals(dpData1.getQuality(), decodedDpData1.getQuality());


        Dp dp_2 = kidsByClass.get(1);
        Assert.assertEquals(dp2.getDatapointName(), dp_2.getDatapointName());
        Assert.assertEquals(dp2.getType(), dp_2.getType());
        Assert.assertEquals(dp2.getDescription(), dp_2.getDescription());

        DpData decodedDpData2 = dp_2.getDpData()[0];
        Assert.assertEquals(dpData2.getTimestamp(), decodedDpData2.getTimestamp());
        Assert.assertEquals(dpData2.getValue(), decodedDpData2.getValue());
        Assert.assertEquals(dpData2.getQuality(), decodedDpData2.getQuality());
    }

    @Test
    public void testGetDpDataAsListDecoding() {
        Dp dp = new Dp("test", "type", "description");
        DpData dpData1 = new DpData(dp, System.currentTimeMillis(), -12, 1);
        dp.addDpData(dpData1);
        DpData dpData2 = new DpData(dp, System.currentTimeMillis()+10, 1, 1);
        dp.addDpData(dpData2);

        List dpDataAsList = dp.getDpDataAsList();
        ObixEncoder.dump(dpDataAsList);

        String string = ObixEncoder.toString(dpDataAsList);
        Obj decodedDpDataAsList = ObixDecoder.fromString(string);

        System.out.println();
        ObixEncoder.dump(decodedDpDataAsList);

        java.util.List<DpData> kidsByClass = ((List)decodedDpDataAsList).getKidsByClass(DpData.class);

        Collections.sort(kidsByClass, new DpDataComparator());

        Assert.assertTrue(kidsByClass.size() == 2);

        DpData decodedDpData1 = kidsByClass.get(1);
        Assert.assertEquals(dpData1.getTimestamp(), decodedDpData1.getTimestamp());
        Assert.assertEquals(dpData1.getValue(), decodedDpData1.getValue());
        Assert.assertEquals(dpData1.getQuality(), decodedDpData1.getQuality());

        DpData decodedDpData2 = kidsByClass.get(0);
        Assert.assertEquals(dpData2.getTimestamp(), decodedDpData2.getTimestamp());
        Assert.assertEquals(dpData2.getValue(), decodedDpData2.getValue());
        Assert.assertEquals(dpData2.getQuality(), decodedDpData2.getQuality());
    }
	
}
