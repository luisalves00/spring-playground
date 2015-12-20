/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.la.springplayground.service;

import com.la.springplayground.entity.User;
import java.util.Locale;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Luís Alves
 */
public interface UserService extends UserDetailsService {
    
    /**
     * Register a user as inactive until account activation.
     * @param u 
     */
    public void register(User u,String password, Locale locale) throws Exception;
    
}
