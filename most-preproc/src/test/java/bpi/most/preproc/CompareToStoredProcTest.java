package bpi.most.preproc;

import bpi.most.domain.datapoint.DatapointDataVO;
import bpi.most.domain.datapoint.DatapointDatasetVO;
import bpi.most.domain.datapoint.DpDataFinderHibernate;
import bpi.most.preproc.generate.PeriodicDataGenerator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;


/**
 * testcases in this class compare generating periodic values of
 * <ul>
 *     <li>
 *          stored procedure implementation
 *     </li>
 *     <li>
 *          Java implementation in module most-preproc
 *     </li>
 * </ul>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:META-INF/most-preproc.spring.xml"})
public class CompareToStoredProcTest {

    private static final int SECONDS_MINUTE = 60;
    private static final int SECONDS_15MINUTES = 900;
    private static final int SECONDS_HALF_HOUR = 1800;
    private static final int SECONDS_HOUR = 3600;
    private static final int SECONDS_2HOURS = 7200;
    private static final int SECONDS_12HOURS = 43200;
    private static final int SECONDS_24HOURS = 86400;


    @PersistenceContext(unitName = "most")
    private EntityManager em;
    private DpDataFinderHibernate dpDfHibernate;

    private PeriodicDataGenerator gen;
    private static final Logger LOG = LoggerFactory.getLogger(CompareToStoredProcTest.class);

    @Before
    public void init(){
        gen = new PeriodicDataGenerator();
        dpDfHibernate = new DpDataFinderHibernate(em);
    }


    @Test
    @Transactional
    public void testAnalogWeightedAvgLinearInterpol() throws Exception {
        //dpDfHibernate.getDataPeriodic();
        Calendar cal = Calendar.getInstance();

        //timeframe is from 6.6.2011 10:00 until 6.6.2011 11:00 --> 1 hour
        cal.set(2011, 05, 06, 10, 00, 00);
        Date start = cal.getTime();
        cal.set(2011, 05, 06, 11, 00, 00);
        Date end = cal.getTime();
        compareValues("occ1", start, end, SECONDS_MINUTE, PeriodicMode.WEIGHTED_AVG_LINEAR_INTERPOLATION, PeriodicType.ANALOG);
        compareValues("occ1", start, end, SECONDS_15MINUTES, PeriodicMode.WEIGHTED_AVG_LINEAR_INTERPOLATION, PeriodicType.ANALOG);
        compareValues("occ1", start, end, SECONDS_HALF_HOUR, PeriodicMode.WEIGHTED_AVG_LINEAR_INTERPOLATION, PeriodicType.ANALOG);

        //timeframe is from 6.6.2011 10:00 until 6.7.2011 10:00 --> 24 hour
        cal.set(Calendar.HOUR_OF_DAY, 10);
        start = cal.getTime();
        cal.set(Calendar.DATE, 7);
        end = cal.getTime();
        compareValues("occ1", start, end, SECONDS_HALF_HOUR, PeriodicMode.WEIGHTED_AVG_LINEAR_INTERPOLATION, PeriodicType.ANALOG);
        compareValues("occ1", start, end, SECONDS_HOUR, PeriodicMode.WEIGHTED_AVG_LINEAR_INTERPOLATION, PeriodicType.ANALOG);
        compareValues("occ1", start, end, SECONDS_2HOURS, PeriodicMode.WEIGHTED_AVG_LINEAR_INTERPOLATION, PeriodicType.ANALOG);
        compareValues("occ1", start, end, SECONDS_12HOURS, PeriodicMode.WEIGHTED_AVG_LINEAR_INTERPOLATION, PeriodicType.ANALOG);
        compareValues("occ1", start, end, SECONDS_24HOURS, PeriodicMode.WEIGHTED_AVG_LINEAR_INTERPOLATION, PeriodicType.ANALOG);
    }


    @Test
    @Transactional
    public void testAnalogWeightedAvgSampleAndHold() throws Exception
    {
        //dpDfHibernate.getDataPeriodic();
        Calendar cal = Calendar.getInstance();

        //timeframe is from 6.6.2011 10:00 until 6.6.2011 11:00 --> 1 hour
        cal.set(2011, 05, 06, 10, 00, 00);
        Date start = cal.getTime();
        cal.set(2011, 05, 06, 11, 00, 00);
        Date end = cal.getTime();
        compareValues("occ1", start, end, SECONDS_MINUTE, PeriodicMode.WEIGHTED_AVG_SAMPLE_AND_HOLD, PeriodicType.ANALOG);
        compareValues("occ1", start, end, SECONDS_15MINUTES, PeriodicMode.WEIGHTED_AVG_LINEAR_INTERPOLATION, PeriodicType.ANALOG);
        compareValues("occ1", start, end, SECONDS_HALF_HOUR, PeriodicMode.WEIGHTED_AVG_LINEAR_INTERPOLATION, PeriodicType.ANALOG);

        //timeframe is from 6.6.2011 10:00 until 6.7.2011 10:00 --> 24 hour
        cal.set(Calendar.HOUR_OF_DAY, 10);
        start = cal.getTime();
        cal.set(Calendar.DATE, 7);
        end = cal.getTime();
        compareValues("occ1", start, end, SECONDS_HALF_HOUR, PeriodicMode.WEIGHTED_AVG_LINEAR_INTERPOLATION, PeriodicType.ANALOG);
        compareValues("occ1", start, end, SECONDS_HOUR, PeriodicMode.WEIGHTED_AVG_LINEAR_INTERPOLATION, PeriodicType.ANALOG);
        compareValues("occ1", start, end, SECONDS_2HOURS, PeriodicMode.WEIGHTED_AVG_LINEAR_INTERPOLATION, PeriodicType.ANALOG);
        compareValues("occ1", start, end, SECONDS_12HOURS, PeriodicMode.WEIGHTED_AVG_LINEAR_INTERPOLATION, PeriodicType.ANALOG);
        compareValues("occ1", start, end, SECONDS_24HOURS, PeriodicMode.WEIGHTED_AVG_LINEAR_INTERPOLATION, PeriodicType.ANALOG);
    }

    @Test
    @Transactional
    public void testBinaryMajoritySampleAndHold() throws Exception {
        //dpDfHibernate.getDataPeriodic();
        Calendar cal = Calendar.getInstance();

        //timeframe is from 07.6.2011 11:00 until 07.6.2011 12:00 --> 1 hour
        cal.set(2011, 05, 07, 11, 00, 00);
        Date start = cal.getTime();
        cal.set(2011, 05, 07, 12, 00, 00);
        Date end = cal.getTime();
        compareValues("con1", start, end, SECONDS_MINUTE, PeriodicMode.MAJORITY_SAMPLE_AND_HOLD, PeriodicType.BINARY);
        compareValues("con1", start, end, SECONDS_15MINUTES, PeriodicMode.MAJORITY_SAMPLE_AND_HOLD, PeriodicType.BINARY);
        compareValues("con1", start, end, SECONDS_HALF_HOUR, PeriodicMode.MAJORITY_SAMPLE_AND_HOLD, PeriodicType.BINARY);

        //timeframe is from 07.6.2011 10:00 until 08.06.2011 10:00 --> 24 hour
        cal.set(Calendar.HOUR_OF_DAY, 10);
        start = cal.getTime();
        cal.set(Calendar.DATE, 8);
        end = cal.getTime();
        compareValues("con1", start, end, SECONDS_HALF_HOUR, PeriodicMode.MAJORITY_SAMPLE_AND_HOLD, PeriodicType.BINARY);
        compareValues("con1", start, end, SECONDS_HOUR, PeriodicMode.MAJORITY_SAMPLE_AND_HOLD, PeriodicType.BINARY);
        compareValues("con1", start, end, SECONDS_2HOURS, PeriodicMode.MAJORITY_SAMPLE_AND_HOLD, PeriodicType.BINARY);
        compareValues("con1", start, end, SECONDS_12HOURS, PeriodicMode.MAJORITY_SAMPLE_AND_HOLD, PeriodicType.BINARY);
        compareValues("con1", start, end, SECONDS_24HOURS, PeriodicMode.MAJORITY_SAMPLE_AND_HOLD, PeriodicType.BINARY);
    }

  @Test
  @Transactional
  public void testBinaryForcedOneDefaultZero() throws Exception
  {
        //dpDfHibernate.getDataPeriodic();
        Calendar cal = Calendar.getInstance();

        //timeframe is from 7.6.2011 11:00 until 7.6.2011 12:00 --> 1 hour
        cal.set(2011, 05, 07, 11, 00, 00);
        Date start = cal.getTime();
        cal.set(2011, 05, 07, 12, 00, 00);
        Date end = cal.getTime();
        compareValues("con1", start, end, SECONDS_MINUTE, PeriodicMode.DOMINATING_ONE_DEFAULT_ZERO, PeriodicType.BINARY);
        compareValues("con1", start, end, SECONDS_15MINUTES, PeriodicMode.DOMINATING_ONE_DEFAULT_ZERO, PeriodicType.BINARY);
        compareValues("con1", start, end, SECONDS_HALF_HOUR, PeriodicMode.DOMINATING_ONE_DEFAULT_ZERO, PeriodicType.BINARY);

        //timeframe is from 7.6.2011 10:00 until 8.7.2011 10:00 --> 24 hour
        cal.set(Calendar.HOUR_OF_DAY, 10);
        start = cal.getTime();
        cal.set(Calendar.DATE, 8);
        end = cal.getTime();
        compareValues("con1", start, end, SECONDS_HALF_HOUR, PeriodicMode.DOMINATING_ONE_DEFAULT_ZERO, PeriodicType.BINARY);
        compareValues("con1", start, end, SECONDS_HOUR, PeriodicMode.DOMINATING_ONE_DEFAULT_ZERO, PeriodicType.BINARY);
        compareValues("con1", start, end, SECONDS_2HOURS, PeriodicMode.DOMINATING_ONE_DEFAULT_ZERO, PeriodicType.BINARY);
        compareValues("con1", start, end, SECONDS_12HOURS, PeriodicMode.DOMINATING_ONE_DEFAULT_ZERO, PeriodicType.BINARY);
        compareValues("con1", start, end, SECONDS_24HOURS, PeriodicMode.DOMINATING_ONE_DEFAULT_ZERO, PeriodicType.BINARY);
  }

  @Test
  @Transactional
  public void testBinaryForcedZeroDefaultOne() throws Exception
  {

          Calendar cal = Calendar.getInstance();

          //timeframe is from 7.6.2011 11:00 until 7.6.2011 12:00 --> 1 hour
          cal.set(2011, 05, 07, 11, 00, 00);
          Date start = cal.getTime();
          cal.set(2011, 05, 07, 12, 00, 00);
          Date end = cal.getTime();
          compareValues("con1", start, end, SECONDS_MINUTE, PeriodicMode.DOMINATING_ZERO_DEFAULT_ONE, PeriodicType.BINARY);
          compareValues("con1", start, end, SECONDS_15MINUTES, PeriodicMode.DOMINATING_ZERO_DEFAULT_ONE, PeriodicType.BINARY);
          compareValues("con1", start, end, SECONDS_HALF_HOUR, PeriodicMode.DOMINATING_ZERO_DEFAULT_ONE, PeriodicType.BINARY);

          //timeframe is from 7.6.2011 10:00 until 8.6.2011 10:00 --> 24 hour
          cal.set(Calendar.HOUR_OF_DAY, 10);
          start = cal.getTime();
          cal.set(Calendar.DATE, 8);
          end = cal.getTime();
          compareValues("con1", start, end, SECONDS_HALF_HOUR, PeriodicMode.DOMINATING_ZERO_DEFAULT_ONE, PeriodicType.BINARY);
          compareValues("con1", start, end, SECONDS_HOUR, PeriodicMode.DOMINATING_ZERO_DEFAULT_ONE, PeriodicType.BINARY);
          compareValues("con1", start, end, SECONDS_2HOURS, PeriodicMode.DOMINATING_ZERO_DEFAULT_ONE, PeriodicType.BINARY);
          compareValues("con1", start, end, SECONDS_12HOURS, PeriodicMode.DOMINATING_ZERO_DEFAULT_ONE, PeriodicType.BINARY);
          compareValues("con1", start, end, SECONDS_24HOURS, PeriodicMode.DOMINATING_ZERO_DEFAULT_ONE, PeriodicType.BINARY);
  }


    private void compareValues(final String dpName, Date start, Date end, int period, final PeriodicMode mode, PeriodicType type) throws Exception {
        DatapointDatasetVO periodicMySql = dpDfHibernate.getDataPeriodic(dpName, start, end, (float)period, mode.getDbCode());
        DatapointDatasetVO realData = dpDfHibernate.getData(dpName, start, end);

        /*LOG.debug("Real data :");
        for (int i=0; i<realData.size(); i++){
            LOG.debug(realData.get(i).toString());

        } */
        DatapointDatasetVO periodicJava =  gen.getValuesPeriodic(realData, start, end, period, mode, type);

        Assert.assertEquals(periodicMySql.size(), periodicJava.size());

        for (int i=0; i<periodicMySql.size(); i++){
            DatapointDataVO dataMySql = periodicMySql.get(i);
            DatapointDataVO dataJava = periodicJava.get(i);
            //LOG.debug("mysql: " + dataMySql.toString());
            //LOG.debug(" java: " + dataJava.toString());

            long secondsMysql = dataMySql.getTimestamp().getTime() / 1000;
            long secondsJava = dataJava.getTimestamp().getTime() / 1000;

            double roundedValMysql = round(dataMySql.getValue());
            double roundedValJava = round(dataJava.getValue());

            double roundedQualMysql = round(dataMySql.getQuality());
            double roundedQualJava = round(dataJava.getQuality());

            /*LOG.debug("Mysql :"+dataMySql.toString());
            LOG.debug("Java :"+dataJava.toString());
            if(secondsMysql!=secondsJava)
                LOG.debug("date not same !");
            if(!(roundedValMysql==roundedValJava || (roundedValMysql>=(roundedValJava-0.05) && roundedValMysql<=(roundedValJava+0.05))))
                LOG.debug("Value not same !");
            if(roundedQualMysql!=roundedQualJava)
                LOG.debug("Quality not same !");
           */
            Assert.assertEquals("date NOT same!",secondsMysql,secondsJava, 0);
            Assert.assertEquals("Value not Same!",roundedValJava, roundedValMysql, 0.05);
            Assert.assertEquals("quality NOT same!",roundedQualMysql,roundedQualJava, 0);
            }

        }



    /*
      rounds the given number to 3 decimal places
     */
    private double round(double val){
        final int SCALE = 3;
        return BigDecimal.valueOf(val).setScale(SCALE, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

}
