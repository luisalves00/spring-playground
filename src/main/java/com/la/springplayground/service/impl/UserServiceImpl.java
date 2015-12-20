/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.la.springplayground.service.impl;

import com.la.springplayground.entity.User;
import com.la.springplayground.jpa.authorization.UserJpaController;
import com.la.springplayground.service.UserService;
import java.io.Serializable;
import java.util.Locale;
import javax.inject.Inject;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Luís Alves
 */
@Service("userService")
public class UserServiceImpl implements UserService, Serializable {

    @Inject
    private UserJpaController userJpa;
    @Inject
    private transient PasswordEncoder passwordEncoder;

    /**
     * Load user by username
     *
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User u = userJpa.findByUsername(username);
        if (u == null) {
            throw new UsernameNotFoundException("Failed to find the user");
        }
        return u;
    }

    /**
     * Register a user as inactive until account activation.
     *
     * @param u The user object
     * @param password The un-hashed password
     * @param locale The user prefered locale
     */
    @Transactional
    public void register(User u, String password, Locale locale) throws Exception {
        u.setLocale(locale.toString());
        u.setSalt(KeyGenerators.string().generateKey());
        u.setPassword(passwordEncoder.encodePassword(password, u.getSalt()));
        u.setEnabled(Boolean.FALSE);
        userJpa.create(u);
        
        //TODO OseOnd mail...TODO: user jms to send email.
    }
}
