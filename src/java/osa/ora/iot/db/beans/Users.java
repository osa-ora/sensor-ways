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
@Table(name = "users")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Users.findAll", query = "SELECT u FROM Users u")
    , @NamedQuery(name = "Users.findById", query = "SELECT u FROM Users u WHERE u.id = :id")
    , @NamedQuery(name = "Users.findByUsername", query = "SELECT u FROM Users u WHERE u.username = :username")
    , @NamedQuery(name = "Users.findByPassword", query = "SELECT u FROM Users u WHERE u.password = :password")
    , @NamedQuery(name = "Users.findByEmailAddress", query = "SELECT u FROM Users u WHERE u.emailAddress = :emailAddress")
    , @NamedQuery(name = "Users.findByIdentityId", query = "SELECT u FROM Users u WHERE u.identityId = :identityId")
    , @NamedQuery(name = "Users.findByUserRole", query = "SELECT u FROM Users u WHERE u.userRole = :userRole")
    , @NamedQuery(name = "Users.findByInvalidLoginTrials", query = "SELECT u FROM Users u WHERE u.invalidLoginTrials = :invalidLoginTrials")
    , @NamedQuery(name = "Users.findByLastLoginTime", query = "SELECT u FROM Users u WHERE u.lastLoginTime = :lastLoginTime")
    , @NamedQuery(name = "Users.findByUserStatus", query = "SELECT u FROM Users u WHERE u.userStatus = :userStatus")
    , @NamedQuery(name = "Users.findByCreationDate", query = "SELECT u FROM Users u WHERE u.creationDate = :creationDate")
    , @NamedQuery(name = "Users.findByUpdateTime", query = "SELECT u FROM Users u WHERE u.updateTime = :updateTime")
    , @NamedQuery(name = "Users.findByLanguage", query = "SELECT u FROM Users u WHERE u.language = :language")
    , @NamedQuery(name = "Users.findByDashboard", query = "SELECT u FROM Users u WHERE u.dashboard = :dashboard")
    , @NamedQuery(name = "Users.findByUserCreation", query = "SELECT u FROM Users u WHERE u.userCreation = :userCreation")
    , @NamedQuery(name = "Users.findByWorkflowCreation", query = "SELECT u FROM Users u WHERE u.workflowCreation = :workflowCreation")
    , @NamedQuery(name = "Users.findBySchedulerCreation", query = "SELECT u FROM Users u WHERE u.schedulerCreation = :schedulerCreation")
    , @NamedQuery(name = "Users.findByDashboardCreation", query = "SELECT u FROM Users u WHERE u.dashboardCreation = :dashboardCreation")
    , @NamedQuery(name = "Users.findBySimulationCreation", query = "SELECT u FROM Users u WHERE u.simulationCreation = :simulationCreation")
    , @NamedQuery(name = "Users.findByIdentityAdmin", query = "SELECT u FROM Users u WHERE u.identityAdmin = :identityAdmin")
    , @NamedQuery(name = "Users.findBySystemAdmin", query = "SELECT u FROM Users u WHERE u.systemAdmin = :systemAdmin")
    , @NamedQuery(name = "Users.findByGuiLanguage", query = "SELECT u FROM Users u WHERE u.guiLanguage = :guiLanguage")
    , @NamedQuery(name = "Users.findByIpAddress", query = "SELECT u FROM Users u WHERE u.ipAddress = :ipAddress")})
public class Users implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 30)
    @Column(name = "username")
    private String username;
    @Size(max = 60)
    @Column(name = "password")
    private String password;
    @Size(max = 40)
    @Column(name = "email_address")
    private String emailAddress;
    @Column(name = "identity_id")
    private Integer identityId;
    @Column(name = "user_role")
    private Integer userRole;
    @Column(name = "invalid_login_trials")
    private Integer invalidLoginTrials;
    @Column(name = "last_login_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastLoginTime;
    @Column(name = "user_status")
    private Integer userStatus;
    @Column(name = "creation_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    @Column(name = "update_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;
    @Column(name = "language")
    private Integer language;
    @Size(max = 100)
    @Column(name = "dashboard")
    private String dashboard;
    @Column(name = "user_creation")
    private Integer userCreation;
    @Column(name = "workflow_creation")
    private Integer workflowCreation;
    @Column(name = "scheduler_creation")
    private Integer schedulerCreation;
    @Column(name = "dashboard_creation")
    private Integer dashboardCreation;
    @Column(name = "simulation_creation")
    private Integer simulationCreation;
    @Column(name = "identity_admin")
    private Integer identityAdmin;
    @Column(name = "system_admin")
    private Integer systemAdmin;
    @Column(name = "gui_language")
    private Integer guiLanguage;
    @Size(max = 45)
    @Column(name = "ip_address")
    private String ipAddress;

    public Users() {
    }

    public Users(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public Integer getIdentityId() {
        return identityId;
    }

    public void setIdentityId(Integer identityId) {
        this.identityId = identityId;
    }

    public Integer getUserRole() {
        return userRole;
    }

    public void setUserRole(Integer userRole) {
        this.userRole = userRole;
    }

    public Integer getInvalidLoginTrials() {
        return invalidLoginTrials;
    }

    public void setInvalidLoginTrials(Integer invalidLoginTrials) {
        this.invalidLoginTrials = invalidLoginTrials;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public Integer getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(Integer userStatus) {
        this.userStatus = userStatus;
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

    public Integer getLanguage() {
        return language;
    }

    public void setLanguage(Integer language) {
        this.language = language;
    }

    public String getDashboard() {
        return dashboard;
    }

    public void setDashboard(String dashboard) {
        this.dashboard = dashboard;
    }

    public Integer getUserCreation() {
        return userCreation;
    }

    public void setUserCreation(Integer userCreation) {
        this.userCreation = userCreation;
    }

    public Integer getWorkflowCreation() {
        return workflowCreation;
    }

    public void setWorkflowCreation(Integer workflowCreation) {
        this.workflowCreation = workflowCreation;
    }

    public Integer getSchedulerCreation() {
        return schedulerCreation;
    }

    public void setSchedulerCreation(Integer schedulerCreation) {
        this.schedulerCreation = schedulerCreation;
    }

    public Integer getDashboardCreation() {
        return dashboardCreation;
    }

    public void setDashboardCreation(Integer dashboardCreation) {
        this.dashboardCreation = dashboardCreation;
    }

    public Integer getSimulationCreation() {
        return simulationCreation;
    }

    public void setSimulationCreation(Integer simulationCreation) {
        this.simulationCreation = simulationCreation;
    }

    public Integer getIdentityAdmin() {
        return identityAdmin;
    }

    public void setIdentityAdmin(Integer identityAdmin) {
        this.identityAdmin = identityAdmin;
    }

    public Integer getSystemAdmin() {
        return systemAdmin;
    }

    public void setSystemAdmin(Integer systemAdmin) {
        this.systemAdmin = systemAdmin;
    }

    public Integer getGuiLanguage() {
        return guiLanguage;
    }

    public void setGuiLanguage(Integer guiLanguage) {
        this.guiLanguage = guiLanguage;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
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
        if (!(object instanceof Users)) {
            return false;
        }
        Users other = (Users) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "osa.ora.iot.db.beans.Users[ id=" + id + " ]";
    }
    
}
