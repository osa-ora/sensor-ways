/**
 * SensorWays IoT Platform
 * =================================================
 * Copyright (c) 2019 : Osama Oransa
 * =================================================
 * This class is used for some ajax interaction to 
 * check for some data e.g. Barcode
 */
package osa.ora.iot;

import java.io.IOException;
import java.io.PrintWriter;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import osa.ora.iot.db.beans.DeviceModel;
import osa.ora.iot.db.beans.Users;
import osa.ora.iot.db.session.UserSessionBean;

/**
 * CheckDataServlet
 * @author ooransa
 */
@WebServlet(name = "CheckDataServlet", urlPatterns = {"/CheckDataServlet"})
public class CheckDataServlet extends HttpServlet {
    @EJB(beanName = "UserSessionBean")
    UserSessionBean userSessionBean;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (request.getSession().getAttribute("USER") == null) {
            response.sendRedirect("error.jsp");
        } else {
            Users user = (Users) request.getSession().getAttribute("USER");
            String action = request.getParameter("action");
            String output = "";
            if (action != null) {
                int actionVal = Integer.parseInt(action);
                switch (actionVal) {
                    case 1:
                        //check email address is unique
                        String email = request.getParameter("email");
                        boolean results = userSessionBean.checkUniqueEmailAddressPerIdentity(user.getIdentityId(),email);
                        output = "{\"status\":" + results + "}";
                        break;
                    case 2:
                        //check barcode is already exist
                        String barcode = request.getParameter("barcode");
                        int deviceId = userSessionBean.checkbarCodeIsThere(barcode);
                        String result="false";
                        if(deviceId>0) {
                            DeviceModel model=userSessionBean.getDeviceModelById(deviceId);
                            result="\""+deviceId+"#"+model.getDeviceName()+
                                    "#"+model.getHighAlertValue1()+
                                    "#"+model.getHighAlertValue2()+
                                    "#"+model.getLowAlertValue1()+
                                    "#"+model.getLowAlertValue2()+"\"";
                            output = "{\"status\":" + result + "}";
                        }else{
                            output = "{\"status\":" + result + "}";
                        }   break;
                    case 3:
                        //check unread notification count
                        long notificationCount = userSessionBean.getUnreadIdentityNotitifcations(user.getIdentityId());
                        //System.out.println("********* notifications="+notificationCount);
                        String notificationVal =""+notificationCount;
                        if(notificationCount>99){
                            notificationVal="99+";
                        }   request.getSession().setAttribute("NOTIFICATION_COUNT", notificationVal);
                        output = "{\"status\":" + notificationVal + "}";
                        break;
                    case 4:
                        //check model is already there
                        String modelName = request.getParameter("model");  
                        boolean modelExist = userSessionBean.checkModelNameIsThere(modelName);
                        if(modelExist) {
                            output = "{\"status\":true}";
                        }else{
                            output = "{\"status\":false}";
                        }   break;
                    default:
                        break;
                }
            }else{
                output = "Error!";
            }
            response.setContentType("application/json;charset=UTF-8");
            try (PrintWriter out = response.getWriter()) {
                out.println(output);
            }
        }
    }
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
