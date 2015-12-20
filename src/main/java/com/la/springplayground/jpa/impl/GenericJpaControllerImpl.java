/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.la.springplayground.jpa.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.UserTransaction;

import org.primefaces.model.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.la.springplayground.entity.EntityBase;
import com.la.springplayground.jpa.GenericJpaController;
import com.la.springplayground.jpa.exceptions.NonexistentEntityException;
import com.la.springplayground.jpa.exceptions.PreexistingEntityException;

/**
 * Facade to JpaControllers
 *
 * @author Luís Alves
 */
public abstract class GenericJpaControllerImpl<T extends EntityBase> implements GenericJpaController<T> {

    private static final Logger logger = LoggerFactory.getLogger(GenericJpaControllerImpl.class.getName());
    public static final String PERSICTENCE_UNIT = "AtMyExpense";
   
    /**
     * Entity class
     */
    private Class<T> entityClass;

    /**
     * Function retrieves entity manager
     *
     * @return entity manager
     */
    protected abstract EntityManager getEntityManager();

    protected abstract UserTransaction getUserTransaction();

    /**
     * Constructor
     *
     * @param entityClass
     */
    public GenericJpaControllerImpl(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    /**
     * Execute a named query
     * 
     * @param <T>
     * @param clazz
     * @param queryName
     * @param params
     * @return 
     */
    public <T> Collection<T> executeNamedQuery(Class<T> clazz, String queryName, Map<String,Object> params){
        EntityManager em = getEntityManager();
        try {
            Query q = em.createNamedQuery(queryName,clazz);
            for(Map.Entry<String,Object> entry : params.entrySet()){
                 q.setParameter(entry.getKey(), entry.getValue());
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }
    
    /**
     * Fetch single result from named query
     * @param <T>
     * @param clazz
     * @param queryName
     * @param params
     * @return 
     */
    public <T> Object fetchSingleResultFromNamedQuery(Class<T> clazz, String queryName, Map<String,Object> params){
        EntityManager em = getEntityManager();
        try {
            Query q = em.createNamedQuery(queryName,clazz);
            for(Map.Entry<String,Object> entry : params.entrySet()){
                 q.setParameter(entry.getKey(), entry.getValue());
            }
            return q.getSingleResult();
        } finally {
            em.close();
        }
    }
    
    
    /**
     * Create db operation
     *
     * @param entity
     * @throws PreexistingEntityException
     * @throws Exception
     */
    public void create(T entity) throws PreexistingEntityException, Exception {
        EntityManager em = getEntityManager();
        try {
            em.persist(entity);
        } catch (Exception ex) {
            if (find(entity.getId()) != null) {
                throw new PreexistingEntityException("Entity " + entity + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    /**
     * Update db operation
     *
     * @param entity
     * @throws NonexistentEntityException
     * @throws Exception
     */
    public void edit(T entity) throws NonexistentEntityException, Exception {
        EntityManager em = getEntityManager();
        try {
            em.merge(entity);
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                if (find(entity.getId()) == null) {
                    throw new NonexistentEntityException("The T with id " + entity.getId() + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    /**
     * Delete db operation
     *
     * @param entity
     * @throws NonexistentEntityException
     */
    public void destroy(T entity) throws Exception {
        EntityManager em = getEntityManager();
        try {
            try {
                entity = em.getReference(entityClass, entity.getId());
                entity.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The T with id " + entity.getId() + " no longer exists.", enfe);
            }
            em.remove(getEntityManager().merge(entity));
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
    
    /**
     * Delete db operation
     *
     * @param id
     * @throws NonexistentEntityException
     */
    public void destroyById(Object id) throws Exception {
        EntityManager em = getEntityManager();
        T entity = null;
        try {      
            try {
                entity = em.getReference(entityClass, id);
                entity.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The T with id " + entity.getId() + " no longer exists.", enfe);
            }
            em.remove(getEntityManager().merge(entity));
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
    
    /**
     * Function find entity by primary key
     * @param id - primary key object
     * @return found entity
     */
    public T find(Object id)
    {
        return getEntityManager().find(entityClass, id);
    }
    
    /**
     * Function find all entities
     * @return list of entities
     */
    public List<T> findAll()
    {
        return findAll(true, -1, -1);
    }

    /**
     * Find with boundaries
     *
     * @param maxResults pageSize
     * @param firstResult offset
     * @return
     */
    public List<T> findAll(int maxResults, int firstResult) {
        return findAll(false, maxResults, firstResult);
    }

    /**
     * Find all or with boundaries
     *
     * @param all if true return all, otherwise return based on the setted
     * boundaries
     * @param maxResults pageSize
     * @param firstResult offset
     * @return
     */
    public List<T> findAll(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaBuilder builder = em.getCriteriaBuilder();
            CriteriaQuery cq = builder.createQuery(entityClass);
            Root gaRoot = cq.from(entityClass);
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

    /**
     * find entities in a lazy way, using Primefaces dataTables with
     * lazyLoading.
     *
     * @param firstResult offset
     * @param maxResults pageSize
     * @param sortField the field to sort
     * @param sortOrder sort (off,asc,desc)
     * @param filters filters using a LIKE query (only supported by now)
     * @param defaultSortColumn if no column is selected to sort use this as
     * default
     * @return List<Entity> with this restictions.
     */
    public Collection<T> findEntitiesLazy(int firstResult, int maxResults, String sortField, SortOrder sortOrder, Map<String, Object> filters, Map<String, SortOrder> defaultSortColumns){
        EntityManager em = getEntityManager();

        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery cq = cb.createQuery();
            Root<T> from = cq.from(entityClass);
            CriteriaQuery select = cq.select(from);
            select.distinct(true);

            if (sortField != null && !sortField.isEmpty()) {
                sorter(cb, from, select, sortField, sortOrder);
            } else {
                if (defaultSortColumns != null) {
                    for (Map.Entry<String, SortOrder> entry : defaultSortColumns.entrySet()) {
                        //test if this works for multiple order.
                        sorter(cb, from, select, entry.getKey(), entry.getValue());
                    }
                }
                //else don't use any order by
            }

            if (!filters.isEmpty()) {
                addFilters(cb, from, select, filters);
            }
            TypedQuery q = em.createQuery(select);

            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);

            return q.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Helper method to add the the LazyLoadingModel query the sort field and
     * order
     *
     * @param cb a query builder
     * @param from
     * @param select
     * @param sortField the field to sort (fieldName or entity.fieldName, the
     * second uses a join query)
     * @param sortOrder the oreder to sort (true -> asc, false -> desc)
     */
    protected void sorter(CriteriaBuilder cb, Root<T> from, CriteriaQuery select, String sortField, SortOrder sortOrder) {
        if (sortField.indexOf('.') == -1) {
            if (sortOrder == SortOrder.ASCENDING) {
                select.orderBy(cb.asc(from.get(sortField)));
            } else  if (sortOrder == SortOrder.DESCENDING){
                select.orderBy(cb.desc(from.get(sortField)));
            }
        } else {
            String[] fields = sortField.split("\\.");

            if (fields.length == 2) {
                /*
                 * Create subquery to find by attribute
                 */
                //Path attr = from.get(sortField);

                Join join = from.join(fields[0], JoinType.LEFT);

                //select.selection(new Selection());

                if (sortOrder == SortOrder.ASCENDING) {
                    select.orderBy(cb.asc(join.get(fields[1])));
                } else if(sortOrder == SortOrder.DESCENDING){
                    select.orderBy(cb.desc(join.get(fields[1])));
                }

            } else {
                logger.warn("Currently only on level of Join is suported! "
                        + "Your p:dataTable is trying to use 2 or higher");
            }
        }
    }

    /**
     * This is an helper method to construct the query filters of a
     * LazyLoadingModel. Look at http://code.google.com/p/jpa-primefaces-test/
     * for some inspiration
     *
     * @param cb criteria builder
     * @param from
     * @param select
     * @param filters a Map of filters. Note that currently we only support 1
     * level of join
     */
    protected void addFilters(CriteriaBuilder cb, Root<T> from, CriteriaQuery select, Map<String, Object> filters) {
        List<Predicate> predicateList = returnFilters(cb, from, select, filters);
        Predicate[] predicates = new Predicate[predicateList.size()];
        predicateList.toArray(predicates);
        select.where(predicates);
    }

    protected List<Predicate> returnFilters(CriteriaBuilder cb, Root<T> from, CriteriaQuery select, Map<String, Object> filters) {
        if (filters != null) {
            Iterator<Entry<String, Object>> iterator = filters.entrySet().iterator();
            List<Predicate> predicateList = new ArrayList<Predicate>();

            while (iterator.hasNext()) {
                Entry<String, Object> entry = iterator.next();
                if (!entry.getValue().equals("")) {

                    if (entry.getKey().indexOf('.') == -1) {
                        //simple query: this is an entity attribute
                        if (entry.getValue().equals("true") || entry.getValue().equals("false")) {
                            Predicate p = cb.equal(from.<String>get(entry.getKey()), entry.getValue());
                            predicateList.add(p);
                        } else {
                            Predicate p = cb.like(cb.lower(from.<String>get(entry.getKey())), "%" + entry.getValue().toString().toLowerCase() + "%");
                            predicateList.add(p);
                        }
                    } else {
                        //This is not simple query because this field could become a reason for table join
                        String[] fields = entry.getKey().split("\\.");


                        if (fields.length == 2) {
                            //This is simple join. We can only deal wtih simple joins (for now)

                            /*
                             * Create subquery to find by attribute
                             */

                            Join join = from.join(fields[0], JoinType.LEFT);
                            //select.distinct(true);

                            Path joinTableAttributePath = join.get(fields[1]); //from.get(fields[0]).get(fields[1]);
                            //select.where(
                            if (entry.getValue().equals("true") || entry.getValue().equals("false")) {
                                Predicate p = cb.equal(from.<String>get(entry.getKey()), entry.getValue());
                                predicateList.add(p);
                            } else {
                                Predicate p = cb.like(cb.lower(joinTableAttributePath), "%" + entry.getValue().toString().toLowerCase() + "%");
                                predicateList.add(p);
                            }
                            //Subquery subquery = cq.subquery(attr.getJavaType());
                            //Root fromAttr = subquery.from(attr.getJavaType());
                            //subquery.select(fromAttr.get("id" + WordUtils.capitalize(fields[0])));
                            //subquery.where(cb.like(cb.lower(fromAttr.get(fields[1])), "%" + entry.getValue().toLowerCase() + "%"));
                            //predicateList.add(cb.in(from.get(fields[0]).get("id" + WordUtils.capitalize(fields[0]))).value(subquery));

                            //}

                        } else {
                            logger.warn("Currently only on level of Join is suported! "
                                    + "Your p:dataTable is trying to use 2 or higher");
                        }

                    }
                }
            }
            return predicateList;
        } else {
            return Collections.EMPTY_LIST;
        }
    }

    /**
     * Get total count Since p:dataTable uses a [ private int rowCount; ] the
     * method returns an int, wich allows only 2 147 483 647 (2^31-1) records.
     *
     * @return count
     */
    public Long getCount() {
        EntityManager em = getEntityManager();

        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<T> rt = cq.from(entityClass);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);

            return (Long) q.getSingleResult();
        } finally {
            em.close();
        }
    }

    /**
     * Get count with applied filters Since p:dataTable uses a [ private int
     * rowCount; ] the method returns an int, wich allows only 2 147 483 647
     * (2^31-1) records.
     *
     * @param filters table filters
     * @return count
     */
    public Long getCount(Map<String, Object> filters) {
        EntityManager em = getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery cq = cb.createQuery();
            Root<T> from = cq.from(entityClass);
            CriteriaQuery select = cq.select(cb.countDistinct(from));
            addFilters(cb, from, select, filters);
            TypedQuery q = em.createQuery(select);
            //System.out.println("count:" + q.getSingleResult());

            return (Long) q.getSingleResult();
        } finally {
            em.close();
        }
    }

}
