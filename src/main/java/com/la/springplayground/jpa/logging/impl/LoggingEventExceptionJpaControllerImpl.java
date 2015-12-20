/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.la.springplayground.jpa.logging.impl;

import com.la.springplayground.jpa.logging.LoggingEventExceptionJpaController;
import com.la.springplayground.entity.logging.LoggingEventException;
import com.la.springplayground.jpa.impl.GenericJpaControllerImpl;
import javax.annotation.Resource;
import javax.inject.Named;
import javax.persistence.*;
import javax.transaction.UserTransaction;
import org.springframework.context.annotation.Scope;

/**
 *
 * @author Luís Alves
 */
@Named("loggingEventExceptionJpa")
@Scope("session")
public class LoggingEventExceptionJpaControllerImpl extends GenericJpaControllerImpl<LoggingEventException> implements LoggingEventExceptionJpaController {

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

    public LoggingEventExceptionJpaControllerImpl() {
        super(LoggingEventException.class);
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
