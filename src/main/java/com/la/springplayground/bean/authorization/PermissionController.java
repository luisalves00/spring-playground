/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.la.springplayground.bean.authorization;

import com.la.springplayground.bean.converter.PermissionConverter;
import com.la.springplayground.bean.util.JsfUtil;
import com.la.springplayground.bean.util.PagingInfo;
import com.la.springplayground.entity.Permission;
import com.la.springplayground.jpa.authorization.impl.PermissionJpaControllerImpl;
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
@Named("permissionBean")
@Scope("session")
public class PermissionController implements Serializable{

    public PermissionController() {
        pagingInfo = new PagingInfo();
        converter = new PermissionConverter();
    }
    private Permission permission = null;
    private List<Permission> permissionItems = null;
    
    @Inject
    private PermissionJpaControllerImpl permissionJpa = null;
    private PermissionConverter converter = null;
    private PagingInfo pagingInfo = null;

 

    public PermissionJpaControllerImpl getJpaController() {
        return permissionJpa;
    }

    public void setJpaController(PermissionJpaControllerImpl jpaController) {
        this.permissionJpa = jpaController;
    }

    public SelectItem[] getPermissionItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(getJpaController().findAll(), false);
    }

    public SelectItem[] getPermissionItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(getJpaController().findAll(), true);
    }

    public Permission getPermission() {
        if (permission == null) {
            permission = (Permission) JsfUtil.getObjectFromRequestParameter("jsfcrud.currentPermission", converter, null);
        }
        if (permission == null) {
            permission = new Permission();
        }
        return permission;
    }

    public String listSetup() {
        reset(true);
        return "permission_list";
    }

    public String createSetup() {
        reset(false);
        permission = new Permission();
        return "permission_create";
    }

    public String create() {
        try {
            getJpaController().create(permission);
            //JsfUtil.addSuccessMessage("Permission was successfully created.");
        } catch (Exception e) {
            //JsfUtil.ensureAddErrorMessage(e, "A persistence error occurred.");
            return null;
        }
        return listSetup();
    }

    public String detailSetup() {
        return scalarSetup("permission_detail");
    }

    public String editSetup() {
        return scalarSetup("permission_edit");
    }

    private String scalarSetup(String destination) {
        reset(false);
        permission = (Permission) JsfUtil.getObjectFromRequestParameter("jsfcrud.currentPermission", converter, null);
        if (permission == null) {
            String requestPermissionString = JsfUtil.getRequestParameter("jsfcrud.currentPermission");
            //JsfUtil.addErrorMessage("The permission with id " + requestPermissionString + " no longer exists.");
            return relatedOrListOutcome();
        }
        return destination;
    }

    public String edit() {
        String permissionString = converter.getAsString(FacesContext.getCurrentInstance(), null, permission);
        String currentPermissionString = JsfUtil.getRequestParameter("jsfcrud.currentPermission");
        if (permissionString == null || permissionString.length() == 0 || !permissionString.equals(currentPermissionString)) {
            String outcome = editSetup();
            if ("permission_edit".equals(outcome)) {
                //JsfUtil.addErrorMessage("Could not edit permission. Try again.");
            }
            return outcome;
        }
        try {
            getJpaController().edit(permission);
            //JsfUtil.addSuccessMessage("Permission was successfully updated.");
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
        String idAsString = JsfUtil.getRequestParameter("jsfcrud.currentPermission");
        Short id = new Short(idAsString);
        try {
            getJpaController().destroyById(id);
            //JsfUtil.addSuccessMessage("Permission was successfully deleted.");
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

    public List<Permission> getPermissionItems() {
        if (permissionItems == null) {
            permissionItems = getJpaController().findAll(pagingInfo.getBatchSize(), pagingInfo.getFirstItem());
        }
        return permissionItems;
    }

    public String next() {
        reset(false);
        return "permission_list";
    }

    public String prev() {
        reset(false);
        return "permission_list";
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
        permission = null;
        permissionItems = null;
        pagingInfo.setItemCount(-1);
        if (resetFirstItem) {
            pagingInfo.setFirstItem(0);
        }
    }

    public void validateCreate(FacesContext facesContext, UIComponent component, Object value) {
        Permission newPermission = new Permission();
        String newPermissionString = converter.getAsString(FacesContext.getCurrentInstance(), null, newPermission);
        String permissionString = converter.getAsString(FacesContext.getCurrentInstance(), null, permission);
        if (!newPermissionString.equals(permissionString)) {
            createSetup();
        }
    }

    public Converter getConverter() {
        return converter;
    }
    
}
