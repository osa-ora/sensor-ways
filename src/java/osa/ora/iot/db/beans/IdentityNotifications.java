/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package osa.ora.iot.db.beans;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ooransa
 */
@Entity
@Table(name = "identity_notifications")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "IdentityNotifications.findAll", query = "SELECT i FROM IdentityNotifications i")
    , @NamedQuery(name = "IdentityNotifications.findById", query = "SELECT i FROM IdentityNotifications i WHERE i.id = :id")
    , @NamedQuery(name = "IdentityNotifications.findByIdentityId", query = "SELECT i FROM IdentityNotifications i WHERE i.identityId = :identityId")
    , @NamedQuery(name = "IdentityNotifications.findByTemplateId", query = "SELECT i FROM IdentityNotifications i WHERE i.templateId = :templateId")
    , @NamedQuery(name = "IdentityNotifications.findByParams", query = "SELECT i FROM IdentityNotifications i WHERE i.params = :params")
    , @NamedQuery(name = "IdentityNotifications.findByReadFlag", query = "SELECT i FROM IdentityNotifications i WHERE i.readFlag = :readFlag")
    , @NamedQuery(name = "IdentityNotifications.findByNotifiedOn", query = "SELECT i FROM IdentityNotifications i WHERE i.notifiedOn = :notifiedOn")
    , @NamedQuery(name = "IdentityNotifications.findByUniqueIdentiter", query = "SELECT i FROM IdentityNotifications i WHERE i.uniqueIdentiter = :uniqueIdentiter")})
public class IdentityNotifications implements Serializable {

    /**
     * @return the template
     */
    public EmailTemplates getTemplate() {
        return template;
    }

    /**
     * @param template the template to set
     */
    public void setTemplate(EmailTemplates template) {
        this.template = template;
    }

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "identity_id")
    private int identityId;
    @Column(name = "template_id")
    private Integer templateId;
    @Size(max = 140)
    @Column(name = "params")
    private String params;
    @Column(name = "read_flag")
    private Short readFlag;
    @Column(name = "notified_on")
    @Temporal(TemporalType.TIMESTAMP)
    private Date notifiedOn;
    @Size(max = 30)
    @Column(name = "unique_identiter")
    private String uniqueIdentiter;
    @Transient
    private EmailTemplates template;
    public IdentityNotifications() {
    }

    public IdentityNotifications(Integer id) {
        this.id = id;
    }

    public IdentityNotifications(Integer id, int identityId) {
        this.id = id;
        this.identityId = identityId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getIdentityId() {
        return identityId;
    }

    public void setIdentityId(int identityId) {
        this.identityId = identityId;
    }

    public Integer getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Integer templateId) {
        this.templateId = templateId;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public Short getReadFlag() {
        return readFlag;
    }

    public void setReadFlag(Short readFlag) {
        this.readFlag = readFlag;
    }

    public Date getNotifiedOn() {
        return notifiedOn;
    }

    public void setNotifiedOn(Date notifiedOn) {
        this.notifiedOn = notifiedOn;
    }

    public String getUniqueIdentiter() {
        return uniqueIdentiter;
    }

    public void setUniqueIdentiter(String uniqueIdentiter) {
        this.uniqueIdentiter = uniqueIdentiter;
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
        if (!(object instanceof IdentityNotifications)) {
            return false;
        }
        IdentityNotifications other = (IdentityNotifications) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "osa.ora.iot.db.beans.IdentityNotifications[ id=" + id + " ]";
    }
    
}
