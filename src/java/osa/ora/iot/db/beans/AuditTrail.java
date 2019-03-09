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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ooransa
 */
@Entity
@Table(name = "audit_trail")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AuditTrail.findAll", query = "SELECT a FROM AuditTrail a")
    , @NamedQuery(name = "AuditTrail.findById", query = "SELECT a FROM AuditTrail a WHERE a.id = :id")
    , @NamedQuery(name = "AuditTrail.findByActionId", query = "SELECT a FROM AuditTrail a WHERE a.actionId = :actionId")
    , @NamedQuery(name = "AuditTrail.findByByUserId", query = "SELECT a FROM AuditTrail a WHERE a.byUserId = :byUserId")
    , @NamedQuery(name = "AuditTrail.findByPerformedAt", query = "SELECT a FROM AuditTrail a WHERE a.performedAt = :performedAt")
    , @NamedQuery(name = "AuditTrail.findByParameters", query = "SELECT a FROM AuditTrail a WHERE a.parameters = :parameters")})
public class AuditTrail implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "action_id")
    private Integer actionId;
    @Column(name = "by_user_id")
    private Integer byUserId;
    @Column(name = "performed_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date performedAt;
    @Size(max = 100)
    @Column(name = "parameters")
    private String parameters;

    public AuditTrail() {
    }

    public AuditTrail(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getActionId() {
        return actionId;
    }

    public void setActionId(Integer actionId) {
        this.actionId = actionId;
    }

    public Integer getByUserId() {
        return byUserId;
    }

    public void setByUserId(Integer byUserId) {
        this.byUserId = byUserId;
    }

    public Date getPerformedAt() {
        return performedAt;
    }

    public void setPerformedAt(Date performedAt) {
        this.performedAt = performedAt;
    }

    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
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
        if (!(object instanceof AuditTrail)) {
            return false;
        }
        AuditTrail other = (AuditTrail) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "osa.ora.iot.db.beans.AuditTrail[ id=" + id + " ]";
    }
    
}
