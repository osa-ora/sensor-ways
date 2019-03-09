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
@Table(name = "bar_codes")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "BarCodes.findAll", query = "SELECT b FROM BarCodes b")
    , @NamedQuery(name = "BarCodes.findByBarCode", query = "SELECT b FROM BarCodes b WHERE b.barCode = :barCode")
    , @NamedQuery(name = "BarCodes.findByDeviceModel", query = "SELECT b FROM BarCodes b WHERE b.deviceModel = :deviceModel")
    , @NamedQuery(name = "BarCodes.findByCreationDate", query = "SELECT b FROM BarCodes b WHERE b.creationDate = :creationDate")})
public class BarCodes implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "bar_code")
    private String barCode;
    @Column(name = "device_model")
    private Integer deviceModel;
    @Column(name = "creation_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;

    public BarCodes() {
    }

    public BarCodes(String barCode) {
        this.barCode = barCode;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public Integer getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(Integer deviceModel) {
        this.deviceModel = deviceModel;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (barCode != null ? barCode.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BarCodes)) {
            return false;
        }
        BarCodes other = (BarCodes) object;
        if ((this.barCode == null && other.barCode != null) || (this.barCode != null && !this.barCode.equals(other.barCode))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "osa.ora.iot.db.beans.BarCodes[ barCode=" + barCode + " ]";
    }
    
}
