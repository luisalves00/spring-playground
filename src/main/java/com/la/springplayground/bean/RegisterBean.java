/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.la.springplayground.bean;

import com.la.springplayground.bean.util.JsfUtil;
import com.la.springplayground.entity.User;
import com.la.springplayground.service.UserService;
import java.io.IOException;
import java.util.Locale;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;

/**
 *
 * @author Luís Alves
 */
@Named("registerBean")
@Scope("view")
public class RegisterBean extends BaseBean{
    
    private Logger logger = LoggerFactory.getLogger(RegisterBean.class);

    @Inject
    private UserService userService;
    private User user;
    private String password;
    private String reEnterPassword;
    private Locale locale;

    /**
     * 
     * @return
     * @throws IOException
     * @throws ServletException 
     */
    public String register() {
        try{
            userService.register(user, password, locale);
        }catch(Exception e){
            logger.error("Error registering the user: " + user.getEmail(), e); 
            JsfUtil.addErrorMessage("error_registering_user", FacesContext.getCurrentInstance(), user.getEmail());
            return null;
        }
        return ""; //TODO:
    }

    /***************************************************************************
     *                           Getters and Setters
     **************************************************************************/ 

    
    public User getUser() {
        if(user == null){
            user = new User();
        }
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getReEnterPassword() {
        return reEnterPassword;
    }

    public void setReEnterPassword(String reEnterPassword) {
        this.reEnterPassword = reEnterPassword;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

}
