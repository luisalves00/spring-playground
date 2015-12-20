/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.la.springplayground.jpa.authorization;

import com.la.springplayground.entity.User;
import com.la.springplayground.jpa.GenericJpaController;

/**
 *
 * @author Luís Alves
 */
public interface UserJpaController<T> extends GenericJpaController<T> {
    
    /**
     * Find by username, i.e. the user email
     * 
     * @param userName
     * @return 
     */
    public User findByUsername(String userName);

}
