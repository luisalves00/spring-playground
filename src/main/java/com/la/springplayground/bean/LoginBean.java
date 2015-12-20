/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.la.springplayground.bean;

import com.la.springplayground.bean.model.LoginModel;
import com.la.springplayground.bean.util.JsfUtil;
import com.la.springplayground.entity.User;
import java.io.IOException;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.security.auth.login.AccountExpiredException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.web.WebAttributes;

/**
 *
 * @author Luís Alves
 */
@Named("loginBean")
@Scope("view")
public class LoginBean extends BaseBean{

    private LoginModel user;
    private boolean loggedIn = false;

    public String doLogin() throws IOException, ServletException {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();

        RequestDispatcher dispatcher = ((ServletRequest) context.getRequest()).getRequestDispatcher("/j_spring_security_check");

        dispatcher.forward((ServletRequest) context.getRequest(), (ServletResponse) context.getResponse());

        FacesContext.getCurrentInstance().responseComplete();

        // It's OK to return null here because Faces is just going to exit.
        return null;

    }

    @PostConstruct
    private void handleErrorMessage() {
        FacesContext ctx = FacesContext.getCurrentInstance();
        Exception e = (Exception) ctx.getExternalContext().getSessionMap().get(
                WebAttributes.AUTHENTICATION_EXCEPTION );

        if (e instanceof BadCredentialsException) {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(
                    WebAttributes.AUTHENTICATION_EXCEPTION, null);
            JsfUtil.addErrorMessage("login_invalid", ctx);
        }else if(e instanceof LockedException){
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(
                    WebAttributes.AUTHENTICATION_EXCEPTION, null);
            JsfUtil.addErrorMessage("login_account_locked", ctx);
        }else if(e instanceof AccountExpiredException){
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(
                    WebAttributes.AUTHENTICATION_EXCEPTION, null);
            JsfUtil.addErrorMessage("login_account_expired", ctx);
        }else if(e instanceof CredentialsExpiredException){
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(
                    WebAttributes.AUTHENTICATION_EXCEPTION, null);
            JsfUtil.addErrorMessage("login_account_credential_expired", ctx);
        }else if(e instanceof DisabledException){
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(
                    WebAttributes.AUTHENTICATION_EXCEPTION, null);
            JsfUtil.addErrorMessage("login_account_disabled", ctx);
        }
    }
    
    /***************************************************************************
     *                           Getters and Setters
     **************************************************************************/ 

    
    public LoginModel getUser() {
        if(user == null){
            user =new LoginModel();
        }
        return user;
    }

    public void setUser(LoginModel user) {
        this.user = user;
    }
    
    public boolean isLoggedIn() {
        return this.loggedIn;
    }

    public void setLoggedIn(final boolean loggedIn) {
        this.loggedIn = loggedIn;
    }
}
