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
@Table(name = "devices")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Devices.findAll", query = "SELECT d FROM Devices d")
    , @NamedQuery(name = "Devices.findById", query = "SELECT d FROM Devices d WHERE d.id = :id")
    , @NamedQuery(name = "Devices.findByDeviceId", query = "SELECT d FROM Devices d WHERE d.deviceId = :deviceId")
    , @NamedQuery(name = "Devices.findByGroupId", query = "SELECT d FROM Devices d WHERE d.groupId = :groupId")
    , @NamedQuery(name = "Devices.findByPassword", query = "SELECT d FROM Devices d WHERE d.password = :password")
    , @NamedQuery(name = "Devices.findByFriendlyName", query = "SELECT d FROM Devices d WHERE d.friendlyName = :friendlyName")
    , @NamedQuery(name = "Devices.findBySecretKey", query = "SELECT d FROM Devices d WHERE d.secretKey = :secretKey")
    , @NamedQuery(name = "Devices.findByLocation", query = "SELECT d FROM Devices d WHERE d.location = :location")
    , @NamedQuery(name = "Devices.findByTags", query = "SELECT d FROM Devices d WHERE d.tags = :tags")
    , @NamedQuery(name = "Devices.findByDevice1", query = "SELECT d FROM Devices d WHERE d.device1 = :device1")
    , @NamedQuery(name = "Devices.findByDevice2", query = "SELECT d FROM Devices d WHERE d.device2 = :device2")
    , @NamedQuery(name = "Devices.findByCustomName", query = "SELECT d FROM Devices d WHERE d.customName = :customName")
    , @NamedQuery(name = "Devices.findByCustomValue", query = "SELECT d FROM Devices d WHERE d.customValue = :customValue")
    , @NamedQuery(name = "Devices.findByNotificationEmail", query = "SELECT d FROM Devices d WHERE d.notificationEmail = :notificationEmail")
    , @NamedQuery(name = "Devices.findByNotificationMobile", query = "SELECT d FROM Devices d WHERE d.notificationMobile = :notificationMobile")
    , @NamedQuery(name = "Devices.findByBarCode", query = "SELECT d FROM Devices d WHERE d.barCode = :barCode")
    , @NamedQuery(name = "Devices.findByLastAction", query = "SELECT d FROM Devices d WHERE d.lastAction = :lastAction")
    , @NamedQuery(name = "Devices.findByLastActionBy", query = "SELECT d FROM Devices d WHERE d.lastActionBy = :lastActionBy")
    , @NamedQuery(name = "Devices.findByDeviceModel", query = "SELECT d FROM Devices d WHERE d.deviceModel = :deviceModel")
    , @NamedQuery(name = "Devices.findByDeviceStatus", query = "SELECT d FROM Devices d WHERE d.deviceStatus = :deviceStatus")
    , @NamedQuery(name = "Devices.findByRegistrationDate", query = "SELECT d FROM Devices d WHERE d.registrationDate = :registrationDate")
    , @NamedQuery(name = "Devices.findByUpdateTime", query = "SELECT d FROM Devices d WHERE d.updateTime = :updateTime")
    , @NamedQuery(name = "Devices.findByLastPingTime", query = "SELECT d FROM Devices d WHERE d.lastPingTime = :lastPingTime")
    , @NamedQuery(name = "Devices.findByIdentity", query = "SELECT d FROM Devices d WHERE d.identity = :identity")
    , @NamedQuery(name = "Devices.findByFirmwareVersion", query = "SELECT d FROM Devices d WHERE d.firmwareVersion = :firmwareVersion")
    , @NamedQuery(name = "Devices.findByHighAlertValue1", query = "SELECT d FROM Devices d WHERE d.highAlertValue1 = :highAlertValue1")
    , @NamedQuery(name = "Devices.findByLowAlertValue1", query = "SELECT d FROM Devices d WHERE d.lowAlertValue1 = :lowAlertValue1")
    , @NamedQuery(name = "Devices.findByHighAlertValue2", query = "SELECT d FROM Devices d WHERE d.highAlertValue2 = :highAlertValue2")
    , @NamedQuery(name = "Devices.findByLowAlertValue2", query = "SELECT d FROM Devices d WHERE d.lowAlertValue2 = :lowAlertValue2")
    , @NamedQuery(name = "Devices.findByTotalInbound", query = "SELECT d FROM Devices d WHERE d.totalInbound = :totalInbound")
    , @NamedQuery(name = "Devices.findByTotalOutbound", query = "SELECT d FROM Devices d WHERE d.totalOutbound = :totalOutbound")
    , @NamedQuery(name = "Devices.findBySmartRules1", query = "SELECT d FROM Devices d WHERE d.smartRules1 = :smartRules1")
    , @NamedQuery(name = "Devices.findBySmartRules2", query = "SELECT d FROM Devices d WHERE d.smartRules2 = :smartRules2")
    , @NamedQuery(name = "Devices.findByDevice1Status", query = "SELECT d FROM Devices d WHERE d.device1Status = :device1Status")
    , @NamedQuery(name = "Devices.findByDevice2Status", query = "SELECT d FROM Devices d WHERE d.device2Status = :device2Status")
    , @NamedQuery(name = "Devices.findByTotalMessages", query = "SELECT d FROM Devices d WHERE d.totalMessages = :totalMessages")
    , @NamedQuery(name = "Devices.findByTotalAlerts", query = "SELECT d FROM Devices d WHERE d.totalAlerts = :totalAlerts")
    , @NamedQuery(name = "Devices.findByTotalInboundMonth", query = "SELECT d FROM Devices d WHERE d.totalInboundMonth = :totalInboundMonth")
    , @NamedQuery(name = "Devices.findByTotalOutboundMonth", query = "SELECT d FROM Devices d WHERE d.totalOutboundMonth = :totalOutboundMonth")
    , @NamedQuery(name = "Devices.findByTotalMessagesMonth", query = "SELECT d FROM Devices d WHERE d.totalMessagesMonth = :totalMessagesMonth")
    , @NamedQuery(name = "Devices.findByDeviceManagement", query = "SELECT d FROM Devices d WHERE d.deviceManagement = :deviceManagement")
    , @NamedQuery(name = "Devices.findByFailedLogin", query = "SELECT d FROM Devices d WHERE d.failedLogin = :failedLogin")
    , @NamedQuery(name = "Devices.findByIpAddress", query = "SELECT d FROM Devices d WHERE d.ipAddress = :ipAddress")
    , @NamedQuery(name = "Devices.findByOfflineFlag", query = "SELECT d FROM Devices d WHERE d.offlineFlag = :offlineFlag")})
public class Devices implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 30)
    @Column(name = "device_id")
    private String deviceId;
    @Column(name = "group_id")
    private Integer groupId;
    @Size(max = 60)
    @Column(name = "password")
    private String password;
    @Size(max = 60)
    @Column(name = "friendly_name")
    private String friendlyName;
    @Size(max = 60)
    @Column(name = "secret_key")
    private String secretKey;
    @Size(max = 60)
    @Column(name = "location")
    private String location;
    @Size(max = 90)
    @Column(name = "tags")
    private String tags;
    @Size(max = 40)
    @Column(name = "device1")
    private String device1;
    @Size(max = 40)
    @Column(name = "device2")
    private String device2;
    @Size(max = 40)
    @Column(name = "custom_name")
    private String customName;
    @Size(max = 40)
    @Column(name = "custom_value")
    private String customValue;
    @Size(max = 300)
    @Column(name = "notification_email")
    private String notificationEmail;
    @Size(max = 20)
    @Column(name = "notification_mobile")
    private String notificationMobile;
    @Size(max = 20)
    @Column(name = "bar_code")
    private String barCode;
    @Column(name = "last_action")
    private Integer lastAction;
    @Column(name = "last_action_by")
    private Integer lastActionBy;
    @Column(name = "device_model")
    private Integer deviceModel;
    @Column(name = "device_status")
    private Integer deviceStatus;
    @Column(name = "registration_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date registrationDate;
    @Column(name = "update_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;
    @Column(name = "last_ping_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastPingTime;
    @Column(name = "identity")
    private Integer identity;
    @Column(name = "firmware_version")
    private Integer firmwareVersion;
    @Size(max = 10)
    @Column(name = "high_alert_value_1")
    private String highAlertValue1;
    @Size(max = 10)
    @Column(name = "low_alert_value_1")
    private String lowAlertValue1;
    @Size(max = 10)
    @Column(name = "high_alert_value_2")
    private String highAlertValue2;
    @Size(max = 10)
    @Column(name = "low_alert_value_2")
    private String lowAlertValue2;
    @Column(name = "total_inbound")
    private Integer totalInbound;
    @Column(name = "total_outbound")
    private Integer totalOutbound;
    @Column(name = "smart_rules1")
    private Integer smartRules1;
    @Column(name = "smart_rules2")
    private Integer smartRules2;
    @Column(name = "device1_status")
    private Integer device1Status;
    @Column(name = "device2_status")
    private Integer device2Status;
    @Column(name = "total_messages")
    private Integer totalMessages;
    @Column(name = "total_alerts")
    private Integer totalAlerts;
    @Column(name = "total_inbound_month")
    private Integer totalInboundMonth;
    @Column(name = "total_outbound_month")
    private Integer totalOutboundMonth;
    @Column(name = "total_messages_month")
    private Integer totalMessagesMonth;
    @Column(name = "device_management")
    private Integer deviceManagement;
    @Column(name = "failed_login")
    private Integer failedLogin;
    @Size(max = 45)
    @Column(name = "ip_address")
    private String ipAddress;
    @Column(name = "offline_flag")
    private Integer offlineFlag;
    @Transient
    private DeviceModel model;
    @Transient
    private Messages lastMessage;

    public Devices() {
    }

    public Devices(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFriendlyName() {
        return friendlyName;
    }

    public void setFriendlyName(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getDevice1() {
        return device1;
    }

    public void setDevice1(String device1) {
        this.device1 = device1;
    }

    public String getDevice2() {
        return device2;
    }

    public void setDevice2(String device2) {
        this.device2 = device2;
    }

    public String getCustomName() {
        return customName;
    }

    public void setCustomName(String customName) {
        this.customName = customName;
    }

    public String getCustomValue() {
        return customValue;
    }

    public void setCustomValue(String customValue) {
        this.customValue = customValue;
    }

    public String getNotificationEmail() {
        return notificationEmail;
    }

    public void setNotificationEmail(String notificationEmail) {
        this.notificationEmail = notificationEmail;
    }

    public String getNotificationMobile() {
        return notificationMobile;
    }

    public void setNotificationMobile(String notificationMobile) {
        this.notificationMobile = notificationMobile;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public Integer getLastAction() {
        return lastAction;
    }

    public void setLastAction(Integer lastAction) {
        this.lastAction = lastAction;
    }

    public Integer getLastActionBy() {
        return lastActionBy;
    }

    public void setLastActionBy(Integer lastActionBy) {
        this.lastActionBy = lastActionBy;
    }

    public Integer getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(Integer deviceModel) {
        this.deviceModel = deviceModel;
    }

    public Integer getDeviceStatus() {
        return deviceStatus;
    }

    public void setDeviceStatus(Integer deviceStatus) {
        this.deviceStatus = deviceStatus;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getLastPingTime() {
        return lastPingTime;
    }

    public void setLastPingTime(Date lastPingTime) {
        this.lastPingTime = lastPingTime;
    }

    public Integer getIdentity() {
        return identity;
    }

    public void setIdentity(Integer identity) {
        this.identity = identity;
    }

    public Integer getFirmwareVersion() {
        return firmwareVersion;
    }

    public void setFirmwareVersion(Integer firmwareVersion) {
        this.firmwareVersion = firmwareVersion;
    }

    public String getHighAlertValue1() {
        return highAlertValue1;
    }

    public void setHighAlertValue1(String highAlertValue1) {
        this.highAlertValue1 = highAlertValue1;
    }

    public String getLowAlertValue1() {
        return lowAlertValue1;
    }

    public void setLowAlertValue1(String lowAlertValue1) {
        this.lowAlertValue1 = lowAlertValue1;
    }

    public String getHighAlertValue2() {
        return highAlertValue2;
    }

    public void setHighAlertValue2(String highAlertValue2) {
        this.highAlertValue2 = highAlertValue2;
    }

    public String getLowAlertValue2() {
        return lowAlertValue2;
    }

    public void setLowAlertValue2(String lowAlertValue2) {
        this.lowAlertValue2 = lowAlertValue2;
    }

    public Integer getTotalInbound() {
        return totalInbound;
    }

    public void setTotalInbound(Integer totalInbound) {
        this.totalInbound = totalInbound;
    }

    public Integer getTotalOutbound() {
        return totalOutbound;
    }

    public void setTotalOutbound(Integer totalOutbound) {
        this.totalOutbound = totalOutbound;
    }

    public Integer getSmartRules1() {
        return smartRules1;
    }

    public void setSmartRules1(Integer smartRules1) {
        this.smartRules1 = smartRules1;
    }

    public Integer getSmartRules2() {
        return smartRules2;
    }

    public void setSmartRules2(Integer smartRules2) {
        this.smartRules2 = smartRules2;
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

    public Integer getTotalInboundMonth() {
        return totalInboundMonth;
    }

    public void setTotalInboundMonth(Integer totalInboundMonth) {
        this.totalInboundMonth = totalInboundMonth;
    }

    public Integer getTotalOutboundMonth() {
        return totalOutboundMonth;
    }

    public void setTotalOutboundMonth(Integer totalOutboundMonth) {
        this.totalOutboundMonth = totalOutboundMonth;
    }

    public Integer getTotalMessagesMonth() {
        return totalMessagesMonth;
    }

    public void setTotalMessagesMonth(Integer totalMessagesMonth) {
        this.totalMessagesMonth = totalMessagesMonth;
    }

    public Integer getDeviceManagement() {
        return deviceManagement;
    }

    public void setDeviceManagement(Integer deviceManagement) {
        this.deviceManagement = deviceManagement;
    }

    public Integer getFailedLogin() {
        return failedLogin;
    }

    public void setFailedLogin(Integer failedLogin) {
        this.failedLogin = failedLogin;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public Integer getOfflineFlag() {
        return offlineFlag;
    }

    public void setOfflineFlag(Integer offlineFlag) {
        this.offlineFlag = offlineFlag;
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
        if (!(object instanceof Devices)) {
            return false;
        }
        Devices other = (Devices) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "osa.ora.iot.db.beans.Devices[ id=" + id + " ]";
    }

    /**
     * @return the model
     */
    public DeviceModel getModel() {
        return model;
    }

    /**
     * @param model the model to set
     */
    public void setModel(DeviceModel model) {
        this.model = model;
    }

    /**
     * @return the lastMessage
     */
    public Messages getLastMessage() {
        return lastMessage;
    }

    /**
     * @param lastMessage the lastMessage to set
     */
    public void setLastMessage(Messages lastMessage) {
        this.lastMessage = lastMessage;
    }
    
}
