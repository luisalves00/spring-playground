/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.la.springplayground.bean.converter;

import com.la.springplayground.bean.authorization.UserController;
import com.la.springplayground.entity.User;
import java.io.Serializable;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author Luís Alves
 */
@FacesConverter(forClass=com.la.springplayground.entity.User.class)
public class UserConverter implements Converter, Serializable {

    public Object getAsObject(FacesContext facesContext, UIComponent component, String string) {
        if (string == null || string.length() == 0) {
            return null;
        }
        Long id = new Long(string);
        UserController controller = (UserController) facesContext.getApplication().getVariableResolver().resolveVariable(facesContext, "user");
        return controller.getJpaController().find(id);
    }

    public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
        if (object == null) {
            return null;
        }
        if (object instanceof User) {
            User o = (User) object;
            return o.getId() == null ? "" : o.getId().toString();
        } else {
            throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: com.la.springplayground.entity.User");
        }
    }
    
}
