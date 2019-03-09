/**
 * SensorWays IoT Platform
 * =================================================
 * Copyright (c) 2019 : Osama Oransa
 * =================================================
 * This class is responsible for managing identity
 * or account settings.
 */
package osa.ora.iot;

import java.io.IOException;
import javax.ejb.EJB;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import osa.ora.iot.beans.IConstant;
import osa.ora.iot.db.beans.TenantSettings;

import osa.ora.iot.db.beans.Users;
import osa.ora.iot.db.session.UserSessionBean;

/**
 * IoTWebSettingServlet
 * @author ooransa
 */
@WebServlet(name = "IoTWebSettingServlet", urlPatterns = {"/SettingServlet"})
public class IoTWebSettingServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
        @EJB(beanName = "UserSessionBean")
        UserSessionBean userSessionBean;        

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public IoTWebSettingServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (request.getSession().getAttribute("USER") == null) {
                    request.getSession().setAttribute("ERROR_MSG", "Your session is expired!");
                    response.sendRedirect("error.jsp");
                    return;
		}
                Users user = (Users) request.getSession().getAttribute("USER");
                //2nd role should be removed from here and from settings.jsp
                if(user.getUserRole()!=IConstant.ROLE_READ_WRITE) { 
                    request.getSession().setAttribute("ERROR_MSG", "Missng the Required Privilages!");
                    response.sendRedirect("error.jsp");
                    return;
                }
		String messages = request.getParameter("messages");
                String language = request.getParameter("language");
		String heartbeats = request.getParameter("heartbeats");
		String intervals = request.getParameter("intervals");
		String grace = request.getParameter("grace");
		String alertMesg = request.getParameter("alertMesg");
                String faultMesg = request.getParameter("faultMesg");
                String alertOffline = request.getParameter("alertOffline");
                String alertOnline = request.getParameter("alertOnline");
                String alertDeviceReg = request.getParameter("alertDeviceReg");
                String alertDeviceUpd = request.getParameter("alertDeviceUpd");
                String alertDeviceIp = request.getParameter("alertDeviceIp");
                String alertDeviceFirmware = request.getParameter("alertDeviceFirmware");
		String timezone = request.getParameter("timezone");
		if (messages == null || heartbeats==null || intervals==null || language==null 
                        || alertMesg==null  || alertOffline==null || alertOnline==null || alertDeviceReg==null 
                        || faultMesg==null || alertDeviceUpd==null || timezone==null || grace==null) {
                    request.getSession().setAttribute("ERROR_MSG", "Missing Preferences Values!");
                    response.sendRedirect("error.jsp");
                    return;
		} else {
			int messagesVal = 0;
			int heartbeatsVal = 0;
			int intervalsVal= 0;
			int alertMessageVal = 0;
                        int alertOfflineVal = 0;
                        int alertOnlineVal = 0;
                        int alertDeviceRegVal = 0;
                        int alertDeviceUpdateVal = 0;
                        int alertDeviceIpVal = 0;                        
                        int alertDeviceFirmwareVal = 0;
                        int faultMesgVal = 0;
                        int languageVal=2;
                        int graceVal=5;
			try {
				messagesVal = Integer.parseInt(messages);
				heartbeatsVal = Integer.parseInt(heartbeats);
				intervalsVal = Integer.parseInt(intervals);
				alertMessageVal = Integer.parseInt(alertMesg);
                                faultMesgVal = Integer.parseInt(faultMesg);
                                alertOfflineVal = Integer.parseInt(alertOffline);
                                alertOnlineVal = Integer.parseInt(alertOnline);
                                alertDeviceRegVal = Integer.parseInt(alertDeviceReg);
                                alertDeviceUpdateVal = Integer.parseInt(alertDeviceUpd);
                                alertDeviceIpVal = Integer.parseInt(alertDeviceIp);
                                alertDeviceFirmwareVal = Integer.parseInt(alertDeviceFirmware);
                                languageVal = Integer.parseInt(language);
                                graceVal = Integer.parseInt(grace);
			} catch (Exception ex) {
				// go to error page ...
                                request.getSession().setAttribute("ERROR_MSG", "Invalid Preferences Values!");
				response.sendRedirect("error.jsp");
                                return;
			}	
                        TenantSettings settings = userSessionBean.saveIdentitySettings(user.getIdentityId(),heartbeatsVal,alertMessageVal
                                ,faultMesgVal, alertOfflineVal, alertOnlineVal, alertDeviceRegVal, alertDeviceUpdateVal,messagesVal,timezone,
                                intervalsVal,languageVal,graceVal,alertDeviceIpVal,alertDeviceFirmwareVal);
                        if(settings !=null){
                            request.getSession().setAttribute("USER_SETTINGS",settings);
                            response.sendRedirect("main.jsp");
                        }else{
                            request.getSession().setAttribute("ERROR_MSG", "Error in Updating Account Preferences!");
                            response.sendRedirect("error.jsp");
                            return;                            
                        }
                        
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
