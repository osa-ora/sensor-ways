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
import javax.persistence.Lob;
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
@Table(name = "device_model")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DeviceModel.findAll", query = "SELECT d FROM DeviceModel d")
    , @NamedQuery(name = "DeviceModel.findById", query = "SELECT d FROM DeviceModel d WHERE d.id = :id")
    , @NamedQuery(name = "DeviceModel.findByDeviceName", query = "SELECT d FROM DeviceModel d WHERE d.deviceName = :deviceName")
    , @NamedQuery(name = "DeviceModel.findByNoOfDevices", query = "SELECT d FROM DeviceModel d WHERE d.noOfDevices = :noOfDevices")
    , @NamedQuery(name = "DeviceModel.findByCustomActionName", query = "SELECT d FROM DeviceModel d WHERE d.customActionName = :customActionName")
    , @NamedQuery(name = "DeviceModel.findByCustomActionValue", query = "SELECT d FROM DeviceModel d WHERE d.customActionValue = :customActionValue")
    , @NamedQuery(name = "DeviceModel.findByUpdateTime", query = "SELECT d FROM DeviceModel d WHERE d.updateTime = :updateTime")
    , @NamedQuery(name = "DeviceModel.findByFirmwareVersion", query = "SELECT d FROM DeviceModel d WHERE d.firmwareVersion = :firmwareVersion")
    , @NamedQuery(name = "DeviceModel.findByModelStatus", query = "SELECT d FROM DeviceModel d WHERE d.modelStatus = :modelStatus")
    , @NamedQuery(name = "DeviceModel.findByHighAlertValue1", query = "SELECT d FROM DeviceModel d WHERE d.highAlertValue1 = :highAlertValue1")
    , @NamedQuery(name = "DeviceModel.findByLowAlertValue1", query = "SELECT d FROM DeviceModel d WHERE d.lowAlertValue1 = :lowAlertValue1")
    , @NamedQuery(name = "DeviceModel.findByHighAlertValue2", query = "SELECT d FROM DeviceModel d WHERE d.highAlertValue2 = :highAlertValue2")
    , @NamedQuery(name = "DeviceModel.findByLowAlertValue2", query = "SELECT d FROM DeviceModel d WHERE d.lowAlertValue2 = :lowAlertValue2")
    , @NamedQuery(name = "DeviceModel.findByHwType", query = "SELECT d FROM DeviceModel d WHERE d.hwType = :hwType")
    , @NamedQuery(name = "DeviceModel.findBySensor1Icon", query = "SELECT d FROM DeviceModel d WHERE d.sensor1Icon = :sensor1Icon")
    , @NamedQuery(name = "DeviceModel.findBySensor2Icon", query = "SELECT d FROM DeviceModel d WHERE d.sensor2Icon = :sensor2Icon")
    , @NamedQuery(name = "DeviceModel.findBySimulationNormalMsg", query = "SELECT d FROM DeviceModel d WHERE d.simulationNormalMsg = :simulationNormalMsg")
    , @NamedQuery(name = "DeviceModel.findBySimulationAlertMsg", query = "SELECT d FROM DeviceModel d WHERE d.simulationAlertMsg = :simulationAlertMsg")
    , @NamedQuery(name = "DeviceModel.findBySimulationErrorMsg", query = "SELECT d FROM DeviceModel d WHERE d.simulationErrorMsg = :simulationErrorMsg")
    , @NamedQuery(name = "DeviceModel.findBySensor1Name", query = "SELECT d FROM DeviceModel d WHERE d.sensor1Name = :sensor1Name")
    , @NamedQuery(name = "DeviceModel.findBySensor2Name", query = "SELECT d FROM DeviceModel d WHERE d.sensor2Name = :sensor2Name")
    , @NamedQuery(name = "DeviceModel.findBySensor1Graph", query = "SELECT d FROM DeviceModel d WHERE d.sensor1Graph = :sensor1Graph")
    , @NamedQuery(name = "DeviceModel.findBySensor2Graph", query = "SELECT d FROM DeviceModel d WHERE d.sensor2Graph = :sensor2Graph")
    , @NamedQuery(name = "DeviceModel.findByNoOfSensors", query = "SELECT d FROM DeviceModel d WHERE d.noOfSensors = :noOfSensors")})
public class DeviceModel implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Integer id;
    @Size(max = 90)
    @Column(name = "device_name")
    private String deviceName;
    @Column(name = "no_of_devices")
    private Integer noOfDevices;
    @Size(max = 40)
    @Column(name = "custom_action_name")
    private String customActionName;
    @Column(name = "custom_action_value")
    private Integer customActionValue;
    @Column(name = "update_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;
    @Column(name = "firmware_version")
    private Integer firmwareVersion;
    @Lob
    @Column(name = "firmware_code")
    private byte[] firmwareCode;
    @Lob
    @Column(name = "firmware_source")
    private byte[] firmwareSource;
    @Column(name = "model_status")
    private Integer modelStatus;
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
    @Column(name = "hw_type")
    private Integer hwType;
    @Column(name = "sensor1_icon")
    private Integer sensor1Icon;
    @Column(name = "sensor2_icon")
    private Integer sensor2Icon;
    @Size(max = 50)
    @Column(name = "simulation_normal_msg")
    private String simulationNormalMsg;
    @Size(max = 50)
    @Column(name = "simulation_alert_msg")
    private String simulationAlertMsg;
    @Size(max = 50)
    @Column(name = "simulation_error_msg")
    private String simulationErrorMsg;
    @Size(max = 45)
    @Column(name = "sensor1_name")
    private String sensor1Name;
    @Size(max = 45)
    @Column(name = "sensor2_name")
    private String sensor2Name;
    @Column(name = "sensor1_graph")
    private Integer sensor1Graph;
    @Column(name = "sensor2_graph")
    private Integer sensor2Graph;
    @Column(name = "no_of_sensors")
    private Integer noOfSensors;
    @Transient
    private int deviceCount;
    @Transient
    private HwType hWType;
    public DeviceModel() {
    }

    public DeviceModel(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public Integer getNoOfDevices() {
        return noOfDevices;
    }

    public void setNoOfDevices(Integer noOfDevices) {
        this.noOfDevices = noOfDevices;
    }

    public String getCustomActionName() {
        return customActionName;
    }

    public void setCustomActionName(String customActionName) {
        this.customActionName = customActionName;
    }

    public Integer getCustomActionValue() {
        return customActionValue;
    }

    public void setCustomActionValue(Integer customActionValue) {
        this.customActionValue = customActionValue;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getFirmwareVersion() {
        return firmwareVersion;
    }

    public void setFirmwareVersion(Integer firmwareVersion) {
        this.firmwareVersion = firmwareVersion;
    }

    public byte[] getFirmwareCode() {
        return firmwareCode;
    }

    public void setFirmwareCode(byte[] firmwareCode) {
        this.firmwareCode = firmwareCode;
    }

    public byte[] getFirmwareSource() {
        return firmwareSource;
    }

    public void setFirmwareSource(byte[] firmwareSource) {
        this.firmwareSource = firmwareSource;
    }

    public Integer getModelStatus() {
        return modelStatus;
    }

    public void setModelStatus(Integer modelStatus) {
        this.modelStatus = modelStatus;
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

    public Integer getHwType() {
        return hwType;
    }

    public void setHwType(Integer hwType) {
        this.hwType = hwType;
    }

    public Integer getSensor1Icon() {
        return sensor1Icon;
    }

    public void setSensor1Icon(Integer sensor1Icon) {
        this.sensor1Icon = sensor1Icon;
    }

    public Integer getSensor2Icon() {
        return sensor2Icon;
    }

    public void setSensor2Icon(Integer sensor2Icon) {
        this.sensor2Icon = sensor2Icon;
    }

    public String getSimulationNormalMsg() {
        return simulationNormalMsg;
    }

    public void setSimulationNormalMsg(String simulationNormalMsg) {
        this.simulationNormalMsg = simulationNormalMsg;
    }

    public String getSimulationAlertMsg() {
        return simulationAlertMsg;
    }

    public void setSimulationAlertMsg(String simulationAlertMsg) {
        this.simulationAlertMsg = simulationAlertMsg;
    }

    public String getSimulationErrorMsg() {
        return simulationErrorMsg;
    }

    public void setSimulationErrorMsg(String simulationErrorMsg) {
        this.simulationErrorMsg = simulationErrorMsg;
    }

    public String getSensor1Name() {
        return sensor1Name;
    }

    public void setSensor1Name(String sensor1Name) {
        this.sensor1Name = sensor1Name;
    }

    public String getSensor2Name() {
        return sensor2Name;
    }

    public void setSensor2Name(String sensor2Name) {
        this.sensor2Name = sensor2Name;
    }

    public Integer getSensor1Graph() {
        return sensor1Graph;
    }

    public void setSensor1Graph(Integer sensor1Graph) {
        this.sensor1Graph = sensor1Graph;
    }

    public Integer getSensor2Graph() {
        return sensor2Graph;
    }

    public void setSensor2Graph(Integer sensor2Graph) {
        this.sensor2Graph = sensor2Graph;
    }

    public Integer getNoOfSensors() {
        return noOfSensors;
    }

    public void setNoOfSensors(Integer noOfSensors) {
        this.noOfSensors = noOfSensors;
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
        if (!(object instanceof DeviceModel)) {
            return false;
        }
        DeviceModel other = (DeviceModel) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "osa.ora.iot.db.beans.DeviceModel[ id=" + id + " ]";
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
     * @return the hWType
     */
    public HwType gethWType() {
        return hWType;
    }

    /**
     * @param hWType the hWType to set
     */
    public void sethWType(HwType hWType) {
        this.hWType = hWType;
    }
    
}
