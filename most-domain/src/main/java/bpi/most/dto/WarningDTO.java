package bpi.most.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * Warning DTO
 * @author robert.zach@tuwien.ac.at
 */
public class WarningDTO implements Serializable {
	private int warningID; //unique identifier
	private int errorCode;
	private int priority;
	private String dpName = null;
	private String description = null;
	private String toDo = null;
	private String source = null;
	private Date fromTime = null;
	private Date toTime = null;
	
	public WarningDTO(int warningID, int errorCode, int priority,
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


    public int getWarningID() {
        return warningID;
    }

    public void setWarningID(int warningID) {
        this.warningID = warningID;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getDpName() {
        return dpName;
    }

    public void setDpName(String dpName) {
        this.dpName = dpName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getToDo() {
        return toDo;
    }

    public void setToDo(String toDo) {
        this.toDo = toDo;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Date getFromTime() {
        return fromTime;
    }

    public void setFromTime(Date fromTime) {
        this.fromTime = fromTime;
    }

    public Date getToTime() {
        return toTime;
    }

    public void setToTime(Date toTime) {
        this.toTime = toTime;
    }
}
