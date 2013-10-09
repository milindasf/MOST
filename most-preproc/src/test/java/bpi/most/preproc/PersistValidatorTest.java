package bpi.most.preproc;

import bpi.most.domain.datapoint.DatapointDataVO;
import bpi.most.domain.datapoint.DatapointVO;
import bpi.most.preproc.validate.PersistValidator;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;


/**
 * Created with IntelliJ IDEA.
 * User: nikunj
 * Date: 11/9/13
 * Time: 7:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class PersistValidatorTest {
    private PersistValidator pvalid;

    private DatapointVO dp;

    private static final Logger LOG = LoggerFactory.getLogger(PersistValidatorTest.class);
    @Before
    public void init(){
       pvalid = new PersistValidator();
       dp=new DatapointVO("name12", "type2", "description2", "custom2", "unit2", new BigDecimal(4.00), new BigDecimal(5.00), new BigDecimal(123453.00), new BigDecimal(1), null, new BigDecimal(3), new BigDecimal(3600), "virtual2", null);
    }

    /**
     * testcases could be sooo simple
     */
    @Test
    public void testRange(){

        //just in range
        Assert.assertTrue(pvalid.validateRange(0.0, 1.0, 0.1));
        Assert.assertTrue(pvalid.validateRange(0.0, 1.0, 0.9));

        //test interval boundary
        Assert.assertTrue(pvalid.validateRange(0.0, 1.0, 0.0));
        Assert.assertTrue(pvalid.validateRange(0.0, 1.0, 1.0));

        //just not in range
        Assert.assertFalse(pvalid.validateRange(0.0, 1.0, -0.1));
        Assert.assertFalse(pvalid.validateRange(0.0, 1.0, 1.1));

        //test negative ranges
        Assert.assertTrue(pvalid.validateRange(-10.0, -5.0, -9.9));
        Assert.assertTrue(pvalid.validateRange(-10.0, -5.0, -5.1));

        //test negative intervall boundary
        Assert.assertTrue(pvalid.validateRange(-10.0, -5.0, -10.0));
        Assert.assertTrue(pvalid.validateRange(-10.0, -5.0, -5.0));

        //test from negative to positive
        Assert.assertTrue(pvalid.validateRange(-10.0, 10.0, 0.0));
        Assert.assertFalse(pvalid.validateRange(-10.0, 10.0, -10.1));
        Assert.assertFalse(pvalid.validateRange(-10.0, 10.0, 10.1));

        //test null min value
        Assert.assertTrue(pvalid.validateRange(null, 10.0, 10.0));
        Assert.assertFalse(pvalid.validateRange(null, 10.0, 10.1));

        //test null max value
        Assert.assertTrue(pvalid.validateRange(10.0, null, 10.0));
        Assert.assertFalse(pvalid.validateRange(10.0, null, 9.9));
    }

    @Test
    public void testValidateSampleInterval()
    {
        //SampleInterval is null
        Assert.assertFalse(pvalid.validateSampleInterval(null, 5000));
        Assert.assertFalse(pvalid.validateSampleInterval(null, 20));

        //Sampleinterval less than Difference
        Assert.assertTrue(pvalid.validateSampleInterval(new BigDecimal(5000),5001));
        Assert.assertTrue(pvalid.validateSampleInterval(new BigDecimal(10),2000));

        //Sampleinterval greater than Differnece
        Assert.assertFalse(pvalid.validateSampleInterval(new BigDecimal(5000), 500));
        Assert.assertFalse(pvalid.validateSampleInterval(new BigDecimal(10), 2));

        //Difference equal to the sample interval
        Assert.assertFalse(pvalid.validateSampleInterval(new BigDecimal(5000), 5000));
        Assert.assertFalse(pvalid.validateSampleInterval(new BigDecimal(10), 10));

    }

    @Test
    public void testValidateSampleIntervalMin()
    {
        //SampleIntervalMin is null
        Assert.assertFalse(pvalid.validateSampleIntervalMin(null, 50));
        Assert.assertFalse(pvalid.validateSampleIntervalMin(null, 2000));

        //SampleintervalMin less than Difference
        Assert.assertTrue(pvalid.validateSampleIntervalMin(new BigDecimal(500), 501));
        Assert.assertTrue(pvalid.validateSampleIntervalMin(new BigDecimal(0), 2000));

        //SampleintervalMin greater than Differnece
        Assert.assertFalse(pvalid.validateSampleIntervalMin(new BigDecimal(200), 50));
        Assert.assertFalse(pvalid.validateSampleIntervalMin(new BigDecimal(101), 2));

        //Difference equal to the sampleintervalMin
        Assert.assertFalse(pvalid.validateSampleIntervalMin(new BigDecimal(2000), 2000));
        Assert.assertFalse(pvalid.validateSampleIntervalMin(new BigDecimal(100), 100));


    }

    @Test
    public void testValidateDeadband()
    {

        //Case 1 : Deadband is null
        Assert.assertTrue(pvalid.validateDeadband(null,4.5,4.0));
        Assert.assertTrue(pvalid.validateDeadband(null,55.0,500.0));


        //Case 2 : new Value is outside the Deadband

        Assert.assertTrue(pvalid.validateDeadband(new BigDecimal(10.0),70.0,60.0));
        Assert.assertTrue(pvalid.validateDeadband(new BigDecimal(15),200.0,222.5));


        //Case 3 : newValue is between deadband range
        Assert.assertFalse(pvalid.validateDeadband(new BigDecimal(10.0), 70.0, 65.0));
        Assert.assertFalse(pvalid.validateDeadband(new BigDecimal(15), 200.0, 202.5));
    }

    //Below two test cases are just to check everything works fine or not.
    @Test
    public void testValidate()
    {
        //Runs validation with all condition
        //Only poesitive case here others will be found in following methods
        Calendar cal = Calendar.getInstance();
        cal.set(2011, 05, 19, 12, 00, 00);
        Date newdate=cal.getTime();
        cal.set(2011, 04, 19, 11, 22, 00);
        Date latestDate=cal.getTime();
        DatapointDataVO newData=new DatapointDataVO(newdate,new Double(4.5));
        DatapointDataVO latestData=new DatapointDataVO(latestDate,new Double(4));

        Assert.assertTrue(pvalid.validate(dp,latestData,newData));
    }
    @Test
    public void testValidateLatestValueNull()
    {
        //Condition 1 If latestValue is null insert without any constraint
        Calendar cal = Calendar.getInstance();
        cal.set(2011, 05, 19, 12, 00, 00);
        Date newdate=cal.getTime();
        cal.set(2011, 04, 19, 11, 22, 00);
        Date latestDate=cal.getTime();
        DatapointDataVO newData=new DatapointDataVO(newdate,new Double(4.5));
        DatapointDataVO latestData=null;

        // The latest value is set to null. The value should be added without any further constraint check.
        Assert.assertTrue(pvalid.validate(dp,latestData,newData));

    }



}
