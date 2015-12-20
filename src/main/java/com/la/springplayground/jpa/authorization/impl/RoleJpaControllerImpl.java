/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.la.springplayground.jpa.authorization.impl;

import com.la.springplayground.jpa.authorization.RoleJpaController;
import com.la.springplayground.entity.Permission;
import com.la.springplayground.entity.Role;
import com.la.springplayground.entity.User;
import com.la.springplayground.jpa.impl.GenericJpaControllerImpl;
import com.la.springplayground.jpa.exceptions.NonexistentEntityException;
import com.la.springplayground.jpa.exceptions.PreexistingEntityException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.annotation.Resource;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.UserTransaction;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 *
 * @author Luís Alves
 */
@Component("roleJpa")
public class RoleJpaControllerImpl extends GenericJpaControllerImpl<Role> implements RoleJpaController<Role> {

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


    public RoleJpaControllerImpl() {
        super(Role.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    protected UserTransaction getUserTransaction() {
        return userTx;
    }

    public void create(Role role) throws PreexistingEntityException, Exception {
        if (role.getPermissionCollection() == null) {
            role.setPermissionCollection(new ArrayList<Permission>());
        }
        if (role.getUserCollection() == null) {
            role.setUserCollection(new ArrayList<User>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Permission> attachedPermissionCollection = new ArrayList<Permission>();
            for (Permission permissionCollectionPermissionToAttach : role.getPermissionCollection()) {
                permissionCollectionPermissionToAttach = em.getReference(permissionCollectionPermissionToAttach.getClass(), permissionCollectionPermissionToAttach.getId());
                attachedPermissionCollection.add(permissionCollectionPermissionToAttach);
            }
            role.setPermissionCollection(attachedPermissionCollection);
            Collection<User> attachedUserCollection = new ArrayList<User>();
            for (User userCollectionUserToAttach : role.getUserCollection()) {
                userCollectionUserToAttach = em.getReference(userCollectionUserToAttach.getClass(), userCollectionUserToAttach.getId());
                attachedUserCollection.add(userCollectionUserToAttach);
            }
            role.setUserCollection(attachedUserCollection);
            em.persist(role);
            for (Permission permissionCollectionPermission : role.getPermissionCollection()) {
                permissionCollectionPermission.getRoleCollection().add(role);
                permissionCollectionPermission = em.merge(permissionCollectionPermission);
            }
            for (User userCollectionUser : role.getUserCollection()) {
                userCollectionUser.getRoleCollection().add(role);
                userCollectionUser = em.merge(userCollectionUser);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findRole(role.getId()) != null) {
                throw new PreexistingEntityException("Role " + role + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Role role) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Role persistentRole = em.find(Role.class, role.getId());
            Collection<Permission> permissionCollectionOld = persistentRole.getPermissionCollection();
            Collection<Permission> permissionCollectionNew = role.getPermissionCollection();
            Collection<User> userCollectionOld = persistentRole.getUserCollection();
            Collection<User> userCollectionNew = role.getUserCollection();
            Collection<Permission> attachedPermissionCollectionNew = new ArrayList<Permission>();
            for (Permission permissionCollectionNewPermissionToAttach : permissionCollectionNew) {
                permissionCollectionNewPermissionToAttach = em.getReference(permissionCollectionNewPermissionToAttach.getClass(), permissionCollectionNewPermissionToAttach.getId());
                attachedPermissionCollectionNew.add(permissionCollectionNewPermissionToAttach);
            }
            permissionCollectionNew = attachedPermissionCollectionNew;
            role.setPermissionCollection(permissionCollectionNew);
            Collection<User> attachedUserCollectionNew = new ArrayList<User>();
            for (User userCollectionNewUserToAttach : userCollectionNew) {
                userCollectionNewUserToAttach = em.getReference(userCollectionNewUserToAttach.getClass(), userCollectionNewUserToAttach.getId());
                attachedUserCollectionNew.add(userCollectionNewUserToAttach);
            }
            userCollectionNew = attachedUserCollectionNew;
            role.setUserCollection(userCollectionNew);
            role = em.merge(role);
            for (Permission permissionCollectionOldPermission : permissionCollectionOld) {
                if (!permissionCollectionNew.contains(permissionCollectionOldPermission)) {
                    permissionCollectionOldPermission.getRoleCollection().remove(role);
                    permissionCollectionOldPermission = em.merge(permissionCollectionOldPermission);
                }
            }
            for (Permission permissionCollectionNewPermission : permissionCollectionNew) {
                if (!permissionCollectionOld.contains(permissionCollectionNewPermission)) {
                    permissionCollectionNewPermission.getRoleCollection().add(role);
                    permissionCollectionNewPermission = em.merge(permissionCollectionNewPermission);
                }
            }
            for (User userCollectionOldUser : userCollectionOld) {
                if (!userCollectionNew.contains(userCollectionOldUser)) {
                    userCollectionOldUser.getRoleCollection().remove(role);
                    userCollectionOldUser = em.merge(userCollectionOldUser);
                }
            }
            for (User userCollectionNewUser : userCollectionNew) {
                if (!userCollectionOld.contains(userCollectionNewUser)) {
                    userCollectionNewUser.getRoleCollection().add(role);
                    userCollectionNewUser = em.merge(userCollectionNewUser);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Short id = role.getId();
                if (findRole(id) == null) {
                    throw new NonexistentEntityException("The role with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Short id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Role role;
            try {
                role = em.getReference(Role.class, id);
                role.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The role with id " + id + " no longer exists.", enfe);
            }
            Collection<Permission> permissionCollection = role.getPermissionCollection();
            for (Permission permissionCollectionPermission : permissionCollection) {
                permissionCollectionPermission.getRoleCollection().remove(role);
                permissionCollectionPermission = em.merge(permissionCollectionPermission);
            }
            Collection<User> userCollection = role.getUserCollection();
            for (User userCollectionUser : userCollection) {
                userCollectionUser.getRoleCollection().remove(role);
                userCollectionUser = em.merge(userCollectionUser);
            }
            em.remove(role);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Role> findRoleEntities() {
        return findRoleEntities(true, -1, -1);
    }

    public List<Role> findRoleEntities(int maxResults, int firstResult) {
        return findRoleEntities(false, maxResults, firstResult);
    }

    private List<Role> findRoleEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Role.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Role findRole(Short id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Role.class, id);
        } finally {
            em.close();
        }
    }

    public int getRoleCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Role> rt = cq.from(Role.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
