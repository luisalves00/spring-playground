/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.la.springplayground.service.impl;

import com.la.springplayground.entity.logging.LoggingEvent;
import com.la.springplayground.jpa.logging.LoggingEventJpaController;
import com.la.springplayground.service.LoggingService;
import java.util.Collection;
import java.util.Map;
import javax.inject.Inject;
import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Luís Alves
 */
@Service("loggingService")
public class LoggingServiceImpl implements LoggingService {

    @Inject
    private LoggingEventJpaController loggingEventJpa;
    
    public Collection<LoggingEvent> findLazyLoggingEvent(int firstResult, int maxResults, String sortField, SortOrder sortOrder, Map<String, Object> filters, Map<String, SortOrder> defaultSortColumns) {
       return loggingEventJpa.findEntitiesLazy(firstResult, maxResults, sortField, sortOrder, filters, defaultSortColumns);
    }

    public Collection<LoggingEvent> findAllLoggingEvent() {
        return loggingEventJpa.findAll();
    }
    
}
