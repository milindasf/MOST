package bpi.most.server.rpc.mock;

import bpi.most.client.rpc.AuthenticationService;
import bpi.most.shared.TestConstants;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * Mock of {@link AuthenticationService} for GWT test cases.
 *
 * @author Jakob Korherr
 */
public class MockGwtRpcAuthenticationService extends RemoteServiceServlet implements AuthenticationService {

    // private static final Logger log = LoggerFactory.getLogger(MockGwtRpcAuthenticationService.class);    

    @Override
    public boolean login(String userId, String plainPassword) {
        return (TestConstants.VALID_USER.equals(userId)
                && TestConstants.VALID_PASSWORD.equals(plainPassword));
    }

    @Override
    public boolean logout() {
        return true;  // TODO stub
    }

    @Override
    public boolean isValidSession(String sessionID) {
        return true;  // TODO stub
    }

    @Override
    public boolean hasModulePermissions(String userId, String moduleId) {
        return true; // TODO stub
    }

}
