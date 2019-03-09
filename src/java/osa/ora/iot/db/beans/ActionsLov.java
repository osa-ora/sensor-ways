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
@Table(name = "actions_lov")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ActionsLov.findAll", query = "SELECT a FROM ActionsLov a")
    , @NamedQuery(name = "ActionsLov.findByActionId", query = "SELECT a FROM ActionsLov a WHERE a.actionId = :actionId")
    , @NamedQuery(name = "ActionsLov.findByActionsName", query = "SELECT a FROM ActionsLov a WHERE a.actionsName = :actionsName")})
public class ActionsLov implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "action_id")
    private Integer actionId;
    @Size(max = 100)
    @Column(name = "actions_name")
    private String actionsName;

    public ActionsLov() {
    }

    public ActionsLov(Integer actionId) {
        this.actionId = actionId;
    }

    public Integer getActionId() {
        return actionId;
    }

    public void setActionId(Integer actionId) {
        this.actionId = actionId;
    }

    public String getActionsName() {
        return actionsName;
    }

    public void setActionsName(String actionsName) {
        this.actionsName = actionsName;
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
        if (!(object instanceof ActionsLov)) {
            return false;
        }
        ActionsLov other = (ActionsLov) object;
        if ((this.actionId == null && other.actionId != null) || (this.actionId != null && !this.actionId.equals(other.actionId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "osa.ora.iot.db.beans.ActionsLov[ actionId=" + actionId + " ]";
    }
    
}
