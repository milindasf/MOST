package bpi.most.domain.connector;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * Finds {@link Connector} domain objects from a given {@link EntityManager}.
 *
 * @author Jakob Korherr
 */
public class ConnectorFinder {

    private static final Logger log = LoggerFactory.getLogger(ConnectorFinder.class);

    private final EntityManager em;

    public ConnectorFinder(EntityManager em) {
        this.em = em;
    }

    public List<Connector> findConnections() {
        final List<Connector> result = new LinkedList<Connector>();

        try {
            ((Session) em.getDelegate()).doWork(new Work() {
                @Override
                public void execute(Connection connection) throws SQLException {
                    CallableStatement cstmt = null;
                    ResultSet rs = null;
                    try {
                        cstmt = connection.prepareCall("{CALL getConnection(?,?,?,?,?)}");
                        cstmt.setString(1, null);
                        cstmt.setString(2, null);
                        cstmt.setString(3, null);
                        cstmt.setString(4, null);
                        cstmt.setString(5, null);
                        cstmt.execute();
                        rs = cstmt.getResultSet();
                        while (rs.next()) {
                            //set worst case values if null.
                            double min = rs.getDouble("min");
                            min = (rs.wasNull()) ? - Double.MAX_VALUE : min;
                            double max = rs.getDouble("max");
                            max = (rs.wasNull()) ? Double.MAX_VALUE : max;
                            double deadband = rs.getDouble("deadband");
                            deadband = (rs.wasNull()) ? 0.0 : deadband;
                            int sampleInterval = rs.getInt("sample_interval");
                            sampleInterval = (rs.wasNull()) ? 0 : sampleInterval; //0 means not used (null)!
                            int minSampleInterval = rs.getInt("sample_interval_min");
                            minSampleInterval = (rs.wasNull()) ? 0 : minSampleInterval;
                            //create DTO
                            result.add(new Connector(rs.getInt("number"),rs.getString("datapoint_name"), rs.getString("device_name"),
                                    rs.getString("connection_type"), rs.getString("connection_variables"),
                                    rs.getBoolean("writeable"), rs.getString("vendor"), rs.getString("model"), rs.getString("unit"),
                                    rs.getString("type"), min, max, deadband, sampleInterval, minSampleInterval ));
                        }
                    } finally {
                        if (rs != null) {
                            rs.close();
                        }
                        if (cstmt != null) {
                            cstmt.close();
                        }
                    }
                }
            });
        }
        catch (HibernateException e) {
            log.error("An exception occurred while calling stored procedure 'getConnection'", e);
        }
        return result;
    }

}
