/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.la.springplayground.entity.logging;

import com.la.springplayground.entity.EntityBase;
import java.io.Serializable;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Luís Alves
 */
@Entity
@Table(name = "logging_event_property")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "LoggingEventProperty.findAll", query = "SELECT l FROM LoggingEventProperty l"),
    @NamedQuery(name = "LoggingEventProperty.findByEventId", query = "SELECT l FROM LoggingEventProperty l WHERE l.loggingEventPropertyPK.eventId = :eventId"),
    @NamedQuery(name = "LoggingEventProperty.findByMappedKey", query = "SELECT l FROM LoggingEventProperty l WHERE l.loggingEventPropertyPK.mappedKey = :mappedKey")})
public class LoggingEventProperty implements EntityBase {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected LoggingEventPropertyPK loggingEventPropertyPK;
    @Lob
    @Column(name = "mapped_value")
    private String mappedValue;
    @JoinColumn(name = "event_id", referencedColumnName = "event_id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private LoggingEvent loggingEvent;

    public LoggingEventProperty() {
    }

    public LoggingEventProperty(LoggingEventPropertyPK loggingEventPropertyPK) {
        this.loggingEventPropertyPK = loggingEventPropertyPK;
    }

    public LoggingEventProperty(long eventId, String mappedKey) {
        this.loggingEventPropertyPK = new LoggingEventPropertyPK(eventId, mappedKey);
    }

    public LoggingEventPropertyPK getLoggingEventPropertyPK() {
        return loggingEventPropertyPK;
    }

    public void setLoggingEventPropertyPK(LoggingEventPropertyPK loggingEventPropertyPK) {
        this.loggingEventPropertyPK = loggingEventPropertyPK;
    }

    public String getMappedValue() {
        return mappedValue;
    }

    public void setMappedValue(String mappedValue) {
        this.mappedValue = mappedValue;
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
        hash += (loggingEventPropertyPK != null ? loggingEventPropertyPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof LoggingEventProperty)) {
            return false;
        }
        LoggingEventProperty other = (LoggingEventProperty) object;
        if ((this.loggingEventPropertyPK == null && other.loggingEventPropertyPK != null) || (this.loggingEventPropertyPK != null && !this.loggingEventPropertyPK.equals(other.loggingEventPropertyPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.la.springplayground.entity.logging.LoggingEventProperty[ loggingEventPropertyPK=" + loggingEventPropertyPK + " ]";
    }

    public Object getId() {
        return loggingEventPropertyPK;
    }
    
}
