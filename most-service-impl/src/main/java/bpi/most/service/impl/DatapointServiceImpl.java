package bpi.most.service.impl;

import bpi.most.domain.datapoint.DatapointVO;
import bpi.most.service.api.DatapointService;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Implementation of {@link bpi.most.service.api.DatapointService}.
 *
 * @author Lukas Weichselbaum
 */
@Service
public class DatapointServiceImpl implements DatapointService {

    private static final Logger log = LoggerFactory.getLogger(DatapointServiceImpl.class);

    @PersistenceContext(unitName = "most")
    private EntityManager em;

    @Override
    public boolean isValidDp(final String dpName) {
        final boolean[] result = new boolean[]{false};  // must use an array in order to be able to access it from inner class
        try {
            ((Session) em.getDelegate()).doWork(new Work() {
                @Override
                public void execute(Connection connection) throws SQLException {
                    CallableStatement cstmt = null;
                    ResultSet rs = null;

                    try {
                        cstmt = connection.prepareCall("{CALL getDatapoint(?)}");
                        cstmt.setString(1, dpName);
                        cstmt.execute();
                        rs = cstmt.getResultSet();
                        if (rs.first()) {
                            // Datapoint exists
                            result[0] = true;
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
            log.error("An exception occurred while calling stored procedure 'getDatapoint'", e);
        }
        return result[0];
    }

    @Override
    public List<DatapointVO> getDatapoints() {
        return null;  // TODO implement
    }

    @Override
    public List<DatapointVO> getDatapoints(String searchstring) {
        return null;   // TODO implement
    }

    @Override
    public List<DatapointVO> getDatapoints(String searchstring, String zone) {
        return null;   // TODO implement
    }

}
