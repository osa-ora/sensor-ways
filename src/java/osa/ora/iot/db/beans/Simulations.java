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
@Table(name = "simulations")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Simulations.findAll", query = "SELECT s FROM Simulations s")
    , @NamedQuery(name = "Simulations.findById", query = "SELECT s FROM Simulations s WHERE s.id = :id")
    , @NamedQuery(name = "Simulations.findByName", query = "SELECT s FROM Simulations s WHERE s.name = :name")
    , @NamedQuery(name = "Simulations.findByDeviceId", query = "SELECT s FROM Simulations s WHERE s.deviceId = :deviceId")
    , @NamedQuery(name = "Simulations.findBySimulationStatus", query = "SELECT s FROM Simulations s WHERE s.simulationStatus = :simulationStatus")
    , @NamedQuery(name = "Simulations.findByAlertProbability", query = "SELECT s FROM Simulations s WHERE s.alertProbability = :alertProbability")
    , @NamedQuery(name = "Simulations.findByErrorProbanility", query = "SELECT s FROM Simulations s WHERE s.errorProbanility = :errorProbanility")
    , @NamedQuery(name = "Simulations.findByOriginalDeviceStatus", query = "SELECT s FROM Simulations s WHERE s.originalDeviceStatus = :originalDeviceStatus")
    , @NamedQuery(name = "Simulations.findByDuration", query = "SELECT s FROM Simulations s WHERE s.duration = :duration")
    , @NamedQuery(name = "Simulations.findByStartTime", query = "SELECT s FROM Simulations s WHERE s.startTime = :startTime")
    , @NamedQuery(name = "Simulations.findByIdentityId", query = "SELECT s FROM Simulations s WHERE s.identityId = :identityId")
    , @NamedQuery(name = "Simulations.findByDevice1Status", query = "SELECT s FROM Simulations s WHERE s.device1Status = :device1Status")
    , @NamedQuery(name = "Simulations.findByDevice2Status", query = "SELECT s FROM Simulations s WHERE s.device2Status = :device2Status")
    , @NamedQuery(name = "Simulations.findByLoopCounter", query = "SELECT s FROM Simulations s WHERE s.loopCounter = :loopCounter")})
public class Simulations implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 45)
    @Column(name = "name")
    private String name;
    @Size(max = 30)
    @Column(name = "device_id")
    private String deviceId;
    @Column(name = "simulation_status")
    private Integer simulationStatus;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "alert_probability")
    private Float alertProbability;
    @Column(name = "error_probanility")
    private Float errorProbanility;
    @Column(name = "original_device_status")
    private Integer originalDeviceStatus;
    @Column(name = "duration")
    private Integer duration;
    @Column(name = "start_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startTime;
    @Column(name = "identity_id")
    private Integer identityId;
    @Column(name = "device1_status")
    private Integer device1Status;
    @Column(name = "device2_status")
    private Integer device2Status;
    @Column(name = "loop_counter")
    private Integer loopCounter;
    @Transient
    private Devices device;

    public Simulations() {
    }

    public Simulations(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public Integer getSimulationStatus() {
        return simulationStatus;
    }

    public void setSimulationStatus(Integer simulationStatus) {
        this.simulationStatus = simulationStatus;
    }

    public Float getAlertProbability() {
        return alertProbability;
    }

    public void setAlertProbability(Float alertProbability) {
        this.alertProbability = alertProbability;
    }

    public Float getErrorProbanility() {
        return errorProbanility;
    }

    public void setErrorProbanility(Float errorProbanility) {
        this.errorProbanility = errorProbanility;
    }

    public Integer getOriginalDeviceStatus() {
        return originalDeviceStatus;
    }

    public void setOriginalDeviceStatus(Integer originalDeviceStatus) {
        this.originalDeviceStatus = originalDeviceStatus;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Integer getIdentityId() {
        return identityId;
    }

    public void setIdentityId(Integer identityId) {
        this.identityId = identityId;
    }

    public Integer getDevice1Status() {
        return device1Status;
    }

    public void setDevice1Status(Integer device1Status) {
        this.device1Status = device1Status;
    }

    public Integer getDevice2Status() {
        return device2Status;
    }

    public void setDevice2Status(Integer device2Status) {
        this.device2Status = device2Status;
    }

    public Integer getLoopCounter() {
        return loopCounter;
    }

    public void setLoopCounter(Integer loopCounter) {
        this.loopCounter = loopCounter;
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
        if (!(object instanceof Simulations)) {
            return false;
        }
        Simulations other = (Simulations) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "osa.ora.iot.db.beans.Simulations[ id=" + id + " ]";
    }

    /**
     * @return the device
     */
    public Devices getDevice() {
        return device;
    }

    /**
     * @param device the device to set
     */
    public void setDevice(Devices device) {
        this.device = device;
    }
    
}
