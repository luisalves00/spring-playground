/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.la.springplayground.jpa.authorization.impl;

import com.la.springplayground.entity.User;
import com.la.springplayground.jpa.authorization.UserJpaController;
import com.la.springplayground.jpa.impl.GenericJpaControllerImpl;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;
import org.springframework.stereotype.Component;

/**
 *
 * @author Luís Alves
 */
@Component("userJpa")
public class UserJpaControllerImpl extends GenericJpaControllerImpl<User> implements UserJpaController<User> {

    /**
     * User transaction manager
     */
    @Resource(name="AtomikosUserTransaction")
    private UserTransaction userTx;
    /**
     * Entity manager
     */
    @PersistenceContext(unitName = GenericJpaControllerImpl.PERSICTENCE_UNIT)
    private EntityManager em;

    public UserJpaControllerImpl() {
        super(User.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    protected UserTransaction getUserTransaction() {
        return userTx;
    }
    
    /**
     * Find by username, i.e. email
     * 
     * @return 
     */
    public User findByUsername(String userName){
        Map<String, Object> params = new HashMap<String, Object>(1);
        params.put("email",userName);
        return (User) fetchSingleResultFromNamedQuery(User.class,"User.findByEmail", params);
    }
}
