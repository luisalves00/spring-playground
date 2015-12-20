/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.la.springplayground.bean;

import com.la.springplayground.entity.logging.LoggingEvent;
import com.la.springplayground.service.LoggingService;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.springframework.context.annotation.Scope;

/**
 *
 * @author Luís Alves
 */
@Named("loggingBean")
@Scope("session")
public class LoggingBean extends BaseBean {
    
    @Inject
    LoggingService loggingService;
    private LazyDataModel<LoggingEvent> loggingEvents;
    
    /**
     * FindAll Logging Events
     * 
     * @return 
     */
    public LazyDataModel<LoggingEvent> getLoggingEvents() {
        if(loggingEvents == null){
            loggingEvents = new LazyDataModel<LoggingEvent>() {
            	@Override
                public List<LoggingEvent> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
                   return (List<LoggingEvent>) loggingService.findLazyLoggingEvent(first, pageSize,sortField,sortOrder,filters, null);
                }
            };
        }
        return loggingEvents;
    }
    //load(int first, int pageSize, List<SortMeta> multiSortMeta, Map<String,Object> filters) 
    //load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters) 
    
}
