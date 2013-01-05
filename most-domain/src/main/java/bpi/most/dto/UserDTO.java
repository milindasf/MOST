package bpi.most.dto;

import bpi.most.domain.datapoint.DatapointVO;
import bpi.most.domain.zone.Zone;

/**
 * Represents a user.
 *
 * @author Lukas Weichselbaum
 */
public class UserDTO {
    private String userName = null;

    public UserDTO(String userName) {
        super();
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    //TODO: implement, permissions are hierarchical! ro,rw,admin
    public boolean hasPermission(Zone zone, DpDTO.Permissions permissions) {
        return true;
    }
	
	//TODO: implement, permissions are hierarchical! ro,rw,admin
	public boolean hasPermission(DatapointVO dp, DpDTO.Permissions permissions) {
		return true;
	}
}
