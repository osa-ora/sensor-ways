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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ooransa
 */
@Entity
@Table(name = "workflows")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Workflows.findAll", query = "SELECT w FROM Workflows w")
    , @NamedQuery(name = "Workflows.findById", query = "SELECT w FROM Workflows w WHERE w.id = :id")
    , @NamedQuery(name = "Workflows.findByWorkflowName", query = "SELECT w FROM Workflows w WHERE w.workflowName = :workflowName")
    , @NamedQuery(name = "Workflows.findByTriggeringId", query = "SELECT w FROM Workflows w WHERE w.triggeringId = :triggeringId")
    , @NamedQuery(name = "Workflows.findByTriggerType", query = "SELECT w FROM Workflows w WHERE w.triggerType = :triggerType")
    , @NamedQuery(name = "Workflows.findByTriggerEvent", query = "SELECT w FROM Workflows w WHERE w.triggerEvent = :triggerEvent")
    , @NamedQuery(name = "Workflows.findByTargetDeviceId", query = "SELECT w FROM Workflows w WHERE w.targetDeviceId = :targetDeviceId")
    , @NamedQuery(name = "Workflows.findByTargetAction", query = "SELECT w FROM Workflows w WHERE w.targetAction = :targetAction")
    , @NamedQuery(name = "Workflows.findByWorkflowStatus", query = "SELECT w FROM Workflows w WHERE w.workflowStatus = :workflowStatus")
    , @NamedQuery(name = "Workflows.findByCreationDate", query = "SELECT w FROM Workflows w WHERE w.creationDate = :creationDate")
    , @NamedQuery(name = "Workflows.findByUpdateTime", query = "SELECT w FROM Workflows w WHERE w.updateTime = :updateTime")
    , @NamedQuery(name = "Workflows.findByIdentity", query = "SELECT w FROM Workflows w WHERE w.identity = :identity")})
public class Workflows implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 30)
    @Column(name = "workflow_name")
    private String workflowName;
    @Size(max = 30)
    @Column(name = "triggering_id")
    private String triggeringId;
    @Column(name = "trigger_type")
    private Integer triggerType;
    @Column(name = "trigger_event")
    private Integer triggerEvent;
    @Size(max = 30)
    @Column(name = "target_device_id")
    private String targetDeviceId;
    @Column(name = "target_action")
    private Integer targetAction;
    @Column(name = "workflow_status")
    private Integer workflowStatus;
    @Column(name = "creation_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    @Column(name = "update_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;
    @Column(name = "identity")
    private Integer identity;
    @Transient
    private Devices sourceDevice;
    @Transient
    private DeviceGroup sourceDeviceGroup;
    @Transient
    private Devices targetDevice;
    public Workflows() {
    }

    public Workflows(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getWorkflowName() {
        return workflowName;
    }

    public void setWorkflowName(String workflowName) {
        this.workflowName = workflowName;
    }

    public String getTriggeringId() {
        return triggeringId;
    }

    public void setTriggeringId(String triggeringId) {
        this.triggeringId = triggeringId;
    }

    public Integer getTriggerType() {
        return triggerType;
    }

    public void setTriggerType(Integer triggerType) {
        this.triggerType = triggerType;
    }

    public Integer getTriggerEvent() {
        return triggerEvent;
    }

    public void setTriggerEvent(Integer triggerEvent) {
        this.triggerEvent = triggerEvent;
    }

    public String getTargetDeviceId() {
        return targetDeviceId;
    }

    public void setTargetDeviceId(String targetDeviceId) {
        this.targetDeviceId = targetDeviceId;
    }

    public Integer getTargetAction() {
        return targetAction;
    }

    public void setTargetAction(Integer targetAction) {
        this.targetAction = targetAction;
    }

    public Integer getWorkflowStatus() {
        return workflowStatus;
    }

    public void setWorkflowStatus(Integer workflowStatus) {
        this.workflowStatus = workflowStatus;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getIdentity() {
        return identity;
    }

    public void setIdentity(Integer identity) {
        this.identity = identity;
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
        if (!(object instanceof Workflows)) {
            return false;
        }
        Workflows other = (Workflows) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "osa.ora.iot.db.beans.Workflows[ id=" + id + " ]";
    }

    /**
     * @return the sourceDevice
     */
    public Devices getSourceDevice() {
        return sourceDevice;
    }

    /**
     * @param sourceDevice the sourceDevice to set
     */
    public void setSourceDevice(Devices sourceDevice) {
        this.sourceDevice = sourceDevice;
    }

    /**
     * @return the sourceDeviceGroup
     */
    public DeviceGroup getSourceDeviceGroup() {
        return sourceDeviceGroup;
    }

    /**
     * @param sourceDeviceGroup the sourceDeviceGroup to set
     */
    public void setSourceDeviceGroup(DeviceGroup sourceDeviceGroup) {
        this.sourceDeviceGroup = sourceDeviceGroup;
    }

    /**
     * @return the targetDevice
     */
    public Devices getTargetDevice() {
        return targetDevice;
    }

    /**
     * @param targetDevice the targetDevice to set
     */
    public void setTargetDevice(Devices targetDevice) {
        this.targetDevice = targetDevice;
    }
    
}
