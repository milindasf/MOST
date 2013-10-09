package bpi.most.preproc.generate;

import java.io.Serializable;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: nikunj
 * Date: 14/9/13
 * Time: 5:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class CalcAverageData implements Serializable {

    private static final long serialVersionUID = 1L;

    private Date timestamp = null;
    private Double value = null;
    private int timeduaration = 0;	//default value is 1

    public CalcAverageData()
    {
        super();
    }

    public CalcAverageData(Double value, Date timestamp) {
        this.value = value;
        this.timestamp = timestamp;
    }

    public CalcAverageData( Double value,Date timestamp, int timeduaration) {
        this.timestamp = timestamp;
        this.value = value;
        this.timeduaration = timeduaration;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public Double getValue() {
        return value;
    }

    public int getTimeduaration() {
        return timeduaration;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public void setTimeduaration(int timeduaration) {
        this.timeduaration = timeduaration;
    }
}
