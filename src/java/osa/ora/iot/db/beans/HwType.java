/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package osa.ora.iot.db.beans;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ooransa
 */
@Entity
@Table(name = "hw_type")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "HwType.findAll", query = "SELECT h FROM HwType h")
    , @NamedQuery(name = "HwType.findById", query = "SELECT h FROM HwType h WHERE h.id = :id")
    , @NamedQuery(name = "HwType.findByType", query = "SELECT h FROM HwType h WHERE h.type = :type")
    , @NamedQuery(name = "HwType.findByValue", query = "SELECT h FROM HwType h WHERE h.value = :value")})
public class HwType implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Integer id;
    @Size(max = 80)
    @Column(name = "type")
    private String type;
    @Size(max = 20)
    @Column(name = "value")
    private String value;

    public HwType() {
    }

    public HwType(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
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
        if (!(object instanceof HwType)) {
            return false;
        }
        HwType other = (HwType) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "osa.ora.iot.db.beans.HwType[ id=" + id + " ]";
    }
    
}
