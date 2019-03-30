/**
 * SensorWays IoT Platform
 * =================================================
 * Copyright (c) 2019 : Osama Oransa
 * =================================================
 * This class is responsible for generating some 
 * static content that should be later stored in the
 * database for helping the GUI to render correctly 
 * and some helping methods.
 * All are static methods
 */

package osa.ora.iot.util;

import javax.servlet.http.HttpServletRequest;

import osa.ora.iot.beans.IConstant;
import osa.ora.iot.db.beans.Devices;
import osa.ora.iot.db.beans.EmailTemplates;
import osa.ora.iot.db.beans.Workflows;
/**
 * IoTUtil
 * @author ooransa
 */
public class IoTUtil {

    private IoTUtil() {
    }
    public static String getClientIpAddr(HttpServletRequest request) {  
        String ip = request.getHeader("X-Forwarded-For");  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("Proxy-Client-IP");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("WL-Proxy-Client-IP");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("HTTP_CLIENT_IP");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getRemoteAddr();  
        }  
        return ip;  
    }    
    public static String formatTrafficData(int data){
        if(data>1048576){
            data = data/1048576;
            return data+" MB(s)";
        }if(data>1024){
            data = data/1024;
            return data+" KB(s)";
        }else{
            return data+" Byte(s)";
        } 
    }
    public static String lastseen(long seconds){
        if(seconds>86400){
            seconds = seconds/86400;
            return Math.abs(seconds)+" day(s) ago";
        }else if(seconds>3600){
            seconds = seconds/3600;
            return Math.abs(seconds)+" hour(s) ago";
        }else if(seconds>60){
            seconds = seconds/60;
            return Math.abs(seconds)+" min(s) ago";
        }else {
            return Math.abs(seconds)+" second(s) ago";
        }
    }
    public static String getDisplayDeviceStatus(int deviceStatus) {
        switch (deviceStatus) {
            case 0:
                return "PENDING LOGIN";
            case 1:
                return "ACTIVE";
            case 2:
                return "INACTIVE";
            case 3:
                return "SIMULATION";
            default:
                return "Not Identified!";
        }
    }
    public static String getDisplayAppStatus(int deviceStatus) {
        switch (deviceStatus) {
            case 1:
                return "Running";
            case 2:
                return "Stopped";
            default:
                return "Not Identified!";
        }
    }
    public static String getDisplayAppType(int type) {
        switch (type) {
            case 1:
                return "Fridge Monitoring";
            case 2:
                return "Fire Alarm";
            case 3:
                return "Farm Irrigation";
            case 4:
                return "Security Monitoring";
            case 5:
                return "Home Monitoring";
            case 6:
                return "Home Automation";
            case 7:
                return "Generic IoT Application";
            default:
                return "Not Identified!";
        }
    }
    public static String getDisplayResults(int result) {
        switch (result) {
            case 0:
                return "FAILED";
            case 1:
                return "SUCCESS";
            default:
                return "Un-identified!";
        }
    }
    public static String getDisplaySimulationStatus(int deviceStatus) {
        switch (deviceStatus) {
            case 1:
                return "Running";
            case 2:
                return "Stopped";
            default:
                return "Not Identified!";
        }
    }
    public static String getDisplayUserStatus(int userStatus) {
        switch (userStatus) {
            case 1:
                return "ACTIVE";
            case 2:
                return "INACTIVE";
            case 3: 
                return "LOCKED";
            default:
                return "Not Identified!";
        }
    }
    public static String getDisplayMessageTypeForWebMenu(int type) {
        String response=getDisplayMessageTypeForWeb(type);
        response=response.replace("<img", "<img width='22px' height='20px'");
        return response;
    }
    public static String getDisplayMessageTypeForWeb(int type) {
        switch (type) {
            case 0:
                return "<img src='images/login.png'/>";
            case 1:
                return "<img src='images/normal.png'/>";
            case 2:
                return "<img src='images/alert.png'/>";
            case 3:
                return "<img src='images/warning.png'/>";
            case 4:
                return "<img src='images/alert_blue.jpg'/>";
            case 5:
                return "<img src='images/warning_gray.png'/>";
            case 6:
                return "<img src='images/error.png'/>";
            case 7:
                return "<img src='images/firmware.png'/>";
            default:
                return "Not Identified!";
        }
    }

    public static String getDisplayUserTypeForWeb(int type) {
        switch (type) {
            case 1:
                return "<img src='images/edit.png'/>";
            case 2:
                return "<img src='images/view.png'/>";
            default:
                return "Not Identified!";
        }
    }

    public static String getDisplayTrigger(int day, int hour, int min) {
        String triggerTime = "";
        switch (day) {
            case 0:
                triggerTime = "All Days";
                break;
            case 1:
                triggerTime = "Saturday";
                break;
            case 2:
                triggerTime = "Sunday";
                break;
            case 3:
                triggerTime = "Monday";
                break;
            case 4:
                triggerTime = "Tuesday";
                break;
            case 5:
                triggerTime = "Wednesday";
                break;
            case 6:
                triggerTime = "Thursday";
                break;
            case 7:
                triggerTime = "Friday";
                break;
        }
        triggerTime += "<br>Time: " + hour + ":" + min;
        return triggerTime;
    }

    public static String getDisplayTriggerIcon(int day, int time) {
        if (time >= 4 && time < 9) {
            return "<img src='images/sunrise.png'/>";
        } else if (time >= 9 && time < 16) {
            return "<img src='images/sun-midday.png'/>";
        } else if (time >= 16 && time < 20) {
            return "<img src='images/sunset.png'/>";
        } else {
            return "<img src='images/night.png'/>";
        }
    }

    public static String getDisplayAction(int action,Workflows workflow) {
        System.out.println("Action=" + action);
        String device1Name="";
        if(workflow.getTriggerType()==2){//device group shouldn't have all existing events ...
            device1Name=workflow.getSourceDeviceGroup().getName();
        }else{
            device1Name=workflow.getSourceDevice().getFriendlyName();       
        }
        switch (action) {
            case 10:
                return "High Alert Message";
            case 11:
                return "Normal Update Message";
            case 13:
                return "Low Alert Message";
            case 14:
                return "Error Alert Message";
            default:
                return "Not Identified!";
        }
    }
    public static String getDisplayTargetAction(int action,String device1Name, String device2Name) {
        System.out.println("Target Action=" + action);
        switch (action) {
            case 0:
                return "RE-LOGIN";
            case 1:
                return "ON " + device1Name;
            case 2:
                return "OFF " + device1Name;
            case 4:
                return "RESTART Device";
            case 5:
                return "UPDATE";
            case 6:
                return "ON " + device2Name;
            case 7:
                return "OFF " + device2Name;
            case 9:
                return "Request New Message";
            default:
                return "Not Identified!";
        }
    }

    public static String getDisplayActionBy(int action) {
        System.out.println("ActionBy=" + action);
        switch (action) {
            case IConstant.ACTION_BY_NONE:
                return "N/A";
            case IConstant.ACTION_BY_SYSTEM:
                return "System Action";
            case IConstant.ACTION_BY_WEB:
                return "Web Action";
            case IConstant.ACTION_BY_MOBILE:
                return "Mobile Action";
            case IConstant.ACTION_BY_WORKFLOW:
                return "Workflow Action";
            case IConstant.ACTION_BY_SCHEDULER:
                return "Scheduler Action";
            default:
                return "N/A";
        }
    }

    public static String payloadExtractor(String payload, String device1Name, String device2Name) {
        payload = payload.replaceAll("T=", "Temperature=");
        payload = payload.replaceAll("H=", "Humidity=");
        payload = payload.replaceAll("MO=", "Motion Detection=");
        payload = payload.replaceAll("DO=0", "Door Open=CLOSED");
        payload = payload.replaceAll("DO=1", "Door Open=OPEN");
        payload = payload.replaceAll("G=", "Gas Level=");
        payload = payload.replaceAll("DS=", device1Name + " Status=");
        payload = payload.replaceAll("DS2=", device2Name + " Status=");
        return payload;
    }
    public static String getDisplayIconForModel(Devices device){
        if(device.getModel().getNoOfSensors()==0 && device.getModel().getNoOfDevices()==1){
            return  "<img src='images/switch.png'/>";
        }
        if(device.getModel().getNoOfSensors()==0 && device.getModel().getNoOfDevices()==2){
            return  "<img src='images/switch.png'/><img src='images/switch.png'/>";
        }
        String icon="";
        switch (device.getModel().getSensor1Icon()){
            case 1:
                icon="<img src='images/temperature.png'/>";
                break;
            case 2:
                icon="<img src='images/humidity.png'/>";
                break;
            case 3:
                icon="<img src='images/motion.png'/>";
                break;
            case 4:
                icon="<img src='images/dust.png'/>";
                break;
            case 5:
                icon="<img src='images/fire.png'/>";
                break;
            case 6:
                icon="<img src='images/smoke.png'/>";
                break;
            case 7:
                icon="<img src='images/alarm.png'/>";
                break;
            case 8:
                icon="<img src='images/gas.png'/>";
                break;
            case 9:
                icon="<img src='images/light.png'/>";
                break;
            case 10:
                icon="<img src='images/door.png'/>";
                break;
            case 11:
                icon="<img src='images/window.png'/>";
                break;
            case 12:
                icon="<img src='images/heart.png'/>";
                break;
            case 13:
                icon="<img src='images/parking.png'/>";
                break;
            case 14:
                icon="<img src='images/drops.png'/>";
                break;
            case 15:
                icon="<img src='images/no-image.png'/>";
                break;
            case 16:
                icon="<img src='images/rain.png'/>";
                break;
            case 17:
                icon="<img src='images/water.png'/>";
                break;
            case 18:
                icon="<img src='images/location.png'/>";
                break;
            case 19:
                icon="<img src='images/trash.png'/>";
                break;
            default:
                icon="<img src='images/no-image.png'/>";
                break;
        }
        if(device.getModel().getNoOfSensors()>1){
            switch (device.getModel().getSensor2Icon()){
                case 1:
                    return icon+"<img src='images/temperature.png'/>";
                case 2:
                    return icon+"<img src='images/humidity.png'/>";
                case 3:
                    return icon+"<img src='images/motion.png'/>";
                case 4:
                    return icon+"<img src='images/dust.png'/>";
                case 5:
                    return icon+"<img src='images/fire.png'/>";
                case 6:
                    return icon+"<img src='images/smoke.png'/>";
                case 7:
                    return icon+"<img src='images/alarm.png'/>";
                case 8:
                    return icon+"<img src='images/gas.png'/>";
                case 9:
                    return icon+"<img src='images/light.png'/>";
                case 10:
                    return icon+"<img src='images/door.png'/>";
                case 11:
                    return icon+"<img src='images/window.png'/>";
                case 12:
                    return icon+"<img src='images/heart.png'/>";
                case 13:
                    return icon+"<img src='images/parking.png'/>";
                case 14:
                    return icon+"<img src='images/drops.png'/>";
                case 15:
                    return icon+"<img src='images/no-image.png'/>";
                case 16:
                    return icon+"<img src='images/rain.png'/>";
                case 17:
                    return icon+"<img src='images/water.png'/>";
                case 18:
                    return icon+"<img src='images/location.png'/>";
                case 19:
                    return icon+"<img src='images/trash.png'/>";
                default:
                    return icon;
            }
        }
        return icon;
    }        
    public static String payloadExtractorForWebMenu(Devices device,String payload,int type) {
        String response=payloadExtractorForWeb(device, payload, type);
        response=response.replace("<img", "<img width='20px' height='20px'");
        return response;
    }
    public static String payloadExtractorForWeb(Devices device,String payload,int type) {
        if(type!=0 && type!=7){
            if(device.getModel().getNoOfSensors()>0){
                String icon="";
                switch (device.getModel().getSensor1Icon()){
                    case 1:
                        icon="<img src='images/temperature.png'/> = ";
                        break;
                    case 2:
                        icon="<img src='images/humidity.png'/> = ";
                        break;
                    case 3:
                        icon="<img src='images/motion.png'/> = ";
                        break;
                    case 4:
                        icon="<img src='images/dust.png'/> = ";
                        break;
                    case 5:
                        icon="<img src='images/fire.png'/> = ";
                        break;
                    case 6:
                        icon="<img src='images/smoke.png'/> = ";
                        break;
                    case 7:
                        icon="<img src='images/alarm.png'/> = ";
                        break;
                    case 8:
                        icon="<img src='images/gas.png'/> = ";
                        break;
                    case 9:
                        icon="<img src='images/light.png'/> = ";
                        break;
                    case 10:
                        icon="<img src='images/door.png'/> = ";
                        break;
                    case 11:
                        icon="<img src='images/window.png'/> = ";
                        break;
                    case 12:
                        icon="<img src='images/heart.png'/> = ";
                        break;
                    case 13:
                        icon="<img src='images/parking.png'/> = ";
                        break;
                    case 14:
                        icon="<img src='images/drops.png'/> = ";
                        break;
                    case 15:
                        icon="<img src='images/no-image.png'/> = ";
                        break;
                    case 16:
                        icon="<img src='images/rain.png'/> = ";
                        break;
                    case 17:
                        icon="<img src='images/water.png'/> = ";
                        break;
                    case 18:
                        icon="<img src='images/location.png'/> = ";
                        break;
                    case 19:
                        icon="<img src='images/trash.png'/>";
                        break;
                    default:
                        icon="";
                        break;
                }
                payload=icon+payload;
            }
            if(device.getModel().getNoOfSensors()>1){
                String icon="";
                switch (device.getModel().getSensor2Icon()){
                    case 1:
                        icon=" <img src='images/temperature.png'/> = ";
                        break;
                    case 2:
                        icon=" <img src='images/humidity.png'/> = ";
                        break;
                    case 3:
                        icon=" <img src='images/motion.png'/> = ";
                        break;
                    case 4:
                        icon=" <img src='images/dust.png'/> = ";
                        break;
                    case 5:
                        icon=" <img src='images/fire.png'/> = ";
                        break;
                    case 6:
                        icon=" <img src='images/smoke.png'/> = ";
                        break;
                    case 7:
                        icon=" <img src='images/alarm.png'/> = ";
                        break;
                    case 8:
                        icon=" <img src='images/gas.png'/> = ";
                        break;
                    case 9:
                        icon=" <img src='images/light.png'/> = ";
                        break;
                    case 10:
                        icon=" <img src='images/door.png'/> = ";
                        break;
                    case 11:
                        icon=" <img src='images/window.png'/> = ";
                        break;
                    case 12:
                        icon=" <img src='images/heart.png'/> = ";
                        break;
                    case 13:
                        icon=" <img src='images/parking.png'/> = ";
                        break;
                    case 14:
                        icon=" <img src='images/drops.png'/> = ";
                        break;
                    case 15:
                        icon=" <img src='images/no-image.png'/> = ";
                        break;
                    case 16:
                        icon=" <img src='images/rain.png'/> = ";
                        break;
                    case 17:
                        icon=" <img src='images/water.png'/> = ";
                        break;
                    case 18:
                        icon=" <img src='images/location.png'/> = ";
                        break;
                    case 19:
                        icon=" <img src='images/trash.png'/>";
                        break;
                    default:
                        icon=" - ";
                        break;
                }
                payload=payload.replaceFirst(",", icon);
            }
            if(device.getModel().getNoOfSensors()==0){
                if(device.getModel().getNoOfDevices()>0){
                    // first device
                    payload = getDeviceNamePicture(device.getDevice1()) + " = "+ payload;
                    payload = payload.replaceAll("F", "OFF");
                    payload = payload.replaceAll("N", "ON");
                }
                if(device.getModel().getNoOfDevices()>1){
                    // second device
                    payload = payload.replaceFirst(",", " - "+getDeviceNamePicture(device.getDevice2()) + " = ");
                }
                
            } else if(device.getModel().getNoOfSensors()>0){
                if(device.getModel().getNoOfDevices()>0){
                    // first device
                    payload = payload.replaceFirst(",", " "+getDeviceNamePicture(device.getDevice1()) + " = ");
                    payload = payload.replaceAll("F", "OFF");
                    payload = payload.replaceAll("N", "ON");
                }
                if(device.getModel().getNoOfDevices()>1){
                    // second device
                    payload = payload.replaceFirst(",", " "+getDeviceNamePicture(device.getDevice2()) + " = ");
                }
            }
        }
         payload = payload.replaceFirst(",L"," [ Low Battery! ]");
        return payload;
    }
    public static String payloadExtractorForNotification(Devices device,String payload) {
        //1st sensor name
        if(device.getModel().getNoOfSensors()>0){
            if(device.getModel().getSensor1Name()!=null){
                payload=device.getModel().getSensor1Name()+" = "+payload;
            }
        }
        //2nd sensor name
        if(device.getModel().getNoOfSensors()>1){
            if(device.getModel().getSensor2Name()!=null){
                payload=payload.replaceFirst(",", " "+device.getModel().getSensor2Name()+" = ");
            }
        }
        if(device.getModel().getNoOfSensors()==0){
            //1st device name
            if(device.getModel().getNoOfDevices()>0){
                // first device
                payload = device.getDevice1() + " = "+ payload;
                payload = payload.replaceAll("F", "OFF");
                payload = payload.replaceAll("N", "ON");
            }
            //2nd device name
            if(device.getModel().getNoOfDevices()>1){
                // second device
                payload = payload.replaceFirst(",", " "+device.getDevice2() + " = ");
            }
        }else{
            //1st device name
            if(device.getModel().getNoOfDevices()>0){
                // first device
                payload = payload.replaceFirst(",", " "+device.getDevice1() + " = ");
                payload = payload.replaceAll("F", "OFF");
                payload = payload.replaceAll("N", "ON");
            }
            //2nd device name
            if(device.getModel().getNoOfDevices()>1){
                // second device
                payload = payload.replaceFirst(",", " "+device.getDevice2() + " = ");
            }            
        }
        payload = payload.replaceFirst(",L"," [ Low Battery! ]");
        return payload;
    }

    public static String getDeviceNamePicture(String deviceName) {
        String device = "";
        if (deviceName != null) {
            device = deviceName.toUpperCase();
        }
        switch (device) {
            case "TV":
                return "<img src='images/tv.png'/>";
            case "ALARM":
                return "<img src='images/alarm.png'/>";
            case "CAMERA":
                return "<img src='images/camera.png'/>";
            case "DOOR":
                return "<img src='images/door.png'/>";
            case "FAN":
                return "<img src='images/fan.png'/>";
            case "HEART":
            case "HEART RATE":
            case "ECG":
                return "<img src='images/heart.png'/>";
            case "LIGHT":
                return "<img src='images/light.png'/>";
            case "SWITCH":
                return "<img src='images/switch.png'/>";
            case "WINDOW":
                return "<img src='images/window.png'/>";
            case "OUTLET SWITCH":
            case "SOCKET SWITCH":
            case "SOCKET":
            case "OUTLET":
                return "<img src='images/outlet-switch.png'/>";
            default:
                return deviceName;
        }
    }
    public static String getEmailBodyLanguage(EmailTemplates entry,int language){
        switch(language){
            case 1:
                return entry.getBodyAr();
            case 2:
                return entry.getBodyEn();
            default:
                return entry.getBodyAr();
        }
    }
    public static String getEmailSubjectLanguage(EmailTemplates entry,int language){
        switch(language){
            case 1:
                return entry.getTitleAr();
            case 2:
                return entry.getTitleEn();
            default:
                return entry.getTitleAr();
        }
    }
    public static String applyParameters(String body,String params){
        String[]parameters=params.split("#");
        if(parameters!=null && parameters.length>0){
            int order=1;
            for (String parameter : parameters) {
                if (parameter != null) {
                    String toReplace="#"+order;
                    body = body.replaceAll(toReplace, parameter);
                    order++;
                }
            }
        }
        return body;
    }
}
