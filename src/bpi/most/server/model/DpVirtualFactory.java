package bpi.most.server.model;


/**
 * POJO used for virtual dp instantiation
 */
public abstract class DpVirtualFactory {

	/**
	 * @param virtualDpId String ID of the requested VirtualDatapoint, e.g. EnergyOfZone, PeopleInZone, etc.
	 * @return returns a Datapoint object or null if the requested type (string id) is not support
	 */
	public abstract Datapoint getVirtualDp(String virtualDpId, String dpName);
	 
}
