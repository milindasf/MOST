package bpi.most.service.impl;

import bpi.most.service.api.AuthenticationService;
import bpi.most.service.impl.utils.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import java.util.List;

/**
 * Implementation of {@link AuthenticationService}.
 * 
 * @author Jakob Korherr
 */
@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private static final Logger LOG = LoggerFactory.getLogger(AuthenticationServiceImpl.class);

    @PersistenceContext(unitName = "most")
    private EntityManager em;

    @Override
    @Transactional
    public boolean isValidPassword(String userName, String plainPassword) {
        byte[] pwHash = null;
        try {
            Query query = em.createQuery("select u.password from User u where u.name = :userName");
            query.setParameter("userName", userName);
            query.setMaxResults(1);

            @SuppressWarnings("unchecked")
            List<byte[]> pwHashList = (List<byte[]>) query.getResultList();
            if (!pwHashList.isEmpty()) {
                pwHash = pwHashList.get(0);
            }
        } catch (PersistenceException e) {
        	LOG.warn("PersistenceException while retrieving password for user " + userName, e);
            return false;
        }

        if (pwHash != null) {
            return BCrypt.checkpw(plainPassword, new String(pwHash));
        }
        return false;
    }

}
