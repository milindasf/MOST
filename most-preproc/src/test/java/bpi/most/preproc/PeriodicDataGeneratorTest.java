package bpi.most.preproc;

import bpi.most.preproc.generate.PeriodicDataGenerator;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: harald
 * Date: 08.08.13
 * Time: 12:13
 * To change this template use File | Settings | File Templates.
 */

public class PeriodicDataGeneratorTest {

    private PeriodicDataGenerator gen;

    @Before
    public void init(){
        gen = new PeriodicDataGenerator();
    }

    @Test
    public void test(){
        Assert.assertTrue(true);
    }

}
