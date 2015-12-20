package com.la.springplayground.bean.util;

import java.text.MessageFormat;
import java.util.*;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.model.SelectItem;

public class JsfUtil {

    public static SelectItem[] getSelectItems(List<?> entities, boolean selectOne) {
        int size = selectOne ? entities.size() + 1 : entities.size();
        SelectItem[] items = new SelectItem[size];
        int i = 0;
        if (selectOne) {
            items[0] = new SelectItem("", "---");
            i++;
        }
        for (Object x : entities) {
            items[i++] = new SelectItem(x, x.toString());
        }
        return items;
    }

    /**
     * Get Faces Message
     *
     * @param ctx
     * @param severity
     * @param msgKey
     * @param args
     * @return
     */
    public static FacesMessage getFacesMessage(FacesContext ctx, FacesMessage.Severity severity, String msgKey, Object... args) {
        String msg = "";
        try {
            Locale loc = ctx.getViewRoot().getLocale();
            ResourceBundle bundle = ResourceBundle.getBundle(ctx.getApplication().getMessageBundle(), loc);
            msg = bundle.getString(msgKey);
        } catch (MissingResourceException e) {
            return new FacesMessage(severity, "???" + msgKey + "???", "");
        }
        if (args != null) {
            MessageFormat format = new MessageFormat(msg);
            msg = format.format(args);
        }
        return new FacesMessage(severity, msg, "");
    }

    /**
     * Add Fatal Message
     *
     * @param ctx
     * @param clientId
     * @param msgKey
     * @param args
     */
    public static void addFatalMessage(String clientId, String msgKey, FacesContext ctx, Object... args) {
        FacesMessage facesMsg = getFacesMessage(ctx, FacesMessage.SEVERITY_FATAL, msgKey, args);
        ctx.addMessage(clientId, facesMsg);
    }

    /**
     * Add Error Message
     *
     * @param ctx
     * @param clientId
     * @param msgKey
     * @param args
     */
    public static void addErrorMessage(String clientId, String msgKey, FacesContext ctx, Object... args) {
        FacesMessage facesMsg = getFacesMessage(ctx, FacesMessage.SEVERITY_ERROR, msgKey, args);
        ctx.addMessage(clientId, facesMsg);
    }

    /**
     * Add Warning Message
     *
     * @param ctx
     * @param clientId
     * @param msgKey
     * @param args
     */
    public static void addWarnMessage(String clientId, String msgKey, FacesContext ctx, Object... args) {
        FacesMessage facesMsg = getFacesMessage(ctx, FacesMessage.SEVERITY_WARN, msgKey, args);
        ctx.addMessage(clientId, facesMsg);
    }

    /**
     * Add Success Message
     *
     * @param ctx
     * @param clientId
     * @param msgKey
     * @param args
     */
    public static void addSuccessMessage(String clientId, String msgKey, FacesContext ctx, Object... args) {
        FacesMessage facesMsg = getFacesMessage(ctx, FacesMessage.SEVERITY_INFO, msgKey, args);
        ctx.addMessage(clientId, facesMsg);
    }

    /**
     * Add Fatal Message
     *
     * @param ctx
     * @param msgKey
     * @param args
     */
    public static void addFatalMessage(String msgKey, FacesContext ctx, Object... args) {
        FacesMessage facesMsg = getFacesMessage(ctx, FacesMessage.SEVERITY_FATAL, msgKey, args);
        ctx.addMessage(null, facesMsg);
    }

    /**
     * Add Error Message
     *
     * @param ctx
     * @param msgKey
     * @param args
     */
    public static void addErrorMessage(String msgKey, FacesContext ctx, Object... args) {
        FacesMessage facesMsg = getFacesMessage(ctx, FacesMessage.SEVERITY_ERROR, msgKey, args);
        ctx.addMessage(null, facesMsg);
    }

    /**
     * Add Warning Message
     *
     * @param ctx
     * @param msgKey
     * @param args
     */
    public static void addWarnMessage(String msgKey, FacesContext ctx, Object... args) {
        FacesMessage facesMsg = getFacesMessage(ctx, FacesMessage.SEVERITY_WARN, msgKey, args);
        ctx.addMessage(null, facesMsg);
    }

    /**
     * Add Success Message
     *
     * @param ctx
     * @param msgKey
     * @param args
     */
    public static void addSuccessMessage(String msgKey, FacesContext ctx, Object... args) {
        FacesMessage facesMsg = getFacesMessage(ctx, FacesMessage.SEVERITY_INFO, msgKey, args);
        ctx.addMessage(null, facesMsg);
    }

    /**
     * Get Resource Text
     *
     * @param msgKey
     * @param ctx
     * @param args
     * @return
     */
    public static String getResourceText(String msgKey, FacesContext ctx, Object... args) {
        String msg = "";
        try {
            Locale loc = ctx.getViewRoot().getLocale();
            ResourceBundle bundle = ResourceBundle.getBundle(ctx.getApplication().getMessageBundle(), loc);
            msg = bundle.getString(msgKey);
        } catch (MissingResourceException e) {
            return "???" + msgKey + "???";
        }
        if (args != null) {
            MessageFormat format = new MessageFormat(msg);
            msg = format.format(args);
        }
        return msg;
    }

    public static String getRequestParameter(String key) {
        return FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(key);
    }

    public static Object getObjectFromRequestParameter(String requestParameterName, Converter converter, UIComponent component) {
        String theId = JsfUtil.getRequestParameter(requestParameterName);
        return converter.getAsObject(FacesContext.getCurrentInstance(), component, theId);
    }

    public static <T> List<T> arrayToList(T[] arr) {
        if (arr == null) {
            return new ArrayList<T>();
        }
        return Arrays.asList(arr);
    }

    public static <T> Set<T> arrayToSet(T[] arr) {
        if (arr == null) {
            return new HashSet<T>();
        }
        return new HashSet(Arrays.asList(arr));
    }

    public static Object[] collectionToArray(Collection<?> c) {
        if (c == null) {
            return new Object[0];
        }
        return c.toArray();
    }

    public static <T> List<T> setToList(Set<T> set) {
        return new ArrayList<T>(set);
    }

    public static String getAsConvertedString(Object object, Converter converter) {
        return converter.getAsString(FacesContext.getCurrentInstance(), null, object);
    }

    public static String getCollectionAsString(Collection<?> collection) {
        if (collection == null || collection.isEmpty()) {
            return "(No Items)";
        }
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (Object item : collection) {
            if (i > 0) {
                sb.append("<br />");
            }
            sb.append(item);
            i++;
        }
        return sb.toString();
    }

    /**
     * Get the full path of the server
     *
     * @param facesContext
     * @param urlEnd appended on the end of the path
     * @return the full server path with the urlEnd appended
     */
    public static String fullServerContextPath(FacesContext facesContext, String urlEnd) {
        int port = facesContext.getExternalContext().getRequestServerPort();
        String url = "";
        if (port == 443 || port == 8443) {
            url = "https://" + facesContext.getExternalContext().getRequestServerName() + facesContext.getExternalContext().getRequestServletPath()
                    + urlEnd;
        } else {
            if (port == 80 || port == 8080) {
                url = "http://" + facesContext.getExternalContext().getRequestServerName()
                        + facesContext.getExternalContext().getRequestServletPath() + urlEnd;
            } else {
                url = "http://" + facesContext.getExternalContext().getRequestServerName() + ":" + String.valueOf(port)
                        + facesContext.getExternalContext().getRequestServletPath() + urlEnd;
            }
        }
        return url;
    }
}
