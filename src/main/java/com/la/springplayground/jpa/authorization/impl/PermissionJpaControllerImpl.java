/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.la.springplayground.jpa.authorization.impl;

import com.la.springplayground.jpa.authorization.PermissionJpaController;
import com.la.springplayground.entity.Permission;
import com.la.springplayground.jpa.impl.GenericJpaControllerImpl;
import java.util.Collection;
import java.util.Map;
import javax.annotation.Resource;
import javax.inject.Named;
import javax.persistence.*;
import javax.transaction.UserTransaction;
import org.primefaces.model.SortOrder;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 *
 * @author Luís Alves
 */
@Component("permissionJpa")
public class PermissionJpaControllerImpl extends GenericJpaControllerImpl<Permission> implements PermissionJpaController<Permission> {

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

    public PermissionJpaControllerImpl() {
        super(Permission.class);
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
