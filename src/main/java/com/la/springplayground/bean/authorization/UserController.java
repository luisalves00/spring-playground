/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.la.springplayground.bean.authorization;

import com.la.springplayground.bean.converter.UserConverter;
import com.la.springplayground.bean.util.JsfUtil;
import com.la.springplayground.entity.User;
import com.la.springplayground.jpa.authorization.impl.UserJpaControllerImpl;
import com.la.springplayground.jpa.exceptions.NonexistentEntityException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import org.springframework.context.annotation.Scope;

/**
 *
 * @author Luís Alves
 */
@Named("userBean")
@Scope("session")
public class UserController implements Serializable {

    public UserController() {
        converter = new UserConverter();
       //ApplicationContext ctx = FacesContextUtils.getWebApplicationContext(FacesContext.getCurrentInstance());
    }
    
    private User user = null;
    private List<User> userItems = null;
    @Inject
    private UserJpaControllerImpl userJpa = null;
    private UserConverter converter = null;

    public UserJpaControllerImpl getJpaController() {
        return userJpa;
    }

    public void setJpaController(UserJpaControllerImpl jpaController) {
        this.userJpa = jpaController;
    }

    
    
    public SelectItem[] getUserItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(userJpa.findAll(), false);
    }

    public SelectItem[] getUserItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(userJpa.findAll(), true);
    }

    public User getUser() {
        if (user == null) {
            user = (User) JsfUtil.getObjectFromRequestParameter("jsfcrud.currentUser", converter, null);
        }
        if (user == null) {
            user = new User();
        }
        return user;
    }

    public String listSetup() {
        reset(true);
        return "user_list";
    }

    public String createSetup() {
        reset(false);
        user = new User();
        return "user_create";
    }

    public String create() {
        FacesContext ctx = FacesContext.getCurrentInstance();
        try {
            getJpaController().create(user);
            JsfUtil.addSuccessMessage("User was successfully created.",ctx);
        } catch (Exception e) {
            JsfUtil.addErrorMessage("A persistence error occurred.", ctx);
            //todo log exception
            return null;
        }
        return listSetup();
    }

    public String detailSetup() {
        return scalarSetup("user_detail");
    }

    public String editSetup() {
        return scalarSetup("user_edit");
    }

    private String scalarSetup(String destination) {
        reset(false);
        user = (User) JsfUtil.getObjectFromRequestParameter("jsfcrud.currentUser", converter, null);
        if (user == null) {
            String requestUserString = JsfUtil.getRequestParameter("jsfcrud.currentUser");
            //JsfUtil.addErrorMessage("The user with id " + requestUserString + " no longer exists.");
            return relatedOrListOutcome();
        }
        return destination;
    }

    public String edit() {
        String userString = converter.getAsString(FacesContext.getCurrentInstance(), null, user);
        String currentUserString = JsfUtil.getRequestParameter("jsfcrud.currentUser");
        if (userString == null || userString.length() == 0 || !userString.equals(currentUserString)) {
            String outcome = editSetup();
            if ("user_edit".equals(outcome)) {
                //JsfUtil.addErrorMessage("Could not edit user. Try again.");
            }
            return outcome;
        }
        try {
            getJpaController().edit(user);
            //JsfUtil.addSuccessMessage("User was successfully updated.");
        } catch (NonexistentEntityException ne) {
            //JsfUtil.addErrorMessage(ne.getLocalizedMessage());
            return listSetup();
        } catch (Exception e) {
            //JsfUtil.ensureAddErrorMessage(e, "A persistence error occurred.");
            return null;
        }
        return detailSetup();
    }

    public String destroy() {
        String idAsString = JsfUtil.getRequestParameter("jsfcrud.currentUser");
        Long id = new Long(idAsString);
        try {
            getJpaController().destroyById(id);
            //JsfUtil.addSuccessMessage("User was successfully deleted.");
        } catch (NonexistentEntityException ne) {
            //JsfUtil.addErrorMessage(ne.getLocalizedMessage());
            return relatedOrListOutcome();
        } catch (Exception e) {
            //JsfUtil.ensureAddErrorMessage(e, "A persistence error occurred.");
            return null;
        }
        return relatedOrListOutcome();
    }

    private String relatedOrListOutcome() {
        String relatedControllerOutcome = relatedControllerOutcome();
        if (relatedControllerOutcome != null) {
            return relatedControllerOutcome;
        }
        return listSetup();
    }

    public List<User> getUserItems() {
        if (userItems == null) {
            userItems = userJpa.findAll();
        }
        return userItems;
    }

    private String relatedControllerOutcome() {
        String relatedControllerString = JsfUtil.getRequestParameter("jsfcrud.relatedController");
        String relatedControllerTypeString = JsfUtil.getRequestParameter("jsfcrud.relatedControllerType");
        if (relatedControllerString != null && relatedControllerTypeString != null) {
            FacesContext context = FacesContext.getCurrentInstance();
            Object relatedController = context.getApplication().getELResolver().getValue(context.getELContext(), null, relatedControllerString);
            try {
                Class<?> relatedControllerType = Class.forName(relatedControllerTypeString);
                Method detailSetupMethod = relatedControllerType.getMethod("detailSetup");
                return (String) detailSetupMethod.invoke(relatedController);
            } catch (ClassNotFoundException e) {
                throw new FacesException(e);
            } catch (NoSuchMethodException e) {
                throw new FacesException(e);
            } catch (IllegalAccessException e) {
                throw new FacesException(e);
            } catch (InvocationTargetException e) {
                throw new FacesException(e);
            }
        }
        return null;
    }

    private void reset(boolean resetFirstItem) {
        user = null;
        userItems = null;
    }

    public void validateCreate(FacesContext facesContext, UIComponent component, Object value) {
        User newUser = new User();
        String newUserString = converter.getAsString(FacesContext.getCurrentInstance(), null, newUser);
        String userString = converter.getAsString(FacesContext.getCurrentInstance(), null, user);
        if (!newUserString.equals(userString)) {
            createSetup();
        }
    }

    public Converter getConverter() {
        return converter;
    }
    
}
