/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.la.springplayground.bean.converter;

import com.la.springplayground.bean.authorization.RoleController;
import com.la.springplayground.entity.Role;
import java.io.Serializable;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author Luís Alves
 */
@FacesConverter(forClass=com.la.springplayground.entity.Role.class)
public class RoleConverter implements Converter, Serializable {

    public Object getAsObject(FacesContext facesContext, UIComponent component, String string) {
        if (string == null || string.length() == 0) {
            return null;
        }
        Short id = new Short(string);
        RoleController controller = (RoleController) facesContext.getApplication().getVariableResolver().resolveVariable(facesContext, "role");
        return controller.getJpaController().findRole(id);
    }

    public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
        if (object == null) {
            return null;
        }
        if (object instanceof Role) {
            Role o = (Role) object;
            return o.getId() == null ? "" : o.getId().toString();
        } else {
            throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: com.la.springplayground.entity.Role");
        }
    }
    
}
