package bpi.most.service.impl.datapoint.virtual;

import javax.persistence.EntityManager;

import bpi.most.domain.datapoint.Datapoint;


/**
 * POJO used for virtual dp instantiation
 */
public abstract class VirtualDatapointFactory {

	/**
	 * @param virtualDpId String ID of the requested VirtualDatapoint, e.g. EnergyOfZone, PeopleInZone, etc.
	 * @return returns a Datapoint object or null if the requested type (string id) is not support
	 */
	public abstract VirtualDatapoint getVirtualDp(Datapoint dpEntity, EntityManager em);

    /**
     * returns the name of the virtual datapoint the implementation provides
     * @return the virtual datapoint type. this correlates with the "virtual" column in the database.
     */
    public abstract String getVirtualType();
}
