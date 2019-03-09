/**
 * SensorWays IoT Platform
 * =================================================
 * Copyright (c) 2019 : Osama Oransa
 * =================================================
 * This class is responsible for managing device groups.
 */
package osa.ora.iot;

import java.io.IOException;
import java.util.List;
import javax.ejb.EJB;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import osa.ora.iot.beans.IConstant;
import osa.ora.iot.db.beans.DeviceGroup;
import osa.ora.iot.db.beans.Users;
import osa.ora.iot.db.session.UserSessionBean;

/**
 * IoTWebDeviceGroupServlet
 * @author ooransa
 */
@WebServlet(name = "IoTWebDeviceGroupServlet", urlPatterns = {"/DeviceGroupServlet"})
public class IoTWebDeviceGroupServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    @EJB(beanName = "UserSessionBean")
    UserSessionBean userSessionBean;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public IoTWebDeviceGroupServlet() {
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
            String action = request.getParameter("action");
            String deviceGroupId = request.getParameter("groupId");
            List<DeviceGroup> deviceGroupList = null;
            if (action == null) {
                request.getSession().setAttribute("ERROR_MSG", "Missing required parameters!");
                response.sendRedirect("error.jsp");
                return;
            } else {
                int actionVal = 0;
                int deviceGroupIdVal=0;
                try {
                    actionVal = Integer.parseInt(action);
                    if(deviceGroupId!=null) deviceGroupIdVal=Integer.parseInt(deviceGroupId);
                } catch (Exception ex) {
                    // do no thing ...
                }
                switch (actionVal) {
                    case IConstant.ACTION_WEB_LIST_DEVICE_GROUP:
                        deviceGroupList = userSessionBean.getIdentityDeviceGroups(user.getIdentityId(),true);
                        request.getSession().setAttribute("DEVICE_GROUP_LIST", deviceGroupList);
                        request.getRequestDispatcher("deviceGroupList.jsp").forward(request, response);
                        break;
                    case IConstant.ACTION_WEB_UPDATE_DEVICE_GROUP:
                        DeviceGroup toUpdatedeviceGroup = userSessionBean.getDeviceGroupById(Integer.parseInt(deviceGroupId),user.getIdentityId());
                        request.getSession().setAttribute("DEVICE_GROUP", toUpdatedeviceGroup);
                        request.getRequestDispatcher("editDeviceGroup.jsp").forward(request, response);
                        break;
                    case IConstant.ACTION_WEB_SAVE_DEVICE_GROUP:
                        String groupName=request.getParameter("name");
                        String devManagement = request.getParameter("dev_manage");
                        String contact=request.getParameter("contact");
                        String alerts=request.getParameter("alerts");
                        String email = request.getParameter("email");
                        String mobile = request.getParameter("mobile");
                        String highAlert1 = request.getParameter("high_alert1");
                        String highAlert2 = request.getParameter("high_alert2");
                        String lowAlert1 = request.getParameter("low_alert1");
                        String lowAlert2 = request.getParameter("low_alert2");
                        DeviceGroup toBeSavedDeviceGroup = (DeviceGroup)request.getSession().getAttribute("DEVICE_GROUP");
                        if(toBeSavedDeviceGroup==null || groupName==null  || devManagement == null || (contact!=null && (email==null)) || 
                                (alerts!=null && ( highAlert1==null || highAlert2==null || lowAlert1==null || lowAlert2==null))){
                            request.getSession().setAttribute("ERROR_MSG", "Missing Mandatory Data!");
                            response.sendRedirect("error.jsp");
                            return;                                
                        }
                        try{
                            Integer.parseInt(devManagement);
                        }catch(Exception ex){
                            request.getSession().setAttribute("ERROR_MSG", "Invalid values for Device Management!");
                            response.sendRedirect("error.jsp");
                            return;                                                                                            
                        }
                        if(alerts!=null){
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
                        }
                        if(contact!=null){
                            if(email.length()<7 || !email.contains("@")){
                                request.getSession().setAttribute("ERROR_MSG", "Invalid Email Address!");
                                response.sendRedirect("error.jsp");
                                return;                                                                
                            }
                        }
                        DeviceGroup editedDeviceGroup = userSessionBean.updateDeviceGroup(toBeSavedDeviceGroup.getId(),user.getIdentityId(),groupName,devManagement,contact,email,
                                mobile,alerts, highAlert1, highAlert2, lowAlert1, lowAlert2);
                        //audit trail
                        userSessionBean.addAuditTrail(user.getId(), actionVal,"DeviceGroup="+editedDeviceGroup.getId()+",Name="+editedDeviceGroup.getName());                                
                            request.getRequestDispatcher("DeviceGroupServlet?action=" + IConstant.ACTION_WEB_LIST_DEVICE_GROUP)
                                .forward(request, response);
                        break;
                    case IConstant.ACTION_WEB_REGISTER_DEVICE_GROUP:
                        deviceGroupList = userSessionBean.getIdentityDeviceGroups(user.getIdentityId(),true);
                        if (user.getUserRole() == IConstant.ROLE_READ_ONLY || (deviceGroupList!=null && deviceGroupList.size()>=50)) {
                            request.getSession().setAttribute("ERROR_MSG", "Missing The Privilages or Exceeded the Limit!");
                            response.sendRedirect("error.jsp");
                            return;
                        }
                        // do actual action!
                        String name=request.getParameter("name");
                        if(name==null){
                            request.getSession().setAttribute("ERROR_MSG", "Missing required parameters!");
                            response.sendRedirect("error.jsp");
                            return;
                        }
                        for(DeviceGroup deviceGroup:deviceGroupList){
                            if(deviceGroup.getName().equalsIgnoreCase(name)){
                                request.getSession().setAttribute("ERROR_MSG", "Device Group Name Already exist!");
                                response.sendRedirect("error.jsp");
                                return;                                
                            }
                        }
                        DeviceGroup newDeviceGroup=userSessionBean.addNewDeviceGroup(name, user.getIdentityId());
                        //audit trail
                        userSessionBean.addAuditTrail(user.getId(), actionVal,"DeviceGroup="+newDeviceGroup.getId()+",Name="+newDeviceGroup.getName());                                
                            request.getRequestDispatcher("DeviceGroupServlet?action=" + IConstant.ACTION_WEB_LIST_DEVICE_GROUP)
                                .forward(request, response);
                        break;
                    case IConstant.ACTION_WEB_DELETE_DEVICE_GROUP:
                        if (user.getUserRole() == IConstant.ROLE_READ_ONLY) {
                            request.getSession().setAttribute("ERROR_MSG", "Missing The Privilages!");
                            response.sendRedirect("error.jsp");
                            return;
                        }
                        // do actual action!
                        boolean results=userSessionBean.removeDeviceGroup(deviceGroupIdVal,user.getIdentityId());
                        // should route to the same page ..
                        if(results) {
                            //audit trail
                            userSessionBean.addAuditTrail(user.getId(), actionVal,"DeviceGroupId="+deviceGroupIdVal);                                                                
                            request.getRequestDispatcher("DeviceGroupServlet?action=" + IConstant.ACTION_WEB_LIST_DEVICE_GROUP)
                                .forward(request, response);
                        } else {
                            request.getSession().setAttribute("ERROR_MSG", "Error while deleting the device group!");
                            response.sendRedirect("error.jsp");
                        }
                        break;
                    case IConstant.ACTION_WEB_ACTIVATE_DEVICE_GROUP:
                        if (user.getUserRole() == IConstant.ROLE_READ_ONLY) {
                            request.getSession().setAttribute("ERROR_MSG", "Missing The Privilages!");
                            response.sendRedirect("error.jsp");
                            return;
                        }
                        // do actual action!
                        boolean activateResults=userSessionBean.activateDeviceGroup(deviceGroupIdVal,user.getIdentityId());
                        // should route to the same page ..
                        if(activateResults) {
                            //audit trail
                            userSessionBean.addAuditTrail(user.getId(), actionVal,"DeviceGroupId="+deviceGroupIdVal);                                                                
                            request.getRequestDispatcher("DeviceGroupServlet?action=" + IConstant.ACTION_WEB_LIST_DEVICE_GROUP)
                                .forward(request, response);
                        } else {
                            request.getSession().setAttribute("ERROR_MSG", "Error while activating devices in this device group!");
                            response.sendRedirect("error.jsp");
                        }
                        break;
                    case IConstant.ACTION_WEB_INACTIVATE_DEVICE_GROUP:
                        if (user.getUserRole() == IConstant.ROLE_READ_ONLY) {
                            request.getSession().setAttribute("ERROR_MSG", "Missing The Privilages!");
                            response.sendRedirect("error.jsp");
                            return;
                        }
                        // do actual action!
                        boolean deactivateResults=userSessionBean.inactivateDeviceGroup(deviceGroupIdVal,user.getIdentityId());
                        // should route to the same page ..
                        if(deactivateResults) {
                            //audit trail
                            userSessionBean.addAuditTrail(user.getId(), actionVal,"DeviceGroupId="+deviceGroupIdVal);                                                                
                            request.getRequestDispatcher("DeviceGroupServlet?action=" + IConstant.ACTION_WEB_LIST_DEVICE_GROUP)
                                .forward(request, response);
                        } else {
                            request.getSession().setAttribute("ERROR_MSG", "Error while deactivating devices in this device group!");
                            response.sendRedirect("error.jsp");
                        }
                        break;
                    case IConstant.ACTION_WEB_RESTART_DEVICE_GROUP:
                        if (user.getUserRole() == IConstant.ROLE_READ_ONLY) {
                            request.getSession().setAttribute("ERROR_MSG", "Missing The Privilages!");
                            response.sendRedirect("error.jsp");
                            return;
                        }
                        // do actual action!
                        boolean restartResults=userSessionBean.restartDeviceGroup(deviceGroupIdVal,user.getIdentityId(),IConstant.ACTION_BY_WEB);
                        // should route to the same page ..
                        if(restartResults) {
                            //audit trail
                            userSessionBean.addAuditTrail(user.getId(), actionVal,"DeviceGroupId="+deviceGroupIdVal);                                                                
                            request.getRequestDispatcher("DeviceGroupServlet?action=" + IConstant.ACTION_WEB_LIST_DEVICE_GROUP)
                                .forward(request, response);
                        } else {
                            request.getSession().setAttribute("ERROR_MSG", "Error while restarting devices in this device group!");
                            response.sendRedirect("error.jsp");
                        }
                        break;
                    case IConstant.ACTION_WEB_GET_MESSAGE_DEVICE_GROUP:
                        if (user.getUserRole() == IConstant.ROLE_READ_ONLY) {
                            request.getSession().setAttribute("ERROR_MSG", "Missing The Privilages!");
                            response.sendRedirect("error.jsp");
                            return;
                        }
                        // do actual action!
                        boolean updateRequestResults=userSessionBean.requestUpdateFromDeviceGroup(deviceGroupIdVal,user.getIdentityId(),IConstant.ACTION_BY_WEB);
                        // should route to the same page ..
                        if(updateRequestResults) {
                            //audit trail
                            userSessionBean.addAuditTrail(user.getId(), actionVal,"DeviceGroupId="+deviceGroupIdVal);                                                                
                            request.getRequestDispatcher("DeviceGroupServlet?action=" + IConstant.ACTION_WEB_LIST_DEVICE_GROUP)
                                .forward(request, response);
                        } else {
                            request.getSession().setAttribute("ERROR_MSG", "Error while requesting updates from devices in this device group!");
                            response.sendRedirect("error.jsp");
                        }
                        break;
                    default:
                        request.getSession().setAttribute("ERROR_MSG", "Missing required parameter!");
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
