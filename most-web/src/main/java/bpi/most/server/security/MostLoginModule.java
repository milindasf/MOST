package bpi.most.server.security;

import bpi.most.server.services.User;
import bpi.most.service.api.AuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.security.auth.Subject;
import javax.security.auth.callback.*;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;
import java.io.IOException;
import java.util.Map;
 
/**
 * 
 * http://tomcat.apache.org/tomcat-6.0-doc/realm-howto.html#JAASRealm
 * 
 * Login module that delegates authentication to the {@link AuthenticationService}.
 * this module can be used as JAASRealm on tomcat servers for http authentication.
 */
public class MostLoginModule implements LoginModule {
 
	private static final Logger LOG = LoggerFactory.getLogger(MostLoginModule.class);
	
	public static final String ROLE = "mostuser";
	
    @Inject
    private AuthenticationService authenticationService;

    /** Callback handler to store between initialization and authentication. */
    private CallbackHandler handler;
 
    /** Subject to store. */
    private Subject subject;
 
    /** Logged in user. */
    private User user;
    
    /**
     * This implementation always return false.
     *
     * @see javax.security.auth.spi.LoginModule#abort()
     */
    @Override
    public boolean abort() throws LoginException {
 
        return false;
    }
 
    /**
     * This is where, should the entire authentication process succeeds,
     * principal would be set.
     *
     * @see javax.security.auth.spi.LoginModule#commit()
     */
    @Override
    public boolean commit() throws LoginException {
 
        try {
 
            MostUserPrincipal userPrincpl = new MostUserPrincipal(user);
            MostRolePrincipal rolePrincpl = new MostRolePrincipal(ROLE);
 
            subject.getPrincipals().add(userPrincpl);
            subject.getPrincipals().add(rolePrincpl);
 
            return true;
 
        } catch (Exception e) {
 
            throw new LoginException(e.getMessage());
        }
    }
 
    /**
     * This implementation ignores both state and options.
     *
     * @see javax.security.auth.spi.LoginModule#initialize(javax.security.auth.Subject,
     *      javax.security.auth.callback.CallbackHandler, java.util.Map,
     *      java.util.Map)
     */
    @SuppressWarnings("rawtypes")
	@Override
    public void initialize(Subject aSubject, CallbackHandler aCallbackHandler, Map aSharedState, Map aOptions) {
 
        handler = aCallbackHandler;
        subject = aSubject;
    }
 
    /**
     * This method checks whether the name and the password are the same.
     *
     * @see javax.security.auth.spi.LoginModule#login()
     */
    @Override
    public boolean login() throws LoginException {
 
        Callback[] callbacks = new Callback[2];
        callbacks[0] = new NameCallback("login");
        callbacks[1] = new PasswordCallback("password", true);
 
        try {
 
            handler.handle(callbacks);
 
            String name = ((NameCallback) callbacks[0]).getName();
            String password = String.valueOf(((PasswordCallback) callbacks[1]).getPassword());
 
            //ask database for authentication
            boolean isValid = authenticationService.isValidPassword(name, password);
            if (!isValid) {
 
                throw new LoginException("Authentication failed");
            }
 
            user = new User(name);
            
            LOG.info("--> authenticated user " + user.getUserName());
 
            return true;
 
        } catch (IOException e) {
 
            throw new LoginException(e.getMessage());
 
        } catch (UnsupportedCallbackException e) {
 
            throw new LoginException(e.getMessage());
        }
    }
 
    /**
     * Clears subject from principal and credentials.
     *
     * @see javax.security.auth.spi.LoginModule#logout()
     */
    @Override
    public boolean logout() throws LoginException {
 
        try {
 
            MostUserPrincipal userPrincpl = new MostUserPrincipal(user);
            MostRolePrincipal rolePrincpl = new MostRolePrincipal("ROLE");
 
            subject.getPrincipals().remove(userPrincpl);
            subject.getPrincipals().remove(rolePrincpl);
 
            return true;
 
        } catch (Exception e) {
 
            throw new LoginException(e.getMessage());
        }
    }
}