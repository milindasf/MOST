package bpi.most.service.impl.db;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;

import javax.annotation.PostConstruct;

/**
 * Provides a pool of db connections using the tomcat jdbc pool implementation
 *
 * @author robert.zach@tuwien.ac.at
 * @author Jakob Korherr
 */
public class DbPool {

	//JDBC pool
	private DataSource datasource;
	
	private static final int VALIDATION_INTERVAL = 30000;
	private static final int TIME_BETWEEN_EVICTION_RUNS_MILLIS = 30000;
	private static final int MAX_WAIT = 10000;
	private static final int REMOVE_ABONDONED_TIMEOUT = 60;
	private static final int MIN_IDLE = 10;
	private static final int MIN_EVICTABLE_IDLE_TIME_MILLIS = 30000;

    @PostConstruct
    protected void init() {
        //TODO try to find data source in context.
        //use default config
        try {
            Class.forName("com.mysql.jdbc.Driver");
            PoolProperties p = new PoolProperties();
            p.setUrl("jdbc:mysql://" + DbProperties.getHostname() + "/" + DbProperties.getDatabase());
            p.setDriverClassName("com.mysql.jdbc.Driver");
            p.setUsername(DbProperties.getUsername());
            p.setPassword(DbProperties.getPassword());
            p.setJmxEnabled(true);
            p.setTestWhileIdle(false);
            p.setTestOnBorrow(true);
            p.setValidationQuery("SELECT 1");
            p.setTestOnReturn(false);
            p.setValidationInterval(VALIDATION_INTERVAL);
            p.setTimeBetweenEvictionRunsMillis(TIME_BETWEEN_EVICTION_RUNS_MILLIS);
            p.setMaxActive(DbProperties.getConPoolMax());
            p.setInitialSize(DbProperties.getConPoolMin());
            p.setMaxWait(MAX_WAIT);
            p.setRemoveAbandonedTimeout(REMOVE_ABONDONED_TIMEOUT);
            p.setMinEvictableIdleTimeMillis(MIN_EVICTABLE_IDLE_TIME_MILLIS);
            p.setMinIdle(MIN_IDLE);
            p.setLogAbandoned(true);
            p.setRemoveAbandoned(true);
            p.setJdbcInterceptors("org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;"+
                    "org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer");
            datasource = new DataSource();
            datasource.setPoolProperties(p);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public javax.sql.DataSource getDataSource() {
        return datasource;
    }
}
