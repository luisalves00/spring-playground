/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.la.springplayground.entity;

/**
 * Interface to be able to treat all entities the same way
 *  
 * @author Luís Alves
 */
public interface EntityBase {
    /**
     * All the entities must implement this...can't return Long because of the
     * Composite Keys so have to do Casting latter.
     * @return the entity Id (Long, EntityPK,...)
     */
    public Object getId();

}
