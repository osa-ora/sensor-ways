/**
 * SensorWays IoT Platform
 * =================================================
 * Copyright (c) 2019 : Osama Oransa
 * =================================================
 * This class is responsible for user's login.
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
import osa.ora.iot.db.beans.TenantSettings;

import osa.ora.iot.db.beans.Users;
import osa.ora.iot.db.session.UserSessionBean;
import osa.ora.iot.util.IoTUtil;

/**
 * LoginServlet
 * @author ooransa
 */
@WebServlet(name = "LoginServlet", urlPatterns = {"/Login"})
public class LoginServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    @EJB(beanName = "UserSessionBean")
    UserSessionBean userSessionBean;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     * response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String useremail = request.getParameter("username");
        String password = request.getParameter("password");
        String identityIdentifier = request.getParameter("identity");
        int identity=0;
        //this part only for register new user, email validation
        //need to be modified for identity account only ...
        String emailAvailable = request.getParameter("email");
        if (emailAvailable != null) {
            boolean exist = userSessionBean.checkEmailUsed(emailAvailable);
            response.setContentType("text/html;charset=UTF-8");
            try (PrintWriter out = response.getWriter()) {
                out.println(""+exist);
            }
            return;
        } else {
            if (useremail == null || password == null || identityIdentifier == null) {
                response.sendRedirect("error.jsp");
                return;
            }
            try{
                identity = Integer.parseInt(identityIdentifier);
            }catch(Exception ex){
                response.sendRedirect("error.jsp");
                return;
            }
            String ipAddress=IoTUtil.getClientIpAddr(request);
            Users user = userSessionBean.authenticateAdminUser(useremail, password,identity ,ipAddress);
            if (user == null) {
                request.getSession().setAttribute("LOGIN_FAILED", "TRUE");
                request.getRequestDispatcher("login.jsp").forward(request, response);
            } else {
                TenantSettings setting = userSessionBean.loadIdentitySettings(identity);
                long notificationCount=userSessionBean.getUnreadIdentityNotitifcations(identity);
                System.out.println("********* notifications="+notificationCount);
                String notificationVal =""+notificationCount;
                if(notificationCount>99){
                    notificationVal="99+";
                }
                request.getSession().setAttribute("USER", user);
                request.getSession().setAttribute("USER_SETTINGS", setting);
                request.getSession().setAttribute("NOTIFICATION_COUNT", notificationVal);
                if (setting == null) {
                    System.out.println("Setting is Null!");
                } else {
                    System.out.println("Setting is loaded!");
                }
                //audit trail
                userSessionBean.addAuditTrail(user.getId(), 50,"UserId="+user.getId()+",IP="+ipAddress);                                                                                
                request.getRequestDispatcher("main.jsp").forward(request, response);
            }
        }
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     * response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        doGet(request, response);
    }

}
