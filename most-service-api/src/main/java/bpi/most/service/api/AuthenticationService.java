package bpi.most.service.api;

/**
 * Interface Specification of Authentication Service.
 * 
 * @author Jakob Korherr
 */
public interface AuthenticationService {
    
    public boolean isValidPassword(String userName, String plainPassword);

}
