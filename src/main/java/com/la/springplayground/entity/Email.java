/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.la.springplayground.entity;

import java.io.Serializable;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Luís Alves
 */
@Entity
@Table(name = "email")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Email.findAll", query = "SELECT e FROM Email e"),
    @NamedQuery(name = "Email.findById", query = "SELECT e FROM Email e WHERE e.id = :id"),
    @NamedQuery(name = "Email.findByBody", query = "SELECT e FROM Email e WHERE e.body = :body"),
    @NamedQuery(name = "Email.findBySubject", query = "SELECT e FROM Email e WHERE e.subject = :subject"),
    @NamedQuery(name = "Email.findByFrom", query = "SELECT e FROM Email e WHERE e.from = :from"),
    @NamedQuery(name = "Email.findByTo", query = "SELECT e FROM Email e WHERE e.to = :to"),
    @NamedQuery(name = "Email.findByAttempts", query = "SELECT e FROM Email e WHERE e.attempts = :attempts")})
public class Email implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @Column(name = "body")
    private String body;
    @Basic(optional = false)
    @Column(name = "subject")
    private String subject;
    @Basic(optional = false)
    @Column(name = "from")
    private String from;
    @Basic(optional = false)
    @Column(name = "to")
    private String to;
    @Basic(optional = false)
    @Column(name = "attempts")
    private int attempts;

    public Email() {
    }

    public Email(Long id) {
        this.id = id;
    }

    public Email(Long id, String body, String subject, String from, String to, int attempts) {
        this.id = id;
        this.body = body;
        this.subject = subject;
        this.from = from;
        this.to = to;
        this.attempts = attempts;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public int getAttempts() {
        return attempts;
    }

    public void setAttempts(int attempts) {
        this.attempts = attempts;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Email)) {
            return false;
        }
        Email other = (Email) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.la.springplayground.entity.Email[ id=" + id + " ]";
    }
    
}
