package bpi.most.infra.db;

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
	
	/**
	 * processes additional attributes defined in custom_attr. e.g.
	 * "myvariable1 value1; myvariable2 value2;"
	 * 
	 * @param allAttr
	 *            value of db column custom_attr
	 * @param requestedAttrName
	 *            attribute e.g. "myvariable1"
	 * @return value of attribute (as string), "" if nothing found
	 */
	public static String getCustomAttr(String allAttr, String requestedAttrName) {
		String myResult = "";
		String myCustomAttr = null;
		int myFound = 0;

		myCustomAttr = allAttr;
		if (myCustomAttr != null) {
			// split multiple Attributes, ex.
			// "myvariable1 someValue; myvariable2 someValue"
			for (String oneAttr : myCustomAttr.split(";")) {
				// split one Attribute definition, ex. "myvariable1 someValue"
				for (String myVariable : oneAttr.split(" ")) {
					// rebuild string
					if (myFound > 1) {
						myResult = myResult + " " + myVariable;
						myFound++;
					}
					if (myFound == 1) {
						// to prevent first " "
						myResult = myVariable;
						myFound++;
					}
					if (myFound == 0 && myVariable.equals(requestedAttrName)) {
						myFound = 1;
					}
				}
				myFound = 0;
			}
		}
		return myResult;
	}
}
