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
@Table(name = "recieved_messages")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RecievedMessages.findAll", query = "SELECT r FROM RecievedMessages r")
    , @NamedQuery(name = "RecievedMessages.findById", query = "SELECT r FROM RecievedMessages r WHERE r.id = :id")
    , @NamedQuery(name = "RecievedMessages.findByDeviceId", query = "SELECT r FROM RecievedMessages r WHERE r.deviceId = :deviceId")
    , @NamedQuery(name = "RecievedMessages.findByPayload", query = "SELECT r FROM RecievedMessages r WHERE r.payload = :payload")
    , @NamedQuery(name = "RecievedMessages.findByType", query = "SELECT r FROM RecievedMessages r WHERE r.type = :type")
    , @NamedQuery(name = "RecievedMessages.findByMessageTime", query = "SELECT r FROM RecievedMessages r WHERE r.messageTime = :messageTime")})
public class RecievedMessages implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 30)
    @Column(name = "device_id")
    private String deviceId;
    @Size(max = 120)
    @Column(name = "payload")
    private String payload;
    @Column(name = "type")
    private Integer type;
    @Column(name = "message_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date messageTime;

    public RecievedMessages() {
    }

    public RecievedMessages(Integer id) {
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RecievedMessages)) {
            return false;
        }
        RecievedMessages other = (RecievedMessages) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "osa.ora.iot.db.beans.RecievedMessages[ id=" + id + " ]";
    }
    
}
