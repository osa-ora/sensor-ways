/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package osa.ora.iot.db.beans;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ooransa
 */
@Entity
@Table(name = "email_templates")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EmailTemplates.findAll", query = "SELECT e FROM EmailTemplates e")
    , @NamedQuery(name = "EmailTemplates.findByActionId", query = "SELECT e FROM EmailTemplates e WHERE e.actionId = :actionId")
    , @NamedQuery(name = "EmailTemplates.findByTitleEn", query = "SELECT e FROM EmailTemplates e WHERE e.titleEn = :titleEn")
    , @NamedQuery(name = "EmailTemplates.findByTitleAr", query = "SELECT e FROM EmailTemplates e WHERE e.titleAr = :titleAr")})
public class EmailTemplates implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "action_id")
    private Integer actionId;
    @Size(max = 255)
    @Column(name = "title_en")
    private String titleEn;
    @Size(max = 255)
    @Column(name = "title_ar")
    private String titleAr;
    @Lob
    @Size(max = 65535)
    @Column(name = "body_en")
    private String bodyEn;
    @Lob
    @Size(max = 65535)
    @Column(name = "body_ar")
    private String bodyAr;

    public EmailTemplates() {
    }

    public EmailTemplates(Integer actionId) {
        this.actionId = actionId;
    }

    public Integer getActionId() {
        return actionId;
    }

    public void setActionId(Integer actionId) {
        this.actionId = actionId;
    }

    public String getTitleEn() {
        return titleEn;
    }

    public void setTitleEn(String titleEn) {
        this.titleEn = titleEn;
    }

    public String getTitleAr() {
        return titleAr;
    }

    public void setTitleAr(String titleAr) {
        this.titleAr = titleAr;
    }

    public String getBodyEn() {
        return bodyEn;
    }

    public void setBodyEn(String bodyEn) {
        this.bodyEn = bodyEn;
    }

    public String getBodyAr() {
        return bodyAr;
    }

    public void setBodyAr(String bodyAr) {
        this.bodyAr = bodyAr;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (actionId != null ? actionId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EmailTemplates)) {
            return false;
        }
        EmailTemplates other = (EmailTemplates) object;
        if ((this.actionId == null && other.actionId != null) || (this.actionId != null && !this.actionId.equals(other.actionId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "osa.ora.iot.db.beans.EmailTemplates[ actionId=" + actionId + " ]";
    }
    
}
