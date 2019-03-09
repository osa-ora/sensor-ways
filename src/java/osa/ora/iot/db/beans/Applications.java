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
import javax.persistence.Lob;
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
@Table(name = "applications")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Applications.findAll", query = "SELECT a FROM Applications a")
    , @NamedQuery(name = "Applications.findById", query = "SELECT a FROM Applications a WHERE a.id = :id")
    , @NamedQuery(name = "Applications.findByName", query = "SELECT a FROM Applications a WHERE a.name = :name")
    , @NamedQuery(name = "Applications.findByIdentityId", query = "SELECT a FROM Applications a WHERE a.identityId = :identityId")
    , @NamedQuery(name = "Applications.findByBannerName", query = "SELECT a FROM Applications a WHERE a.bannerName = :bannerName")
    , @NamedQuery(name = "Applications.findByStatus", query = "SELECT a FROM Applications a WHERE a.status = :status")
    , @NamedQuery(name = "Applications.findByLoginRequired", query = "SELECT a FROM Applications a WHERE a.loginRequired = :loginRequired")
    , @NamedQuery(name = "Applications.findByApplicationType", query = "SELECT a FROM Applications a WHERE a.applicationType = :applicationType")
    , @NamedQuery(name = "Applications.findByContactEmail", query = "SELECT a FROM Applications a WHERE a.contactEmail = :contactEmail")
    , @NamedQuery(name = "Applications.findByContactMobile", query = "SELECT a FROM Applications a WHERE a.contactMobile = :contactMobile")
    , @NamedQuery(name = "Applications.findByAlertEnabled", query = "SELECT a FROM Applications a WHERE a.alertEnabled = :alertEnabled")
    , @NamedQuery(name = "Applications.findByStartDate", query = "SELECT a FROM Applications a WHERE a.startDate = :startDate")
    , @NamedQuery(name = "Applications.findByCreatedDate", query = "SELECT a FROM Applications a WHERE a.createdDate = :createdDate")
    , @NamedQuery(name = "Applications.findByTotalMessages", query = "SELECT a FROM Applications a WHERE a.totalMessages = :totalMessages")
    , @NamedQuery(name = "Applications.findByTotalAlerts", query = "SELECT a FROM Applications a WHERE a.totalAlerts = :totalAlerts")
    , @NamedQuery(name = "Applications.findByTotalDevices", query = "SELECT a FROM Applications a WHERE a.totalDevices = :totalDevices")
    , @NamedQuery(name = "Applications.findByProjectScope", query = "SELECT a FROM Applications a WHERE a.projectScope = :projectScope")
    , @NamedQuery(name = "Applications.findByDeviceGroups", query = "SELECT a FROM Applications a WHERE a.deviceGroups = :deviceGroups")})
public class Applications implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 150)
    @Column(name = "name")
    private String name;
    @Column(name = "identity_id")
    private Integer identityId;
    @Lob
    @Column(name = "banner")
    private byte[] banner;
    @Size(max = 60)
    @Column(name = "banner_name")
    private String bannerName;
    @Column(name = "status")
    private Integer status;
    @Column(name = "login_required")
    private Integer loginRequired;
    @Column(name = "application_type")
    private Integer applicationType;
    @Size(max = 120)
    @Column(name = "contact_email")
    private String contactEmail;
    @Size(max = 45)
    @Column(name = "contact_mobile")
    private String contactMobile;
    @Column(name = "alert_enabled")
    private Integer alertEnabled;
    @Column(name = "start_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;
    @Column(name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    @Column(name = "total_messages")
    private Integer totalMessages;
    @Column(name = "total_alerts")
    private Integer totalAlerts;
    @Column(name = "total_devices")
    private Integer totalDevices;
    @Size(max = 300)
    @Column(name = "project_scope")
    private String projectScope;
    @Size(max = 500)
    @Column(name = "device_groups")
    private String deviceGroups;

    public Applications() {
    }

    public Applications(Integer id) {
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

    public Integer getIdentityId() {
        return identityId;
    }

    public void setIdentityId(Integer identityId) {
        this.identityId = identityId;
    }

    public byte[] getBanner() {
        return banner;
    }

    public void setBanner(byte[] banner) {
        this.banner = banner;
    }

    public String getBannerName() {
        return bannerName;
    }

    public void setBannerName(String bannerName) {
        this.bannerName = bannerName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getLoginRequired() {
        return loginRequired;
    }

    public void setLoginRequired(Integer loginRequired) {
        this.loginRequired = loginRequired;
    }

    public Integer getApplicationType() {
        return applicationType;
    }

    public void setApplicationType(Integer applicationType) {
        this.applicationType = applicationType;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getContactMobile() {
        return contactMobile;
    }

    public void setContactMobile(String contactMobile) {
        this.contactMobile = contactMobile;
    }

    public Integer getAlertEnabled() {
        return alertEnabled;
    }

    public void setAlertEnabled(Integer alertEnabled) {
        this.alertEnabled = alertEnabled;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Integer getTotalMessages() {
        return totalMessages;
    }

    public void setTotalMessages(Integer totalMessages) {
        this.totalMessages = totalMessages;
    }

    public Integer getTotalAlerts() {
        return totalAlerts;
    }

    public void setTotalAlerts(Integer totalAlerts) {
        this.totalAlerts = totalAlerts;
    }

    public Integer getTotalDevices() {
        return totalDevices;
    }

    public void setTotalDevices(Integer totalDevices) {
        this.totalDevices = totalDevices;
    }

    public String getProjectScope() {
        return projectScope;
    }

    public void setProjectScope(String projectScope) {
        this.projectScope = projectScope;
    }

    public String getDeviceGroups() {
        return deviceGroups;
    }

    public void setDeviceGroups(String deviceGroups) {
        this.deviceGroups = deviceGroups;
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
        if (!(object instanceof Applications)) {
            return false;
        }
        Applications other = (Applications) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "osa.ora.iot.db.beans.Applications[ id=" + id + " ]";
    }
    
}
