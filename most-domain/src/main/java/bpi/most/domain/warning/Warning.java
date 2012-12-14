/* TODO: Refactor! */

package bpi.most.domain.warning;

import java.util.Date;

/**
 * Warning DTO
 * @author robert.zach@tuwien.ac.at
 */
public class Warning {
	int warningID; //unique identifier
	int errorCode;
	int priority;
	String dpName = null;
	String description = null;
	String toDo = null;
	String source = null;
	Date fromTime = null;
	Date toTime = null;
	
	public Warning(int warningID, int errorCode, int priority,
                   String dpName, String description, String toDo, String source,
                   Date fromTime, Date toTime) {
		super();
		this.warningID = warningID;
		this.errorCode = errorCode;
		this.priority = priority;
		this.dpName = dpName;
		this.description = description;
		this.toDo = toDo;
		this.source = source;
		this.fromTime = fromTime;
		this.toTime = toTime;
	}

}
