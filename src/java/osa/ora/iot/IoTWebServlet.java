/**
 * SensorWays IoT Platform
 * =================================================
 * Copyright (c) 2019 : Osama Oransa
 * =================================================
 * This class is responsible for most of the web
 * interactions using different actions and required
 * parameters for each action.
 */
package osa.ora.iot;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.ejb.EJB;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import osa.ora.iot.beans.IConstant;
import osa.ora.iot.db.beans.ActionsLov;
import osa.ora.iot.db.beans.DeviceGroup;
import osa.ora.iot.db.beans.DeviceModel;
import osa.ora.iot.db.beans.Devices;
import osa.ora.iot.db.beans.HwType;
import osa.ora.iot.db.beans.Messages;
import osa.ora.iot.db.beans.SystemConfig;
import osa.ora.iot.db.beans.SystemJobs;
import osa.ora.iot.db.beans.TenantSettings;
import osa.ora.iot.db.beans.Users;
import osa.ora.iot.db.session.UserSessionBean;

/**
 * IoTWebServlet
 * @author ooransa
 */
@WebServlet(name = "IoTWebServlet", urlPatterns = {"/IoTWebServlet"})
public class IoTWebServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
        @EJB(beanName = "UserSessionBean")
        UserSessionBean userSessionBean;        

    /**
     * @see HttpServlet#HttpServlet()
     */
    public IoTWebServlet() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     * response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (request.getSession().getAttribute("USER") == null) {
            request.getSession().setAttribute("ERROR_MSG", "Your session is expired!");
            response.sendRedirect("error.jsp");
            return;
        } else {
            Users user = (Users) request.getSession().getAttribute("USER");
            TenantSettings settings = (TenantSettings) request.getSession().getAttribute("USER_SETTINGS");
            String action = request.getParameter("action");
            String deviceId = request.getParameter("deviceId");
            Devices deviceDetails=null;
            List<Devices> userDeviceList;
            //List<Device> deviceList = null;
            if (action == null) {
                request.getSession().setAttribute("ERROR_MSG", "Missing required parameters!");
                response.sendRedirect("error.jsp");
                return;
            } else {
                int actionVal = 0;
                try {
                    actionVal = Integer.parseInt(action);
                } catch (Exception ex) {
                    // do no thing ...
                }
                switch (actionVal) {
                    case IConstant.ACTION_WEB_LIST_DEVICES: // list all devices information
                        String groupId=request.getParameter("group");
                        if(groupId!=null){
                            if("-1".equals(groupId)){
                                groupId=null;
                            }
                        }
                        userDeviceList = userSessionBean.getUserDevices(user.getIdentityId(),true,true, groupId);
                        List<DeviceGroup> deviceGroupList = userSessionBean.getIdentityDeviceGroups(user.getIdentityId(),true);
                        request.getSession().setAttribute("DEVICE_GROUP_LIST", deviceGroupList);
                        request.getSession().setAttribute("DEVICE_LIST", userDeviceList);
                        request.getRequestDispatcher("deviceList.jsp").forward(request, response);
                        break;
                    case IConstant.ACTION_WEB_CONFIG_REPORT: 
                        userDeviceList = userSessionBean.getUserDevices(user.getIdentityId(),true,false,null);
                        request.getSession().setAttribute("DEVICE_LIST", userDeviceList);
                        request.getRequestDispatcher("report.jsp").forward(request, response);
                        break;
                    case IConstant.ACTION_WEB_LIST_SYS_JOBS: 
                        List<SystemJobs> sysJobsList = userSessionBean.getJobsList();
                        request.getSession().setAttribute("JOBS_LIST", sysJobsList);
                        request.getRequestDispatcher("systemJobs.jsp").forward(request, response);
                        break;
                    case IConstant.ACTION_WEB_EXECUTE_REPORT: 
                        String source = request.getParameter("source");
                        String event = request.getParameter("event");
                        String format = request.getParameter("format");
                        String duration = request.getParameter("duration");
                        if(source==null || event==null || format==null || duration==null){
                            request.getSession().setAttribute("ERROR_MSG", "Missing The Required Report Parameters!");
                            response.sendRedirect("error.jsp");
                            return;                                                
                        }
                        int formatVal=0;
                        int durationVal=0;
                        int eventVal=0;
                        try{
                            formatVal=Integer.parseInt(format);
                            eventVal=Integer.parseInt(event);
                            durationVal=Integer.parseInt(duration);
                        }catch(Exception ex){
                            request.getSession().setAttribute("ERROR_MSG", "Invalid Report Parameters Values!");
                            response.sendRedirect("error.jsp");
                            return;
                        }
                        List<String> allRecords=userSessionBean.generateReport(source, eventVal,durationVal, user.getIdentityId(), 2000);
                        response.setContentType("application/csv; charset=UTF-8");
                        //response.setCharacterEncoding("ISO-8859-1");
                        response.setHeader("Content-Disposition", "attachment; filename=\"report.csv\"");
                        response.setCharacterEncoding("UTF-8");
                        try (PrintWriter out = response.getWriter()) {
                            if(formatVal==1) {// .csv with headers
                                if(eventVal==1){
                                    out.println("Device Id, Device Friendly Name, Device Model, Device Firmware Version, Device Bar Code, Device Status, Device Location, Device Last Ping Time, Device Tags, Device Last Update Time, Inbound Traffic (bytes), Outbound Traffic (bytes)");
                                }else{
                                    out.println("Device Id, Message Id, Message Content, Message Type, Message Time");
                                }
                            }
                            for (String line : allRecords) {
                                //line=line.replaceAll(",", "\t\t");
                                out.println(line);
                            }
                        }                        
                        break;
                    case IConstant.ACTION_WEB_LIST_DEVICE_MESSAGES:
                        //no need to load the device again, get it from the session scope
                        deviceDetails = userSessionBean.getUserDeviceDetails(user.getIdentityId(), deviceId,true);
                        if (deviceDetails == null) {
                            // device not found for this identity or missing something
                            request.getSession().setAttribute("ERROR_MSG", "Error Loading this device details!");
                            response.sendRedirect("error.jsp");
                        } else {
                            //TODO: load all device messages here
                            List<Messages> messages=userSessionBean.getDeviceMessages(deviceId,settings.getMaxRetainedMessages());
                            //TODO: put them in the session
                            request.getSession().setAttribute("DEVICE_DETAILS", deviceDetails);
                            request.getSession().setAttribute("MESSAGES_LIST", messages);
                            System.out.println("Device id="+deviceDetails.getId());
                            System.out.println("Device Model id="+deviceDetails.getModel().getId());
                            request.getRequestDispatcher("deviceMessages.jsp").forward(request, response);
                        }
                        break;
                    case IConstant.ACTION_WEB_DELETE_ALL_DEVICE_MESSAGES:
                        deviceDetails = userSessionBean.getUserDeviceDetails(user.getIdentityId(), deviceId,true);
                        if (deviceDetails == null) {
                            // device not found for this identity or missing something
                            request.getSession().setAttribute("ERROR_MSG", "Error Loading this device details!");
                            response.sendRedirect("error.jsp");
                        } else {
                            //Delete all device messages
                            userSessionBean.purgeDeviceMessages(deviceId);
                            //audit record for this deletion
                            userSessionBean.addAuditTrail(user.getId(), actionVal,"DeviceId="+deviceId);
                            //reload in case of any new messages
                            List<Messages> messages=userSessionBean.getDeviceMessages(deviceId,settings.getMaxRetainedMessages());
                            //TODO: put them in the session
                            request.getSession().setAttribute("DEVICE_DETAILS", deviceDetails);
                            request.getSession().setAttribute("MESSAGES_LIST", messages);
                            request.getRequestDispatcher("deviceMessages.jsp").forward(request, response);
                        }
                        break;
                    case IConstant.ACTION_WEB_MESSAGE_GRAPH:
                        //no need to load the device again, get it from the session scope
                        deviceDetails = userSessionBean.getUserDeviceDetails(user.getIdentityId(), deviceId,true);
                        String type = request.getParameter("type");
                        String sensor = request.getParameter("sensor");
                        if (deviceDetails == null || type==null || sensor==null) {
                            // device not found for this identity or missing something
                            request.getSession().setAttribute("ERROR_MSG", "Error Loading this device details!");
                            response.sendRedirect("error.jsp");
                        } else {
                            //TODO: load all device messages here
                            List<Messages> messages=userSessionBean.getDeviceMessages(deviceId,settings.getMaxRetainedMessages());
                            //TODO: put them in the session
                            request.getSession().setAttribute("DEVICE_DETAILS", deviceDetails);
                            request.getSession().setAttribute("MESSAGES_LIST", messages);
                            request.getSession().setAttribute("GRAPH", type);
                            request.getSession().setAttribute("SENSOR", sensor);
                            request.getRequestDispatcher("graphs.jsp").forward(request, response);
                        }
                        break;
                    case IConstant.ACTION_WEB_MESSAGE_GRAPH_REFRESH:
                        //no need to load the device again, get it from the session scope
                        String refresh = request.getParameter("refresh");
                        String connect = request.getParameter("connect");
                        deviceDetails=(Devices)request.getSession().getAttribute("DEVICE_DETAILS");
                        type=(String)request.getSession().getAttribute("GRAPH");       
                        sensor = (String)request.getSession().getAttribute("SENSOR");  
                        if(!refresh.equals("0")) request.getSession().setAttribute("REFRESH", refresh);
                        if(!connect.equals("0")) request.getSession().setAttribute("CONNECT", connect);
                        response.sendRedirect("IoTWebServlet?deviceId="+deviceDetails.getDeviceId()+"&action="+IConstant.ACTION_WEB_MESSAGE_GRAPH+"&type="+type+"&sensor="+sensor);
                        break;
                    case IConstant.ACTION_WEB_REGISTER_MODEL: 
                        if(user.getSystemAdmin()!=1){
                            request.getSession().setAttribute("ERROR_MSG", "Missing The Required Privilages!");
                            response.sendRedirect("error.jsp");
                            return;                    
                        }
                        List<HwType> hwTypeList = userSessionBean.getHWTypes();
                        request.getSession().setAttribute("HW_TYPES", hwTypeList);
                        request.getRequestDispatcher("registerDeviceModel.jsp").forward(request, response);
                        break;
                    case IConstant.ACTION_WEB_REGISTER_DEVICE: 
                        request.getRequestDispatcher("registerDevice.jsp").forward(request, response);
                        break;
                    case IConstant.ACTION_WEB_REGISTER_BARCODE: 
                        if(user.getSystemAdmin()!=1){
                            request.getSession().setAttribute("ERROR_MSG", "Missing The Required Privilages!");
                            response.sendRedirect("error.jsp");
                            return;                    
                        }
                        //only we can register devices for active models
                        List<DeviceModel> deviceModelList = userSessionBean.getActiveDeviceModels();
                        request.getSession().setAttribute("DEVICE_MODELS", deviceModelList);
                        request.getRequestDispatcher("registerBarcode.jsp").forward(request, response);
                        break;
                    case IConstant.ACTION_WEB_MANAGE_MODELS: 
                        if(user.getSystemAdmin()!=1){
                            request.getSession().setAttribute("ERROR_MSG", "Missing The Required Privilages!");
                            response.sendRedirect("error.jsp");
                            return;                    
                        }
                        List<DeviceModel> modelList = userSessionBean.getAllDeviceModels(true);
                        request.getSession().setAttribute("DEVICE_MODELS", modelList);
                        request.getRequestDispatcher("manageModels.jsp").forward(request, response);
                        break;
                    case IConstant.ACTION_WEB_LIST_AUDIT: 
                        String maxRowsStr = request.getParameter("rows");
                        String actionParameter = request.getParameter("actionFilter");
                        int maxRows=50;
                        int actionValue=1000;
                        if(maxRowsStr!=null){
                            try{
                                if(actionParameter!=null) actionValue=Integer.parseInt(actionParameter);
                                if(maxRowsStr!=null) maxRows=Integer.parseInt(maxRowsStr);
                            }catch(Exception ex){
                                //do nothing
                            }
                        }
                        List<ActionsLov> allActions=userSessionBean.getAllActions();
                        List<Object> auditRecords= userSessionBean.selectAllAuditRecords(maxRows,user.getIdentityId(), actionValue, user.getSystemAdmin()==1);
                        request.getSession().setAttribute("ACTIONS", allActions);
                        request.getSession().setAttribute("AUDIT_RECORDS", auditRecords);
                        request.getRequestDispatcher("viewAudit.jsp").forward(request, response);
                        break;
                    case IConstant.ACTION_WEB_LOAD_SYS_CONFIG: 
                        if(user.getSystemAdmin()!=1){
                            request.getSession().setAttribute("ERROR_MSG", "Missing The Required Privilages!");
                            response.sendRedirect("error.jsp");
                            return;                    
                        }
                        SystemConfig config=userSessionBean.getSystemConfigurations();
                        request.getSession().setAttribute("SYS_CONFIG", config);
                        request.getRequestDispatcher("global.jsp").forward(request, response);
                        break;
                    case IConstant.ACTION_WEB_SAVE_SYS_CONFIG: 
                        if(user.getSystemAdmin()!=1){
                            request.getSession().setAttribute("ERROR_MSG", "Missing The Required Privilages!");
                            response.sendRedirect("error.jsp");
                            return;                    
                        }
                        String host = request.getParameter("host");
                        String http = request.getParameter("http");
                        String https = request.getParameter("https");
                        String email_enabled = request.getParameter("email_enabled");
                        String language = request.getParameter("language");
                        String emailUser = request.getParameter("email");
                        String emailpassword = request.getParameter("password");
                        String server = request.getParameter("server");
                        String ssl = request.getParameter("ssl");
                        String port = request.getParameter("port");
                        String rest = request.getParameter("rest");
                        String android = request.getParameter("android");
                        String iOs = request.getParameter("iOs");
                        String timezone = request.getParameter("timezone");
                        if(host==null || http==null || https==null || email_enabled==null || language==null || emailUser==null || emailpassword==null || server==null 
                                || ssl==null || port==null || rest==null || android==null || iOs==null || timezone==null){
                            request.getSession().setAttribute("ERROR_MSG", "Missing The Required Parameters!");
                            response.sendRedirect("error.jsp");
                            return; 
                        }
                        if(emailUser.length()<7 || !emailUser.contains("@")){
                            request.getSession().setAttribute("ERROR_MSG", "Invalid Email Address!");
                            response.sendRedirect("error.jsp");
                            return;                                                                
                        }
                        int httpsPortVal=0;
                        int httpPortVal=0;
                        int emailEnabledVal=0;
                        int languageVal=0;
                        int sslVal=0;
                        try{
                            httpPortVal=Integer.parseInt(http);
                            httpsPortVal=Integer.parseInt(https);
                            emailEnabledVal=Integer.parseInt(email_enabled);
                            languageVal=Integer.parseInt(language);
                            sslVal=Integer.parseInt(ssl);
                            Integer.parseInt(port);
                            Float.parseFloat(rest);
                            Float.parseFloat(android);
                            Float.parseFloat(iOs);
                        }catch(Exception ex){
                            ex.printStackTrace();
                            request.getSession().setAttribute("ERROR_MSG", "Invalid Parameter Values!");
                            response.sendRedirect("error.jsp");
                            return; 
                        }
                        SystemConfig newConfig=userSessionBean.saveSystemConfigurations(host,httpPortVal,httpsPortVal,emailEnabledVal,languageVal,emailUser,emailpassword,server,sslVal,port,rest,android,iOs,timezone);
                        //audit trail
                        userSessionBean.addAuditTrail(user.getId(), actionVal,"Version="+newConfig.getVersionId());                        
                        request.getSession().setAttribute("SYS_CONFIG", newConfig);
                        request.getRequestDispatcher("global.jsp").forward(request, response);
                        break;
                    case IConstant.ACTION_WEB_SAVE_MODEL: 
                        if(user.getSystemAdmin()!=1){
                            request.getSession().setAttribute("ERROR_MSG", "Missing The Required Privilages!");
                            response.sendRedirect("error.jsp");
                            return;                    
                        }                        
                        String name = request.getParameter("name");
                        String status = request.getParameter("status");
                        String version = request.getParameter("version");
                        String noOfSensors = request.getParameter("sensorsCount");
                        String sensor1 = request.getParameter("sensor1");
                        String sensor2 = request.getParameter("sensor2");
                        String sensorIcon1 = request.getParameter("sensor_icon1");
                        String sensorIcon2 = request.getParameter("sensor_icon2");
                        String sensorGraph1 = request.getParameter("graph1");
                        String sensorGraph2 = request.getParameter("graph2");
                        String device0 = request.getParameter("device0");
                        String device1 = request.getParameter("device1");
                        String device2 = request.getParameter("device2");
                        String highalert1 = request.getParameter("high_alert1");
                        String highalert2 = request.getParameter("high_alert2");
                        String lowalert1 = request.getParameter("low_alert1");
                        String lowalert2 = request.getParameter("low_alert2");
                        String hwTypeStr = request.getParameter("hwVal");
                        String normal = request.getParameter("normal");
                        String alert = request.getParameter("alert");
                        String error = request.getParameter("error");
                        if(name==null || hwTypeStr ==null || version==null || status==null || normal==null || noOfSensors==null || 
                                ("1".equals(noOfSensors) && (sensor1==null || sensorIcon1==null || sensorGraph1==null)) ||
                                ("2".equals(noOfSensors) && (sensor1==null || sensorIcon1==null || sensorGraph1==null || sensor2==null || sensorIcon2==null || sensorGraph2==null)) ||
                                alert==null || error == null  || userSessionBean.checkModelNameIsThere(name)){
                            request.getSession().setAttribute("ERROR_MSG", "Missing The Required Parameters or Model Already exist!");
                            response.sendRedirect("error.jsp");
                            return;                    
                        }
                        if(device0==null && device1==null && device2==null){
                            request.getSession().setAttribute("ERROR_MSG", "You need to select at least one model type!");
                            response.sendRedirect("error.jsp");
                            return;                    
                        }
                        int versionVal=0;
                        int statusVal=1;
                        int hwTypeVal=1;
                        int noOfSensorsVal=1;
                        int sensor1Icon=-1;
                        int sensor2ICon=-1;
                        int sensor1Graph=-1;
                        int sensor2Graph=-1;
                        try{
                            hwTypeVal=Integer.parseInt(hwTypeStr);
                            noOfSensorsVal=Integer.parseInt(noOfSensors);
                            if(noOfSensorsVal>0){
                                sensor1Icon=Integer.parseInt(sensorIcon1);
                                sensor1Graph=Integer.parseInt(sensorGraph1);                                
                            }
                            if(noOfSensorsVal>1){
                                sensor2ICon=Integer.parseInt(sensorIcon2);
                                sensor2Graph=Integer.parseInt(sensorGraph2);
                            }
                        }catch(Exception ex){
                            request.getSession().setAttribute("ERROR_MSG", "Cannot recognize required attributes to register this new Model!");
                            response.sendRedirect("error.jsp");
                            return;                                                
                        }
                        boolean deviceSensor=true;
                        boolean deviceControl1=true;
                        boolean deviceControl2=true;
                        if(device0==null){
                            deviceSensor=false;
                        }
                        if(device1==null){
                            deviceControl1=false;
                        }
                        if(device2==null){
                            deviceControl2=false;
                        }
                        if("1".equals(version)){
                            versionVal=1;
                        }
                        if("2".equals(status)){
                            statusVal=2;
                        }
                        int createdModels = userSessionBean.createNewModel(name,versionVal,statusVal,deviceSensor, deviceControl1, deviceControl2,highalert1,
                                highalert2,lowalert1, lowalert2, hwTypeVal, sensor1, sensor2,sensor1Icon, sensor2ICon, normal,alert, error, sensor1Graph, sensor2Graph,noOfSensorsVal);
                        if(createdModels>0){
                            userSessionBean.addAuditTrail(user.getId(), actionVal,"Total DeviceModels Created="+createdModels);                                                                
                            request.getRequestDispatcher("IoTWebServlet?action=" + IConstant.ACTION_WEB_MANAGE_MODELS)
                                    .forward(request, response);
                        }else{
                            request.getSession().setAttribute("ERROR_MSG", "Failed to save new model!");
                            response.sendRedirect("error.jsp");
                            return;                                                
                        }
                        break;
                    case IConstant.ACTION_WEB_ACTIVATE_MODEL:
                        if(user.getSystemAdmin()!=1){
                            request.getSession().setAttribute("ERROR_MSG", "Missing The Required Privilages!");
                            response.sendRedirect("error.jsp");
                            return;                    
                        }                        
                        String modelIdToActive = request.getParameter("modelId");
                        int modelIdValToActive=0;
                        try{
                            modelIdValToActive=Integer.parseInt(modelIdToActive);
                        }catch(Exception ex){
                            //do no thing ..
                        }
                        if(modelIdToActive==null || modelIdValToActive==0){
                            request.getSession().setAttribute("ERROR_MSG", "Missing The Required Parameters!");
                            response.sendRedirect("error.jsp");
                            return;                    
                        } 
                        // do actual action!
                        boolean activateRestuls = userSessionBean.activateDeviceModel(modelIdValToActive);
                        // should route to the same page ..
                        if (activateRestuls) {
                            //audit trail
                            userSessionBean.addAuditTrail(user.getId(), actionVal,"DeviceModelId="+modelIdValToActive);                                                                
                            request.getRequestDispatcher("IoTWebServlet?action=" + IConstant.ACTION_WEB_MANAGE_MODELS)
                                    .forward(request, response);
                        } else {
                            request.getSession().setAttribute("ERROR_MSG", "Failed to activate device model!");
                            response.sendRedirect("error.jsp");
                        }
                        break;
                    case IConstant.ACTION_WEB_INACTIVATE_MODEL:
                        if(user.getSystemAdmin()!=1){
                            request.getSession().setAttribute("ERROR_MSG", "Missing The Required Privilages!");
                            response.sendRedirect("error.jsp");
                            return;                    
                        }                        
                        // do actual action!
                        String modelIdToInactive = request.getParameter("modelId");
                        int modelIdValToInactive=0;
                        try{
                            modelIdValToInactive=Integer.parseInt(modelIdToInactive);
                        }catch(Exception ex){
                            //do no thing ..
                        }
                        if(modelIdToInactive==null || modelIdValToInactive==0){
                            request.getSession().setAttribute("ERROR_MSG", "Missing The Required Parameters!");
                            response.sendRedirect("error.jsp");
                            return;                    
                        } 
                        boolean inactivateRestuls = userSessionBean.inactivateDeviceModel(modelIdValToInactive);
                        // should route to the same page ..
                        if (inactivateRestuls) {
                            //audit trail
                            userSessionBean.addAuditTrail(user.getId(), actionVal,"DeviceModelId="+modelIdValToInactive);                                                                
                            request.getRequestDispatcher("IoTWebServlet?action=" + IConstant.ACTION_WEB_MANAGE_MODELS)
                                    .forward(request, response);
                        } else {
                            request.getSession().setAttribute("ERROR_MSG", "Failed to inactivate device model!");
                            response.sendRedirect("error.jsp");
                        }
                        break;
                        
                    case IConstant.ACTION_WEB_UPDATE_FIRMWARE: 
                        if(user.getSystemAdmin()!=1){
                            request.getSession().setAttribute("ERROR_MSG", "Missing The Required Privilages!");
                            response.sendRedirect("error.jsp");
                            return;                    
                        }
                        //we can update firmware even for inactive device model as long as some 
                        //devices already uses it
                        //being inactive means no more barcode registered and no more new devices
                        //on boarded for that model
                        List<DeviceModel> deviceModelList2 = userSessionBean.getAllDeviceModels(false);
                        request.getSession().setAttribute("DEVICE_MODELS", deviceModelList2);
                        request.getSession().removeAttribute("d1FileName");
                        request.getSession().removeAttribute("d1FileData");
                        request.getSession().removeAttribute("inoFileName");
                        request.getSession().removeAttribute("inoFileData");
                        request.getRequestDispatcher("updateFirmware.jsp").forward(request, response);
                        break;
                    case IConstant.ACTION_WEB_SAVE_FIRMWARE:
                        if(user.getSystemAdmin()!=1){
                            request.getSession().setAttribute("ERROR_MSG", "Missing The Required Privilages!");
                            response.sendRedirect("error.jsp");
                            return;                    
                        }                        
                        String modelStr = request.getParameter("model");
                        //String d1FileName=(String)request.getSession().getAttribute("d1FileName");
                        byte[] d1FileData=(byte[])request.getSession().getAttribute("d1FileData");
                        //String inoFileName=(String)request.getSession().getAttribute("inoFileName");
                        byte[] inoFileData=(byte[])request.getSession().getAttribute("inoFileData");
                        int modelVal=0;
                        try{
                            modelVal=Integer.parseInt(modelStr);
                        }catch(Exception ex){
                            request.getSession().setAttribute("ERROR_MSG", "Invalid Device Model!");
                            response.sendRedirect("error.jsp");
                            return;
                        }
                        if(modelStr==null || d1FileData==null || d1FileData.length==0 || inoFileData==null || inoFileData.length==0){
                            request.getSession().setAttribute("ERROR_MSG", "Missing or Wrong Mandatory Data!");
                            response.sendRedirect("error.jsp");
                            return;                                
                        }
                        DeviceModel existingModel=userSessionBean.getDeviceModelById(modelVal);
                        int newVersion=existingModel.getFirmwareVersion()+1;
                        //validate file content contains the new version inside..
                        String validationOutput=IoTOTAServlet.validateBinary(newVersion, d1FileData, existingModel.gethWType().getValue()); 
                        if(validationOutput!=null){
                            request.getSession().setAttribute("ERROR_MSG", validationOutput);
                            response.sendRedirect("error.jsp");
                            return;                                                            
                        }
                        //TODO: do we need to do any source code validation ?
                        boolean saved=userSessionBean.saveNewFirmware(modelVal,newVersion, d1FileData, inoFileData);
                        request.getSession().removeAttribute("d1FileName");
                        request.getSession().removeAttribute("d1FileData");
                        request.getSession().removeAttribute("inoFileName");
                        request.getSession().removeAttribute("inoFileData");
                        //audit trail
                        if(saved){
                            userSessionBean.addAuditTrail(user.getId(), actionVal,"Firmware="+newVersion+",Model="+modelStr);
                            userSessionBean.actionDevicesUponFirmwareUpdate(existingModel,newVersion);
                            request.getRequestDispatcher("main.jsp").forward(request, response);
                        }else{
                            request.getSession().setAttribute("ERROR_MSG", "Failed to Upload New Firmware!");
                            response.sendRedirect("error.jsp");
                            return;                                                
                        }
                        break;
                    case IConstant.ACTION_WEB_SAVE_BARCODE:
                        if(user.getSystemAdmin()!=1){
                            request.getSession().setAttribute("ERROR_MSG", "Missing The Required Privilages!");
                            response.sendRedirect("error.jsp");
                            return;                    
                        }                        
                        String newBarcode = request.getParameter("barcode");
                        String model = request.getParameter("model");
                        if(newBarcode==null || model==null || newBarcode.length()<7){
                            request.getSession().setAttribute("ERROR_MSG", "Missing Mandatory Data!");
                            response.sendRedirect("error.jsp");
                            return;                                
                        }
                        int modelVal2=0;
                        try{
                            modelVal2=Integer.parseInt(model);
                        }catch(Exception ex){
                            request.getSession().setAttribute("ERROR_MSG", "Invalid Device Model!");
                            response.sendRedirect("error.jsp");
                            return;
                        }
                        newBarcode=userSessionBean.registerNewBarcode(newBarcode, modelVal2);
                        //audit trail
                        userSessionBean.addAuditTrail(user.getId(), actionVal,"Barcode="+newBarcode+",Model="+model);
                        request.getRequestDispatcher("main.jsp").forward(request, response);
                        break;
                    case IConstant.ACTION_WEB_SAVE_NEW_DEVICE:
                        String barcode = request.getParameter("barcode");
                        String newDeviceId="";
                        String password="";
                        synchronized(barcode){
                            DeviceModel modelOfDevice=userSessionBean.checkbarCodeIsThereAndLoadModel(barcode);
                            if(modelOfDevice==null){
                                request.getSession().setAttribute("ERROR_MSG", "Invalid Device Barcode!");
                                response.sendRedirect("error.jsp");
                                return;
                            }
                            password = request.getParameter("password");
                            String friendlyName = request.getParameter("friendlyName");
                            String location = request.getParameter("location");
                            String tag = request.getParameter("tag");
                            String customName = request.getParameter("custom_name");
                            String customValue = request.getParameter("custom_value");
                            String email = request.getParameter("email");
                            String mobile= request.getParameter("mobile"); //could be null 
                            String device1Name = request.getParameter("device1Name");
                            String device2Name = request.getParameter("device2Name");
                            String highAlert1 = request.getParameter("high_alert1");
                            String highAlert2 = request.getParameter("high_alert2");
                            String lowAlert1 = request.getParameter("low_alert1");
                            String lowAlert2 = request.getParameter("low_alert2");
                            String devManagement = request.getParameter("dev_manage");
                            String group = request.getParameter("group");
                            if(group==null || password==null || "".equals(password) || friendlyName==null || location==null || email==null || devManagement ==null ||
                                    (modelOfDevice.getId()>IConstant.ONE_DEVICE_THRESHOLD && device1Name==null) 
                                    || (modelOfDevice.getId()>IConstant.TWO_DEVICE_THRESHOLD && device2Name==null) ){
                                request.getSession().setAttribute("ERROR_MSG", "Missing Mandatory Data!");
                                response.sendRedirect("error.jsp");
                                return;                                
                            }
                            //i think this is useless ...
                            if("".equals(highAlert1)) highAlert1=null;
                            if("".equals(highAlert2)) highAlert1=null;
                            if("".equals(lowAlert1)) highAlert1=null;
                            if("".equals(lowAlert2)) highAlert1=null;
                            //don't allow null values for alerts
                            if((modelOfDevice.getHighAlertValue1()!=null && highAlert1==null) ||
                                    (modelOfDevice.getHighAlertValue2()!=null && highAlert2==null) ||
                                    (modelOfDevice.getLowAlertValue1()!=null && lowAlert1==null) ||
                                    (modelOfDevice.getLowAlertValue2()!=null && lowAlert2==null)){
                                request.getSession().setAttribute("ERROR_MSG", "Missing Mandatory Sensors Alert Values!");
                                response.sendRedirect("error.jsp");
                                return;                                                                
                            }
                            try{
                                if(highAlert1!=null) Integer.parseInt(highAlert1);
                                if(highAlert2!=null) Integer.parseInt(highAlert2);
                                if(lowAlert1!=null) Integer.parseInt(lowAlert1);
                                if(lowAlert2!=null) Integer.parseInt(lowAlert2);
                            }catch(Exception ex){
                                request.getSession().setAttribute("ERROR_MSG", "Invalid values for Sensors Alert Values!");
                                response.sendRedirect("error.jsp");
                                return;                                                                                            
                            }
                            if(email.length()<7 || !email.contains("@")){
                                request.getSession().setAttribute("ERROR_MSG", "Invalid Email Address!");
                                response.sendRedirect("error.jsp");
                                return;                                                                
                            }
                            int deviceManagment=0;
                            try{
                                deviceManagment=Integer.parseInt(devManagement);
                                if(deviceManagment>2 || deviceManagment<0) throw new Exception("Error!");
                            }catch(Exception ex){
                                request.getSession().setAttribute("ERROR_MSG", "Invalid values for Device Management!");
                                response.sendRedirect("error.jsp");
                                return;   
                            }
                            newDeviceId=userSessionBean.registerNewDevice(user.getIdentityId(),barcode,password,friendlyName,
                                    location,tag, customName,customValue, email, mobile,modelOfDevice.getId(),device1Name, 
                                    device2Name, highAlert1, highAlert2, lowAlert1, lowAlert2,deviceManagment,group);
                            userSessionBean.removeBarCode(barcode);
                        }
                        //audit trail
                        userSessionBean.addAuditTrail(user.getId(), actionVal,"DeviceId="+newDeviceId);
                        //after saving ...
                        userDeviceList = userSessionBean.getUserDevices(user.getIdentityId(),true,true,null);
                        request.getSession().setAttribute("DEVICE_LIST", userDeviceList);
                        request.getSession().setAttribute("NEW_DEVICE_ID", newDeviceId);
                        request.getSession().setAttribute("NEW_DEVICE_PASSWORD", password);
                        request.getRequestDispatcher("deviceConfirmation.jsp").forward(request, response);
                        break;
                    case IConstant.ACTION_WEB_EDIT_DEVICE:
                        Devices deviceToEdit=(Devices)request.getSession().getAttribute("DEVICE_DETAILS");
                        String friendlyName = request.getParameter("friendlyName");
                        String location = request.getParameter("location");
                        String tag = request.getParameter("tag");
                        String customName = request.getParameter("custom_name");
                        String customValue = request.getParameter("custom_value");
                        String email = request.getParameter("email");
                        String mobile = request.getParameter("mobile");
                        String device1Name = request.getParameter("device1Name");
                        String device2Name = request.getParameter("device2Name");
                        String highAlert1 = request.getParameter("high_alert1");
                        String highAlert2 = request.getParameter("high_alert2");
                        String lowAlert1 = request.getParameter("low_alert1");
                        String lowAlert2 = request.getParameter("low_alert2");                            
                        String sr1 = request.getParameter("sr1");
                        String sr2 = request.getParameter("sr2");
                        String sr3 = request.getParameter("sr3");
                        String sr4 = request.getParameter("sr4");
                        String devManagement = request.getParameter("dev_manage");
                        String group = request.getParameter("group");
                        if(group==null || deviceToEdit==null || friendlyName==null || location==null || email==null   || devManagement == null ||
                                (deviceToEdit.getDeviceModel()>IConstant.ONE_DEVICE_THRESHOLD && (device1Name==null || sr1==null || sr2==null)) 
                                || (deviceToEdit.getDeviceModel()>IConstant.TWO_DEVICE_THRESHOLD && (device2Name==null || sr3==null ||sr4==null)) ){
                            request.getSession().setAttribute("ERROR_MSG", "Missing Mandatory Data!");
                            response.sendRedirect("error.jsp");
                            return;                                
                        }
                        //i think this is useless ...
                        if("".equals(highAlert1)) highAlert1=null;
                        if("".equals(highAlert2)) highAlert1=null;
                        if("".equals(lowAlert1)) highAlert1=null;
                        if("".equals(lowAlert2)) highAlert1=null;
                        //don't allow null values for alerts
                        if((deviceToEdit.getModel().getNoOfSensors()>0 && (highAlert1==null || lowAlert1==null)) ||
                            (deviceToEdit.getModel().getNoOfSensors()>1 && (highAlert1==null || lowAlert1==null || highAlert2==null || lowAlert2==null))){
                            request.getSession().setAttribute("ERROR_MSG", "Missing Mandatory Sensors Alert Values!");
                            response.sendRedirect("error.jsp");
                            return;                                                                
                        }
                        try{
                            if(highAlert1!=null) Integer.parseInt(highAlert1);
                            if(highAlert2!=null) Integer.parseInt(highAlert2);
                            if(lowAlert1!=null) Integer.parseInt(lowAlert1);
                            if(lowAlert2!=null) Integer.parseInt(lowAlert2);
                        }catch(Exception ex){
                            request.getSession().setAttribute("ERROR_MSG", "Invalid values for Sensors Alert Values!");
                            response.sendRedirect("error.jsp");
                            return;                                                                                            
                        }
                        if(email.length()<7 || !email.contains("@")){
                            request.getSession().setAttribute("ERROR_MSG", "Invalid Email Address!");
                            response.sendRedirect("error.jsp");
                            return;                                                                
                        }
                        //smart rules
                        int smartRule1=0;
                        int smartRule2=0;
                        int smartRule3=0;
                        int smartRule4=0;
                        try{
                            if(deviceToEdit.getDeviceModel()>IConstant.ONE_DEVICE_THRESHOLD){
                                smartRule1=Integer.parseInt(sr1);
                                smartRule2=Integer.parseInt(sr2);
                                smartRule1+=smartRule2;
                            }
                            if(deviceToEdit.getDeviceModel()>IConstant.TWO_DEVICE_THRESHOLD){
                                smartRule3=Integer.parseInt(sr3);
                                smartRule4=Integer.parseInt(sr4);
                                smartRule3+=smartRule4;
                            }
                        }catch(Exception ex){
                            request.getSession().setAttribute("ERROR_MSG", "Invalid Smart Rules Values!");
                            response.sendRedirect("error.jsp");
                            return;   
                        }
                        int deviceManagment = 0;
                        try{
                            deviceManagment=Integer.parseInt(devManagement);
                            if(deviceManagment>2 || deviceManagment<0) throw new Exception("Error!");
                        }catch(Exception ex){
                            request.getSession().setAttribute("ERROR_MSG", "Invalid values for Device Management!");
                            response.sendRedirect("error.jsp");
                            return;   
                        }
                        deviceToEdit=userSessionBean.updateDevice(deviceToEdit,friendlyName,location,tag, customName,customValue, email, mobile, 
                                device1Name, device2Name, highAlert1,highAlert2,lowAlert1,lowAlert2, smartRule1, smartRule3,deviceManagment,group);
                        if(deviceToEdit==null){
                            request.getSession().setAttribute("ERROR_MSG", "Failed to Update Device Details!");
                            response.sendRedirect("error.jsp");
                            return;                                                            
                        }
                        //audit trail
                        userSessionBean.addAuditTrail(user.getId(), actionVal,"DeviceId="+deviceToEdit.getDeviceId());
                        //after saving ...
                        userDeviceList = userSessionBean.getUserDevices(user.getIdentityId(),true,true,null);
                        request.getSession().setAttribute("DEVICE_LIST", userDeviceList);
                        deviceToEdit=userSessionBean.getDeviceById(deviceToEdit.getDeviceId(), true);
                        request.getSession().setAttribute("DEVICE_DETAILS", deviceToEdit);
                        request.getRequestDispatcher("deviceMessages.jsp").forward(request, response);
                        break;
                    case IConstant.ACTION_ON_DEVICE1:
                    case IConstant.ACTION_OFF_DEVICE1:
                    case IConstant.ACTION_ON_DEVICE2:
                    case IConstant.ACTION_OFF_DEVICE2:
                    case IConstant.ACTION_RESTART:
                    case IConstant.ACTION_GET_MESSAGE:
                        if (user.getUserRole() == IConstant.ROLE_READ_ONLY) {
                            request.getSession().setAttribute("ERROR_MSG", "Missing the required privilages!");
                            response.sendRedirect("error.jsp");
                        } else {
                            // do actual action!
                            //need to be differentiated from Mobile here ...
                            Devices device=userSessionBean.setDeviceLastAction(actionVal, deviceId, user.getIdentityId(),IConstant.ACTION_BY_WEB);
                            if(device!=null){
                                //audit trail
                                userSessionBean.addAuditTrail(user.getId(), actionVal,"DeviceId="+device.getDeviceId());                                
                                // should route to the same page ..
                                request.getRequestDispatcher("IoTWebServlet?action=" + IConstant.ACTION_WEB_LIST_DEVICE_MESSAGES)
                                        .forward(request, response);
                                return;
                            }else{
                                // device not found for this identity or missing something
                                request.getSession().setAttribute("ERROR_MSG", "Error Loading this device details!");
                                response.sendRedirect("error.jsp");
                            }
                        }
                    case IConstant.ACTION_WEB_ENABLED_SYS_JOBS:
                        if(user.getSystemAdmin()!=1){
                            request.getSession().setAttribute("ERROR_MSG", "Missing The Required Privilages!");
                            response.sendRedirect("error.jsp");
                            return;                    
                        }                        
                        String sysJob = request.getParameter("jobId");
                        int sysJobToActive=0;
                        try{
                            sysJobToActive=Integer.parseInt(sysJob);
                        }catch(Exception ex){
                            //do no thing ..
                        }
                        if(sysJob==null || sysJobToActive==0){
                            request.getSession().setAttribute("ERROR_MSG", "Missing The Required Parameters!");
                            response.sendRedirect("error.jsp");
                            return;                    
                        } 
                        // do actual action!
                        userSessionBean.enableJob(sysJobToActive);
                        //audit trail
                        userSessionBean.addAuditTrail(user.getId(), actionVal,"SystemJobId="+sysJobToActive);                                                                
                        request.getRequestDispatcher("IoTWebServlet?action=" + IConstant.ACTION_WEB_LIST_SYS_JOBS)
                                .forward(request, response);
                        break;
                    case IConstant.ACTION_WEB_DISABLED_SYS_JOBS:
                        if(user.getSystemAdmin()!=1){
                            request.getSession().setAttribute("ERROR_MSG", "Missing The Required Privilages!");
                            response.sendRedirect("error.jsp");
                            return;                    
                        }                        
                        String sysJobToDel = request.getParameter("jobId");
                        int sysJobToDisable=0;
                        try{
                            sysJobToDisable=Integer.parseInt(sysJobToDel);
                        }catch(Exception ex){
                            //do no thing ..
                        }
                        if(sysJobToDel==null || sysJobToDisable==0){
                            request.getSession().setAttribute("ERROR_MSG", "Missing The Required Parameters!");
                            response.sendRedirect("error.jsp");
                            return;                    
                        } 
                        // do actual action!
                        userSessionBean.disableJob(sysJobToDisable);
                        //audit trail
                        userSessionBean.addAuditTrail(user.getId(), actionVal,"SystemJobId="+sysJobToDisable);                                                                
                        request.getRequestDispatcher("IoTWebServlet?action=" + IConstant.ACTION_WEB_LIST_SYS_JOBS)
                                .forward(request, response);
                        break;
                    case IConstant.ACTION_WEB_ACTIVATE_DEVICE:
                        if (user.getUserRole() == IConstant.ROLE_READ_ONLY) {
                            request.getSession().setAttribute("ERROR_MSG", "Missing the required privilages!");
                            response.sendRedirect("error.jsp");
                        } else {
                            // do actual action!
                            //need to be differentiated from Mobile here ...
                            Devices device=userSessionBean.activateDevice(deviceId,user.getIdentityId());
                            if(device!=null){
                                //audit trail
                                userSessionBean.addAuditTrail(user.getId(), actionVal,"DeviceId="+device.getDeviceId());                                
                                // should route to the same page ..
                                request.getRequestDispatcher("IoTWebServlet?action=" + IConstant.ACTION_WEB_LIST_DEVICES)
                                        .forward(request, response);
                                return;
                            }else{
                                // device not found for this identity or missing something
                                request.getSession().setAttribute("ERROR_MSG", "Error Loading this device details!");
                                response.sendRedirect("error.jsp");
                            }
                        }
                    case IConstant.ACTION_WEB_INACTIVATE_DEVICE:
                        if (user.getUserRole() == IConstant.ROLE_READ_ONLY) {
                            request.getSession().setAttribute("ERROR_MSG", "Missing the required privilages!");
                            response.sendRedirect("error.jsp");
                        } else {
                            // do actual action!
                            //need to be differentiated from Mobile here ...
                            Devices device=userSessionBean.inactivateDevice(deviceId,user.getIdentityId());
                            if(device!=null){
                                //audit trail
                                userSessionBean.addAuditTrail(user.getId(), actionVal,"DeviceId="+device.getDeviceId());                                
                                // should route to the same page ..
                                request.getRequestDispatcher("IoTWebServlet?action=" + IConstant.ACTION_WEB_LIST_DEVICES)
                                        .forward(request, response);
                                return;
                            }else{
                                // device not found for this identity or missing something
                                request.getSession().setAttribute("ERROR_MSG", "Error Loading this device details!");
                                response.sendRedirect("error.jsp");
                            }
                        }
                    default:
                        request.getSession().setAttribute("ERROR_MSG", "Missing required parameters!");
                        response.sendRedirect("error.jsp");
                        return;
                }
            }
        }
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     * response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO Auto-generated method stub
        doGet(request, response);
    }
}
