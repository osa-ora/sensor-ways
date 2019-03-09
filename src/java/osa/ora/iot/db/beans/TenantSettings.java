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
@Table(name = "tenant_settings")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TenantSettings.findAll", query = "SELECT t FROM TenantSettings t")
    , @NamedQuery(name = "TenantSettings.findById", query = "SELECT t FROM TenantSettings t WHERE t.id = :id")
    , @NamedQuery(name = "TenantSettings.findByIdentity", query = "SELECT t FROM TenantSettings t WHERE t.identity = :identity")
    , @NamedQuery(name = "TenantSettings.findByIdentityName", query = "SELECT t FROM TenantSettings t WHERE t.identityName = :identityName")
    , @NamedQuery(name = "TenantSettings.findByTenantStatus", query = "SELECT t FROM TenantSettings t WHERE t.tenantStatus = :tenantStatus")
    , @NamedQuery(name = "TenantSettings.findByMaxRetainedMessages", query = "SELECT t FROM TenantSettings t WHERE t.maxRetainedMessages = :maxRetainedMessages")
    , @NamedQuery(name = "TenantSettings.findByPingInterval", query = "SELECT t FROM TenantSettings t WHERE t.pingInterval = :pingInterval")
    , @NamedQuery(name = "TenantSettings.findByUpdateInterval", query = "SELECT t FROM TenantSettings t WHERE t.updateInterval = :updateInterval")
    , @NamedQuery(name = "TenantSettings.findByTimezone", query = "SELECT t FROM TenantSettings t WHERE t.timezone = :timezone")
    , @NamedQuery(name = "TenantSettings.findByAlertEmailMessage", query = "SELECT t FROM TenantSettings t WHERE t.alertEmailMessage = :alertEmailMessage")
    , @NamedQuery(name = "TenantSettings.findByUpdateTime", query = "SELECT t FROM TenantSettings t WHERE t.updateTime = :updateTime")
    , @NamedQuery(name = "TenantSettings.findByMaxUsers", query = "SELECT t FROM TenantSettings t WHERE t.maxUsers = :maxUsers")
    , @NamedQuery(name = "TenantSettings.findByMaxDevices", query = "SELECT t FROM TenantSettings t WHERE t.maxDevices = :maxDevices")
    , @NamedQuery(name = "TenantSettings.findByAlertEmailLanguage", query = "SELECT t FROM TenantSettings t WHERE t.alertEmailLanguage = :alertEmailLanguage")
    , @NamedQuery(name = "TenantSettings.findByAlertEmailOnline", query = "SELECT t FROM TenantSettings t WHERE t.alertEmailOnline = :alertEmailOnline")
    , @NamedQuery(name = "TenantSettings.findByAlertEmailOffline", query = "SELECT t FROM TenantSettings t WHERE t.alertEmailOffline = :alertEmailOffline")
    , @NamedQuery(name = "TenantSettings.findByDeviceRegistrationEmail", query = "SELECT t FROM TenantSettings t WHERE t.deviceRegistrationEmail = :deviceRegistrationEmail")
    , @NamedQuery(name = "TenantSettings.findByDeviceUpdateEmail", query = "SELECT t FROM TenantSettings t WHERE t.deviceUpdateEmail = :deviceUpdateEmail")
    , @NamedQuery(name = "TenantSettings.findByAlertGracePeriod", query = "SELECT t FROM TenantSettings t WHERE t.alertGracePeriod = :alertGracePeriod")
    , @NamedQuery(name = "TenantSettings.findBySmsQouta", query = "SELECT t FROM TenantSettings t WHERE t.smsQouta = :smsQouta")
    , @NamedQuery(name = "TenantSettings.findBySmsConsumed", query = "SELECT t FROM TenantSettings t WHERE t.smsConsumed = :smsConsumed")
    , @NamedQuery(name = "TenantSettings.findByFaultNotificationEmail", query = "SELECT t FROM TenantSettings t WHERE t.faultNotificationEmail = :faultNotificationEmail")
    , @NamedQuery(name = "TenantSettings.findByAdminUserEmail", query = "SELECT t FROM TenantSettings t WHERE t.adminUserEmail = :adminUserEmail")
    , @NamedQuery(name = "TenantSettings.findByPurgeAfter", query = "SELECT t FROM TenantSettings t WHERE t.purgeAfter = :purgeAfter")
    , @NamedQuery(name = "TenantSettings.findByAlertDeviceIpChange", query = "SELECT t FROM TenantSettings t WHERE t.alertDeviceIpChange = :alertDeviceIpChange")
    , @NamedQuery(name = "TenantSettings.findByAlertFirmwareAvailable", query = "SELECT t FROM TenantSettings t WHERE t.alertFirmwareAvailable = :alertFirmwareAvailable")})
public class TenantSettings implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "identity")
    private Integer identity;
    @Size(max = 100)
    @Column(name = "identity_name")
    private String identityName;
    @Column(name = "tenant_status")
    private Integer tenantStatus;
    @Column(name = "max_retained_messages")
    private Integer maxRetainedMessages;
    @Column(name = "ping_interval")
    private Integer pingInterval;
    @Column(name = "update_interval")
    private Integer updateInterval;
    @Size(max = 20)
    @Column(name = "timezone")
    private String timezone;
    @Column(name = "alert_email_message")
    private Integer alertEmailMessage;
    @Column(name = "update_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;
    @Column(name = "max_users")
    private Integer maxUsers;
    @Column(name = "max_devices")
    private Integer maxDevices;
    @Column(name = "alert_email_language")
    private Integer alertEmailLanguage;
    @Column(name = "alert_email_online")
    private Integer alertEmailOnline;
    @Column(name = "alert_email_offline")
    private Integer alertEmailOffline;
    @Column(name = "device_registration_email")
    private Integer deviceRegistrationEmail;
    @Column(name = "device_update_email")
    private Integer deviceUpdateEmail;
    @Column(name = "alert_grace_period")
    private Integer alertGracePeriod;
    @Column(name = "sms_qouta")
    private Integer smsQouta;
    @Column(name = "sms_consumed")
    private Integer smsConsumed;
    @Column(name = "fault_notification_email")
    private Integer faultNotificationEmail;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "admin_user_email")
    private String adminUserEmail;
    @Column(name = "purge_after")
    private Integer purgeAfter;
    @Column(name = "alert_device_ip_change")
    private Integer alertDeviceIpChange;
    @Column(name = "alert_firmware_available")
    private Integer alertFirmwareAvailable;
    @Transient
    private Integer currentUsers;
    @Transient
    private Integer currentDevices;
    public TenantSettings() {
    }

    public TenantSettings(Integer id) {
        this.id = id;
    }

    public TenantSettings(Integer id, String adminUserEmail) {
        this.id = id;
        this.adminUserEmail = adminUserEmail;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdentity() {
        return identity;
    }

    public void setIdentity(Integer identity) {
        this.identity = identity;
    }

    public String getIdentityName() {
        return identityName;
    }

    public void setIdentityName(String identityName) {
        this.identityName = identityName;
    }

    public Integer getTenantStatus() {
        return tenantStatus;
    }

    public void setTenantStatus(Integer tenantStatus) {
        this.tenantStatus = tenantStatus;
    }

    public Integer getMaxRetainedMessages() {
        return maxRetainedMessages;
    }

    public void setMaxRetainedMessages(Integer maxRetainedMessages) {
        this.maxRetainedMessages = maxRetainedMessages;
    }

    public Integer getPingInterval() {
        return pingInterval;
    }

    public void setPingInterval(Integer pingInterval) {
        this.pingInterval = pingInterval;
    }

    public Integer getUpdateInterval() {
        return updateInterval;
    }

    public void setUpdateInterval(Integer updateInterval) {
        this.updateInterval = updateInterval;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public Integer getAlertEmailMessage() {
        return alertEmailMessage;
    }

    public void setAlertEmailMessage(Integer alertEmailMessage) {
        this.alertEmailMessage = alertEmailMessage;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getMaxUsers() {
        return maxUsers;
    }

    public void setMaxUsers(Integer maxUsers) {
        this.maxUsers = maxUsers;
    }

    public Integer getMaxDevices() {
        return maxDevices;
    }

    public void setMaxDevices(Integer maxDevices) {
        this.maxDevices = maxDevices;
    }

    public Integer getAlertEmailLanguage() {
        return alertEmailLanguage;
    }

    public void setAlertEmailLanguage(Integer alertEmailLanguage) {
        this.alertEmailLanguage = alertEmailLanguage;
    }

    public Integer getAlertEmailOnline() {
        return alertEmailOnline;
    }

    public void setAlertEmailOnline(Integer alertEmailOnline) {
        this.alertEmailOnline = alertEmailOnline;
    }

    public Integer getAlertEmailOffline() {
        return alertEmailOffline;
    }

    public void setAlertEmailOffline(Integer alertEmailOffline) {
        this.alertEmailOffline = alertEmailOffline;
    }

    public Integer getDeviceRegistrationEmail() {
        return deviceRegistrationEmail;
    }

    public void setDeviceRegistrationEmail(Integer deviceRegistrationEmail) {
        this.deviceRegistrationEmail = deviceRegistrationEmail;
    }

    public Integer getDeviceUpdateEmail() {
        return deviceUpdateEmail;
    }

    public void setDeviceUpdateEmail(Integer deviceUpdateEmail) {
        this.deviceUpdateEmail = deviceUpdateEmail;
    }

    public Integer getAlertGracePeriod() {
        return alertGracePeriod;
    }

    public void setAlertGracePeriod(Integer alertGracePeriod) {
        this.alertGracePeriod = alertGracePeriod;
    }

    public Integer getSmsQouta() {
        return smsQouta;
    }

    public void setSmsQouta(Integer smsQouta) {
        this.smsQouta = smsQouta;
    }

    public Integer getSmsConsumed() {
        return smsConsumed;
    }

    public void setSmsConsumed(Integer smsConsumed) {
        this.smsConsumed = smsConsumed;
    }

    public Integer getFaultNotificationEmail() {
        return faultNotificationEmail;
    }

    public void setFaultNotificationEmail(Integer faultNotificationEmail) {
        this.faultNotificationEmail = faultNotificationEmail;
    }

    public String getAdminUserEmail() {
        return adminUserEmail;
    }

    public void setAdminUserEmail(String adminUserEmail) {
        this.adminUserEmail = adminUserEmail;
    }

    public Integer getPurgeAfter() {
        return purgeAfter;
    }

    public void setPurgeAfter(Integer purgeAfter) {
        this.purgeAfter = purgeAfter;
    }

    public Integer getAlertDeviceIpChange() {
        return alertDeviceIpChange;
    }

    public void setAlertDeviceIpChange(Integer alertDeviceIpChange) {
        this.alertDeviceIpChange = alertDeviceIpChange;
    }

    public Integer getAlertFirmwareAvailable() {
        return alertFirmwareAvailable;
    }

    public void setAlertFirmwareAvailable(Integer alertFirmwareAvailable) {
        this.alertFirmwareAvailable = alertFirmwareAvailable;
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
        if (!(object instanceof TenantSettings)) {
            return false;
        }
        TenantSettings other = (TenantSettings) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "osa.ora.iot.db.beans.TenantSettings[ id=" + id + " ]";
    }

    /**
     * @return the currentUsers
     */
    public Integer getCurrentUsers() {
        return currentUsers;
    }

    /**
     * @param currentUsers the currentUsers to set
     */
    public void setCurrentUsers(Integer currentUsers) {
        this.currentUsers = currentUsers;
    }

    /**
     * @return the currentDevices
     */
    public Integer getCurrentDevices() {
        return currentDevices;
    }

    /**
     * @param currentDevices the currentDevices to set
     */
    public void setCurrentDevices(Integer currentDevices) {
        this.currentDevices = currentDevices;
    }
    
}
