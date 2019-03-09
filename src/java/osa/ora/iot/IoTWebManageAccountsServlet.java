/**
 * SensorWays IoT Platform
 * =================================================
 * Copyright (c) 2019 : Osama Oransa
 * =================================================
 * This class is responsible for managing different
 * user's accounts.
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
 * IoTWebManageAccountsServlet
 * @author ooransa
 */
@WebServlet(name = "IoTWebManageAccountsServlet", urlPatterns = {"/ManageAccountsServlet"})
public class IoTWebManageAccountsServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    @EJB(beanName = "UserSessionBean")
    UserSessionBean userSessionBean;
    SystemConfig systemConfigurations;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public IoTWebManageAccountsServlet() {
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
            String accountId = request.getParameter("accountId");
            List<TenantSettings> tenantList = null;
            if (action == null) {
                request.getSession().setAttribute("ERROR_MSG", "Missing required parameters!");
                response.sendRedirect("error.jsp");
                return;
            } else {
                if(user.getSystemAdmin()!=1){
                    request.getSession().setAttribute("ERROR_MSG", "Missing The Required Privilages!");
                    response.sendRedirect("error.jsp");
                    return;                    
                }
                int actionVal = 0;
                int accountIdVal = 0;
                try {
                    actionVal = Integer.parseInt(action);
                    if (accountId != null) {
                        accountIdVal = Integer.parseInt(accountId);
                    }
                } catch (Exception ex) {
                    // do no thing ...
                }
                switch (actionVal) {
                    case IConstant.ACTION_WEB_LIST_USERS:
                        tenantList = userSessionBean.getIdentityLists();
                        request.getSession().setAttribute("TENANTS_LIST", tenantList);
                        request.getRequestDispatcher("accountsList.jsp").forward(request, response);
                        break;
                    case IConstant.ACTION_WEB_LOGIN_TO_USER_ACCOUNT:
                        if(request.getSession().getAttribute("ORIGINAL")!=null){
                            request.getSession().setAttribute("ERROR_MSG", "Logout first from this current account!");
                            response.sendRedirect("error.jsp");
                            return;                            
                        }
                        if(accountIdVal==user.getIdentityId()){
                            request.getSession().setAttribute("ERROR_MSG", "You Already in this Account!");
                            response.sendRedirect("error.jsp");
                            return;                            
                        }
                        TenantSettings loginSetting = userSessionBean.loadIdentitySettings(accountIdVal);
                        if (loginSetting == null) {
                            request.getSession().setAttribute("ERROR_MSG", "Error while Login to this Account!");
                            response.sendRedirect("error.jsp");
                            return;                            
                        }
                        long notificationCount=userSessionBean.getUnreadIdentityNotitifcations(loginSetting.getIdentity());
                        System.out.println("********* notifications="+notificationCount);
                        String notificationVal =""+notificationCount;
                        if(notificationCount>99){
                            notificationVal="99+";
                        }
                        request.getSession().setAttribute("ORIGINAL",user.getIdentityId());
                        user.setDashboard(null);
                        user.setIdentityId(accountIdVal);
                        request.getSession().setAttribute("USER", user);
                        request.getSession().setAttribute("USER_SETTINGS", loginSetting);
                        request.getSession().setAttribute("NOTIFICATION_COUNT", notificationVal);
                        //audit trail
                        userSessionBean.addAuditTrail(user.getId(), actionVal,"UserId="+user.getId()+",AccountId="+loginSetting.getIdentity());                                                                                
                        request.getRequestDispatcher("main.jsp").forward(request, response);                        
                        break;
                    case IConstant.ACTION_WEB_LOGOUT_FROM_USER_ACCOUNT:
                        if(request.getSession().getAttribute("ORIGINAL")==null){
                            request.getSession().setAttribute("ERROR_MSG", "Error while Logging out!");
                            response.sendRedirect("error.jsp");
                            return;                            
                        }
                        int currentIdentity=user.getIdentityId();
                        TenantSettings originalSetting = userSessionBean.loadIdentitySettings((Integer)request.getSession().getAttribute("ORIGINAL"));
                        if (originalSetting == null) {
                            request.getSession().setAttribute("ERROR_MSG", "Error while Logging out of this Account "+currentIdentity);
                            response.sendRedirect("error.jsp");
                            return;                            
                        }
                        long notificationCountOriginal=userSessionBean.getUnreadIdentityNotitifcations(originalSetting.getIdentity());
                        System.out.println("********* notifications="+notificationCountOriginal);
                        String notificationValOriginal =""+notificationCountOriginal;
                        if(notificationCountOriginal>99){
                            notificationValOriginal="99+";
                        }
                        request.getSession().removeAttribute("ORIGINAL");
                        user.setDashboard(userSessionBean.getUserDashboard(user.getId()));
                        user.setIdentityId(originalSetting.getIdentity());
                        request.getSession().setAttribute("USER", user);
                        request.getSession().setAttribute("USER_SETTINGS", originalSetting);
                        request.getSession().setAttribute("NOTIFICATION_COUNT", notificationValOriginal);
                        //audit trail
                        userSessionBean.addAuditTrail(user.getId(), actionVal,"UserId="+user.getId()+",AccountId="+currentIdentity);                                                                                
                        request.getRequestDispatcher("main.jsp").forward(request, response);                        
                        break;
                    case IConstant.ACTION_WEB_LOAD_ACCOUNT_FOR_UPDATE:
                        TenantSettings setting = userSessionBean.loadIdentitySettings(accountIdVal);
                        Users adminUser=userSessionBean.getUser(setting.getAdminUserEmail(),accountIdVal);
                        request.getSession().setAttribute("EDIT_SETTINGS", setting);
                        request.getSession().setAttribute("EDIT_USER", adminUser);
                        request.getRequestDispatcher("editAccount.jsp").forward(request, response);
                        break;
                    case IConstant.ACTION_WEB_REGISTER_USER:
                        // do actual action!
                        String accountName = request.getParameter("name");
                        String userName = request.getParameter("username");
                        String emailAddress = request.getParameter("emailAddress");
                        String status = request.getParameter("status");
                        String max_users = request.getParameter("max_users");
                        String max_devices = request.getParameter("max_devices");
                        String max_sms = request.getParameter("max_sms");
                        String userManagement = request.getParameter("users");
                        String workflow = request.getParameter("workflow");
                        String scheduler = request.getParameter("scheduler");
                        String dashboard = request.getParameter("dashboard");
                        String simulation = request.getParameter("simulation");
                        if (accountName == null || emailAddress == null || userName==null || status == null || max_devices==null || max_sms==null || max_users==null) {
                            request.getSession().setAttribute("ERROR_MSG", "Missing required parameters!");
                            response.sendRedirect("error.jsp");
                            return;
                        } 
                        int maxUsersVal=0;
                        int maxDevicesVal=0;
                        int maxSmsVal=0;
                        int statusVal=0;
                        try{
                            maxUsersVal=Integer.parseInt(max_users);
                            maxDevicesVal=Integer.parseInt(max_devices);
                            maxSmsVal=Integer.parseInt(max_sms);
                            statusVal=Integer.parseInt(status);
                        }catch(Exception ex){
                            request.getSession().setAttribute("ERROR_MSG", "Invalid Data Values!");
                            response.sendRedirect("error.jsp");
                            return;
                        }
                        //id should come from the DB ...
                        int identityId = userSessionBean.addNewAccount(accountName, emailAddress, statusVal, maxUsersVal, 
                                maxDevicesVal,maxSmsVal);
                        String tempPassword = userSessionBean.addNewUser(userName,emailAddress, identityId, statusVal, 
                                IConstant.ROLE_READ_WRITE,workflow,scheduler,dashboard,simulation,userManagement,1);
                        userSessionBean.sendAccountRegistrationNotification(tempPassword, userName, identityId, emailAddress);
                        //audit trail
                        userSessionBean.addAuditTrail(user.getId(), actionVal,"Account="+accountName);                                                                
                        tenantList = userSessionBean.getIdentityLists();
                        request.getSession().setAttribute("TENANTS_LIST", tenantList);
                        request.getRequestDispatcher("accountsList.jsp").forward(request, response);                        
                        break;
                    case IConstant.ACTION_WEB_UPDATE_ACCOUNT:
                        // do actual action!
                        String accountNameE = request.getParameter("name");
                        String emailAddressE = request.getParameter("emailAddress");
                        String max_usersE = request.getParameter("max_users");
                        String max_devicesE = request.getParameter("max_devices");
                        String max_smsE = request.getParameter("max_sms");
                        String userManagementE = request.getParameter("users");
                        String workflowE = request.getParameter("workflow");
                        String schedulerE = request.getParameter("scheduler");
                        String dashboardE = request.getParameter("dashboard");
                        String simulationE = request.getParameter("simulation");
                        if (accountNameE == null || emailAddressE == null || max_devicesE==null || max_smsE==null || max_usersE==null) {
                            request.getSession().setAttribute("ERROR_MSG", "Missing required parameters!");
                            response.sendRedirect("error.jsp");
                            return;
                        } 
                        int maxUsersValE=0;
                        int maxDevicesValE=0;
                        int maxSmsValE=0;
                        try{
                            maxUsersValE=Integer.parseInt(max_usersE);
                            maxDevicesValE=Integer.parseInt(max_devicesE);
                            maxSmsValE=Integer.parseInt(max_smsE);
                        }catch(Exception ex){
                            request.getSession().setAttribute("ERROR_MSG", "Invalid Data Values!");
                            response.sendRedirect("error.jsp");
                            return;
                        }
                        TenantSettings oldTenant=userSessionBean.loadIdentitySettings(accountIdVal);
                        if(!oldTenant.getAdminUserEmail().equalsIgnoreCase(emailAddressE) && userSessionBean.checkUniqueEmailAddressPerIdentity(accountIdVal,emailAddressE)){
                                //new user !! will throw error, admin must be an existing uesr.
                                request.getSession().setAttribute("ERROR_MSG", "You can only update the admin user to an existing user in this account!");
                                response.sendRedirect("error.jsp");
                                return;
                        }
                         userSessionBean.updateAccount(accountIdVal, accountNameE, emailAddressE, maxUsersValE,
                                maxDevicesValE, maxSmsValE);
                        Users newAdminUser = userSessionBean.updateTenantAdminUser(emailAddressE, accountIdVal, workflowE, schedulerE, dashboardE, simulationE, userManagementE);
                        userSessionBean.sendAccountUpdateNotification(newAdminUser.getUsername(), accountIdVal, emailAddressE);
                        //audit trail
                        userSessionBean.addAuditTrail(user.getId(), actionVal,"Account="+accountNameE);                                                                
                        tenantList = userSessionBean.getIdentityLists();
                        request.getSession().setAttribute("TENANTS_LIST", tenantList);
                        request.getRequestDispatcher("accountsList.jsp").forward(request, response);                        
                        break;
                    case IConstant.ACTION_WEB_ACTIVATE_USER:
                        // do actual action!
                        boolean activateRestuls = userSessionBean.activateAccount(accountIdVal);
                        // should route to the same page ..
                        if (activateRestuls) {
                            //audit trail
                            userSessionBean.addAuditTrail(user.getId(), actionVal,"AccountId="+accountIdVal);                                                                
                            request.getRequestDispatcher("ManageAccountsServlet?action=" + IConstant.ACTION_WEB_LIST_USERS)
                                    .forward(request, response);
                        } else {
                            response.sendRedirect("error.jsp");
                        }
                    break;
                    case IConstant.ACTION_WEB_INACTIVATE_USER:
                        if (user.getIdentityId()==accountIdVal) {
                            request.getSession().setAttribute("ERROR_MSG", "Cannot Inactivate Account of the System Admin User!");
                            response.sendRedirect("error.jsp");
                            return;
                        } 
                        // do actual action!
                        boolean inactivateRestuls = userSessionBean.inactivateAccount(accountIdVal);
                        // should route to the same page ..
                        if (inactivateRestuls) {
                            //audit trail
                            userSessionBean.addAuditTrail(user.getId(), actionVal,"AccountId="+accountIdVal);                                                                
                            request.getRequestDispatcher("ManageAccountsServlet?action=" + IConstant.ACTION_WEB_LIST_USERS)
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
