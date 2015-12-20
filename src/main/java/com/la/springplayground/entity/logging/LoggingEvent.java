/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.la.springplayground.entity.logging;

import com.la.springplayground.entity.EntityBase;
import java.io.Serializable;
import java.util.Collection;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Luís Alves
 */
@Entity
@Table(name = "logging_event")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "LoggingEvent.findAll", query = "SELECT l FROM LoggingEvent l"),
    @NamedQuery(name = "LoggingEvent.findByTimestmp", query = "SELECT l FROM LoggingEvent l WHERE l.timestmp = :timestmp"),
    @NamedQuery(name = "LoggingEvent.findByLoggerName", query = "SELECT l FROM LoggingEvent l WHERE l.loggerName = :loggerName"),
    @NamedQuery(name = "LoggingEvent.findByLevelString", query = "SELECT l FROM LoggingEvent l WHERE l.levelString = :levelString"),
    @NamedQuery(name = "LoggingEvent.findByThreadName", query = "SELECT l FROM LoggingEvent l WHERE l.threadName = :threadName"),
    @NamedQuery(name = "LoggingEvent.findByReferenceFlag", query = "SELECT l FROM LoggingEvent l WHERE l.referenceFlag = :referenceFlag"),
    @NamedQuery(name = "LoggingEvent.findByArg0", query = "SELECT l FROM LoggingEvent l WHERE l.arg0 = :arg0"),
    @NamedQuery(name = "LoggingEvent.findByArg1", query = "SELECT l FROM LoggingEvent l WHERE l.arg1 = :arg1"),
    @NamedQuery(name = "LoggingEvent.findByArg2", query = "SELECT l FROM LoggingEvent l WHERE l.arg2 = :arg2"),
    @NamedQuery(name = "LoggingEvent.findByArg3", query = "SELECT l FROM LoggingEvent l WHERE l.arg3 = :arg3"),
    @NamedQuery(name = "LoggingEvent.findByCallerFilename", query = "SELECT l FROM LoggingEvent l WHERE l.callerFilename = :callerFilename"),
    @NamedQuery(name = "LoggingEvent.findByCallerClass", query = "SELECT l FROM LoggingEvent l WHERE l.callerClass = :callerClass"),
    @NamedQuery(name = "LoggingEvent.findByCallerMethod", query = "SELECT l FROM LoggingEvent l WHERE l.callerMethod = :callerMethod"),
    @NamedQuery(name = "LoggingEvent.findByCallerLine", query = "SELECT l FROM LoggingEvent l WHERE l.callerLine = :callerLine"),
    @NamedQuery(name = "LoggingEvent.findByEventId", query = "SELECT l FROM LoggingEvent l WHERE l.eventId = :eventId")})
public class LoggingEvent implements EntityBase {
    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @Column(name = "timestmp")
    private long timestmp;
    @Basic(optional = false)
    @Lob
    @Column(name = "formatted_message")
    private String formattedMessage;
    @Basic(optional = false)
    @Column(name = "logger_name")
    private String loggerName;
    @Basic(optional = false)
    @Column(name = "level_string")
    private String levelString;
    @Column(name = "thread_name")
    private String threadName;
    @Column(name = "reference_flag")
    private Short referenceFlag;
    @Column(name = "arg0")
    private String arg0;
    @Column(name = "arg1")
    private String arg1;
    @Column(name = "arg2")
    private String arg2;
    @Column(name = "arg3")
    private String arg3;
    @Basic(optional = false)
    @Column(name = "caller_filename")
    private String callerFilename;
    @Basic(optional = false)
    @Column(name = "caller_class")
    private String callerClass;
    @Basic(optional = false)
    @Column(name = "caller_method")
    private String callerMethod;
    @Basic(optional = false)
    @Column(name = "caller_line")
    private String callerLine;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "event_id")
    private Long eventId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "loggingEvent")
    private Collection<LoggingEventException> loggingEventExceptionCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "loggingEvent")
    private Collection<LoggingEventProperty> loggingEventPropertyCollection;

    public LoggingEvent() {
    }

    public LoggingEvent(Long eventId) {
        this.eventId = eventId;
    }

    public LoggingEvent(Long eventId, long timestmp, String formattedMessage, String loggerName, String levelString, String callerFilename, String callerClass, String callerMethod, String callerLine) {
        this.eventId = eventId;
        this.timestmp = timestmp;
        this.formattedMessage = formattedMessage;
        this.loggerName = loggerName;
        this.levelString = levelString;
        this.callerFilename = callerFilename;
        this.callerClass = callerClass;
        this.callerMethod = callerMethod;
        this.callerLine = callerLine;
    }

    public long getTimestmp() {
        return timestmp;
    }

    public void setTimestmp(long timestmp) {
        this.timestmp = timestmp;
    }

    public String getFormattedMessage() {
        return formattedMessage;
    }

    public void setFormattedMessage(String formattedMessage) {
        this.formattedMessage = formattedMessage;
    }

    public String getLoggerName() {
        return loggerName;
    }

    public void setLoggerName(String loggerName) {
        this.loggerName = loggerName;
    }

    public String getLevelString() {
        return levelString;
    }

    public void setLevelString(String levelString) {
        this.levelString = levelString;
    }

    public String getThreadName() {
        return threadName;
    }

    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }

    public Short getReferenceFlag() {
        return referenceFlag;
    }

    public void setReferenceFlag(Short referenceFlag) {
        this.referenceFlag = referenceFlag;
    }

    public String getArg0() {
        return arg0;
    }

    public void setArg0(String arg0) {
        this.arg0 = arg0;
    }

    public String getArg1() {
        return arg1;
    }

    public void setArg1(String arg1) {
        this.arg1 = arg1;
    }

    public String getArg2() {
        return arg2;
    }

    public void setArg2(String arg2) {
        this.arg2 = arg2;
    }

    public String getArg3() {
        return arg3;
    }

    public void setArg3(String arg3) {
        this.arg3 = arg3;
    }

    public String getCallerFilename() {
        return callerFilename;
    }

    public void setCallerFilename(String callerFilename) {
        this.callerFilename = callerFilename;
    }

    public String getCallerClass() {
        return callerClass;
    }

    public void setCallerClass(String callerClass) {
        this.callerClass = callerClass;
    }

    public String getCallerMethod() {
        return callerMethod;
    }

    public void setCallerMethod(String callerMethod) {
        this.callerMethod = callerMethod;
    }

    public String getCallerLine() {
        return callerLine;
    }

    public void setCallerLine(String callerLine) {
        this.callerLine = callerLine;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    @XmlTransient
    public Collection<LoggingEventException> getLoggingEventExceptionCollection() {
        return loggingEventExceptionCollection;
    }

    public void setLoggingEventExceptionCollection(Collection<LoggingEventException> loggingEventExceptionCollection) {
        this.loggingEventExceptionCollection = loggingEventExceptionCollection;
    }

    @XmlTransient
    public Collection<LoggingEventProperty> getLoggingEventPropertyCollection() {
        return loggingEventPropertyCollection;
    }

    public void setLoggingEventPropertyCollection(Collection<LoggingEventProperty> loggingEventPropertyCollection) {
        this.loggingEventPropertyCollection = loggingEventPropertyCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (eventId != null ? eventId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof LoggingEvent)) {
            return false;
        }
        LoggingEvent other = (LoggingEvent) object;
        if ((this.eventId == null && other.eventId != null) || (this.eventId != null && !this.eventId.equals(other.eventId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.la.springplayground.entity.logging.LoggingEvent[ eventId=" + eventId + " ]";
    }

    public Object getId() {
        return eventId;
    }
    
}
