/**
 * SensorWays IoT Platform
 * =================================================
 * Copyright (c) 2019 : Osama Oransa
 * =================================================
 * This class is used to download latest device 
 * firmware and replace the variables with actual
 * device data suach as password, id, etc..
 */
package osa.ora.iot;

import java.io.IOException;
import java.util.Base64;
import java.util.Date;
import javax.ejb.EJB;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import osa.ora.iot.db.beans.Devices;
import osa.ora.iot.db.beans.RecievedMessages;
import osa.ora.iot.db.beans.SystemConfig;
import osa.ora.iot.db.session.UserSessionBean;

/**
 * IoTOTAServlet
 * @author ooransa
 */
@WebServlet(name = "IoTOTAServlet", urlPatterns = {"/IoTOTAServlet"})
public class IoTOTAServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    @EJB(beanName = "UserSessionBean")
    UserSessionBean userSessionBean;
    SystemConfig systemConfigurations;
    public static String DEVICE_ID = "DEVICE_ID#DEVICE_ID#DEVICE_ID#DEVICE_ID";
    public static String DEVICE_PASSWORD = "DEVICE_PASSWORD#DEVICE_PASSWORD#DEVICE_PASSWORD";
    public static String HOST = "HOST_HOST_HOST_HOST_HOST";
    public static String HTTPS_PORT = "PORT_HTTPS";
    public static String HTTP_PORT = "HTTP_PORT";
    public static String SSID = "WIFI_NAME_HERE#WIFI_NAME_HERE";
    public static String SSID_PASS = "WIFI_PASS_HERE#WIFI_PASS_HERE";
    /**
     * @see HttpServlet#HttpServlet()
     */
    public IoTOTAServlet() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     * response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     * response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //System.out.println("UPDATE FIRMWARE REQUEST ##################################");
        String deviceId = request.getParameter("i");//deviceId
        String devicePassword = null;
        RecievedMessages message = null;
        Devices device = null;
        if(systemConfigurations==null){
            systemConfigurations=userSessionBean.getSystemConfigurations();
        }       
        devicePassword = request.getParameter("p");
        String modelStr= request.getParameter("m");
        String wifiStr= request.getParameter("w");
        String wifiPassStr= request.getParameter("wp");
        System.out.println("UPDATE FIRMWARE REQUEST");
        if(modelStr==null || wifiStr==null || wifiPassStr==null || deviceId==null ||devicePassword==null){
            System.out.println("UPDATE FIRMWARE REQUEST ################################## Invalid Parameters!!");
            //do nothing
            response.setStatus(304);
            return;            
        }
        //decode base64 the input values ... as no https for now
        deviceId = new String(Base64.getDecoder().decode(deviceId));
        devicePassword = new String(Base64.getDecoder().decode(devicePassword));
        wifiStr = new String(Base64.getDecoder().decode(wifiStr));
        wifiPassStr = new String(Base64.getDecoder().decode(wifiPassStr));
        device = userSessionBean.loginDeviceForUpdate(deviceId, devicePassword);
        //System.out.println("UPDATE DATA:"+devicePassword+"-"+wifiStr+"-"+wifiPassStr);
        //login the device and return new secret key ...
        if (device != null) {
            System.out.println("UPDATE FIRMWARE REQUEST ################################## "+device.getModel().getFirmwareVersion());
            if(device.getModel().getFirmwareVersion()>device.getFirmwareVersion()){
                System.out.println("UPDATE FIRMWARE REQUEST ################################## "+device.getModel().getFirmwareVersion());
                message = new RecievedMessages();
                message.setPayload("UPDATE FIRMWARE Request: Version: "+device.getFirmwareVersion()+" To "+device.getModel().getFirmwareVersion());
                message.setDeviceId(deviceId);
                message.setMessageTime(new Date());
                message.setType(7);//Update Firmware request message type ...
                userSessionBean.acceptMessage(deviceId, device.getSecretKey(), message);
                response.setStatus(200);
                String mimeType = "application/octet-stream";
                response.setContentType(mimeType);
                /**** Setting The Headers For The Response Object ****/
                String headerKey = "Content-Disposition";
                String headerValue = String.format("attachment; filename=\"%s\"", "IoTDevice-firmware.bin");
                response.setHeader(headerKey, headerValue);
                byte[] data=device.getModel().getFirmwareCode();
                //only one device h/w model per device model
                String file=new String(data);
                int passwordIndex=file.indexOf(DEVICE_PASSWORD);
                int deviceIdIndex=file.indexOf(DEVICE_ID);
                int hostIndex=file.indexOf(HOST);
                int httpsIndex=file.indexOf(HTTPS_PORT);
                int httpIndex=file.indexOf(HTTP_PORT);
                int wifiIndex=file.indexOf(SSID);
                int wifiPassIndex=file.indexOf(SSID_PASS);
                System.out.println("File Length="+file.length());
                //assume the update firmware request size is roughly 40 english charachter i.e. 40 bytes
                userSessionBean.saveDeviceTraffic(deviceId, 40, file.length());
                response.setHeader("Content-Length", ""+file.length());              
                ServletOutputStream os=response.getOutputStream();
                for(int i=0;i<data.length;i++){
                    if(i==passwordIndex){
                        os.write(padRight(devicePassword, DEVICE_PASSWORD.length()).getBytes());
                        i=i+DEVICE_PASSWORD.length()-1;
                        System.out.println("Device Password found");
                    }else if(i==deviceIdIndex){
                        os.write(padRight(deviceId,DEVICE_ID.length()).getBytes());
                        i=i+DEVICE_ID.length()-1;
                        System.out.println("Device Id found");
                    }else if(i==hostIndex){
                        os.write(padRight(systemConfigurations.getPlatformHost(),HOST.length()).getBytes());
                        i=i+HOST.length()-1;
                        System.out.println("Host found:"+systemConfigurations.getPlatformHost());
                    }else if(i==httpIndex){
                        os.write(padRight(""+systemConfigurations.getPlatformHttpPort(),HTTP_PORT.length()).getBytes());
                        i=i+HTTP_PORT.length()-1;
                        System.out.println("http found");
                    }else if(i==httpsIndex){
                        os.write(padRight(""+systemConfigurations.getPlatformHttpsPort(),HTTPS_PORT.length()).getBytes());
                        i=i+HTTPS_PORT.length()-1;
                        System.out.println("Https found");
                    }else if(i==wifiIndex){
                        os.write(padRight(wifiStr,SSID.length()).getBytes());
                        i=i+SSID.length()-1;
                        System.out.println("wifi ssid found");
                    }else if(i==wifiPassIndex){
                        os.write(padRight(wifiPassStr,SSID_PASS.length()).getBytes());
                        i=i+SSID_PASS.length()-1;
                        System.out.println("wifi password found");
                    }else{
                        os.write(data[i]);
                    }
                }
                os.flush();
                System.out.println("UPDATE FIRMWARE REQUEST Finish");
                return;
            }else{
                //do nothing
                response.setStatus(304);
                return;
            }
        } else {
            response.setContentType("text/plain");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().println("Error: failed device login!#");
        }
    }
    private static String padRight(String s, int n) {
     return String.format("%1$-" + n + "s", s);  
    }
    /**
     * Used to validate if the binary contains some required parameters while uploading
     * a new version of it.
     * @param newVersion
     * @param d1FileData
     * @param hardwarePrefix
     * @return 
     */
    public static String validateBinary(int newVersion, byte[] d1FileData, String hardwarePrefix) {
        String newVersionStr=hardwarePrefix+newVersion;
        System.out.println("Check HW version:"+newVersionStr);
        String test=new String(d1FileData);
        if(test.indexOf(newVersionStr)==-1){
            return "Wrong HW or Version in the firmware binary file!";
        }
        if(test.indexOf(IoTOTAServlet.DEVICE_ID)==-1){
            return "Missing deviceId placeholder!";
        }
        if(test.indexOf(IoTOTAServlet.DEVICE_PASSWORD)==-1){
            return "Missing device password placeholder!";
        }
        if(test.indexOf(IoTOTAServlet.HOST)==-1){
            return "Missing host placeholder!";
        }
        if(test.indexOf(IoTOTAServlet.HTTPS_PORT)==-1){
            return "Missing HTTPS port placeholder!";
        }
        if(test.indexOf(IoTOTAServlet.HTTP_PORT)==-1){
            return "Missing HTTP port placeholder!";
        }
        if(test.indexOf(IoTOTAServlet.SSID)==-1){
            return "Missing wifi placeholder!";
        }
        if(test.indexOf(IoTOTAServlet.SSID_PASS)==-1){
            return "Missing wifi password placeholder!";
        }
        //TODO: need to add validation for device model as well e.g. TEMP Device
        return null;
    }
}
