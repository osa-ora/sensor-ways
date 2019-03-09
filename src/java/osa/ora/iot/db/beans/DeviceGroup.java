/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package osa.ora.iot.db.beans;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ooransa
 */
@Entity
@Table(name = "device_group")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DeviceGroup.findAll", query = "SELECT d FROM DeviceGroup d")
    , @NamedQuery(name = "DeviceGroup.findById", query = "SELECT d FROM DeviceGroup d WHERE d.id = :id")
    , @NamedQuery(name = "DeviceGroup.findByName", query = "SELECT d FROM DeviceGroup d WHERE d.name = :name")
    , @NamedQuery(name = "DeviceGroup.findByIdentityId", query = "SELECT d FROM DeviceGroup d WHERE d.identityId = :identityId")})
public class DeviceGroup implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 60)
    @Column(name = "name")
    private String name;
    @Column(name = "identity_id")
    private Integer identityId;
    @Transient
    private int deviceCount;
    @Transient
    private List<Devices> deviceList;
    public DeviceGroup() {
    }

    public DeviceGroup(Integer id) {
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DeviceGroup)) {
            return false;
        }
        DeviceGroup other = (DeviceGroup) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "osa.ora.iot.db.beans.DeviceGroup[ id=" + id + " ]";
    }

    /**
     * @return the deviceCount
     */
    public int getDeviceCount() {
        return deviceCount;
    }

    /**
     * @param deviceCount the deviceCount to set
     */
    public void setDeviceCount(int deviceCount) {
        this.deviceCount = deviceCount;
    }

    /**
     * @return the deviceList
     */
    public List<Devices> getDeviceList() {
        return deviceList;
    }

    /**
     * @param deviceList the deviceList to set
     */
    public void setDeviceList(List<Devices> deviceList) {
        this.deviceList = deviceList;
    }
    
}
