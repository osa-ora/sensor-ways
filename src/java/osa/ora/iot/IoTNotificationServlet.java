/**
 * SensorWays IoT Platform
 * =================================================
 * Copyright (c) 2019 : Osama Oransa
 * =================================================
 * This class is used to manage user notifications
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
import osa.ora.iot.db.beans.IdentityNotifications;
import osa.ora.iot.db.beans.Users;
import osa.ora.iot.db.session.UserSessionBean;

/**
 * IoTNotificationServlet
 * @author ooransa
 */
@WebServlet(name = "IoTNotificationServlet", urlPatterns = {"/NotificationServlet"})
public class IoTNotificationServlet extends HttpServlet {
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
        //check user session already exist
        if (request.getSession().getAttribute("USER") == null) {
            request.getSession().setAttribute("ERROR_MSG", "Your session is expired!");
            response.sendRedirect("error.jsp");
            return;
        } else {
            Users user = (Users) request.getSession().getAttribute("USER");
            String action = request.getParameter("action");
            String notificationId = request.getParameter("notificationId");
            List<IdentityNotifications> identityNotificationList = null;
            if (action == null) {
                request.getSession().setAttribute("ERROR_MSG", "Missing required parameters!");
                response.sendRedirect("error.jsp");
                return;
            } else {
                int actionVal = 0;
                int notificationIdVal=0;
                try {
                    actionVal = Integer.parseInt(action);
                    if(notificationId!=null) notificationIdVal=Integer.parseInt(notificationId);
                } catch (Exception ex) {
                    // do no thing ...
                }
                long notificationCount=0;
                String notificationVal="0";
                switch (actionVal) {
                    case IConstant.ACTION_WEB_LIST_NOTIFICATIONS:
                        String typeId = request.getParameter("typeId");
                        try{
                            if(typeId!=null) Integer.parseInt(typeId);
                        }catch(Exception ex){
                            request.getSession().setAttribute("ERROR_MSG", "Invalid Filter Value!");
                            response.sendRedirect("error.jsp");
                            return;                            
                        }
                        //TODO: Need to be added to account/identity preferences/settings
                        int maxResults=50;
                        identityNotificationList = userSessionBean.getIdentityNotifications(user.getIdentityId(),typeId,maxResults);
                        request.getSession().setAttribute("NOTIFICATION_LIST", identityNotificationList);
                            notificationCount = userSessionBean.getUnreadIdentityNotitifcations(user.getIdentityId());
                            //System.out.println("********* notifications="+notificationCount);
                            notificationVal =""+notificationCount;
                            //only show max of 99 notification count
                            if(notificationCount>99){
                                notificationVal="99+";
                            }                    
                            request.getSession().setAttribute("NOTIFICATION_COUNT", notificationVal);                            
                        request.getRequestDispatcher("notifications.jsp").forward(request, response);
                        break;
                    //ACTION_WEB_READ_NOTIFICATIONS
                    //will not be used as read will be done by ajax .. or open the notification page 
                    //will make all notification as read ..
                    case IConstant.ACTION_WEB_READ_NOTIFICATION:
                        // do actual action!
                        boolean readUpdateResult=userSessionBean.updateNotificationRaadFlag(notificationIdVal, user.getIdentityId(),(short) 1);
                        // should route to the same page ..
                        if(readUpdateResult) {
                            notificationCount = userSessionBean.getUnreadIdentityNotitifcations(user.getIdentityId());
                            System.out.println("********* notifications="+notificationCount);
                            notificationVal =""+notificationCount;
                            if(notificationCount>99){
                                notificationVal="99+";
                            }                    
                            request.getSession().setAttribute("NOTIFICATION_COUNT", notificationVal);                            
                            //audit trail
                            //not need for audit here ?!
                            userSessionBean.addAuditTrail(user.getId(), actionVal,"NotificationId="+notificationId);                                                                
                            request.getRequestDispatcher("NotificationServlet?action=" + IConstant.ACTION_WEB_LIST_NOTIFICATIONS)
                                .forward(request, response);
                        } else {
                            request.getSession().setAttribute("ERROR_MSG", "Failed to Update Notification Read Flag!");
                            response.sendRedirect("error.jsp");
                            return;
                        }
                        break;
                    case IConstant.ACTION_WEB_UNREAD_NOTIFICATION:
                        // do actual action!
                        boolean unreadUpdateResult=userSessionBean.updateNotificationRaadFlag(notificationIdVal, user.getIdentityId(),(short) 0);
                        // should route to the same page ..
                        if(unreadUpdateResult) {
                            notificationCount = userSessionBean.getUnreadIdentityNotitifcations(user.getIdentityId());
                            System.out.println("********* notifications="+notificationCount);
                            notificationVal =""+notificationCount;
                            if(notificationCount>99){
                                notificationVal="99+";
                            }                    
                            request.getSession().setAttribute("NOTIFICATION_COUNT", notificationVal);                            
                            //audit trail
                            //not need for audit here ?!
                            userSessionBean.addAuditTrail(user.getId(), actionVal,"NotificationId="+notificationId);                                                                
                            request.getRequestDispatcher("NotificationServlet?action=" + IConstant.ACTION_WEB_LIST_NOTIFICATIONS)
                                .forward(request, response);
                        } else {
                            request.getSession().setAttribute("ERROR_MSG", "Failed to Update Notification Read Flag!");
                            response.sendRedirect("error.jsp");
                            return;
                        }
                        break;
                    case IConstant.ACTION_WEB_DELETE_NOTIFICATION:
                        // do actual action!
                        boolean deleteResult=userSessionBean.deleteNotification(notificationIdVal, user.getIdentityId());
                        // should route to the same page ..
                        if(deleteResult) {
                            notificationCount = userSessionBean.getUnreadIdentityNotitifcations(user.getIdentityId());
                            System.out.println("********* notifications="+notificationCount);
                            notificationVal =""+notificationCount;
                            if(notificationCount>99){
                                notificationVal="99+";
                            }                    
                            request.getSession().setAttribute("NOTIFICATION_COUNT", notificationVal);                            
                            //audit trail
                            //not need for audit here ?!
                            userSessionBean.addAuditTrail(user.getId(), actionVal,"NotificationId="+notificationId);                                                                
                            request.getRequestDispatcher("NotificationServlet?action=" + IConstant.ACTION_WEB_LIST_NOTIFICATIONS)
                                .forward(request, response);
                        } else {
                            request.getSession().setAttribute("ERROR_MSG", "Failed to Delete Notification!");
                            response.sendRedirect("error.jsp");
                            return;
                        }
                        break;
                    case IConstant.ACTION_WEB_DELETE_ALL_NOTIFICATIONS:
                        // do actual action!
                        boolean deleteAllResult=userSessionBean.deleteAllIdentityNotifications(user.getIdentityId());
                        // should route to the same page ..
                        if(deleteAllResult) {
                            notificationCount = userSessionBean.getUnreadIdentityNotitifcations(user.getIdentityId());
                            System.out.println("********* notifications="+notificationCount);
                            notificationVal =""+notificationCount;
                            if(notificationCount>99){
                                notificationVal="99+";
                            }                    
                            request.getSession().setAttribute("NOTIFICATION_COUNT", notificationVal);                            
                            //audit trail
                            userSessionBean.addAuditTrail(user.getId(), actionVal,"IdentityId="+user.getIdentityId()+"UserId="+user.getId());                                                                
                            request.getRequestDispatcher("NotificationServlet?action=" + IConstant.ACTION_WEB_LIST_NOTIFICATIONS)
                                .forward(request, response);
                        } else {
                            request.getSession().setAttribute("ERROR_MSG", "Failed to Delete Notification!");
                            response.sendRedirect("error.jsp");
                            return;
                        }
                        break;
                    case IConstant.ACTION_WEB_DELETE_READ_NOTIFICATIONS:
                        // do actual action!
                        boolean deleteReadResult=userSessionBean.deleteAllReadIdentityNotifications(user.getIdentityId());
                        // should route to the same page ..
                        if(deleteReadResult) {
                            notificationCount = userSessionBean.getUnreadIdentityNotitifcations(user.getIdentityId());
                            System.out.println("********* notifications="+notificationCount);
                            notificationVal =""+notificationCount;
                            if(notificationCount>99){
                                notificationVal="99+";
                            }                    
                            request.getSession().setAttribute("NOTIFICATION_COUNT", notificationVal);                            
                            //audit trail
                            userSessionBean.addAuditTrail(user.getId(), actionVal,"IdentityId="+user.getIdentityId()+"UserId="+user.getId());                                                                
                            request.getRequestDispatcher("NotificationServlet?action=" + IConstant.ACTION_WEB_LIST_NOTIFICATIONS)
                                .forward(request, response);
                        } else {
                            request.getSession().setAttribute("ERROR_MSG", "Failed to Delete Notification!");
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
