/**
 * SensorWays IoT Platform
 * =================================================
 * Copyright (c) 2019 : Osama Oransa
 * =================================================
 * This class is responsible for managing users within
 * user's account.
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
import osa.ora.iot.db.beans.SystemConfig;
import osa.ora.iot.db.beans.TenantSettings;
import osa.ora.iot.db.beans.Users;
import osa.ora.iot.db.session.UserSessionBean;

/**
 * IoTWebManageUsersServlet
 * @author ooransa
 */
@WebServlet(name = "IoTWebManageUsersServlet", urlPatterns = {"/ManageUsersServlet"})
public class IoTWebManageUsersServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    @EJB(beanName = "UserSessionBean")
    UserSessionBean userSessionBean;
    SystemConfig systemConfigurations;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public IoTWebManageUsersServlet() {
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
            String userId = request.getParameter("userId");
            List<Users> usersList = null;
            if (action == null) {
                request.getSession().setAttribute("ERROR_MSG", "Missing required parameters!");
                response.sendRedirect("error.jsp");
                return;
            } else {
                int actionVal = 0;
                int userIdVal = 0;
                try {
                    actionVal = Integer.parseInt(action);
                    if (userId != null) {
                        userIdVal = Integer.parseInt(userId);
                    }
                } catch (Exception ex) {
                    // do no thing ...
                }
                switch (actionVal) {
                    case IConstant.ACTION_WEB_LIST_USERS:
                        usersList = userSessionBean.getUsersForIdentity(user.getIdentityId());
                        request.getSession().setAttribute("USERS_LIST", usersList);
                        request.getRequestDispatcher("usersList.jsp").forward(request, response);
                        break;
                    case IConstant.ACTION_WEB_REGISTER_USER:
                        TenantSettings setting= (TenantSettings) request.getSession().getAttribute("USER_SETTINGS");
                        if (user.getUserRole() == IConstant.ROLE_READ_ONLY || 
                            userSessionBean.getUsersForIdentity(user.getIdentityId()).size()>setting.getMaxUsers()) {
                            request.getSession().setAttribute("ERROR_MSG", "Missing The Privilages or Exceeded the Limit!");
                            response.sendRedirect("error.jsp");
                            return;
                        }
                        // do actual action!
                        String name = request.getParameter("name");
                        String emailAddress = request.getParameter("emailAddress");
                        String status = request.getParameter("status");
                        String role = request.getParameter("role");
                        String workflow = request.getParameter("workflow");
                        String scheduler = request.getParameter("scheduler");
                        String userManagement = request.getParameter("users");
                        String dashboard = request.getParameter("dashboard");
                        String simulation = request.getParameter("simulation");
                        int roleVal=0;
                        if (name == null || emailAddress == null || status == null || role == null ||
                                !userSessionBean.checkUniqueEmailAddressPerIdentity(user.getIdentityId(),emailAddress)) {
                            request.getSession().setAttribute("ERROR_MSG", "Missing required parameters!");
                            response.sendRedirect("error.jsp");
                            return;
                        }
                        try {
                            roleVal = Integer.parseInt(role);
                        } catch (Exception ex) {
                            request.getSession().setAttribute("ERROR_MSG", "Invalid Device Model!");
                            response.sendRedirect("error.jsp");
                            return;
                        }                                
                        //id should come from the DB ...
                        String tempPassword = userSessionBean.addNewUser(name, emailAddress, user.getIdentityId(), Integer.parseInt(status), 
                                roleVal,workflow,scheduler,dashboard, simulation, userManagement,0);
                        userSessionBean.sendUserRegistrationNotification(tempPassword, name, user, emailAddress);
                        //audit trail
                        userSessionBean.addAuditTrail(user.getId(), actionVal,"Email="+emailAddress);                                                                
                        usersList = userSessionBean.getUsersForIdentity(user.getIdentityId());
                        request.getSession().setAttribute("USERS_LIST", usersList);
                        request.getRequestDispatcher("usersList.jsp").forward(request, response);
                        break;
                    case IConstant.ACTION_WEB_DELETE_USER:
                        if (user.getUserRole() == IConstant.ROLE_READ_ONLY) {
                            request.getSession().setAttribute("ERROR_MSG", "Missing The Required Privilages!");
                            response.sendRedirect("error.jsp");
                            return;
                        }
                        // do actual action!
                        boolean restuls = userSessionBean.deleteUser(userIdVal, user.getIdentityId());
                        // should route to the same page ..
                        if (restuls) {
                            //audit trail
                            userSessionBean.addAuditTrail(user.getId(), actionVal,"UserId="+userIdVal);                                                                
                            request.getRequestDispatcher("ManageUsersServlet?action=" + IConstant.ACTION_WEB_LIST_USERS)
                                    .forward(request, response);
                        } else {
                            response.sendRedirect("error.jsp");
                        }
                        break;
                    case IConstant.ACTION_WEB_ACTIVATE_USER:
                        if (user.getUserRole() == IConstant.ROLE_READ_ONLY) {
                            request.getSession().setAttribute("ERROR_MSG", "Missing The Required Privilages!");
                            response.sendRedirect("error.jsp");
                            return;
                        }
                        // do actual action!
                        boolean activateRestuls = userSessionBean.activateUser(userIdVal, user.getIdentityId());
                        // should route to the same page ..
                        if (activateRestuls) {
                            //audit trail
                            userSessionBean.addAuditTrail(user.getId(), actionVal,"UserId="+userIdVal);                                                                
                            request.getRequestDispatcher("ManageUsersServlet?action=" + IConstant.ACTION_WEB_LIST_USERS)
                                    .forward(request, response);
                        } else {
                            response.sendRedirect("error.jsp");
                        }
                        break;
                    case IConstant.ACTION_WEB_INACTIVATE_USER:
                        if (user.getUserRole() == IConstant.ROLE_READ_ONLY) {
                            request.getSession().setAttribute("ERROR_MSG", "Missing The Required Privilages!");
                            response.sendRedirect("error.jsp");
                            return;
                        }
                        // do actual action!
                        boolean inactivateRestuls = userSessionBean.inactivateUser(userIdVal, user.getIdentityId());
                        // should route to the same page ..
                        if (inactivateRestuls) {
                            //audit trail
                            userSessionBean.addAuditTrail(user.getId(), actionVal,"UserId="+userIdVal);                                                                
                            request.getRequestDispatcher("ManageUsersServlet?action=" + IConstant.ACTION_WEB_LIST_USERS)
                                    .forward(request, response);
                        } else {
                            response.sendRedirect("error.jsp");
                        }
                        break;
                    case IConstant.ACTION_WEB_MAKE_USER_READONLY:
                        if (user.getUserRole() == IConstant.ROLE_READ_ONLY) {
                            request.getSession().setAttribute("ERROR_MSG", "Missing The Required Privilages!");
                            response.sendRedirect("error.jsp");
                            return;
                        }
                        // do actual action!
                        boolean readonlyRestuls = userSessionBean.updateUserRole(userIdVal, user.getIdentityId(),IConstant.ROLE_READ_ONLY);
                        // should route to the same page ..
                        if (readonlyRestuls) {
                            //audit trail
                            userSessionBean.addAuditTrail(user.getId(), actionVal,"UserId="+userIdVal);                                                                
                            request.getRequestDispatcher("ManageUsersServlet?action=" + IConstant.ACTION_WEB_LIST_USERS)
                                    .forward(request, response);
                        } else {
                            response.sendRedirect("error.jsp");
                        }
                        break;
                    case IConstant.ACTION_WEB_MAKE_USER_READWRITE:
                        if (user.getUserRole() == IConstant.ROLE_READ_ONLY) {
                            request.getSession().setAttribute("ERROR_MSG", "Missing The Required Privilages!");
                            response.sendRedirect("error.jsp");
                            return;
                        }
                        // do actual action!
                        boolean readwriteRestuls = userSessionBean.updateUserRole(userIdVal, user.getIdentityId(),IConstant.ROLE_READ_WRITE);
                        // should route to the same page ..
                        if (readwriteRestuls) {
                            //audit trail
                            userSessionBean.addAuditTrail(user.getId(), actionVal,"UserId="+userIdVal);                                                                
                            request.getRequestDispatcher("ManageUsersServlet?action=" + IConstant.ACTION_WEB_LIST_USERS)
                                    .forward(request, response);
                        } else {
                            response.sendRedirect("error.jsp");
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
