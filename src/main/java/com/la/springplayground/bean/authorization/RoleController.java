/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.la.springplayground.bean.authorization;

import com.la.springplayground.bean.converter.RoleConverter;
import com.la.springplayground.bean.util.JsfUtil;
import com.la.springplayground.bean.util.PagingInfo;
import com.la.springplayground.entity.Role;
import com.la.springplayground.jpa.authorization.impl.RoleJpaControllerImpl;
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
@Named("roleBean")
@Scope("session")
public class RoleController implements Serializable {

    public RoleController() {
        pagingInfo = new PagingInfo();
        converter = new RoleConverter();
    }
    private Role role = null;
    private List<Role> roleItems = null;
    @Inject
    private RoleJpaControllerImpl roleJpa = null;
    private RoleConverter converter = null;
    private PagingInfo pagingInfo = null;

    public PagingInfo getPagingInfo() {
        if (pagingInfo.getItemCount() == -1) {
            pagingInfo.setItemCount(getJpaController().getRoleCount());
        }
        return pagingInfo;
    }

    public RoleJpaControllerImpl getJpaController() {
        return roleJpa;
    }

    public void setJpaController(RoleJpaControllerImpl jpaController) {
        this.roleJpa = jpaController;
    }
    
    public SelectItem[] getRoleItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(getJpaController().findAll(), false);
    }

    public SelectItem[] getRoleItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(getJpaController().findAll(), true);
    }

    public Role getRole() {
        if (role == null) {
            role = (Role) JsfUtil.getObjectFromRequestParameter("jsfcrud.currentRole", converter, null);
        }
        if (role == null) {
            role = new Role();
        }
        return role;
    }

    public String listSetup() {
        reset(true);
        return "role_list";
    }

    public String createSetup() {
        reset(false);
        role = new Role();
        return "role_create";
    }

    public String create() {
        try {
            getJpaController().create(role);
            //JsfUtil.addSuccessMessage("Role was successfully created.");
        } catch (Exception e) {
            //JsfUtil.ensureAddErrorMessage(e, "A persistence error occurred.");
            return null;
        }
        return listSetup();
    }

    public String detailSetup() {
        return scalarSetup("role_detail");
    }

    public String editSetup() {
        return scalarSetup("role_edit");
    }

    private String scalarSetup(String destination) {
        reset(false);
        role = (Role) JsfUtil.getObjectFromRequestParameter("jsfcrud.currentRole", converter, null);
        if (role == null) {
            String requestRoleString = JsfUtil.getRequestParameter("jsfcrud.currentRole");
            //JsfUtil.addErrorMessage("The role with id " + requestRoleString + " no longer exists.");
            return relatedOrListOutcome();
        }
        return destination;
    }

    public String edit() {
        String roleString = converter.getAsString(FacesContext.getCurrentInstance(), null, role);
        String currentRoleString = JsfUtil.getRequestParameter("jsfcrud.currentRole");
        if (roleString == null || roleString.length() == 0 || !roleString.equals(currentRoleString)) {
            String outcome = editSetup();
            if ("role_edit".equals(outcome)) {
                //JsfUtil.addErrorMessage("Could not edit role. Try again.");
            }
            return outcome;
        }
        try {
            getJpaController().edit(role);
            //JsfUtil.addSuccessMessage("Role was successfully updated.");
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
        String idAsString = JsfUtil.getRequestParameter("jsfcrud.currentRole");
        Short id = new Short(idAsString);
        try {
            getJpaController().destroy(id);
            //JsfUtil.addSuccessMessage("Role was successfully deleted.");
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

    public List<Role> getRoleItems() {
        if (roleItems == null) {
            getPagingInfo();
            roleItems = getJpaController().findRoleEntities(pagingInfo.getBatchSize(), pagingInfo.getFirstItem());
        }
        return roleItems;
    }

    public String next() {
        reset(false);
        getPagingInfo().nextPage();
        return "role_list";
    }

    public String prev() {
        reset(false);
        getPagingInfo().previousPage();
        return "role_list";
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
        role = null;
        roleItems = null;
        pagingInfo.setItemCount(-1);
        if (resetFirstItem) {
            pagingInfo.setFirstItem(0);
        }
    }

    public void validateCreate(FacesContext facesContext, UIComponent component, Object value) {
        Role newRole = new Role();
        String newRoleString = converter.getAsString(FacesContext.getCurrentInstance(), null, newRole);
        String roleString = converter.getAsString(FacesContext.getCurrentInstance(), null, role);
        if (!newRoleString.equals(roleString)) {
            createSetup();
        }
    }

    public Converter getConverter() {
        return converter;
    }
    
}
