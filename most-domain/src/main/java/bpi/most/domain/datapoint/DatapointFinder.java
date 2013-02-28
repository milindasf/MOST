package bpi.most.domain.datapoint;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.transform.ResultTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.sql.rowset.spi.TransactionalWriter;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

import static bpi.most.infra.db.DbUtils.findIndex;
import static bpi.most.infra.db.DbUtils.prepareSearchParameter;

/**
 * Finds {@link DatapointVO}s from a given {@link javax.persistence.EntityManager}.
 *
 * @author Jakob Korherr
 * @author Lukas Weichselbaum
 */
public class DatapointFinder {

    private static final Logger LOG = LoggerFactory.getLogger(DatapointFinder.class);

    private final EntityManager em;

    public DatapointFinder(EntityManager em) {
        this.em = em;
    }


    public DatapointVO getDatapoint(String dpName) {
        LOG.debug("Fetching datapoint: {}", dpName);
        // noinspection unchecked
        Session tempses = ((Session) em.getDelegate());
        List<DatapointVO> result = tempses.createSQLQuery("{CALL getDatapoint(:name)}")
                .setParameter("name", dpName)
                .setReadOnly(true)
                .setResultTransformer(new DatapointVOResultTransformer())
                .list();

        if(result.size() > 0){
//            LOG.debug("Found datapoint: {}", dpName);
            return result.get(0);
        }

        return null;
    }

    public Datapoint getDatapointEntity(String dpName) {
        LOG.debug("Fetching datapoint: {}", dpName);
        // noinspection unchecked
        List<Datapoint> result = ((Session) em.getDelegate()).createSQLQuery("{CALL getDatapoint(:name)}")
                .setParameter("name", dpName)
                .setReadOnly(true)
                .setResultTransformer(new DatapointResultTransformer())
                .list();

        if (result.size() > 0) {
            return result.get(0);
        }

        return null;
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
     * Datapoints in a sub zone.
     *
     * @param zoneId    Unique ID of the zone
     * @param sublevels Level of the subzones
     * @return List of Datapoints in the sub zone, null if empty
     */
    public List<DatapointVO> getDpFromSubZones(int zoneId, int sublevels) {
        //get zone info from db
        LOG.debug("Fetching datpoints form sub zone: {}, sublevels: {}", zoneId, sublevels);
        try {
            // noinspection unchecked
            List<DatapointVO> zoneList = ((Session) em.getDelegate()).createSQLQuery("{CALL getDatapointsInSubzones(:p,:level)}")
                    .setParameter("p", zoneId)
                    .setParameter("level", sublevels)
                    .setReadOnly(true)
                    .setResultTransformer(new DatapointVOResultTransformer())
                    .list();

            if (zoneList.size() == 0) {
                return null;
            } else {
                return zoneList;
            }
        } catch (HibernateException e) {
            LOG.warn(e.getMessage());
            return null;
        }
    }

    /**
     * Transforms a result of the stored procedure getDatapoint() into a {@link DatapointVO}.
     */
    private static class DatapointVOResultTransformer implements ResultTransformer {

        private boolean initialized = false;
        private int typeIndex;
        private int nameIndex;
        private int virtualIndex;

        @Override
        public DatapointVO transformTuple(Object[] tuple, String[] aliases) {
            if (!initialized) {
                initializeIndexes(aliases);
            }
            return new DatapointVO((String) tuple[nameIndex], (String) tuple[typeIndex], (String) tuple[nameIndex], (String) tuple[virtualIndex]);
        }

        private void initializeIndexes(String[] aliases) {
            nameIndex = findIndex(aliases, "datapoint_name");
            typeIndex = findIndex(aliases, "type");
            virtualIndex = findIndex(aliases, "virtual");
            initialized = true;
        }

        @Override
        public List transformList(List collection) {
            return collection;
        }

    }

    //TODO ASE

    /**
     * Transforms a result of the stored procedure getDatapoint() into a {@link DatapointVO}.
     */
    private static class DatapointResultTransformer implements ResultTransformer {

        private boolean initialized = false;
        private int typeIndex;
        private int nameIndex;
        private int virtualIndex;
        private int unitIndex;
        private int minIndex;
        private int maxIndex;
        private int accuracyIndex;
        private int deadbandIndex;
        private int sample_intervalIndex;
        private int sample_interval_minIndex;
        private int watchdogIndex;
        private int custom_attrIndex;
        private int descriptionIndex;
        private int zone_idzoneIndex;

        @Override
        public Datapoint transformTuple(Object[] tuple, String[] aliases) {
            if (!initialized) {
                initializeIndexes(aliases);
            }
            Datapoint dp = new Datapoint();
            dp.setName((String) tuple[nameIndex]);
            dp.setType((String) tuple[typeIndex]);
            dp.setVirtual((String) tuple[virtualIndex]);
            dp.setUnit((String) tuple[unitIndex]);
            dp.setMin((BigDecimal) tuple[minIndex]);
            dp.setMax((BigDecimal) tuple[maxIndex]);
            dp.setAccuracy((BigDecimal) tuple[accuracyIndex]);
            dp.setDeadband((BigDecimal) tuple[deadbandIndex]);
            dp.setSample_interval((BigDecimal) tuple[sample_intervalIndex]);
            dp.setSample_interval_min((BigDecimal) tuple[sample_interval_minIndex]);
            dp.setWatchdog((BigDecimal) tuple[watchdogIndex]);
            dp.setCustom_attr((String) tuple[custom_attrIndex]);
            dp.setDescription((String) tuple[descriptionIndex]);
            dp.setZone_idzone((Integer) tuple[zone_idzoneIndex]);
            return dp;
        }

        private void initializeIndexes(String[] aliases) {
            nameIndex = findIndex(aliases, "datapoint_name");
            typeIndex = findIndex(aliases, "type");
            virtualIndex = findIndex(aliases, "virtual");
            unitIndex = findIndex(aliases, "unit");
            minIndex = findIndex(aliases, "min");
            maxIndex = findIndex(aliases, "max");
            accuracyIndex = findIndex(aliases, "accuracy");
            deadbandIndex = findIndex(aliases, "deadband");
            sample_intervalIndex = findIndex(aliases, "sample_interval");
            sample_interval_minIndex = findIndex(aliases, "sample_interval_min");
            watchdogIndex = findIndex(aliases, "watchdog");
            custom_attrIndex = findIndex(aliases, "custom_attr");
            descriptionIndex = findIndex(aliases, "description");
            zone_idzoneIndex = findIndex(aliases, "zone_idzone");
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
