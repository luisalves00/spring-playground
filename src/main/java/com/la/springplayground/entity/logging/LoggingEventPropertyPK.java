/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.la.springplayground.entity.logging;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author Luís Alves
 */
@Embeddable
public class LoggingEventPropertyPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "event_id")
    private long eventId;
    @Basic(optional = false)
    @Column(name = "mapped_key")
    private String mappedKey;

    public LoggingEventPropertyPK() {
    }

    public LoggingEventPropertyPK(long eventId, String mappedKey) {
        this.eventId = eventId;
        this.mappedKey = mappedKey;
    }

    public long getEventId() {
        return eventId;
    }

    public void setEventId(long eventId) {
        this.eventId = eventId;
    }

    public String getMappedKey() {
        return mappedKey;
    }

    public void setMappedKey(String mappedKey) {
        this.mappedKey = mappedKey;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) eventId;
        hash += (mappedKey != null ? mappedKey.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof LoggingEventPropertyPK)) {
            return false;
        }
        LoggingEventPropertyPK other = (LoggingEventPropertyPK) object;
        if (this.eventId != other.eventId) {
            return false;
        }
        if ((this.mappedKey == null && other.mappedKey != null) || (this.mappedKey != null && !this.mappedKey.equals(other.mappedKey))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.la.springplayground.entity.logging.LoggingEventPropertyPK[ eventId=" + eventId + ", mappedKey=" + mappedKey + " ]";
    }
    
}
