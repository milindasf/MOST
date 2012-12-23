package bpi.most.domain.datapoint;

import org.hibernate.Session;
import org.hibernate.transform.ResultTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import java.util.LinkedList;
import java.util.List;

import static bpi.most.infra.db.DbUtils.findIndex;
import static bpi.most.infra.db.DbUtils.prepareSearchParameter;

/**
 * Finds {@link DatapointVO}s from a given {@link javax.persistence.EntityManager}.
 *
 * @author Jakob Korherr
 */
public class DatapointFinder {

    private static final Logger LOG = LoggerFactory.getLogger(DatapointFinder.class);

    private final EntityManager em;

    public DatapointFinder(EntityManager em) {
        this.em = em;
    }

    public List<DatapointVO> getDatapoints(String searchstring) {
        LOG.debug("Fetching datapoints with searchstring {}", searchstring);
        // noinspection unchecked
        return ((Session) em.getDelegate()).createSQLQuery("{CALL getDatapoint(:searchstring)}")
                .setParameter("searchstring", prepareSearchParameter(searchstring))
                .setReadOnly(true)
                .setResultTransformer(new DatapointVOResultTransformer())
                .list();
    }

    public List<DatapointVO> getDatapoints(String searchstring, String zone) {
    	LOG.debug("Fetching datapoints with searchstring {} for zone {}", searchstring, zone);
        // noinspection unchecked
        return ((Session) em.getDelegate()).createSQLQuery("{CALL getDatapoint(:searchstring)}")
                .setParameter("searchstring", prepareSearchParameter(searchstring))
                .setReadOnly(true)
                .setResultTransformer(new DatapointVOZoneFilteringResultTransformer(zone))
                .list();
    }

    /**
     * Transforms a result of the stored procedure getDatapoint() into a {@link DatapointVO}.
     */
    private static class DatapointVOResultTransformer implements ResultTransformer {

        private boolean initialized = false;
        private int typeIndex;
        private int nameIndex;

        @Override
        public DatapointVO transformTuple(Object[] tuple, String[] aliases) {
            if (!initialized) {
                initializeIndexes(aliases);
            }
            return new DatapointVO((String) tuple[nameIndex], (String) tuple[typeIndex], (String) tuple[nameIndex]);
        }

        private void initializeIndexes(String[] aliases) {
            nameIndex = findIndex(aliases, "datapoint_name");
            typeIndex = findIndex(aliases, "type");
            initialized = true;
        }

        @Override
        public List transformList(List collection) {
            return collection;
        }

    }

    /**
     * Transforms a result of the stored procedure getDatapoint() into a {@link DatapointVO}
     * after matching a zone filter.
     */
    private static final class DatapointVOZoneFilteringResultTransformer extends DatapointVOResultTransformer {

        private boolean initialized = false;
        private int zoneIdZoneIndex;
        private final String zone;

        private DatapointVOZoneFilteringResultTransformer(String zone) {
            this.zone = zone;
        }

        @Override
        public DatapointVO transformTuple(Object[] tuple, String[] aliases) {
            if (!initialized) {
                zoneIdZoneIndex = findIndex(aliases, "zone_idzone");
                initialized = true;
            }

            // TODO not yet implementet in the way we want it to use; as for now this is an example
            // TODO: compare Zone objects!! not String with zone id
            if (zone.equals(String.valueOf(tuple[zoneIdZoneIndex]))) {
                return super.transformTuple(tuple, aliases);
            }
            return null; // will be filtered by transformList()
        }

        @Override
        public List transformList(List collection) {
            // remove null entries in list
            List<Object> list = new LinkedList<Object>();
            for (Object o : collection) {
                if (o != null) {
                    list.add(o);
                }
            }
            return list;
        }
    }

}
