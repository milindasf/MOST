package bpi.most.service.impl.utils;

import javax.persistence.PersistenceException;

/**
 * Utils for database access.
 *
 * @author Jakob Korherr
 */
public final class DbUtils {

    private DbUtils() {
        // no instantiation
    }

    public static String prepareSearchParameter(String searchParameter) {
        if (searchParameter == null) {
            return null;
        }
        else {
            return "%" + searchParameter + "%";
        }
    }

    public static int findIndex(String[] aliases, String columnName) {
        for (int i = 0; i < aliases.length; i++) {
            if (columnName.equals(aliases[i])) {
                return i;
            }
        }
        throw new PersistenceException("Could not find column " + columnName + " in database query result.");
    }


}
