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
@Table(name = "messages")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Messages.findAll", query = "SELECT m FROM Messages m")
    , @NamedQuery(name = "Messages.findById", query = "SELECT m FROM Messages m WHERE m.id = :id")
    , @NamedQuery(name = "Messages.findByDeviceId", query = "SELECT m FROM Messages m WHERE m.deviceId = :deviceId")
    , @NamedQuery(name = "Messages.findByPayload", query = "SELECT m FROM Messages m WHERE m.payload = :payload")
    , @NamedQuery(name = "Messages.findByType", query = "SELECT m FROM Messages m WHERE m.type = :type")
    , @NamedQuery(name = "Messages.findByMessageTime", query = "SELECT m FROM Messages m WHERE m.messageTime = :messageTime")
    , @NamedQuery(name = "Messages.findByFormat", query = "SELECT m FROM Messages m WHERE m.format = :format")})
public class Messages implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 30)
    @Column(name = "device_id")
    private String deviceId;
    @Size(max = 300)
    @Column(name = "payload")
    private String payload;
    @Column(name = "type")
    private Integer type;
    @Column(name = "message_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date messageTime;
    @Column(name = "format")
    private Integer format;
    @Lob
    @Column(name = "binary_payload")
    private byte[] binaryPayload;

    public Messages() {
    }

    public Messages(Integer id) {
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

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Date getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(Date messageTime) {
        this.messageTime = messageTime;
    }

    public Integer getFormat() {
        return format;
    }

    public void setFormat(Integer format) {
        this.format = format;
    }

    public byte[] getBinaryPayload() {
        return binaryPayload;
    }

    public void setBinaryPayload(byte[] binaryPayload) {
        this.binaryPayload = binaryPayload;
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
        if (!(object instanceof Messages)) {
            return false;
        }
        Messages other = (Messages) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "osa.ora.iot.db.beans.Messages[ id=" + id + " ]";
    }
    
}
