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
public class LoggingEventExceptionPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "event_id")
    private long eventId;
    @Basic(optional = false)
    @Column(name = "i")
    private short i;

    public LoggingEventExceptionPK() {
    }

    public LoggingEventExceptionPK(long eventId, short i) {
        this.eventId = eventId;
        this.i = i;
    }

    public long getEventId() {
        return eventId;
    }

    public void setEventId(long eventId) {
        this.eventId = eventId;
    }

    public short getI() {
        return i;
    }

    public void setI(short i) {
        this.i = i;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) eventId;
        hash += (int) i;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof LoggingEventExceptionPK)) {
            return false;
        }
        LoggingEventExceptionPK other = (LoggingEventExceptionPK) object;
        if (this.eventId != other.eventId) {
            return false;
        }
        if (this.i != other.i) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.la.springplayground.entity.logging.LoggingEventExceptionPK[ eventId=" + eventId + ", i=" + i + " ]";
    }
    
}
