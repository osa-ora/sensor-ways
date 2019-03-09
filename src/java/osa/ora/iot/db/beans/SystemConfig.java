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
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ooransa
 */
@Entity
@Table(name = "system_config")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SystemConfig.findAll", query = "SELECT s FROM SystemConfig s")
    , @NamedQuery(name = "SystemConfig.findByVersionId", query = "SELECT s FROM SystemConfig s WHERE s.versionId = :versionId")
    , @NamedQuery(name = "SystemConfig.findByApiVersion", query = "SELECT s FROM SystemConfig s WHERE s.apiVersion = :apiVersion")
    , @NamedQuery(name = "SystemConfig.findByAndroidVersion", query = "SELECT s FROM SystemConfig s WHERE s.androidVersion = :androidVersion")
    , @NamedQuery(name = "SystemConfig.findByEmailEnabled", query = "SELECT s FROM SystemConfig s WHERE s.emailEnabled = :emailEnabled")
    , @NamedQuery(name = "SystemConfig.findBySmtpServerIp", query = "SELECT s FROM SystemConfig s WHERE s.smtpServerIp = :smtpServerIp")
    , @NamedQuery(name = "SystemConfig.findBySmtpPort", query = "SELECT s FROM SystemConfig s WHERE s.smtpPort = :smtpPort")
    , @NamedQuery(name = "SystemConfig.findByUseSSL", query = "SELECT s FROM SystemConfig s WHERE s.useSSL = :useSSL")
    , @NamedQuery(name = "SystemConfig.findByEmailUser", query = "SELECT s FROM SystemConfig s WHERE s.emailUser = :emailUser")
    , @NamedQuery(name = "SystemConfig.findByEmailPassword", query = "SELECT s FROM SystemConfig s WHERE s.emailPassword = :emailPassword")
    , @NamedQuery(name = "SystemConfig.findByDefaultEmailLanguage", query = "SELECT s FROM SystemConfig s WHERE s.defaultEmailLanguage = :defaultEmailLanguage")
    , @NamedQuery(name = "SystemConfig.findByUpdatedDate", query = "SELECT s FROM SystemConfig s WHERE s.updatedDate = :updatedDate")
    , @NamedQuery(name = "SystemConfig.findByIosVersion", query = "SELECT s FROM SystemConfig s WHERE s.iosVersion = :iosVersion")
    , @NamedQuery(name = "SystemConfig.findByPlatformHost", query = "SELECT s FROM SystemConfig s WHERE s.platformHost = :platformHost")
    , @NamedQuery(name = "SystemConfig.findByPlatformHttpsPort", query = "SELECT s FROM SystemConfig s WHERE s.platformHttpsPort = :platformHttpsPort")
    , @NamedQuery(name = "SystemConfig.findByPlatformHttpPort", query = "SELECT s FROM SystemConfig s WHERE s.platformHttpPort = :platformHttpPort")
    , @NamedQuery(name = "SystemConfig.findByDefaultTimezone", query = "SELECT s FROM SystemConfig s WHERE s.defaultTimezone = :defaultTimezone")})
public class SystemConfig implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "version_id")
    private Integer versionId;
    @Size(max = 10)
    @Column(name = "api_version")
    private String apiVersion;
    @Size(max = 10)
    @Column(name = "android_version")
    private String androidVersion;
    @Column(name = "email_enabled")
    private Integer emailEnabled;
    @Size(max = 45)
    @Column(name = "smtp_server_ip")
    private String smtpServerIp;
    @Size(max = 10)
    @Column(name = "smtp_port")
    private String smtpPort;
    @Column(name = "useSSL")
    private Integer useSSL;
    @Size(max = 60)
    @Column(name = "email_user")
    private String emailUser;
    @Size(max = 60)
    @Column(name = "email_password")
    private String emailPassword;
    @Column(name = "default_email_language")
    private Integer defaultEmailLanguage;
    @Column(name = "updated_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedDate;
    @Size(max = 10)
    @Column(name = "ios_version")
    private String iosVersion;
    @Size(max = 45)
    @Column(name = "platform_host")
    private String platformHost;
    @Column(name = "platform_https_port")
    private Integer platformHttpsPort;
    @Column(name = "platform_http_port")
    private Integer platformHttpPort;
    @Size(max = 45)
    @Column(name = "default_timezone")
    private String defaultTimezone;

    public SystemConfig() {
    }

    public SystemConfig(Integer versionId) {
        this.versionId = versionId;
    }

    public Integer getVersionId() {
        return versionId;
    }

    public void setVersionId(Integer versionId) {
        this.versionId = versionId;
    }

    public String getApiVersion() {
        return apiVersion;
    }

    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    public String getAndroidVersion() {
        return androidVersion;
    }

    public void setAndroidVersion(String androidVersion) {
        this.androidVersion = androidVersion;
    }

    public Integer getEmailEnabled() {
        return emailEnabled;
    }

    public void setEmailEnabled(Integer emailEnabled) {
        this.emailEnabled = emailEnabled;
    }

    public String getSmtpServerIp() {
        return smtpServerIp;
    }

    public void setSmtpServerIp(String smtpServerIp) {
        this.smtpServerIp = smtpServerIp;
    }

    public String getSmtpPort() {
        return smtpPort;
    }

    public void setSmtpPort(String smtpPort) {
        this.smtpPort = smtpPort;
    }

    public Integer getUseSSL() {
        return useSSL;
    }

    public void setUseSSL(Integer useSSL) {
        this.useSSL = useSSL;
    }

    public String getEmailUser() {
        return emailUser;
    }

    public void setEmailUser(String emailUser) {
        this.emailUser = emailUser;
    }

    public String getEmailPassword() {
        return emailPassword;
    }

    public void setEmailPassword(String emailPassword) {
        this.emailPassword = emailPassword;
    }

    public Integer getDefaultEmailLanguage() {
        return defaultEmailLanguage;
    }

    public void setDefaultEmailLanguage(Integer defaultEmailLanguage) {
        this.defaultEmailLanguage = defaultEmailLanguage;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getIosVersion() {
        return iosVersion;
    }

    public void setIosVersion(String iosVersion) {
        this.iosVersion = iosVersion;
    }

    public String getPlatformHost() {
        return platformHost;
    }

    public void setPlatformHost(String platformHost) {
        this.platformHost = platformHost;
    }

    public Integer getPlatformHttpsPort() {
        return platformHttpsPort;
    }

    public void setPlatformHttpsPort(Integer platformHttpsPort) {
        this.platformHttpsPort = platformHttpsPort;
    }

    public Integer getPlatformHttpPort() {
        return platformHttpPort;
    }

    public void setPlatformHttpPort(Integer platformHttpPort) {
        this.platformHttpPort = platformHttpPort;
    }

    public String getDefaultTimezone() {
        return defaultTimezone;
    }

    public void setDefaultTimezone(String defaultTimezone) {
        this.defaultTimezone = defaultTimezone;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (versionId != null ? versionId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SystemConfig)) {
            return false;
        }
        SystemConfig other = (SystemConfig) object;
        if ((this.versionId == null && other.versionId != null) || (this.versionId != null && !this.versionId.equals(other.versionId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "osa.ora.iot.db.beans.SystemConfig[ versionId=" + versionId + " ]";
    }
    
}
