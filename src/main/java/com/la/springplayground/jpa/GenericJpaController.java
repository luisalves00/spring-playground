package com.la.springplayground.jpa;

import com.la.springplayground.jpa.exceptions.NonexistentEntityException;
import com.la.springplayground.jpa.exceptions.PreexistingEntityException;
import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import org.primefaces.model.SortOrder;

/**
 *
 * @author Luís Alves
 */
public interface GenericJpaController< T> extends Serializable {

    /**
     * Execute a named query
     *
     * @param <T>
     * @param clazz
     * @param queryName
     * @param params
     * @return
     */
    <T> Collection<T> executeNamedQuery(Class<T> clazz, String queryName, Map<String, Object> params);

    /**
     * Fetch single result from named query
     *
     * @param <T>
     * @param clazz
     * @param queryName
     * @param params
     * @return
     */
    <T> Object fetchSingleResultFromNamedQuery(Class<T> clazz, String queryName, Map<String, Object> params);

    /**
     * Create Entity
     *
     * @param t
     * @return
     */
    void create(T t) throws PreexistingEntityException, Exception;

    /**
     * Update entity
     *
     * @param t
     * @return
     */
    void edit(T t) throws NonexistentEntityException, Exception;

    /**
     * Destroy entity
     *
     * @param id
     */
    void destroy(T id) throws Exception;

    /**
     * Destroy entity
     *
     * @param id
     */
    void destroyById(Object id) throws Exception;

    /**
     * Function find entity by primary key
     *
     * @param id - primary key object
     * @return found entity
     */
    T find(Object id);

    /**
     * Function find all entities
     *
     * @return list of entities
     */
    Collection<T> findAll();

    /**
     * Find with boundaries
     *
     * @param maxResults pageSize
     * @param firstResult offset
     * @return
     */
    Collection<T> findAll(int maxResults, int firstResult);

    /**
     * Get total count Since p:dataTable uses a [ private int rowCount; ] the
     * method returns an int, wich allows only 2 147 483 647 (2^31-1) records.
     *
     * @return count
     */
    Long getCount();

    /**
     * *************************************************************************
     * LazyLoading Methods
     * ************************************************************************
     */
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
    Collection<T> findEntitiesLazy(int firstResult, int maxResults, String sortField, SortOrder sortOrder, Map<String, Object> filters, Map<String, SortOrder> defaultSortColumns);

    /**
     * Get count with applied filters Since p:dataTable uses a [ private int
     * rowCount; ] the method returns an int, wich allows only 2 147 483 647
     * (2^31-1) records.
     *
     * @param filters table filters
     * @return count
     */
    Long getCount(Map<String, Object> filters);
}