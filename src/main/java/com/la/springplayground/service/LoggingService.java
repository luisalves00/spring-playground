/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.la.springplayground.service;

import com.la.springplayground.entity.logging.LoggingEvent;
import java.util.Collection;
import java.util.Map;
import org.primefaces.model.SortOrder;

/**
 *
 * @author Luís Alves
 */
public interface LoggingService {
    
    Collection<LoggingEvent> findLazyLoggingEvent(int firstResult, int maxResults, String sortField, SortOrder sortOrder, Map<String, Object> filters, Map<String, SortOrder> defaultSortColumns);

    Collection<LoggingEvent> findAllLoggingEvent();
    
}
