package bpi.most.obix.contracts;

import bpi.most.obix.objects.DpData;
import bpi.most.obix.objects.List;
import bpi.most.obix.objects.Str;

/**
 * Equivalent to DpDTO.
 *
 * @author Alexej Strelzow
 */
public interface Datapoint extends Point {

    /**
     * @return The name of the data point as {@link Str}
     */
    Str getDatapointName();

//	/**
//	 * Sets the name of the data point.
//	 * @param datapointName The name of the data point as {@link Str} 
//	 */
//	void setDatapointName(Str datapointName);
//	
//	/**
//	 * Sets the name of the data point.
//	 * @param datapointName The name a string
//	 */
//	void setDatapointName(String datapointName);

    /**
     * @return The type of the data point as {@link Str}
     */
    Str getType();

//	/**
//	 * Sets the type of the data point.
//	 * @param type The type of the data point as {@link Str} 
//	 */
//	void setType(Str type);
//	
//	/**
//	 * Sets the type of the data point.
//	 * @param datapointName The type a string
//	 */
//	void setType(String type);

    /**
     * @return The description of the data point as {@link Str}
     */
    Str getDescription();

//	/**
//	 * Sets the description of the data point.
//	 * @param type The description of the data point as {@link Str} 
//	 */
//	void setDescription(Str description);
//	
//	/**
//	 * Sets the description of the data point.
//	 * @param type The description of the data point as string 
//	 */
//	void setDescription(String description);

    /**
     * @return A {@link List} of {@link DpData}, or <code>null</code>
     */
    List getDpData();

}
