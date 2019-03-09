/**
 * SensorWays IoT Platform
 * =================================================
 * Copyright (c) 2019 : Osama Oransa
 * =================================================
 * This class is responsible for building, and viewing
 * different IoT applications over this platform
 */
package osa.ora.iot;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.ejb.EJB;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import osa.ora.iot.beans.IConstant;
import osa.ora.iot.db.beans.Applications;
import osa.ora.iot.db.beans.DeviceGroup;
import osa.ora.iot.db.beans.Users;
import osa.ora.iot.db.session.UserSessionBean;

/**
 * IoTWebAppServlet
 * @author ooransa
 * 
 */
@WebServlet(name = "IoTWebAppServlet", urlPatterns = {"/IoTAppServlet"})
@MultipartConfig
public class IoTWebAppServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    @EJB(beanName = "UserSessionBean")
    UserSessionBean userSessionBean;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public IoTWebAppServlet() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     * response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //Note: we might need to remove this check from the luanch app to make it enabled 
        //over non-authentication channel.
        if (request.getSession().getAttribute("USER") == null) {
            request.getSession().setAttribute("ERROR_MSG", "Your session is expired!");
            response.sendRedirect("error.jsp");
            return;
        } else {
            Users user = (Users) request.getSession().getAttribute("USER");
            String action = request.getParameter("action");
            String appId = request.getParameter("appId");
            List<Applications> applicationList = null;
            if (action == null) {
                request.getSession().setAttribute("ERROR_MSG", "Missing required parameters!");
                response.sendRedirect("error.jsp");
                return;
            } else {
                int actionVal = 0;
                int appIdVal=0;
                try {
                    actionVal = Integer.parseInt(action);
                    if(appId!=null) appIdVal=Integer.parseInt(appId);
                } catch (Exception ex) {
                    // do no thing ...
                }
                switch (actionVal) {
                    case IConstant.ACTION_WEB_LIST_APPLICATIONS:
                        List <DeviceGroup> deviceGroupList = userSessionBean.getIdentityDeviceGroups(user.getIdentityId(),true);
                        request.getSession().setAttribute("DEVICE_GROUP_LIST", deviceGroupList);
                        applicationList = userSessionBean.getIdentityApplications(user.getIdentityId());
                        request.getSession().setAttribute("APP_LIST", applicationList);
                        request.getRequestDispatcher("applicationList.jsp").forward(request, response);
                        break;
                    case IConstant.ACTION_WEB_LUNCH_APPLICATION:
                        Applications lunchApp = userSessionBean.getApplicationById(appIdVal);
                        if(!Objects.equals(user.getIdentityId(), lunchApp.getIdentityId())){
                            request.getSession().setAttribute("ERROR_MSG", "Missing required privilages!"+lunchApp.getIdentityId()+"-"+user.getIdentityId());
                            response.sendRedirect("error.jsp");
                            return;                            
                        }
                        String[] deviceGroupId=lunchApp.getDeviceGroups().split(",");
                        List<DeviceGroup> deviceGroups=new ArrayList<DeviceGroup>();
                        for(int i=0;i<deviceGroupId.length;i++){
                            DeviceGroup group=userSessionBean.getDeviceGroupById(Integer.parseInt(deviceGroupId[i]), user.getIdentityId());
                            group.setDeviceList(userSessionBean.getUserDevices(user.getIdentityId(), true, true, deviceGroupId[i]));
                            deviceGroups.add(group);
                        }
                        request.getSession().setAttribute("My_APP", lunchApp);
                        request.getSession().setAttribute("My_APP_GROUPS", deviceGroups);
                        request.getRequestDispatcher("runApp.jsp").forward(request, response);
                        break;
                    case IConstant.ACTION_WEB_REGISTER_NEW_APP:
                        String name=request.getParameter("name");
                        String scope=request.getParameter("scope");
                        String type=request.getParameter("type");
                        String login=request.getParameter("login");
                        String email=request.getParameter("email");
                        String mobile=request.getParameter("mobile");
                        String status=request.getParameter("status");
                        String alert=request.getParameter("alert");
                        String[] groups=request.getParameterValues("groups");
                        Part filePart = request.getPart("file"); // Retrieves <input type="file" name="file">
                        byte[] fileData = null;
                        String fileName = "";
                        if(filePart!=null){
                            fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString(); // MSIE fix.
                            if(fileName!=null){
                                System.out.println("FileName="+fileName);
                                InputStream inputStream = filePart.getInputStream();            
                                final ByteArrayOutputStream out = new ByteArrayOutputStream();
                                byte[] buf = new byte[1024];
                                int read;
                                while ((read = inputStream.read(buf)) != -1) {
                                    out.write(buf, 0, read);
                                }
                                fileData=out.toByteArray();
                            }
                        }
                        if (name == null || scope == null || status == null || type == null || login == null || groups == null || groups.length<1 ||
                                alert==null || email==null ) {
                            request.getSession().setAttribute("ERROR_MSG", "Missing required parameters!");
                            response.sendRedirect("error.jsp");
                            return;
                        }
                        if(email.length()<7 || !email.contains("@")){
                            request.getSession().setAttribute("ERROR_MSG", "Invalid Email Address!");
                            response.sendRedirect("error.jsp");
                            return;                                                                
                        }
                        int typeVal=0;
                        int loginVal=0;
                        int statusVal=0;
                        int alertVal=0;
                        String deviceGroup="";
                        try {
                            typeVal=Integer.parseInt(type);
                            loginVal=Integer.parseInt(login);
                            statusVal=Integer.parseInt(status);
                            alertVal=Integer.parseInt(alert);
                            for(int i=0;i<groups.length;i++){
                                Integer.parseInt(groups[i]);
                                deviceGroup+=groups[i]+",";
                            }
                            System.out.println("before :"+deviceGroup);
                            deviceGroup=deviceGroup.substring(0,deviceGroup.length()-1);
                            System.out.println("after :"+deviceGroup);
                        } catch (Exception ex) {
                            request.getSession().setAttribute("ERROR_MSG", "Invalid Application Parameters!");
                            response.sendRedirect("error.jsp");
                            return;
                        }                                                        
                        Applications newApp = userSessionBean.saveNewApplication(user.getIdentityId(),name,scope,
                                statusVal,typeVal,loginVal,alertVal,email, mobile, deviceGroup,fileName, fileData);
                        //audit trail
                        userSessionBean.addAuditTrail(user.getId(), actionVal,"ApplicationId="+newApp.getId());                                                                
                        request.getRequestDispatcher("IoTAppServlet?action=" + IConstant.ACTION_WEB_LIST_APPLICATIONS)
                                .forward(request, response);
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
