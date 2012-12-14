package bpi.most.service.impl;

import bpi.most.domain.Connector;
import bpi.most.domain.User;
import bpi.most.service.api.ConnectorService;
import bpi.most.service.impl.db.DbPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * Implementation of {ConnectorService}.
 *
 * @author robert.zach@tuwien.ac.at
 * @author Lukas Weichselbaum
 */
@Service
public class ConnectorServiceImpl implements ConnectorService {

    private static final Logger log = LoggerFactory.getLogger(ConnectorServiceImpl.class);

    @Inject
    private DbPool dbPool;

    @Override
    public List<Connector> getConnection(User user) {
        return getConnection();
    }

    /**
     * @return all connections
     */
    public List<Connector> getConnection() {
        List<Connector> result = new LinkedList<Connector>();
        Connection connection = null;
        CallableStatement cstmt = null;
        ResultSet rs = null;

        try {
            connection = dbPool.getDataSource().getConnection();
            cstmt = connection.prepareCall("{CALL getConnection(?,?,?,?,?)}");  //TODO jakl: check if we should use JPA instead
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
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            try {
                if (rs != null)
                    rs.close();
                if (cstmt != null)
                    cstmt.close();
                if (connection != null)
                    connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
