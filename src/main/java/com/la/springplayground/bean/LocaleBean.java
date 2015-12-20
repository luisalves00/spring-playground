/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.la.springplayground.bean;


import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Scope;

/**
 *
 * @author Luís Alves
 */
@Named("localeBean")
@Scope("singleton")
public class LocaleBean extends BaseBean {

    /**
     * Find the locale using the query text
     * @param query
     * @return 
     */
    public List<Locale> complete(String query) {
        List<Locale> result = new ArrayList<Locale>();
        Locale list[] = Locale.getAvailableLocales();
        for (int i = 0; i < list.length; i++) {
            if (list[i].getDisplayName(FacesContext.getCurrentInstance().getViewRoot().getLocale()).toLowerCase().contains(query.toLowerCase())
                    && StringUtils.isNotBlank(list[i].getCountry())) {
                   result.add(list[i]);       
            }
        }
        return result;
    }
}
