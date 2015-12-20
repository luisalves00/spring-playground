/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.la.springplayground.entity.logging;

import com.la.springplayground.entity.EntityBase;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Luís Alves
 */
@Entity
@Table(name = "logging_event_exception")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "LoggingEventException.findAll", query = "SELECT l FROM LoggingEventException l"),
    @NamedQuery(name = "LoggingEventException.findByEventId", query = "SELECT l FROM LoggingEventException l WHERE l.loggingEventExceptionPK.eventId = :eventId"),
    @NamedQuery(name = "LoggingEventException.findByI", query = "SELECT l FROM LoggingEventException l WHERE l.loggingEventExceptionPK.i = :i"),
    @NamedQuery(name = "LoggingEventException.findByTraceLine", query = "SELECT l FROM LoggingEventException l WHERE l.traceLine = :traceLine")})
public class LoggingEventException implements EntityBase {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected LoggingEventExceptionPK loggingEventExceptionPK;
    @Basic(optional = false)
    @Column(name = "trace_line")
    private String traceLine;
    @JoinColumn(name = "event_id", referencedColumnName = "event_id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private LoggingEvent loggingEvent;

    public LoggingEventException() {
    }

    public LoggingEventException(LoggingEventExceptionPK loggingEventExceptionPK) {
        this.loggingEventExceptionPK = loggingEventExceptionPK;
    }

    public LoggingEventException(LoggingEventExceptionPK loggingEventExceptionPK, String traceLine) {
        this.loggingEventExceptionPK = loggingEventExceptionPK;
        this.traceLine = traceLine;
    }

    public LoggingEventException(long eventId, short i) {
        this.loggingEventExceptionPK = new LoggingEventExceptionPK(eventId, i);
    }

    public LoggingEventExceptionPK getLoggingEventExceptionPK() {
        return loggingEventExceptionPK;
    }

    public void setLoggingEventExceptionPK(LoggingEventExceptionPK loggingEventExceptionPK) {
        this.loggingEventExceptionPK = loggingEventExceptionPK;
    }

    public String getTraceLine() {
        return traceLine;
    }

    public void setTraceLine(String traceLine) {
        this.traceLine = traceLine;
    }

    public LoggingEvent getLoggingEvent() {
        return loggingEvent;
    }

    public void setLoggingEvent(LoggingEvent loggingEvent) {
        this.loggingEvent = loggingEvent;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (loggingEventExceptionPK != null ? loggingEventExceptionPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof LoggingEventException)) {
            return false;
        }
        LoggingEventException other = (LoggingEventException) object;
        if ((this.loggingEventExceptionPK == null && other.loggingEventExceptionPK != null) || (this.loggingEventExceptionPK != null && !this.loggingEventExceptionPK.equals(other.loggingEventExceptionPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.la.springplayground.entity.logging.LoggingEventException[ loggingEventExceptionPK=" + loggingEventExceptionPK + " ]";
    }

    public Object getId() {
        return loggingEventExceptionPK;
    }
    
}
