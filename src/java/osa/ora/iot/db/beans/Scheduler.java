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
@Table(name = "scheduler")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Scheduler.findAll", query = "SELECT s FROM Scheduler s")
    , @NamedQuery(name = "Scheduler.findById", query = "SELECT s FROM Scheduler s WHERE s.id = :id")
    , @NamedQuery(name = "Scheduler.findBySchedulerName", query = "SELECT s FROM Scheduler s WHERE s.schedulerName = :schedulerName")
    , @NamedQuery(name = "Scheduler.findByTriggeringDay", query = "SELECT s FROM Scheduler s WHERE s.triggeringDay = :triggeringDay")
    , @NamedQuery(name = "Scheduler.findByTriggeringHour", query = "SELECT s FROM Scheduler s WHERE s.triggeringHour = :triggeringHour")
    , @NamedQuery(name = "Scheduler.findByTriggeringMin", query = "SELECT s FROM Scheduler s WHERE s.triggeringHour = :triggeringMin")
    , @NamedQuery(name = "Scheduler.findByTargetDeviceId", query = "SELECT s FROM Scheduler s WHERE s.targetDeviceId = :targetDeviceId")
    , @NamedQuery(name = "Scheduler.findByTargetAction", query = "SELECT s FROM Scheduler s WHERE s.targetAction = :targetAction")
    , @NamedQuery(name = "Scheduler.findBySchedulerStatus", query = "SELECT s FROM Scheduler s WHERE s.schedulerStatus = :schedulerStatus")
    , @NamedQuery(name = "Scheduler.findByCreationDate", query = "SELECT s FROM Scheduler s WHERE s.creationDate = :creationDate")
    , @NamedQuery(name = "Scheduler.findByUpdateTime", query = "SELECT s FROM Scheduler s WHERE s.updateTime = :updateTime")
    , @NamedQuery(name = "Scheduler.findByIdentity", query = "SELECT s FROM Scheduler s WHERE s.identity = :identity")})
public class Scheduler implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 30)
    @Column(name = "scheduler_name")
    private String schedulerName;
    @Column(name = "triggering_day")
    private Integer triggeringDay;
    @Column(name = "triggering_hour")
    private Integer triggeringHour;
    @Column(name = "triggering_min")
    private Integer triggeringMin;
    @Size(max = 30)
    @Column(name = "target_device_id")
    private String targetDeviceId;
    @Column(name = "target_action")
    private Integer targetAction;
    @Column(name = "scheduler_status")
    private Integer schedulerStatus;
    @Column(name = "creation_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    @Column(name = "update_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;
    @Column(name = "identity")
    private Integer identity;
    @Transient
    private Devices targetDevice;

    public Scheduler() {
    }

    public Scheduler(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSchedulerName() {
        return schedulerName;
    }

    public void setSchedulerName(String schedulerName) {
        this.schedulerName = schedulerName;
    }

    public Integer getTriggeringDay() {
        return triggeringDay;
    }

    public void setTriggeringDay(Integer triggeringDay) {
        this.triggeringDay = triggeringDay;
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

    public Integer getSchedulerStatus() {
        return schedulerStatus;
    }

    public void setSchedulerStatus(Integer schedulerStatus) {
        this.schedulerStatus = schedulerStatus;
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
        if (!(object instanceof Scheduler)) {
            return false;
        }
        Scheduler other = (Scheduler) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "osa.ora.iot.db.beans.Scheduler[ id=" + id + " ]";
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

    public Integer getTriggeringHour() {
        return triggeringHour;
    }

    public void setTriggeringHour(Integer triggeringHour) {
        this.triggeringHour = triggeringHour;
    }

    public Integer getTriggeringMin() {
        return triggeringMin;
    }

    public void setTriggeringMin(Integer triggeringMin) {
        this.triggeringMin = triggeringMin;
    }
    
}
