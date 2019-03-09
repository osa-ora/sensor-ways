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
@Table(name = "system_jobs")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SystemJobs.findAll", query = "SELECT s FROM SystemJobs s")
    , @NamedQuery(name = "SystemJobs.findById", query = "SELECT s FROM SystemJobs s WHERE s.id = :id")
    , @NamedQuery(name = "SystemJobs.findByName", query = "SELECT s FROM SystemJobs s WHERE s.name = :name")
    , @NamedQuery(name = "SystemJobs.findByLastExecution", query = "SELECT s FROM SystemJobs s WHERE s.lastExecution = :lastExecution")
    , @NamedQuery(name = "SystemJobs.findByLastExecutionRows", query = "SELECT s FROM SystemJobs s WHERE s.lastExecutionRows = :lastExecutionRows")
    , @NamedQuery(name = "SystemJobs.findByStatus", query = "SELECT s FROM SystemJobs s WHERE s.status = :status")
    , @NamedQuery(name = "SystemJobs.findByLastExecutionResults", query = "SELECT s FROM SystemJobs s WHERE s.lastExecutionResults = :lastExecutionResults")
    , @NamedQuery(name = "SystemJobs.findByParam1Value", query = "SELECT s FROM SystemJobs s WHERE s.param1Value = :param1Value")
    , @NamedQuery(name = "SystemJobs.findByParam1Type", query = "SELECT s FROM SystemJobs s WHERE s.param1Type = :param1Type")})
public class SystemJobs implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 120)
    @Column(name = "name")
    private String name;
    @Column(name = "last_execution")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastExecution;
    @Column(name = "last_execution_rows")
    private Integer lastExecutionRows;
    @Lob
    @Size(max = 65535)
    @Column(name = "execution_log")
    private String executionLog;
    @Column(name = "status")
    private Integer status;
    @Column(name = "last_execution_results")
    private Integer lastExecutionResults;
    @Column(name = "param1_value")
    private Integer param1Value;
    @Size(max = 45)
    @Column(name = "param1_type")
    private String param1Type;

    public SystemJobs() {
    }

    public SystemJobs(Integer id) {
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

    public Date getLastExecution() {
        return lastExecution;
    }

    public void setLastExecution(Date lastExecution) {
        this.lastExecution = lastExecution;
    }

    public Integer getLastExecutionRows() {
        return lastExecutionRows;
    }

    public void setLastExecutionRows(Integer lastExecutionRows) {
        this.lastExecutionRows = lastExecutionRows;
    }

    public String getExecutionLog() {
        return executionLog;
    }

    public void setExecutionLog(String executionLog) {
        this.executionLog = executionLog;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getLastExecutionResults() {
        return lastExecutionResults;
    }

    public void setLastExecutionResults(Integer lastExecutionResults) {
        this.lastExecutionResults = lastExecutionResults;
    }

    public Integer getParam1Value() {
        return param1Value;
    }

    public void setParam1Value(Integer param1Value) {
        this.param1Value = param1Value;
    }

    public String getParam1Type() {
        return param1Type;
    }

    public void setParam1Type(String param1Type) {
        this.param1Type = param1Type;
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
        if (!(object instanceof SystemJobs)) {
            return false;
        }
        SystemJobs other = (SystemJobs) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "osa.ora.iot.db.beans.SystemJobs[ id=" + id + " ]";
    }
    
}
