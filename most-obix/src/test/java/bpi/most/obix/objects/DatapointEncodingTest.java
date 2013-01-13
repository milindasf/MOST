package bpi.most.obix.objects;

import bpi.most.obix.io.ObixEncoder;
import bpi.most.obix.objects.Dp;
import bpi.most.obix.objects.DpData;
import junit.framework.TestCase;
import org.junit.Test;

public class DatapointEncodingTest extends TestCase {

//	<obj href="http://myhome/thermostat">
//		<real name="spaceTemp" units="obix:units/fahrenheit" val="67.2"/>
//		<real name="setpoint" unit="obix:units/fahrenheit" val="72.0"/>
//		<bool name="furnaceOn" val="true"/>
//	</obj>


    @Test
    public void testDpPresentation() {
//		<?xml version="1.0" encoding="UTF-8"?>
//		<dp href="/obix/dp/test" is="obix:Datapoint">
//		  <str name="dataPointName" val="test"/>
//		  <str name="type" val="type"/>
//		  <str name="description" val="description"/>
//		</dp>

        Dp dp = new Dp("test", "type", "description");
        ObixEncoder.dump(dp);
    }

    @Test
    public void testDpDataPresentation() {
//		<?xml version="1.0" encoding="UTF-8"?>
//		<dpData href="/obix/dp/test/data" is="obix:DatapointData">
//		  <abstime name="timestamp" val="2012-12-30T19:53:12.519+01:00"/>
//		  <real name="value" val="-12.0" unit="obix:units/celsius"/>
//		  <real name="quality" val="1.0"/>
//		</dpData>
        System.out.println();
        Dp dp = new Dp("test", "type", "description");
        DpData dpData = new DpData(dp, System.currentTimeMillis(), -12, 1);

        ObixEncoder.dump(dpData);

    }

    @Test
    public void testDpFullPresentation() {
//		<?xml version="1.0" encoding="UTF-8"?>
//		<dpData href="/obix/dp/test/data" is="obix:DatapointData">
//		  <abstime name="timestamp" val="2012-12-30T19:53:12.519+01:00"/>
//		  <real name="value" val="-12.0" unit="obix:units/celsius"/>
//		  <real name="quality" val="1.0"/>
//		</dpData>
        System.out.println();
        Dp dp = new Dp("test", "type", "description");
        DpData dpData = new DpData(dp, System.currentTimeMillis(), -12, 1);
        DpData dpData2 = new DpData(dp, System.currentTimeMillis()+1000, +12, 1);

        dp.addDpData(dpData);
        dp.setShowData(true);
        ObixEncoder.dump(dp);

        System.out.println();
        dp.setShowData(false);
        ObixEncoder.dump(dp);

        System.out.println();
        dp.addDpData(dpData2);
        dp.setShowData(true);
        ObixEncoder.dump(dp);
    }

//	@Test
//	public void testString() {
//		Str str = new Str("name", "maus");
//		ObixEncoder.dump(str);
//	}

}
