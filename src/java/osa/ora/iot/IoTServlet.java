/**
 * SensorWays IoT Platform
 * =================================================
 * Copyright (c) 2019 : Osama Oransa
 * =================================================
 * This class is used to interact with IoT Devices
 * using HTTP/s protocol, it enabled the devices to :
 * Login, send update messages, and ping the backend
 * for keep alive the device
 */
package osa.ora.iot;

import java.io.IOException;
import java.util.Base64;
import java.util.Date;
import javax.ejb.EJB;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import osa.ora.iot.beans.IConstant;
import osa.ora.iot.db.beans.TenantSettings;
import osa.ora.iot.db.beans.Devices;
import osa.ora.iot.db.beans.RecievedMessages;
import osa.ora.iot.db.beans.SystemConfig;
import osa.ora.iot.db.session.UserSessionBean;
import osa.ora.iot.util.IoTUtil;

/**
 * IoTServlet
 * @author ooransa
 */
@WebServlet(name = "IoTServlet", urlPatterns = {"/IoTServlet"})
public class IoTServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    @EJB(beanName = "UserSessionBean")
    UserSessionBean userSessionBean;
    SystemConfig systemConfigurations;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public IoTServlet() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     * response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.getWriter().println("Error: Not Supported Operation!");
        return;
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     * response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String type = request.getParameter("t");
        String action = request.getParameter("a");
        String deviceId = request.getParameter("i");//deviceId
        String versionStr = request.getParameter("v");//current device version
        String userAgent = request.getHeader("User-Agent");
        String size=request.getHeader("Content-Length");
        int requestSize=0;
        try{
            requestSize=Integer.parseInt(size);
        }catch(Exception ex){
            //do no thing ...
        }
        String deviceSecretKey = null;
        String devicePassword = null;
        String messagePayload = null;
        String actionRequired = null;
        RecievedMessages message = null;
        Devices device = null;
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        if(systemConfigurations==null){
            systemConfigurations=userSessionBean.getSystemConfigurations();
        }
        if (action == null || type == null || userAgent == null || deviceId==null || !"OsaOraIoTDevice".equals(userAgent)) {
            response.getWriter().println("Error: Missing required parameters!");
            return;
        } else {
            int actionVal = 0;
            int typeVal = 0;
            int version = 0;
            try {
                actionVal = Integer.parseInt(action);
                typeVal = Integer.parseInt(type);
                version= Integer.parseInt(versionStr);
            } catch (Exception ex) {
                //do no thing ... 
            }
            deviceId=new String(Base64.getDecoder().decode(deviceId));
            switch (actionVal) {
                case IConstant.ACTION_RE_LOGIN:
                    devicePassword = request.getParameter("p");
                    if(devicePassword!=null){
                        devicePassword = new String(Base64.getDecoder().decode(devicePassword));
                    }
                    device = userSessionBean.loginDevice(deviceId, devicePassword, version, IoTUtil.getClientIpAddr(request));
                    //login the device and return new secret key ...
                    if (device != null) {
                        TenantSettings settings = userSessionBean.loadIdentitySettings(device.getIdentity());
                        message = new RecievedMessages();
                        message.setPayload("DEVICE CREDENTIALS");
                        message.setDeviceId(deviceId);
                        message.setMessageTime(new Date());
                        message.setType(typeVal);
                        userSessionBean.acceptMessage(deviceId, device.getSecretKey(), message);
                        deviceSecretKey = device.getSecretKey() + "#" + settings.getPingInterval() + "#" 
                                + settings.getUpdateInterval()
                                + "#" + device.getHighAlertValue1() + "#" + device.getHighAlertValue2()
                                + "#" + device.getLowAlertValue1()+ "#" + device.getLowAlertValue2()
                                + "#" + device.getDevice1Status()+ "#" + device.getDevice2Status()
                                + "#" + device.getSmartRules1()+ "#" + device.getSmartRules2()
                                +"#" + device.getModel().getFirmwareVersion();
                        userSessionBean.saveDeviceTraffic(deviceId, requestSize, deviceSecretKey.length());
                        response.getWriter().println("s:" + deviceSecretKey);
                    } else {
                        response.getWriter().println("Error: failed device login!#");
                    }
                    break;
                case IConstant.ACTION_UPDATE:
                    deviceSecretKey = request.getParameter("s"); //secretKey
                    messagePayload = request.getParameter("m");
                    message = new RecievedMessages();
                    message.setPayload(messagePayload);
                    message.setDeviceId(deviceId);
                    message.setMessageTime(new Date());
                    message.setType(typeVal);
                    System.out.println("Type:"+typeVal);
                    actionRequired = userSessionBean.acceptMessage(deviceId, deviceSecretKey, message);
                    System.out.println("Will return actions:"+actionRequired);
                    if (actionRequired!=null) {
                        userSessionBean.saveDeviceTraffic(deviceId, requestSize, actionRequired.length());
                        response.getWriter().println("s:" + actionRequired);
                    } else {
                        userSessionBean.saveDeviceTraffic(deviceId, requestSize, 0);
                        response.getWriter().println("Error: failed to validate secretkey!");
                    }
                    break;
                case IConstant.ACTION_PING:
                    deviceSecretKey = request.getParameter("s"); //secretKey
                    actionRequired = userSessionBean.acceptPing(deviceId, deviceSecretKey, new Date());
                    if (actionRequired != null) {
                        userSessionBean.saveDeviceTraffic(deviceId, requestSize, actionRequired.length());
                        response.getWriter().println("s:" + actionRequired);
                    } else {
                        response.getWriter().println("Error: failed to validate secretkey!");
                    }
                    break;
                default:
                    response.getWriter().println("Error: Missing proper action!");
                    return;
            }
        }
    }  
}
