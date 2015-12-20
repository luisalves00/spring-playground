/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.la.springplayground.bean;

import com.la.springplayground.entity.User;
import javax.inject.Named;
import org.springframework.context.annotation.Scope;

/**
 * Store the user information during a session
 *  
 * @author Luís Alves
 */
@Named("securityBean")
@Scope("session")
public class SecurityBean extends BaseBean {
    
    private User user;

    /**
     * @return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(User user) {
        this.user = user;
    }
    
}
