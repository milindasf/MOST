package bpi.most.domain.datapoint;

import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created with IntelliJ IDEA.
 * User: harald
 * Date: 18.09.13
 * Time: 14:02
 * To change this template use File | Settings | File Templates.
 */
public class SpringInstanceTestClassRunner extends SpringJUnit4ClassRunner {

    private InstanceTestClassListener instanceSetupListener;

    public SpringInstanceTestClassRunner(Class<?> clazz) throws InitializationError {
        super(clazz);
    }

    @Override
    protected Object createTest() throws Exception {
        Object test = super.createTest();
        // Note that JUnit4 will call this createTest() multiple times for each
        // test method, so we need to ensure to call "beforeClassSetup" only once.
        if (test instanceof InstanceTestClassListener && instanceSetupListener == null) {
            instanceSetupListener = (InstanceTestClassListener) test;
            instanceSetupListener.beforeClassSetup();
        }
        return test;
    }

    @Override
    public void run(RunNotifier notifier) {
        super.run(notifier);
        if (instanceSetupListener != null)
            instanceSetupListener.afterClassSetup();
    }
}
