/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.la.springplayground.jpa.logging.impl;


import com.la.springplayground.entity.logging.LoggingEvent;
import com.la.springplayground.jpa.impl.GenericJpaControllerImpl;
import com.la.springplayground.jpa.logging.LoggingEventJpaController;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;
import org.springframework.stereotype.Component;

/**
 *
 * @author Luís Alves
 */
@Component("loggingEventJpa")
public class LoggingEventJpaControllerImpl extends GenericJpaControllerImpl<LoggingEvent> implements LoggingEventJpaController {

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

    public LoggingEventJpaControllerImpl() {
        super(LoggingEvent.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    protected UserTransaction getUserTransaction() {
        return userTx;
    }

  
}
