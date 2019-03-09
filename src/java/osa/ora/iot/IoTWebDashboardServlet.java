/**
 * SensorWays IoT Platform
 * =================================================
 * Copyright (c) 2019 : Osama Oransa
 * =================================================
 * This class is responsible for dashboards management.
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
import osa.ora.iot.db.beans.Devices;
import osa.ora.iot.db.beans.Messages;
import osa.ora.iot.db.beans.SystemConfig;
import osa.ora.iot.db.beans.TenantSettings;
import osa.ora.iot.db.beans.Users;
import osa.ora.iot.db.session.UserSessionBean;

/**
 * IoTWebDashboardServlet
 * @author ooransa
 */
@WebServlet(name = "IoTWebDashboardServlet", urlPatterns = {"/DashboardServlet"})
public class IoTWebDashboardServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    @EJB(beanName = "UserSessionBean")
    UserSessionBean userSessionBean;
    SystemConfig systemConfigurations;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public IoTWebDashboardServlet() {
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
            if(user.getDashboardCreation()==0){
                response.sendRedirect("error.jsp");
                return;
            }
            TenantSettings settings = (TenantSettings) request.getSession().getAttribute("USER_SETTINGS");
            String action = request.getParameter("action");
            System.out.println("Action:"+action);
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
                    case IConstant.ACTION_WEB_SHOW_DASHBOARD:
                        System.out.println("Dashoboard="+user.getDashboard());
                        if(user.getDashboard()!=null && user.getDashboard().length()>3){
                            String[] parts=user.getDashboard().split("##");
                            int counter=1;
                            String type="";
                            for(String part:parts){
                                //String[] data=part.split("#");
                                Devices deviceDetails = userSessionBean.getUserDeviceDetails(user.getIdentityId(), part,true);
                                List<Messages> messages=userSessionBean.getDeviceMessages(part,settings.getMaxRetainedMessages());
                                //TODO: Need to define which sensor ..
                                type=""+deviceDetails.getModel().getSensor1Graph();
                                String key="DEVICE_DETAILS"+counter;
                                request.getSession().setAttribute(key, deviceDetails);
                                System.out.println("Save Device for key "+key+" Device:"+deviceDetails.getDeviceId());
                                key="MESSAGES_LIST"+counter;
                                request.getSession().setAttribute(key, messages);
                                System.out.println("Save Messages for key "+key+" messages:"+messages.size());
                                key="GRAPH"+counter;
                                request.getSession().setAttribute(key, type);
                                System.out.println("show Graph for key "+key+" graph type:"+type);
                                counter++;
                            }
                            request.getRequestDispatcher("dashboard.jsp").forward(request, response);
                        }else{
                            request.getRequestDispatcher("DashboardServlet?action=37").forward(request, response);
                        }
                        break;
                    case IConstant.ACTION_WEB_MESSAGE_GRAPH_REFRESH:
                        //no need to load the device again, get it from the session scope
                        String refresh = request.getParameter("refresh");
                        String connect = request.getParameter("connect");
                        if(!refresh.equals("0")) request.getSession().setAttribute("D_REFRESH", refresh);
                        if(!connect.equals("0")) request.getSession().setAttribute("D_CONNECT", connect);
                        response.sendRedirect("DashboardServlet?action="+IConstant.ACTION_WEB_SHOW_DASHBOARD);
                        break;
                    case IConstant.ACTION_WEB_CREATE_DASHBOARD:
                        List<Devices> userDeviceList = userSessionBean.getUserDevices(user.getIdentityId(),true,false,null);
                        if(userDeviceList==null || userDeviceList.size()<2){
                            request.getSession().setAttribute("ERROR_MSG", "Missing the minimal devices to create dashboard i.e. 2 devices!");
                            response.sendRedirect("error.jsp");
                            return;                            
                        }
                        request.getSession().setAttribute("DEVICE_LIST", userDeviceList);
                        request.getRequestDispatcher("createDashboard.jsp").forward(request, response);                        
                        break;
                    case IConstant.ACTION_WEB_SAVE_DASHBOARD:
                        String type=request.getParameter("layout");
                        String deviceId1=request.getParameter("source1");
                        String deviceId2=request.getParameter("source2");
                        String deviceId3=request.getParameter("source3");
                        String deviceId4=request.getParameter("source4");
                        if(deviceId1==null || deviceId2==null || type==null || (type.equals("2") && (deviceId3==null || deviceId4==null))){
                            request.getSession().setAttribute("ERROR_MSG", "Missing required dashboard parameters!");
                            response.sendRedirect("error.jsp");
                            return;                            
                        }
                        String dashboard="";
                        dashboard=deviceId1+"##"+deviceId2;
                        if(type.equals("2")){
                            dashboard+="##"+deviceId3+"##"+deviceId4;
                        }
                        for(int i=1;i<5;i++){
                            String key="DEVICE_DETAILS"+i;
                            request.getSession().removeAttribute(key);
                            key="MESSAGES_LIST"+i;
                            request.getSession().removeAttribute(key);
                            key="GRAPH"+i;
                            request.getSession().removeAttribute(key);
                        }
                        System.out.println("clean session from old dashboard");
                        Users userUpdated = userSessionBean.updateUserDetails(user.getId(), dashboard);
                        System.out.println("Dashboard="+dashboard);
                        if(userUpdated!=null){
                            //audit trail
                            userSessionBean.addAuditTrail(user.getId(), actionVal,"UserId="+userUpdated.getId());                                                                
                            //should go to dashboard view page
                            request.getSession().setAttribute("USER", userUpdated);
                            request.getRequestDispatcher("main.jsp").forward(request, response);                        
                        }else {
                            request.getSession().setAttribute("ERROR_MSG", "Failed To Save User Dashboard!");
                            response.sendRedirect("error.jsp");
                            return;
                        }
                        break;
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
